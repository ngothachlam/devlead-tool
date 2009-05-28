package com.jonas.jira.access;

import java.io.IOException;
import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.TestObjects;
import com.jonas.jira.jirastat.criteria.JiraCriteriaBuilder;

public class JiraClientTest extends JonasTestCase {

   JiraClient jiraClient = null;

   @Override
   public void setUp() throws Exception {
      super.setUp();
      jiraClient = JiraClient.JiraClientAtlassin;
   }

   public void testShouldGetFixVersionsOk() throws HttpException, IOException, JiraException {
      jiraClient.login();
      JiraVersion[] fixVersions = jiraClient.getFixVersionsFromProject(JiraProject.ATLASSIN_TST, false);
      assertEquals(5, fixVersions.length);
   }

   public void testShouldGetFixVersionsOkIfThereAreNoJiras() throws HttpException, IOException, JDOMException, JiraException {
      jiraClient.login();
      JiraIssue[] jiras = null;
      try {
         JiraVersion jiraVersion = new JiraVersion("1", JiraProject.ATLASSIN_TST, "empty", false);
         JiraCriteriaBuilder builder = new JiraCriteriaBuilder().fixVersion(jiraVersion);
         jiras = jiraClient.getJiras(builder);
         assertTrue(false);
      } catch (JiraException e) {
      }
      assertEquals(0, jiras.length);
   }

   public void testShouldGetJirasForFixVersionOk() throws HttpException, IOException, JDOMException, JiraException {
      jiraClient.login();
      JiraCriteriaBuilder builder = new JiraCriteriaBuilder().fixVersion(TestObjects.Version_AtlassainTST);
      JiraIssue[] jiras = jiraClient.getJiras(builder);;
      assertTrue(jiras.length > 0);
   }

}
