package com.jonas.agile.devleadtool.component.table.model;

import com.jonas.jira.JiraIssue;
import junit.framework.TestCase;

public class JiraTableModelTest extends TestCase {

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldCalculateAlreadyExistsOk() {
      JiraTableModel model = new JiraTableModel();
      String jira = "LLU-1";
      assertFalse(model.exists(jira));
      JiraIssue jiraIssue = new JiraIssue("LLU-1", "Open", "Resolved");
      model.addRow(jiraIssue);
      assertTrue(model.exists(jira));
   }

   public void testShouldAddRowOk() {
      JiraTableModel model = new JiraTableModel();
      JiraIssue jiraIssue = new JiraIssue("LLU-1", "Open", "Resolved");
      JiraIssue jiraIssue2 = new JiraIssue("LLU-2", "Open", "Resolved");
      assertTrue(model.addRow(jiraIssue));
      assertFalse(model.addRow(jiraIssue));
      assertTrue(model.addRow(jiraIssue2));
   }

}
