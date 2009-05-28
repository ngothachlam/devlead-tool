package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteIssueType;
import com.atlassian.jira.rpc.soap.beans.RemoteResolution;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraResolution;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.listener.JiraListener;
import com.jonas.jira.jirastat.criteria.JiraCriteriaBuilder;
import com.jonas.jira.jirastat.criteria.JiraHttpCriteria;
import com.jonas.jira.utils.JiraBuilder;

public class JiraClient {

   private static final JonasXpathEvaluator JONAS_XPATH_EVALUATOR = new JonasXpathEvaluator("/rss/channel/item", new XMLOutputter());

   public static final JiraClient JiraClientAolBB = new JiraClient(JiraHttpClient.AOLBB, JiraSoapClient.AOLBB, JiraBuilder.getInstance());
   public static final JiraClient JiraClientAtlassin = new JiraClient(JiraHttpClient.ATLASSIN, JiraSoapClient.ATLASSIN, JiraBuilder.getInstance());

   private JiraHttpClient httpClient;
   private JiraBuilder jiraBuilder;
   private JiraSoapClient jiraSoapClient;

   private JiraClient(JiraHttpClient jiraHttpClient, JiraSoapClient jiraSoapClient, JiraBuilder jiraBuilder) {
      this.httpClient = jiraHttpClient;
      this.jiraSoapClient = jiraSoapClient;
      this.jiraBuilder = jiraBuilder;
   }

   private void cacheJiraVersions(RemoteVersion[] fixVersions, JiraProject jiraProject) {
      for (RemoteVersion remoteVersion : fixVersions) {
         jiraBuilder.cachedJiraVersion(remoteVersion, jiraProject);
      }
   }

   public JiraVersion[] getFixVersionsFromProject(JiraProject jiraProject, boolean includeArchived) throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      return getFixVersionsFromProject(jiraProject, includeArchived, true);

   }

   public JiraVersion[] getFixVersionsFromProject(JiraProject jiraProject, boolean includeArchived, boolean includeNonArchived) throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      cacheJiraVersionsForProject(jiraProject);
      return jiraProject.getFixVersions(includeArchived, includeNonArchived);
   }

   public void cacheJiraVersionsForProject(JiraProject jiraProject) throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      JiraListener.notifyListenersOfAccess(JiraListener.JiraAccessUpdate.GETTING_FIXVERSION);
      RemoteVersion[] fixVersions = jiraSoapClient.getFixVersions(jiraProject);
      cacheJiraVersions(fixVersions, jiraProject);
   }

   public JiraIssue getJira(String jira) throws HttpException, IOException, JDOMException, JiraException {
      JiraListener.notifyListenersOfAccess(JiraListener.JiraAccessUpdate.GETTING_FIXVERSION);
      loadJiraTypesIfRequired();
      loadResolutionsIfRequired();
      JiraIssue jiraIssue = httpClient.getJira(jira, JONAS_XPATH_EVALUATOR, jiraBuilder);
      return jiraIssue;
   }

   public JiraIssue[] getJiras(JiraCriteriaBuilder criteriaBuilder) throws HttpException, IOException, JDOMException, JiraException {
      loadResolutionsIfRequired();
      loadJiraTypesIfRequired();
      List<JiraIssue> jiras = httpClient.getJiras(JONAS_XPATH_EVALUATOR, jiraBuilder, criteriaBuilder);
      return jiras.toArray(new JiraIssue[jiras.size()]);
   }

   public String getJiraUrl() {
      return httpClient.getJiraUrl();
   }

   private void loadResolutions() throws RemotePermissionException, RemoteAuthenticationException, java.rmi.RemoteException {
      RemoteResolution[] remoteResolutions = jiraSoapClient.getResolutions();
      JiraResolution.setResolutions(remoteResolutions);
   }

   private void loadJiraTypes() {
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

   private void loadJiraTypesIfRequired() {
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

   public void updateSprint(String jiraKey, String sprint) throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException, ServiceException {
      jiraSoapClient.updateSprint(jiraKey, sprint);
   }

   public String createMergeJira(String jira, String fixForMerge) throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
      return jiraSoapClient.createMergeJira(jira, fixForMerge).getKey();
   }

   public String getBaseUrl() {
      return httpClient.getBaseUrl();
   }
}
