package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteIssueType;
import com.atlassian.jira.rpc.soap.beans.RemoteResolution;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraResolution;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.utils.JiraBuilder;

public class JiraClient {

   private static final JonasXpathEvaluator JONAS_XPATH_EVALUATOR = new JonasXpathEvaluator("/rss/channel/item");
   
   public static final JiraClient JiraClientAolBB = new JiraClient(new JiraHttpClient(ClientConstants.JIRA_URL_AOLBB), new JiraSoapClient(
         ClientConstants.AOLBB_WS), JiraBuilder.getInstance());
   public static final JiraClient JiraClientAtlassin = new JiraClient(new JiraHttpClient(ClientConstants.JIRA_URL_ATLASSIN), new JiraSoapClient(
         ClientConstants.ATLASSIN_WS), JiraBuilder.getInstance());

   private JiraHttpClient httpClient;
   private JiraBuilder jiraBuilder;
   private JiraSoapClient jiraSoapClient;
   private Logger log = MyLogger.getLogger(JiraClient.class);

   public JiraClient(JiraHttpClient jiraHttpClient, JiraSoapClient jiraSoapClient, JiraBuilder jiraBuilder) {
      this.httpClient = jiraHttpClient;
      this.jiraSoapClient = jiraSoapClient;
      this.jiraBuilder = jiraBuilder;
   }

   private void buildJiraVersions(RemoteVersion[] fixVersions, JiraProject jiraProject) {
      jiraProject.clearFixVersions();
      for (int i = 0; i < fixVersions.length; i++) {
         jiraBuilder.buildJiraVersion(fixVersions[i], jiraProject);
      }
   }

   public JiraVersion[] getFixVersionsFromProject(JiraProject jiraProject, boolean isArchived) throws RemotePermissionException, RemoteAuthenticationException,
         RemoteException, java.rmi.RemoteException {
      JiraListener.notifyListenersOfAccess(JiraListener.JiraAccessUpdate.GETTING_FIXVERSION);
      RemoteVersion[] fixVersions = jiraSoapClient.getFixVersions(jiraProject);
      buildJiraVersions(fixVersions, jiraProject);
      return jiraProject.getFixVersions(isArchived);
   }

   public JiraIssue getJira(String jira, JiraProject project) throws HttpException, IOException, JDOMException, JiraException {
      JiraListener.notifyListenersOfAccess(JiraListener.JiraAccessUpdate.GETTING_FIXVERSION);
      loadJiraTypesIfRequired();
      loadResolutionsIfRequired();
      JiraIssue jiraIssue = httpClient.getJira(jira, JONAS_XPATH_EVALUATOR, jiraBuilder);
      return jiraIssue;
      // // TODO thread this!!
      // loadResolutionsIfRequired();
      // loadJiraTypesIfRequired();
      // JiraListener.notifyListenersOfAccess(JiraListener.JiraAccessUpdate.GETTING_JIRA);
      // RemoteIssue remoteJira = jiraSoapClient.getJira(jira.toUpperCase());
      // JiraIssue jiraIssue = jiraBuilder.buildJira(remoteJira, project);
      // if (jiraIssue == null) {
      // throw new JiraIssueNotFoundException("Jira [" + jira + "] doesn't exist in " + project.getJiraKey());
      // }
      // return jiraIssue;
   }

   //FIXME call this from a popup from a "Fix Version" button;
   public JiraIssue[] getJirasFromFixVersion(JiraVersion version) throws HttpException, IOException, JDOMException, JiraException {
      loadResolutionsIfRequired();
      loadJiraTypesIfRequired();
      List<JiraIssue> jiras = httpClient.getJiras(version, JONAS_XPATH_EVALUATOR, jiraBuilder);
      return (JiraIssue[]) jiras.toArray(new JiraIssue[jiras.size()]);
   }

   //FIXME call this from a popup similar to fix version;
   public JiraIssue[] getJirasFromFilter() throws HttpException, IOException, JiraException, JDOMException{
      loadResolutionsIfRequired();
      loadJiraTypesIfRequired();
      List<JiraIssue> jiras = httpClient.getJirasFromFilter(JiraFilter.DevsupportPrioFilter, JONAS_XPATH_EVALUATOR, jiraBuilder);
      return (JiraIssue[]) jiras.toArray(new JiraIssue[jiras.size()]);
   }
   
   public String getJiraUrl() {
      return httpClient.getJiraUrl();
   }

   private void loadResolutions() throws RemotePermissionException, RemoteAuthenticationException, java.rmi.RemoteException {
      RemoteResolution[] remoteResolutions = jiraSoapClient.getResolutions();
      JiraResolution.setResolutions(remoteResolutions);
   }

   private void loadJiraTypes() throws RemotePermissionException, RemoteAuthenticationException, java.rmi.RemoteException {
      RemoteIssueType[] remoteTypes = jiraSoapClient.getTypes();
      if (remoteTypes != null)
         JiraType.setTypes(remoteTypes);
   }

   private void loadResolutionsIfRequired() throws RemotePermissionException, RemoteAuthenticationException, java.rmi.RemoteException {
      if (JiraResolution.getAmount() < 1) {
         synchronized (JiraResolution.class) {
            if (JiraResolution.getAmount() < 1) {
               loadResolutions();
            }
         }
      }
   }

   private void loadJiraTypesIfRequired() throws RemotePermissionException, RemoteAuthenticationException, java.rmi.RemoteException {
      if (JiraType.getAmount() < 1) {
         synchronized (JiraType.class) {
            if (JiraType.getAmount() < 1) {
               loadJiraTypes();
            }
         }
      }
   }

   public void login() throws HttpException, IOException, JiraException {
      JiraListener.notifyListenersOfAccess(JiraListener.JiraAccessUpdate.LOGGING_IN);
      httpClient.loginToJira();
   }
}
