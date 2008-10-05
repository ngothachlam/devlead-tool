package com.jonas.jira.access;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteField;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteIssueType;
import com.atlassian.jira.rpc.soap.beans.RemoteResolution;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
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

   public RemoteIssue getJira(final String jiraName) throws RemotePermissionException, RemoteAuthenticationException,
         com.atlassian.jira.rpc.exception.RemoteException, RemoteException, JiraIssueNotFoundException {
      log.debug("Getting Jira " + jiraName);
      JiraTokenCommand command = new JiraTokenCommand(new JiraAccessAction() {

         public Object accessJiraAndReturn() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
               RemoteException {
            log.trace("accessJiraAndReturn!!");
            String token2 = getToken();
            log.trace("Getting Jira Action!! (" + token2 + " - " + jiraName + ")");
            return jiraSoapService.getIssue(token2, jiraName);
         }

      });
      RemoteIssue execute = null;
      try {
         execute = (RemoteIssue) command.execute();
      } catch (java.rmi.RemoteException e) {
         AxisFault axisFault = (org.apache.axis.AxisFault) e;
         if ("java.lang.NullPointerException".equals(axisFault.getFaultString()))
            throw new JiraIssueNotFoundException("Jira \"" + jiraName + "\" not found!");
      }
      log.trace("Getting Jira Done!" + jiraName);
      return execute;
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

   public RemoteIssueType[] getTypes() throws RemotePermissionException, RemoteAuthenticationException, com.atlassian.jira.rpc.exception.RemoteException,
         RemoteException {
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
