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

   private static List<String> customFieldsForCloning = new ArrayList<String>(1);
   private static JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator;
   private static final Logger log = MyLogger.getLogger(JiraSoapClient.class);
   private static final String LOGIN_NAME = "soaptester";
   private static final String LOGIN_PASSWORD = "soaptester";

   public static final JiraSoapClient AOLBB = new JiraSoapClient(ClientConstants.AOLBB_WS);
   public static final JiraSoapClient ATLASSIN = new JiraSoapClient(ClientConstants.ATLASSIN_WS);

   private JiraSoapService jiraSoapService = null;

   private String token;
   private String type = "13";
   static {
      customFieldsForCloning.add(JiraCustomFields.LLUSprint.toString());
      customFieldsForCloning.add(JiraCustomFields.LLUProject.toString());
   }

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

   private static JiraSoapServiceServiceLocator getLocator() {
      JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = new JiraSoapServiceServiceLocator();
      // log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
      return jiraSoapServiceServiceLocator;
   }

   public void closeJira(String issueKey, String resolution) throws com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      RemoteFieldValue[] actionParams = new RemoteFieldValue[2];
      actionParams[0] = new RemoteFieldValue("assignee", new String[] { "" });
      actionParams[1] = new RemoteFieldValue("resolution", new String[] { resolution });
      jiraSoapService.progressWorkflowAction(getToken(), issueKey, "2", actionParams);
   }

   @SuppressWarnings("finally")
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
      // setProjectToMerge(cloneIssue);

      RemoteVersion[] clonedFixVersions = setFixVersionOnClone(jira, mergeFixVersionName, cloneIssue);

      RemoteIssue createIssue = null;
      try {
         createIssue = jiraSoapService.createIssue(token, cloneIssue);
         // update original jira to now exclude the merge jiras fixversion
         removeClonedFixVersionFromOriginalIssue(originalJiraIssue, clonedFixVersions);

         // add comment to original
         // RemoteComment comment = new RemoteComment();
         // comment.setBody("Created Merge Jira: " + createIssue.getKey());
         // comment.setLevel(level);
         // jiraSoapService.addComment(getToken(), jira, comment);

         // link both jiras together.
      } catch (Throwable e) {
         e.printStackTrace();
      } finally {
         return createIssue;
      }
   }

   public RemoteField[] getFieldsForEdit(final String jiraKey) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      log.debug("getFieldsForEdit!");
      JiraTokenCommand<RemoteField[]> command = new JiraTokenCommand<RemoteField[]>(new JiraAccessAction<RemoteField[]>() {
         public RemoteField[] accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
            return jiraSoapService.getFieldsForEdit(getToken(), jiraKey);
         }

      });
      RemoteField[] versions = command.execute();
      if (log.isDebugEnabled()) {
         log.debug("Getting Editable fields Done!");
         for (int i = 0; i < versions.length; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("RemoteField").append(i).append("] {");
            sb.append("name=").append(versions[i].getName());
            sb.append(",id=").append(versions[i].getId());
            sb.append("}");
            log.debug(sb.toString());

         }
      }
      return versions;
   }

   public RemoteFilter getFilter(String filterName) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      RemoteFilter[] filters = getFilters();
      RemoteFilter filter = null;
      for (RemoteFilter remoteFilter : filters) {
         if (remoteFilter.getName().equals(filterName)) {
            filter = remoteFilter;
            break;
         }
      }
      return filter;
   }

   public RemoteFilter[] getFilters() throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      log.debug("Getting Fix Versions!");
      JiraTokenCommand<RemoteFilter[]> command = new JiraTokenCommand<RemoteFilter[]>(new JiraAccessAction<RemoteFilter[]>() {
         public RemoteFilter[] accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
            return jiraSoapService.getSavedFilters(getToken());
         }

      });

      RemoteFilter[] filters = command.execute();
      if (log.isDebugEnabled()) {
         log.debug("Getting RemoveFilters Done!");
         for (int i = 0; i < filters.length; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("RemoteFilter[").append(i).append("] {");
            sb.append("name=").append(filters[i].getName());
            sb.append(",id=").append(filters[i].getId());
            sb.append("}");
            log.debug(sb.toString());
         }
      }

      return filters;
   }

   public RemoteVersion getFixVersion(final String fixName, JiraProject jiraProject) throws RemotePermissionException,
         RemoteAuthenticationException, RemoteException {
      log.debug("Getting FixVersion");

      RemoteVersion[] versions = getFixVersions(jiraProject);
      for (int i = 0; i < versions.length; i++) {
         log.debug("Checking fixversion: " + versions[i].getName() + " is equal to " + fixName);
         if (fixName.trim().equals(versions[i].getName().trim())) {
            return versions[i];
         }
      }
      log.debug("Getting FixVersion Done!");
      return null;
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

   public RemoteIssueType[] getTypes() {
      log.debug("Getting Resolutions");

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

   public void login() throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      renewToken();
   }

   public void testGetActions(String issueKey) throws java.rmi.RemoteException {
      System.out.println("Progressing workflow of " + issueKey + "...");
      RemoteNamedObject[] availableActions = jiraSoapService.getAvailableActions(getToken(), issueKey);
      String actionId = null;
      for (int i = 0; i < availableActions.length; i++) {
         RemoteNamedObject availableAction = availableActions[i];
         System.out.println("availableAction: " + availableAction.getId() + " - " + availableAction.getName());
         if (actionId == null)
            actionId = availableAction.getId();
      }

      if (actionId != null) {
         RemoteField[] fieldsForAction = jiraSoapService.getFieldsForAction(getToken(), issueKey, actionId);
         for (int i = 0; i < fieldsForAction.length; i++) {
            RemoteField remoteField = fieldsForAction[i];
            System.out.println("remoteField: " + remoteField.getId() + " - " + remoteField.getName());
         }

         RemoteFieldValue[] actionParams = new RemoteFieldValue[] { new RemoteFieldValue("assignee", new String[] { LOGIN_NAME }) };

         RemoteIssue remoteIssue = jiraSoapService.progressWorkflowAction(getToken(), issueKey, actionId, actionParams);
         System.out.println("Progressed workflow of " + remoteIssue.getKey() + " to: " + remoteIssue.getStatus());
      }
   }

   public String reopenJira(String issueKey) throws com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      RemoteIssue jira = jiraSoapService.getIssue(getToken(), issueKey);
      System.out.println(jira.getResolution());

      RemoteFieldValue[] actionParams = new RemoteFieldValue[1];
      actionParams[0] = new RemoteFieldValue("assignee", new String[] { "" });
      jiraSoapService.progressWorkflowAction(getToken(), issueKey, "3", actionParams);

      return jira.getResolution();
   }

   public void updateSprint(final String jiraKey, final String sprint) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException, ServiceException {
      // JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
      // JiraSoapService jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();

      updateCustomField(jiraKey, sprint, JiraCustomFields.LLUSprint.toString());
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

   private String getToken() throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      log.debug("Getting Token!");
      if (token == null) {
         log.trace("Syncing Token Block");
         synchronized (this) {
            log.trace("Syncing Token Block has been synced");
            if (token == null) {
               log.debug("Token needs renewing!");
               renewToken();
            }
            log.trace("Block Synced Done!");
         }
      }
      return token;
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

   private synchronized void renewToken() throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
         RemoteException {
      log.debug("Renewing Token");
      token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);
      log.debug("Renewing Token Done!");
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

   private void setOriginalEstimateToZero(RemoteIssue cloneIssue) {
      String[] originalEstimates = { "0d" };
      RemoteCustomFieldValue[] customFieldValues = { new RemoteCustomFieldValue("timetracking", "timetracking", originalEstimates) };
      cloneIssue.setCustomFieldValues(customFieldValues);
   }

   private void setProjectToMerge(RemoteIssue cloneIssue) {
      String[] merge = { "Merge" };
      RemoteCustomFieldValue[] customFieldValues = { new RemoteCustomFieldValue(JiraCustomFields.LLUProject.toString(), null, merge) };
      cloneIssue.setCustomFieldValues(customFieldValues);
   }

   private RemoteIssue updateCustomField(final String jiraKey, final String sprint, final String customField) throws RemotePermissionException,
         RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      // JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
      // public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
      // com.atlassian.jira.rpc.exception.RemoteException,
      // RemoteException {
      String[] values = new String[] { sprint };
      RemoteFieldValue remoteFieldValue = new RemoteFieldValue(customField, values);
      RemoteFieldValue[] remoteFieldValues = new RemoteFieldValue[] { remoteFieldValue };
      return jiraSoapService.updateIssue(getToken(), jiraKey, remoteFieldValues);
      // }
      //
      // });
      // RemoteIssue issue = (RemoteIssue) command.execute();
      // return issue;
   }

   /**
    * For testing!!
    * 
    * @param string
    * @param lluSystemsProvisioning
    * @return
    * @throws RemotePermissionException
    * @throws RemoteAuthenticationException
    * @throws com.atlassian.jira.rpc.exception.RemoteException
    * @throws RemoteException
    */
   void printAllCustomFieldInfo() throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      log.debug("Getting Resolutions");

      JiraTokenCommand<RemoteField[]> command = new JiraTokenCommand<RemoteField[]>(new JiraAccessAction<RemoteField[]>() {
         public RemoteField[] accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
            return jiraSoapService.getCustomFields(getToken());
         }

      });
      RemoteField[] execute = command.execute();
      for (RemoteField remoteField : execute) {
         log.debug("Getting RemoteField: " + remoteField.getId() + " - " + remoteField.getName());
      }
   }

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
            renewToken();
         }
         return action.accessJiraAndReturn();
      }
   }
}
