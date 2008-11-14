package com.jonas.jira.access;

import java.awt.SystemColor;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteComment;
import com.atlassian.jira.rpc.soap.beans.RemoteField;
import com.atlassian.jira.rpc.soap.beans.RemoteFieldValue;
import com.atlassian.jira.rpc.soap.beans.RemoteFilter;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteIssueType;
import com.atlassian.jira.rpc.soap.beans.RemoteResolution;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.atlassian.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import com.atlassian.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceService;
import com.atlassian.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;

/**
 * Sample JIRA SOAP client. Note that the constants sit in the {@link ClientConstants} interface
 */
public class JiraSoapClient {

   private static final Logger log = MyLogger.getLogger(JiraSoapClient.class);
   private static final String LOGIN_NAME = "soaptester";
   private static final String LOGIN_PASSWORD = "soaptester";

   private JiraSoapService jiraSoapService = null;
   private String token;
   private static JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator;

   public JiraSoapClient(String address) {
      jiraSoapServiceServiceLocator = getLocator();
      jiraSoapServiceServiceLocator.setJirasoapserviceV2EndpointAddress(address);
      try {
         jiraSoapService = jiraSoapServiceServiceLocator.getJirasoapserviceV2();
      } catch (ServiceException e) {
         e.printStackTrace();
      }
   }

   private static JiraSoapServiceServiceLocator getLocator() {
      JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = new JiraSoapServiceServiceLocator();
      log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
      return jiraSoapServiceServiceLocator;
   }

   @SuppressWarnings("finally")
   public RemoteIssue createMergeJira(final String jira, final String mergeFixVersionName) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      RemoteIssue originalJiraIssue = jiraSoapService.getIssue(getToken(), jira);

      RemoteIssue cloneIssue = new RemoteIssue();
      cloneIssue.setProject(originalJiraIssue.getProject());
      cloneIssue.setType("13");
      cloneIssue.setComponents(originalJiraIssue.getComponents());
      cloneIssue.setSummary("[Merge for " + originalJiraIssue.getKey() + "] " + originalJiraIssue.getSummary());

      cloneIssue.setAffectsVersions(originalJiraIssue.getAffectsVersions());

      RemoteVersion[] versions = jiraSoapService.getVersions(getToken(), PlannerHelper.getProjectKey(jira));
      List<RemoteVersion> cloneFixVersions = new ArrayList<RemoteVersion>();
      for (RemoteVersion remoteVersion : versions) {
         System.out.println("RemoteVersion: " + remoteVersion.getName());
         if (remoteVersion.getName().trim().equalsIgnoreCase(mergeFixVersionName.trim())) {
            System.out.println("match: " + mergeFixVersionName);
            cloneFixVersions.add(remoteVersion);
         }
      }
      cloneIssue.setFixVersions(cloneFixVersions.toArray(new RemoteVersion[cloneFixVersions.size()]));

      RemoteIssue createIssue = null;
      try {
         createIssue = jiraSoapService.createIssue(token, cloneIssue);
//         RemoteComment comment = new RemoteComment();
//         comment.setBody("Created Merge Jira: " + createIssue.getKey());
//         comment.setLevel(level);
         // jiraSoapService.addComment(getToken(), jira, comment);
      } catch (Throwable e) {
         e.printStackTrace();
      } finally {
         return createIssue;
      }
   }

   public RemoteVersion getFixVersion(final String fixName, JiraProject jiraProject) throws RemotePermissionException, RemoteAuthenticationException,
         RemoteException {
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
      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
               RemoteException {
            return jiraSoapService.getVersions(getToken(), jiraProject.getJiraKey());
         }

      });
      RemoteVersion[] versions = (RemoteVersion[]) command.execute();
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

   public RemoteField[] getFieldsForEdit(final String jiraKey) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      log.debug("getFieldsForEdit!");
      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
               RemoteException {
            return jiraSoapService.getFieldsForEdit(getToken(), jiraKey);
         }

      });
      RemoteField[] versions = (RemoteField[]) command.execute();
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

   public void updateSprint(final String jiraKey, final String sprint) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException, ServiceException {
      JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
      JiraSoapService jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();

      String token = jiraSoapService.login("soaptester", "soaptester");

      RemoteIssue issue = updateCustomField(jiraSoapService, jiraKey, sprint, "customfield_10282");
   }

   private RemoteIssue updateCustomField(final JiraSoapService jiraSoapService2, final String jiraKey, final String sprint, final String customField)
         throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      // JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
      // public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
      // RemoteException {
      String[] values = new String[] { sprint };
      RemoteFieldValue remoteFieldValue = new RemoteFieldValue(customField, values);
      RemoteFieldValue[] remoteFieldValues = new RemoteFieldValue[] { remoteFieldValue };
      return jiraSoapService2.updateIssue(getToken(), jiraKey, remoteFieldValues);
      // }
      //
      // });
      // RemoteIssue issue = (RemoteIssue) command.execute();
      // return issue;
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

   public RemoteFilter[] getFilters() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
         RemoteException {
      log.debug("Getting Fix Versions!");
      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
               RemoteException {
            return jiraSoapService.getSavedFilters(getToken());
         }

      });

      RemoteFilter[] filters = (RemoteFilter[]) command.execute();
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

   public RemoteResolution[] getResolutions() throws RemotePermissionException, RemoteAuthenticationException, RemoteException {
      log.debug("Getting Resolutions");

      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
               RemoteException {
            return jiraSoapService.getResolutions(getToken());
         }

      });
      return (RemoteResolution[]) command.execute();
   }

   public RemoteIssueType[] getTypes() {
      log.debug("Getting Resolutions");

      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
               RemoteException {
            log.debug("Accessing: " + jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
            return jiraSoapService.getIssueTypes(getToken());
         }

      });
      // FIXME doesn't work on TST project!!
      RemoteIssueType[] execute = null;
      try {
         execute = (RemoteIssueType[]) command.execute();
      } catch (Throwable e) {
         e.printStackTrace();
      }
      return execute;
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

   private synchronized void renewToken() throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      log.debug("Renewing Token");
      token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);
      log.debug("Renewing Token Done!");
   }

   interface JiraAccessAction {
      Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
            RemoteException;
   }

   class JiraTokenCommand {

      private final JiraAccessAction action;
      private final Logger log = MyLogger.getLogger(JiraTokenCommand.class);

      public JiraTokenCommand(JiraAccessAction action) {
         this.action = action;
      }

      public Object execute() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
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
   void printAllCustomFieldInfo() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
         RemoteException {
      log.debug("Getting Resolutions");

      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public RemoteField[] accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
            return jiraSoapService.getCustomFields(getToken());
         }

      });
      RemoteField[] execute = (RemoteField[]) command.execute();
      for (RemoteField remoteField : execute) {
         log.debug("Getting RemoteField: " + remoteField.getId() + " - " + remoteField.getName());
      }
   }

}
