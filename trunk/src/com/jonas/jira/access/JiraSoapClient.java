package com.jonas.jira.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteCustomFieldValue;
import com.atlassian.jira.rpc.soap.beans.RemoteField;
import com.atlassian.jira.rpc.soap.beans.RemoteFieldValue;
import com.atlassian.jira.rpc.soap.beans.RemoteFilter;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteIssueType;
import com.atlassian.jira.rpc.soap.beans.RemoteNamedObject;
import com.atlassian.jira.rpc.soap.beans.RemoteResolution;
import com.atlassian.jira.rpc.soap.beans.RemoteUser;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.atlassian.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import com.atlassian.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraCustomFields;
import com.jonas.jira.JiraProject;

/**
 * Sample JIRA SOAP client. Note that the constants sit in the {@link ClientConstants} interface
 */
public class JiraSoapClient {

   interface JiraAccessAction<T> {
      T accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
            RemoteException;
   }
   class JiraTokenCommand<T> {

      private final JiraAccessAction<T> action;
      private final Logger log = MyLogger.getLogger(JiraTokenCommand.class);

      public JiraTokenCommand(JiraAccessAction<T> action) {
         this.action = action;
      }

      public T execute() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
            RemoteException {
         // RemoteException - If there was some problem preventing the operation from working.
         // RemotePermissionException - If the user is not permitted to perform this operation in this context.
         // RemoteAuthenticationException - If the token is invalid or the SOAP session has timed out
         // RemoteValidationException - If the arguments and their properties are incomplete or malformed.

         try {
            log.debug("ConductingAction : " + action);
            return action.accessJiraAndReturn();
         } catch (RemoteAuthenticationException e) {
            log.info(e);
            log.info("Session timed out. Renewing token again!");
            // renewToken();
         }
         return action.accessJiraAndReturn();
      }
   }
   public static final JiraSoapClient AOLBB = new JiraSoapClient(ClientConstants.AOLBB_WS);
   public static final JiraSoapClient ATLASSIN = new JiraSoapClient(ClientConstants.ATLASSIN_WS);
   private static List<String> customFieldsForCloning = new ArrayList<String>(1);

   private static JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator;
   private static final Logger log = MyLogger.getLogger(JiraSoapClient.class);

   private static final String LOGIN_NAME = "soaptester";

   private static final String LOGIN_PASSWORD = "soaptester";

   private JiraSoapService jiraSoapService = null;
   private String token = null;

   private Object tokenLock = new Object();

   private String type = "13";

   private JiraSoapClient(String address) {
      jiraSoapServiceServiceLocator = getLocator();
      jiraSoapServiceServiceLocator.setJirasoapserviceV2EndpointAddress(address);
      log.debug("setting up " + jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
      try {
         jiraSoapService = jiraSoapServiceServiceLocator.getJirasoapserviceV2();
      } catch (ServiceException e) {
         e.printStackTrace();
      }
   }

   static {
      customFieldsForCloning.add(JiraCustomFields.LLUSprint.toString());
      customFieldsForCloning.add(JiraCustomFields.LLUProject.toString());
   }

   private static JiraSoapServiceServiceLocator getLocator() {
      JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = new JiraSoapServiceServiceLocator();
      // log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
      return jiraSoapServiceServiceLocator;
   }

   public boolean clearToken() throws RemoteException {
      synchronized (tokenLock) {
         boolean logout = jiraSoapService.logout(token);
         token = null;
         return logout;
      }
   }

   private void copyCustomFieldValuesFromOriginalToClone(RemoteIssue originalJiraIssue, RemoteIssue cloneIssue) {
      RemoteCustomFieldValue[] originalCustomFieldValues = originalJiraIssue.getCustomFieldValues();
      List<RemoteCustomFieldValue> fieldsToBeCloned = new ArrayList<RemoteCustomFieldValue>();

      for (RemoteCustomFieldValue field : originalCustomFieldValues) {
         if (customFieldsForCloning.contains(field.getCustomfieldId())) {
            fieldsToBeCloned.add(field);
         }
      }
      cloneIssue.setCustomFieldValues(fieldsToBeCloned.toArray(new RemoteCustomFieldValue[fieldsToBeCloned.size()]));
   }

   public RemoteIssue createMergeJira(final String jira, final String mergeFixVersionName) throws RemotePermissionException,
         RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      RemoteIssue originalJiraIssue = jiraSoapService.getIssue(getToken(), jira);

      RemoteIssue cloneIssue = new RemoteIssue();

      copyCustomFieldValuesFromOriginalToClone(originalJiraIssue, cloneIssue);

      cloneIssue.setProject(originalJiraIssue.getProject());
      cloneIssue.setType(type);
      cloneIssue.setComponents(originalJiraIssue.getComponents());
      cloneIssue.setSummary("[Merge for " + originalJiraIssue.getKey() + "] " + originalJiraIssue.getSummary());
      cloneIssue.setAffectsVersions(originalJiraIssue.getAffectsVersions());
      cloneIssue.setPriority("1");
      // setOriginalEstimateToZero(cloneIssue);

      RemoteVersion[] clonedFixVersions = setFixVersionOnClone(jira, mergeFixVersionName, cloneIssue);

      RemoteIssue createIssue = null;
      try {
         createIssue = jiraSoapService.createIssue(token, cloneIssue);
         removeClonedFixVersionFromOriginalIssue(originalJiraIssue, clonedFixVersions);

         // add comment to original
         // RemoteComment comment = new RemoteComment();
         // comment.setBody("Created Merge Jira: " + createIssue.getKey());
         // comment.setLevel(level);
         // jiraSoapService.addComment(getToken(), jira, comment);

         // link both jiras together.
      } catch (Throwable e) {
         e.printStackTrace();
      }
      return createIssue;
   }

   public RemoteVersion[] getFixVersions(final JiraProject jiraProject) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      log.debug("Getting Fix Versions!");
      JiraTokenCommand<RemoteVersion[]> command = new JiraTokenCommand<RemoteVersion[]>(new JiraAccessAction<RemoteVersion[]>() {
         public RemoteVersion[] accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
            return jiraSoapService.getVersions(getToken(), jiraProject.getJiraKey());
         }

      });
      RemoteVersion[] versions = command.execute();
      if (log.isDebugEnabled()) {
         log.debug("Getting Fix Versions Done!");
         for (int i = 0; i < versions.length; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("Fix Version[").append(i).append("] {");
            sb.append("name=").append(versions[i].getName());
            sb.append(",id=").append(versions[i].getId());
            sb.append("}");
            log.debug(sb.toString());

         }
      }

      return versions;
   }

   public RemoteIssue getJira(String jira) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      return jiraSoapService.getIssue(getToken(), jira);
   }

   public RemoteResolution[] getResolutions() throws RemotePermissionException, RemoteAuthenticationException, RemoteException {
      log.debug("Getting Resolutions");

      JiraAccessAction<RemoteResolution[]> action = new JiraAccessAction<RemoteResolution[]>() {
         public RemoteResolution[] accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
            return jiraSoapService.getResolutions(getToken());
         }

      };
      JiraTokenCommand<RemoteResolution[]> command = new JiraTokenCommand<RemoteResolution[]>(action);
      return command.execute();
   }

   private String getToken() throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      log.debug("Getting Token!");
      if (token == null) {
         log.trace("Token is null!");
         synchronized (tokenLock) {
            log.trace("Token Block has been synced");
            if (token == null) {
               log.debug("Token needs renewing!");
               renewToken();
            }
         }
         log.trace("Token Block has been un-synced");
      }
      log.debug("Done Getting Token!");
      return token;
   }

   public RemoteIssueType[] getTypes() {
      log.debug("Getting Types");

      JiraTokenCommand<RemoteIssueType[]> command = new JiraTokenCommand<RemoteIssueType[]>(new JiraAccessAction<RemoteIssueType[]>() {
         public RemoteIssueType[] accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
            log.debug("Accessing: " + jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
            return jiraSoapService.getIssueTypes(getToken());
         }

      });
      // FIXME doesn't work on TST project!!
      RemoteIssueType[] execute = null;
      try {
         execute = command.execute();
      } catch (Throwable e) {
         e.printStackTrace();
      }
      return execute;
   }

   private void removeClonedFixVersionFromOriginalIssue(RemoteIssue originalJiraIssue, RemoteVersion[] clonesFixVersions)
         throws RemoteException, com.atlassian.jira.rpc.exception.RemoteException {
      RemoteFieldValue[] remoteFieldValues = new RemoteFieldValue[1];
      RemoteVersion[] originalFixVersions = originalJiraIssue.getFixVersions();
      List<String> values = new ArrayList<String>();
      for (RemoteVersion originalFixTemp : originalFixVersions) {
         boolean fixVersionHasBeenCloned = false;
         for (RemoteVersion clonesFixTemp : clonesFixVersions) {
            if (originalFixTemp.equals(clonesFixTemp)) {
               fixVersionHasBeenCloned = true;
               break;
            }
         }
         if (!fixVersionHasBeenCloned) {
            values.add(originalFixTemp.getId());
         }
      }
      remoteFieldValues[0] = new RemoteFieldValue("fixVersions", values.toArray(new String[values.size()]));
      jiraSoapService.updateIssue(token, originalJiraIssue.getKey(), remoteFieldValues);
   }

   public void renewToken() throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      synchronized (tokenLock) {
         log.debug("Renewing Token");
         clearToken();
         token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);
         // user = jiraSoapService.getUser(LOGIN_NAME, LOGIN_PASSWORD);
         log.debug("Renewing Token Done!");
      }
   }

   private RemoteVersion[] setFixVersionOnClone(final String jira, final String mergeFixVersionName, RemoteIssue cloneIssue)
         throws RemoteException, RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException {
      RemoteVersion[] versions = jiraSoapService.getVersions(getToken(), PlannerHelper.getProjectKey(jira));
      List<RemoteVersion> cloneFixVersions = new ArrayList<RemoteVersion>();
      for (RemoteVersion remoteVersion : versions) {
         if (remoteVersion.getName().trim().equalsIgnoreCase(mergeFixVersionName.trim())) {
            cloneFixVersions.add(remoteVersion);
         }
      }
      RemoteVersion[] array = cloneFixVersions.toArray(new RemoteVersion[cloneFixVersions.size()]);
      cloneIssue.setFixVersions(array);
      return array;
   }

   private RemoteIssue updateCustomField(final String jiraKey, final String sprint, final String customField) throws RemotePermissionException,
         RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      String[] values = new String[] { sprint };
      RemoteFieldValue remoteFieldValue = new RemoteFieldValue(customField, values);
      RemoteFieldValue[] remoteFieldValues = new RemoteFieldValue[] { remoteFieldValue };
      return jiraSoapService.updateIssue(getToken(), jiraKey, remoteFieldValues);
   }

   public void updateSprint(final String jiraKey, final String sprint) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException, ServiceException {
      updateCustomField(jiraKey, sprint, JiraCustomFields.LLUSprint.toString());
   }

}
