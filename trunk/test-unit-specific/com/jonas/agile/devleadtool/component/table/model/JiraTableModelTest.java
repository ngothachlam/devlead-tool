package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class JiraTableModelTest extends JonasTestCase {

   private JiraTableModel model;

   private void assertRow(String[] strings, MyTableModel model, int row) {
      int cols = 0;
      for (; cols < strings.length; cols++) {
         Object valueAt = model.getValueAt(row, cols);
         assertEquals("Value at column " + cols + " is incorrect", strings[cols], valueAt);
      }
      assertEquals("Amount of cols are not as expected! (Expected : " + cols + " but is: " + model.getColumnCount() + ")", cols, model.getColumnCount());
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
      header.add(Column.B_Release);
      header.add(Column.J_Sprint);
      header.add(Column.J_FixVersion);
      header.add(Column.J_Status);
      header.add(Column.J_Resolution);
      header.add(Column.J_BuildNo);
      header.add(Column.J_Dev_Estimate);
      header.add(Column.J_Dev_Spent);
      
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      String[] content = new String[] { "Jira", "Description", "B_BoardStatus", "J_Type", "B_Release", "J_Sprint", "J_FixVersion", "J_Status", "J_Resolution", "J_BuildNo", "J_Dev_Estimate", "J_Dev_Spent" };
      contents.add(getTestRowVector(content, 0));
      contents.add(getTestRowVector(content, 1));

      model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(12, model.getColumnCount());
      assertRow(getTestRowArray(content, 0), model, 0);
      assertRow(getTestRowArray(content, 1), model, 1);
   }

   private String[] getTestRowArray(String[] content, int i) {
      String[] result = new String[content.length];
      for (int j = 0; j < content.length; j++) {
         result[j] = content[j] + "-"+i;
      }
      return result;
   }

   public void testShouldAddRowOk() {
      model = new JiraTableModel();
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved", "Type");
      JiraIssue jiraIssue2 = new JiraIssue("Jira-2", "Summary 2", "Open", "Resolved", "Type");
      model.addJira("Jira-1");
   }

   public void testShouldCompareEstimatesOk(){
      model = new JiraTableModel();
      assertEquals(true, model.isJiraEstimatesOk("", null));
      assertEquals(true, model.isJiraEstimatesOk("", ""));
      assertEquals(true, model.isJiraEstimatesOk("", "0"));
      assertEquals(true, model.isJiraEstimatesOk("", "0.0"));
      assertEquals(false, model.isJiraEstimatesOk("", "0.1"));
      
      assertEquals(true, model.isJiraEstimatesOk(null, ""));
      assertEquals(true, model.isJiraEstimatesOk(null, "0"));
      assertEquals(true, model.isJiraEstimatesOk(null, "0.0"));
      assertEquals(false, model.isJiraEstimatesOk(null, "0.1"));
      
      assertEquals(false, model.isJiraEstimatesOk("1.0", null));
      assertEquals(false, model.isJiraEstimatesOk("1.0", ""));
      assertEquals(false, model.isJiraEstimatesOk("1.0", "0"));
      assertEquals(false, model.isJiraEstimatesOk("1.0", "0.0"));
      
      assertEquals(false, model.isJiraEstimatesOk("0.9", "1.0"));
      assertEquals(false, model.isJiraEstimatesOk("1.0", "0.9"));
      assertEquals(true, model.isJiraEstimatesOk("1.0", "1.0"));
      assertEquals(false, model.isJiraEstimatesOk("1.1", "1.0"));
      assertEquals(false, model.isJiraEstimatesOk("1.0", "1.1"));
      
      assertEquals(true, model.isJiraEstimatesOk("merge", null));
      assertEquals(true, model.isJiraEstimatesOk("merge", ""));
      assertEquals(true, model.isJiraEstimatesOk("merge", "0"));
      assertEquals(false, model.isJiraEstimatesOk("merge", "0.1"));
      assertEquals(false, model.isJiraEstimatesOk("merge", "1.1"));
   }
}
