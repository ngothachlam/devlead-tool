package com.jonas.agile.devleadtool.component.table.model;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class JiraTableModelTest extends JonasTestCase {

   public void testShouldCalculateAlreadyExistsOk() {
      JiraTableModel model = new JiraTableModel();
      String jira = "LLU-1";
      assertFalse(model.exists(jira));
      JiraIssue jiraIssue = new JiraIssue("LLU-1", "Summary 1", "Open", "Resolved");
      model.addRow(jiraIssue);
      assertTrue(model.exists(jira));
   }

   public void testShouldAddRowOk() {
      JiraTableModel model = new JiraTableModel();
      JiraIssue jiraIssue = new JiraIssue("LLU-1", "Summary 1",  "Open", "Resolved");
      JiraIssue jiraIssue2 = new JiraIssue("LLU-2", "Summary 2", "Open", "Resolved");
      assertTrue(model.addRow(jiraIssue));
      assertFalse(model.addRow(jiraIssue));
      assertTrue(model.addRow(jiraIssue2));
   }

}
