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
import com.jonas.jira.JiraProject;

/**
 * Sample JIRA SOAP client. Note that the constants sit in the {@link ClientConstants} interface
 */
public class JiraSoapClient {

   private static final Logger log = MyLogger.getLogger(JiraSoapClient.class);
   private static final String LOGIN_NAME = "soaptester";
   private static final String LOGIN_PASSWORD = "soaptester";
   private static JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator;
   private static List<String> customFieldsForCloning = new ArrayList<String>(1);

   static {
      customFieldsForCloning.add("customfield_10282");
   }

   private JiraSoapService jiraSoapService = null;
   private String token;
   private String type = "13";

   public JiraSoapClient(String address) {
      jiraSoapServiceServiceLocator = getLocator();
      jiraSoapServiceServiceLocator.setJirasoapserviceV2EndpointAddress(address);
      log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
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

   @SuppressWarnings("finally")
   public RemoteIssue createMergeJira(final String jira, final String mergeFixVersionName) throws RemotePermissionException,
         RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      RemoteIssue originalJiraIssue = jiraSoapService.getIssue(getToken(), jira);

      RemoteIssue cloneIssue = new RemoteIssue();
      cloneIssue.setProject(originalJiraIssue.getProject());
      cloneIssue.setType(type);
      cloneIssue.setComponents(originalJiraIssue.getComponents());
      cloneIssue.setSummary("[Merge for " + originalJiraIssue.getKey() + "] " + originalJiraIssue.getSummary());
      cloneIssue.setAffectsVersions(originalJiraIssue.getAffectsVersions());
      cloneIssue.setPriority("1");
      setOriginalEstimateToZero(cloneIssue);

      RemoteVersion[] clonedFixVersions = setFixVersionOnClone(jira, mergeFixVersionName, cloneIssue);
      copyCustomFieldValuesFromOriginalToClone(originalJiraIssue, cloneIssue);

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

   private void setOriginalEstimateToZero(RemoteIssue cloneIssue) {
      String[] originalEstimates = { "0d" };
      RemoteCustomFieldValue[] customFieldValues = { new RemoteCustomFieldValue("timetracking", "timetracking", originalEstimates) };
      cloneIssue.setCustomFieldValues(customFieldValues);
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

   private void copyCustomFieldValuesFromOriginalToClone(RemoteIssue originalJiraIssue, RemoteIssue cloneIssue) {
      RemoteCustomFieldValue[] originalCustomFieldValues = originalJiraIssue.getCustomFieldValues();
      List<RemoteCustomFieldValue> fieldsToBeCloned = new ArrayList<RemoteCustomFieldValue>();

      int matches = 0;
      for (RemoteCustomFieldValue field : originalCustomFieldValues) {
         // log.debug("CustomField Value Key: " + field.getKey() + " getCustomfieldId: " + field.getCustomfieldId());
         // String[] values = field.getValues();
         // for (String string : values) {
         // log.debug("\tValue: " + string);
         // }
         if (customFieldsForCloning.contains(field.getCustomfieldId())) {
            fieldsToBeCloned.add(field);
            matches++;
         }
      }
      cloneIssue.setCustomFieldValues(fieldsToBeCloned.toArray(new RemoteCustomFieldValue[fieldsToBeCloned.size()]));
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
      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
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
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
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
      // JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
      // JiraSoapService jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();

      updateCustomField(jiraKey, sprint, "customfield_10282");
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
      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
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
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
            return jiraSoapService.getResolutions(getToken());
         }

      });
      return (RemoteResolution[]) command.execute();
   }

   public RemoteIssueType[] getTypes() {
      log.debug("Getting Resolutions");

      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {
         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
               com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
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

   private synchronized void renewToken() throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
         RemoteException {
      log.debug("Renewing Token");
      token = jiraSoapService.login(LOGIN_NAME, LOGIN_PASSWORD);
      log.debug("Renewing Token Done!");
   }

   interface JiraAccessAction {
      Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException,
            com.atlassian.jira.rpc.exception.RemoteException, RemoteException;
   }

   class JiraTokenCommand {

      private final JiraAccessAction action;
      private final Logger log = MyLogger.getLogger(JiraTokenCommand.class);

      public JiraTokenCommand(JiraAccessAction action) {
         this.action = action;
      }

      public Object execute() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
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

   public void login() throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      renewToken();
   }

   public void closeJira(String string) throws RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException, RemoteException {
      // Close: http://10.155.38.105/jira/secure/WorkflowUIDispatcher.jspa?id=43402&action=2
      // ReOpen: http://10.155.38.105/jira/secure/WorkflowUIDispatcher.jspa?id=43402&action=3
      RemoteNamedObject[] actions = jiraSoapService.getAvailableActions(getToken(), string);

      for (int i = 0; i < actions.length; i++) {
         RemoteNamedObject action = actions[i];
      }

   }

   public void testProgressWorkflow(String issueKey) throws java.rmi.RemoteException {
      System.out.println("Progressing workflow of " + issueKey + "...");
      RemoteNamedObject[] availableActions = jiraSoapService.getAvailableActions(getToken(), issueKey);
      String actionId = null;
      for (int i = 0; i < availableActions.length; i++) {
         RemoteNamedObject availableAction = availableActions[i];
         System.out.println("availableAction: " + availableAction.getId() + " - " + availableAction.getName());
         if (actionId == null)
            actionId = availableAction.getId();
      }

      // if (actionId != null) {
      // RemoteField[] fieldsForAction = jiraSoapService.getFieldsForAction(token, issueKey, actionId);
      // for (int i = 0; i < fieldsForAction.length; i++) {
      // RemoteField remoteField = fieldsForAction[i];
      // System.out.println("remoteField: " + remoteField.getId() + " - " + remoteField.getName());
      // }
      //
      // RemoteFieldValue[] actionParams = new RemoteFieldValue[] { new RemoteFieldValue("assignee", new String[] { LOGIN_NAME }) };
      //
      // RemoteIssue remoteIssue = jiraSoapService.progressWorkflowAction(token, issueKey, actionId, actionParams);
      // System.out.println("Progressed workflow of " + remoteIssue.getKey() + " to: " + remoteIssue.getStatus());
      // }
   }

   // RemoteNamedObject[] getAvailableActions(String token, String issueKey)
   //
   // For Example in Ruby:
   //
   // action = @jira.getAvailableActions("TEST-7")
   //
   // remoteField = RemoteFieldValue.new('status', action[0].id )
   //
   // updatedissue = @jira.updateIssue("TEST-7", remoteField)
   //
   // Regards,
   //      
   //      
   // RemoteFieldValue[] remoteFieldValues = new RemoteFieldValue[1];
   // remoteFieldValues[0] = new RemoteFieldValue("fixVersions", values.toArray(new String[values.size()]));
   // jiraSoapService.updateIssue(in0, in1, in2);

}


class CustomField {
   public CustomField sprint = new CustomField("customfield_10282", "Sprint");
   private String id;
   private String name;

   private CustomField(String id, String name) {
      super();
      this.id = id;
      this.name = name;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }
}