package com.jonas.jira.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraCloseHelper {

   JiraClient jiraClient = JiraClient.JiraClientAolBB;
   JiraSoapClient jiraSoapClient = JiraSoapClient.AOLBB;
   JiraHttpClient jiraHttpClient = JiraHttpClient.AOLBB;

   public static void main(String[] args) {
      JiraCloseHelper jiraCloser = new JiraCloseHelper();
      try {
         jiraCloser.closeAllResolvedJirasInArchivedFixVersions(JiraProject.TALK);
      } catch (Throwable e) {
         e.printStackTrace();
      }
   }

   private void closeAllResolvedJirasInArchivedFixVersions(JiraProject jiraProject) throws HttpException, IOException, JiraException, JDOMException {
      jiraHttpClient.loginToJira();

      List<JiraVersion> nonArchivedFixVersions = getNonArchivedFixVersions(jiraProject);
      closeAllResolvedJirasInFixVersions(nonArchivedFixVersions);

   }

   private List<JiraVersion> getNonArchivedFixVersions(JiraProject project) throws RemotePermissionException, RemoteAuthenticationException,
         RemoteException, java.rmi.RemoteException {
      List<JiraVersion> archivedFixVersions = new ArrayList<JiraVersion>();
      JiraVersion[] fixVersions = jiraClient.getFixVersionsFromProject(project, true);
      for (JiraVersion jiraVersion : fixVersions) {
         if (jiraVersion.isArchived()) {
            archivedFixVersions.add(jiraVersion);
         } 
      }
      return archivedFixVersions;
   }

   private void closeAllResolvedJirasInFixVersions(List<JiraVersion> relevantFixVersions) throws HttpException, IOException, JDOMException, JiraException {
      for (JiraVersion relevantFixVersion : relevantFixVersions) {
         String resolveJirasOnlyCriteria = "&status=1&status=3&status=4&status=5";
         JiraIssue[] jirasToClose = jiraClient.getJirasFromFixVersion(relevantFixVersion, resolveJirasOnlyCriteria);
         for (JiraIssue jiraToClose : jirasToClose) {
            boolean httpClose = false;
            try {
               httpClose = calculateResolutionEtcAndCloseJira(jiraToClose);
            } catch (JiraException e) {
            }
            System.out.println(httpClose ? "Closed " : "Did not manage to close" + jiraToClose.getKey());
         }
      }
   }

   private boolean calculateResolutionEtcAndCloseJira(JiraIssue jira) throws HttpException, IOException, JiraException {
      RemoteIssue remoteJira = jiraSoapClient.getJira(jira.getKey());
      JiraProject jiraProject = JiraProject.getProjectByJira(jira.getKey());
      jiraHttpClient.closeJira(remoteJira.getId(), remoteJira.getResolution(), jiraProject.getCloseAction());
      return true;
   }
}
