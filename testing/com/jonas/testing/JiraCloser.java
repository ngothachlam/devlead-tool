package com.jonas.testing;

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
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.jira.access.JiraHttpClient;
import com.jonas.jira.access.JiraSoapClient;

public class JiraCloser {

   JiraClient jiraClient = JiraClient.JiraClientAolBB;
   JiraSoapClient jiraSoapClient = JiraSoapClient.AOLBB;
   JiraHttpClient jiraHttpClient = JiraHttpClient.AOLBB;

   public static void main(String[] args) {

      JiraCloser jiraCloser = new JiraCloser();

      try {
         jiraCloser.testCreateJira();
      } catch (Throwable e) {
         e.printStackTrace();
      }
   }

   private void testCreateJira() throws HttpException, IOException, JiraException, JDOMException {
      jiraHttpClient.loginToJira();

      List<JiraVersion> versionsToClear = getNonArchivedFixVersions(JiraProject.TALK);
      closeJirasAgainstFixVersion(versionsToClear);

   }

   private List<JiraVersion> getNonArchivedFixVersions(JiraProject project) throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
         java.rmi.RemoteException {
      JiraVersion[] fixVersions = jiraClient.getFixVersionsFromProject(project, true);
      List<JiraVersion> versionsToClear = new ArrayList<JiraVersion>();
      for (JiraVersion jiraVersion : fixVersions) {
         if (jiraVersion.isArchived()) {
            System.out.println("version added: " + jiraVersion.getName());
            versionsToClear.add(jiraVersion);
         } else {
            System.out.println("version skipped: " + jiraVersion.getName());
         }
      }
      return versionsToClear;
   }

   private void closeJirasAgainstFixVersion(List<JiraVersion> versionsToClear) throws HttpException, IOException, JDOMException, JiraException {
      for (JiraVersion jiraVersion : versionsToClear) {
         JiraIssue[] jiras = jiraClient.getJirasFromFixVersion(jiraVersion, "&status=1&status=3&status=4&status=5");
         System.out.println("version: " + jiraVersion.getName());
         for (JiraIssue jira : jiras) {
            boolean httpClose = false;
             try {
             httpClose = httpClose(jira);
             } catch (JiraException e) {
             }
            if (httpClose) {
               System.out.println("Closed " + jira.getKey());
            } else {
               System.out.println("Did not manage to close " + jira.getKey());
            }
         }
         try {
            Thread.sleep(2000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   private boolean httpClose(JiraIssue jira) throws HttpException, IOException, JiraException {
      RemoteIssue remoteJira = jiraSoapClient.getJira(jira.getKey());
      jiraHttpClient.closeJira(remoteJira.getId(), remoteJira.getResolution(), "701");
      return true;
   }
}
