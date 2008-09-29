package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class JiraTableModelTest extends JonasTestCase {

   private MyTableModel model;

   private void assertRow(String[] strings, MyTableModel model, int row) {
      for (int col = 0; col < strings.length; col++) {
         assertEquals(strings[col], model.getValueAt(row, col));
      }
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
      header.add(Column.J_FixVersion);
      header.add(Column.J_Status);
      header.add(Column.J_Resolution);
      header.add(Column.J_BuildNo);
      header.add(Column.Dev_Estimate);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      contents.add(getTestRowVector(new String[] { "Jira", "Description", "FixVersion", "Status", "Resolution", "BuildNo", "Estimate" }, 0));
      contents.add(getTestRowVector(new String[] { "Jira", "Description", "FixVersion", "Status", "Resolution", "BuildNo", "Estimate" }, 1));

      model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(15, model.getColumnCount());
      assertRow(new String[] { "Jira-0", "Description-0", "FixVersion-0", "Status-0", "Resolution-0", "BuildNo-0", "Estimate-0" }, model, 0);
      assertRow(new String[] { "Jira-1", "Description-1", "FixVersion-1", "Status-1", "Resolution-1", "BuildNo-1", "Estimate-1" }, model, 1);
   }

   public void testShouldBeConstructedFromDAOOkWhenColsAreMixedUp() {
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);
      header.add(Column.J_Status);
      header.add(Column.J_FixVersion);
      header.add(Column.J_BuildNo);
      header.add(Column.J_Resolution);
      header.add(Column.Dev_Estimate);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      contents.add(getTestRowVector(new String[] { "Description", "Jira", "Status", "FixVersion", "BuildNo", "Resolution", "Estimate" }, 0));
      contents.add(getTestRowVector(new String[] { "Description", "Jira", "Status", "FixVersion", "BuildNo", "Resolution", "Estimate" }, 1));

      model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertRow(new String[] { "Jira-0", "Description-0", "FixVersion-0", "Status-0", "Resolution-0", "BuildNo-0", "Estimate-0" }, model, 0);
      assertRow(new String[] { "Jira-1", "Description-1", "FixVersion-1", "Status-1", "Resolution-1", "BuildNo-1", "Estimate-1" }, model, 1);
   }

   public void testShouldBeConstructedFromDAOOkWhenColsAreMixedUpAndLessThanOriginal() {
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      contents.add(getTestRowVector(new String[] { "Description", "Jira" }, 0));
      contents.add(getTestRowVector(new String[] { "Description", "Jira" }, 1));

      model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertRow(new String[] { "Jira-0", "Description-0", null, null, null, null, null }, model, 0);
      assertRow(new String[] { "Jira-1", "Description-1", null, null, null, null, null }, model, 1);
   }

   public void testShouldBeConstructedFromDAOOkWhenColsAreMixedUpAndMoreThanOriginal() {
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);
      header.add(Column.J_Status);
      header.add(Column.J_FixVersion);
      header.add(Column.J_BuildNo);
      header.add(Column.J_Resolution);
      header.add(Column.Dev_Estimate);
      header.add(Column.Note);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      contents.add(getTestRowVector(new String[] { "Description", "Jira", "Status", "FixVersion", "BuildNo", "Resolution", "Estimate" }, 0));
      contents.add(getTestRowVector(new String[] { "Description", "Jira", "Status", "FixVersion", "BuildNo", "Resolution", "Estimate" }, 1));

      model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertRow(new String[] { "Jira-0", "Description-0", "FixVersion-0", "Status-0", "Resolution-0", "BuildNo-0", "Estimate-0" }, model, 0);
      assertRow(new String[] { "Jira-1", "Description-1", "FixVersion-1", "Status-1", "Resolution-1", "BuildNo-1", "Estimate-1" }, model, 1);
   }

   public void testShouldAddRowOk() {
      model = new JiraTableModel();
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved", "Type");
      JiraIssue jiraIssue2 = new JiraIssue("Jira-2", "Summary 2", "Open", "Resolved", "Type");
      model.addJira("Jira-1");
   }


}
