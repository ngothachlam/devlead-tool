package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class JiraTableModelTest extends JonasTestCase {

   private MyTableModel model;

   private void assertRow(String[] strings, MyTableModel model, int row) {
      int cols = 0;
      for (; cols < strings.length; cols++) {
         assertEquals(strings[cols], model.getValueAt(row, cols));
      }
      assertEquals(cols, model.getColumnCount());
   }

   private Vector<Object> getTestRowVector(String[] strings, int i) {
      Vector<Object> vector = new Vector<Object>();
      for (String string : strings) {
         vector.add(string + "-" + i);
      }
      return vector;
   }

   public void testShouldBeConstructedFromDAOOk() {
      Vector<Column> header = new Vector<Column>();

      header.add(Column.Jira);
      header.add(Column.Description);
      header.add(Column.B_BoardStatus);
      header.add(Column.J_Type);
      header.add(Column.Release);
      header.add(Column.J_FixVersion);
      header.add(Column.Planned_Sprint);
      header.add(Column.Resolved_Sprint);
      header.add(Column.Closed_Sprint);
      header.add(Column.J_Status);
      header.add(Column.J_Resolution);
      header.add(Column.J_BuildNo);
      header.add(Column.Dev_Estimate);
      header.add(Column.Dev_Actual);
      header.add(Column.J_Dev_Estimate);
      header.add(Column.J_Dev_Spent);
      header.add(Column.Note);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      String[] content = new String[] { "Jira", "Description", "B_BoardStatus", "J_Type", "B_Release", "J_FixVersion", "Planned_Sprint",
            "Resolved_Sprint", "Closed_Sprint", "J_Status", "J_Resolution", "J_BuildNo", "Dev_Estimate", "Dev_Actual", "J_Dev_Estimate",
            "J_Dev_Spent", "Note" };
      contents.add(getTestRowVector(content, 0));
      contents.add(getTestRowVector(content, 1));

      model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(12, model.getColumnCount());
      assertRow(new String[] { "Jira-0", "Description-0", "BoardStatus-0", "J_Type-0", "Release-0", "J_FixVersion-0", "Planned_Sprint-0",
            "Resolved_Sprint-0", "Closed_Sprint-0", "J_Status-0", "J_Resolution-0", "J_BuildNo-0", "Dev_Estimate-0", "Dev_Actual-0",
            "J_Dev_Estimate-0", "J_Dev_Spent-0", "Note-0" }, model, 0);
      assertRow(new String[] { "Jira-1", "Description-1", "BoardStatus-1", "J_Type-1", "Release-1", "J_FixVersion-1", "Planned_Sprint-1",
            "Resolved_Sprint-1", "Closed_Sprint-1", "J_Status-1", "J_Resolution-1", "J_BuildNo-1", "Dev_Estimate-1", "Dev_Actual-1",
            "J_Dev_Estimate-1", "J_Dev_Spent-1", "Note-1" }, model, 1);
   }

   public void testShouldAddRowOk() {
      model = new JiraTableModel();
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved", "Type");
      JiraIssue jiraIssue2 = new JiraIssue("Jira-2", "Summary 2", "Open", "Resolved", "Type");
      model.addJira("Jira-1");
   }

}
