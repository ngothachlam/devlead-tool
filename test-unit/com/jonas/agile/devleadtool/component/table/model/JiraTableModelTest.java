package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class JiraTableModelTest extends JonasTestCase {

   private void assertRow(MyTableModel model, int i) {
      assertEquals(8, model.getColumnCount());
      assertEquals("Jira-" + i, model.getValueAt(i, 0));
      assertEquals("Description " + i, model.getValueAt(i, 1));
      assertEquals("FixVersion " + i, model.getValueAt(i, 2));
      assertEquals("Status " + i, model.getValueAt(i, 3));
      assertEquals("Resolution " + i, model.getValueAt(i, 4));
      assertEquals("BuildNo " + i, model.getValueAt(i, 5));
      assertEquals("URL " + i, model.getValueAt(i, 6));
      assertEquals("BoardStatus " + i, model.getValueAt(i, 7));
   }

   private void assertRow(String[] strings, MyTableModel model, int row) {
      for (int col = 0; col < strings.length; col++) {
         assertEquals(strings[col], model.getValueAt(row, col));
      }
   }

   private Vector<Object> getARowVector(String[] strings, int i) {
      Vector<Object> vector = new Vector<Object>();
      for (String string : strings) {
         vector.add(string + "-" + i);
      }
      return vector;
   }

   private void getMixedHeader(Vector<Column> header) {
      header.add(Column.Description);
      header.add(Column.Jira);
      header.add(Column.Status);
      header.add(Column.FixVersion);
      header.add(Column.BuildNo);
      header.add(Column.Resolution);
      header.add(Column.BoardStatus);
      header.add(Column.URL);
   }

   private Vector<Object> getMixedRowVector(int i) {
      Vector<Object> vector = new Vector<Object>();
      vector.add("Description " + i);
      vector.add("Jira-" + i);
      vector.add("Status " + i);
      vector.add("FixVersion " + i);
      vector.add("BuildNo " + i);
      vector.add("Resolution " + i);
      vector.add("BoardStatus " + i);
      vector.add("URL " + i);
      return vector;
   }

   public void testShouldConvertionNumbersWithStandardColumnsOk() {
      MyTableModel model = new JiraTableModel();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);

      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Description, 1);

      List convert = model.getConvertionNumbers(header, columnNames);

      assertEquals(2, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(0, convert.get(1));
   }

   public void testShouldConvertionNumbersWithFewerColumnsOk() {
      MyTableModel model = new JiraTableModel();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);
      
      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Note, 1);
      columnNames.put(Column.BuildNo, 2);
      columnNames.put(Column.Description, 3);
      
      List convert = model.getConvertionNumbers(header, columnNames);
      
      assertEquals(4, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(-1, convert.get(1));
      assertEquals(-1, convert.get(2));
      assertEquals(0, convert.get(3));
   }
   
   public void testShouldConvertionNumbersWithMoreColumnsOk() {
      MyTableModel model = new JiraTableModel();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);
      
      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Note, 1);
      columnNames.put(Column.BuildNo, 2);
      columnNames.put(Column.Description, 3);
      
      List convert = model.getConvertionNumbers(header, columnNames);
      
      assertEquals(4, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(-1, convert.get(1));
      assertEquals(-1, convert.get(2));
      assertEquals(0, convert.get(3));
      assertTrue(false);
   }
   
   public void testShouldSortVectorBasedOnListWithStandardColumnsOk() {
      MyTableModel model = new JiraTableModel();

      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(0);

      Vector<Object> mixedRowVector = new Vector<Object>();
      mixedRowVector.add("Description");
      mixedRowVector.add("Jira");

      Vector result = model.sortVectorBasedOnList(originalList, mixedRowVector);
      assertEquals("Jira", result.get(0));
      assertEquals("Description", result.get(1));
      assertEquals(2, result.size());
   }

   public void testShouldSortVectorBasedOnListWithFewerColumnsOk() {
      MyTableModel model = new JiraTableModel();

      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(-1);
      originalList.add(0);
      originalList.add(-1);

      Vector<Object> mixedRowVector = new Vector<Object>();
      mixedRowVector.add("Description");
      mixedRowVector.add("Jira");

      Vector result = model.sortVectorBasedOnList(originalList, mixedRowVector);
      assertEquals("Jira", result.get(0));
      assertEquals(null, result.get(1));
      assertEquals("Description", result.get(2));
      assertEquals(null, result.get(3));
      assertEquals(4, result.size());
   }

   public void testShouldBeConstructedFromDAOOk() {
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Jira);
      header.add(Column.Description);
      header.add(Column.FixVersion);
      header.add(Column.Status);
      header.add(Column.Resolution);
      header.add(Column.BuildNo);
      header.add(Column.Estimate);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      contents.add(getARowVector(new String[] { "Jira", "Description", "FixVersion", "Status", "Resolution", "BuildNo", "Estimate" }, 0));
      contents.add(getARowVector(new String[] { "Jira", "Description", "FixVersion", "Status", "Resolution", "BuildNo", "Estimate" }, 1));

      MyTableModel model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertRow(new String[] { "Jira-0", "Description-0", "FixVersion-0", "Status-0", "Resolution-0", "BuildNo-0", "Estimate-0" }, model, 0);
      assertRow(new String[] { "Jira-1", "Description-1", "FixVersion-1", "Status-1", "Resolution-1", "BuildNo-1", "Estimate-1" }, model, 1);
   }

   public void testShouldBeConstructedFromDAOOkWhenColsAreMixedUp() {
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);
      header.add(Column.Status);
      header.add(Column.FixVersion);
      header.add(Column.BuildNo);
      header.add(Column.Resolution);
      header.add(Column.Estimate);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      contents.add(getARowVector(new String[] { "Description", "Jira", "Status", "FixVersion", "BuildNo", "Resolution", "Estimate" }, 0));
      contents.add(getARowVector(new String[] { "Description", "Jira", "Status", "FixVersion", "BuildNo", "Resolution", "Estimate" }, 1));

      MyTableModel model = new JiraTableModel(contents, header);

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
      contents.add(getARowVector(new String[] { "Description", "Jira" }, 0));
      contents.add(getARowVector(new String[] { "Description", "Jira" }, 1));

      MyTableModel model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertRow(new String[] { "Jira-0", "Description-0", null, null, null, null, null }, model, 0);
      assertRow(new String[] { "Jira-1", "Description-1", null, null, null, null, null }, model, 1);
   }

   public void testShouldBeConstructedFromDAOOkWhenColsAreMixedUpAndMoreThanOriginal() {
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);
      header.add(Column.Status);
      header.add(Column.FixVersion);
      header.add(Column.BuildNo);
      header.add(Column.Resolution);
      header.add(Column.Estimate);
      header.add(Column.Note);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      contents.add(getARowVector(new String[] { "Description", "Jira", "Status", "FixVersion", "BuildNo", "Resolution", "Estimate" }, 0));
      contents.add(getARowVector(new String[] { "Description", "Jira", "Status", "FixVersion", "BuildNo", "Resolution", "Estimate" }, 1));

      MyTableModel model = new JiraTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertRow(new String[] { "Jira-0", "Description-0", "FixVersion-0", "Status-0", "Resolution-0", "BuildNo-0", "Estimate-0" }, model, 0);
      assertRow(new String[] { "Jira-1", "Description-1", "FixVersion-1", "Status-1", "Resolution-1", "BuildNo-1", "Estimate-1" }, model, 1);
   }

   public void testShouldAddRowOk() {
      JiraTableModel model = new JiraTableModel();
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved");
      JiraIssue jiraIssue2 = new JiraIssue("Jira-2", "Summary 2", "Open", "Resolved");
      assertTrue(model.addRow(jiraIssue));
      assertFalse(model.addRow(jiraIssue));
      assertTrue(model.addRow(jiraIssue2));
   }

   public void testShouldCalculateAlreadyExistsOk() {
      JiraTableModel model = new JiraTableModel();
      String jira = "Jira-1";
      assertFalse(model.exists(jira));
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved");
      model.addRow(jiraIssue);
      assertTrue(model.exists(jira));
   }
}
