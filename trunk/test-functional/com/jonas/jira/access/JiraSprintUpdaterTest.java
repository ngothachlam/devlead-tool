package com.jonas.jira.access;

import java.io.IOException;
import com.jonas.testHelpers.JiraXMLHelper;
import junit.framework.TestCase;

public class JiraSprintUpdaterTest extends TestCase {

   public void tedstShouldUpdateJiraOk() throws IOException, JiraException {
      JiraSprintUpdater updater = new JiraSprintUpdater("http://10.155.38.105/jira");
      updater.loginToJira();
      updater.updateSprint("LLU-4243", "42028", "Sprint 2");
   }
   
   public void testShouldCreateMergeJiraOk() throws IOException, JiraException {
      JiraSprintUpdater updater = new JiraSprintUpdater(ClientConstants.JIRA_URL_ATLASSIN);
      updater.loginToJira();
      updater.createClone("JRA-15915", "76216");
   }

   private void output(JiraXMLHelper helper, String url) throws IOException {
      System.out.println(helper.getXML(url));
   }

}
