package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class JiraTableModelTest extends JonasTestCase {

   public void testGetConvertionNumbersWithStandardColumnsOk() {
      JiraTableModel model = new JiraTableModel();
      Vector<Column> header = new Vector<Column>();
      getMixedHeader(header);
      List convert = model.getConvertionNumbers(header, JiraTableModel.columnNames);

      assertEquals(1, convert.get(0));
      assertEquals(0, convert.get(1));
      assertEquals(3, convert.get(2));
      assertEquals(2, convert.get(3));
      assertEquals(5, convert.get(4));
      assertEquals(4, convert.get(5));
      assertEquals(7, convert.get(6));
      assertEquals(6, convert.get(7));
   }

   public void testGetConvertionNumbersWithFewerColumnsOk() {
      JiraTableModel model = new JiraTableModel();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.FixVersion); //2
      header.add(Column.Jira); //0

      List convert = model.getConvertionNumbers(header, JiraTableModel.columnNames);

      assertEquals(8, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(-1, convert.get(1));
      assertEquals(0, convert.get(2));
      assertEquals(-1, convert.get(3));
      assertEquals(-1, convert.get(4));
      assertEquals(-1, convert.get(5));
      assertEquals(-1, convert.get(6));
      assertEquals(-1, convert.get(7));
   }

   public void testShouldSortVectorBasedOnListOk() {
      JiraTableModel model = new JiraTableModel();

      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(0);
      originalList.add(3);
      originalList.add(2);
      originalList.add(5);
      originalList.add(4);
      originalList.add(7);
      originalList.add(6);

      Vector<Object> mixedRowVector = getMixedRowVector(0);

      Vector result = model.sortVectorBasedOnList(originalList, mixedRowVector);
      assertEquals("Jira-0", result.get(0));
      assertEquals("Description 0", result.get(1));
      assertEquals("FixVersion 0", result.get(2));
      assertEquals("Status 0", result.get(3));
      assertEquals("Resolution 0", result.get(4));
      assertEquals("BuildNo 0", result.get(5));
      assertEquals("URL 0", result.get(6));
      assertEquals("BoardStatus 0", result.get(7));
   }

   public void testShouldSortVectorBasedOnListWithFewerOk() {
      JiraTableModel model = new JiraTableModel();

      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(0);
      originalList.add(3);
      originalList.add(2);
      originalList.add(-1);
      originalList.add(4);
      originalList.add(7);
      originalList.add(6);

      Vector<Object> mixedRowVector = new Vector<Object>();
      int i = 0;
      mixedRowVector.add("Description " + i);
      mixedRowVector.add("Jira-" + i);
      mixedRowVector.add("Status " + i);
      mixedRowVector.add("FixVersion " + i);
      mixedRowVector.add("BuildNo " + i);
      mixedRowVector.add("Resolution " + i);
      mixedRowVector.add("BoardStatus " + i);
      mixedRowVector.add("URL " + i);

      Vector result = model.sortVectorBasedOnList(originalList, mixedRowVector);
      assertEquals("Jira-0", result.get(0));
      assertEquals("Description 0", result.get(1));
      assertEquals("FixVersion 0", result.get(2));
      assertEquals("Status 0", result.get(3));
      assertEquals(null, result.get(4));
      assertEquals("BuildNo 0", result.get(5));
      assertEquals("URL 0", result.get(6));
      assertEquals("BoardStatus 0", result.get(7));
   }

   public void testShouldCalculateAlreadyExistsOk() {
      JiraTableModel model = new JiraTableModel();
      String jira = "Jira-1";
      assertFalse(model.exists(jira));
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved");
      model.addRow(jiraIssue);
      assertTrue(model.exists(jira));
   }

   public void testShouldAddRowOk() {
      JiraTableModel model = new JiraTableModel();
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved");
      JiraIssue jiraIssue2 = new JiraIssue("Jira-2", "Summary 2", "Open", "Resolved");
      assertTrue(model.addRow(jiraIssue));
      assertFalse(model.addRow(jiraIssue));
      assertTrue(model.addRow(jiraIssue2));
   }

   public void testShouldBeCreatedFromDAOOk() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();

      header.add(Column.Jira);
      header.add(Column.Description);
      header.add(Column.FixVersion);
      header.add(Column.Status);
      header.add(Column.Resolution);
      header.add(Column.BuildNo);
      header.add(Column.URL);
      header.add(Column.BoardStatus);

      contents.add(getRowVector(0));
      contents.add(getRowVector(1));
      contents.add(getMixedRowVector(2));

      JiraTableModel model = new JiraTableModel(contents, header);

      assertRow(model, 0);
      assertRow(model, 1);
   }

   public void testShouldBeCreatedFromDAOOkWhenColsAreMixedUp() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();

      getMixedHeader(header);
      contents.add(getMixedRowVector(0));
      contents.add(getMixedRowVector(1));

      JiraTableModel model = new JiraTableModel(contents, header);

      assertRow(model, 0);
      assertRow(model, 1);
   }

   public void testShouldBeCreatedFromDAOOkWhenColsAreMixedUpAndLessThanOriginal() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();

      header.add(Column.Jira);
      header.add(Column.FixVersion);
      header.add(Column.Resolution);
      header.add(Column.URL);

      Vector<Object> vector = new Vector<Object>();
      int i = 0;
      vector.add("Jira-" + i);
      vector.add("FixVersion " + i);
      vector.add("Resolution " + i);
      vector.add("URL " + i);

      contents.add(vector);
      contents.add(getMixedRowVector(1));

      JiraTableModel model = new JiraTableModel(contents, header);

      assertEquals(8, model.getColumnCount());
      assertEquals("Jira-" + i, model.getValueAt(i, 0));
      assertEquals(null, model.getValueAt(i, 1));
      assertEquals("FixVersion " + i, model.getValueAt(i, 2));
      assertEquals(null, model.getValueAt(i, 3));
      assertEquals("Resolution " + i, model.getValueAt(i, 4));
      assertEquals(null, model.getValueAt(i, 5));
      assertEquals("URL " + i, model.getValueAt(i, 6));
      assertEquals(null, model.getValueAt(i, 7));
   }

   public void testShouldBeCreatedFromDAOOkWhenColsAreMixedUpAndMoreThanOriginal() {
      assertTrue(false);
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

   private void assertRow(JiraTableModel model, int i) {
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

   private Vector<Object> getRowVector(int i) {
      Vector<Object> vector = new Vector<Object>();
      vector.add("Jira-" + i);
      vector.add("Description " + i);
      vector.add("FixVersion " + i);
      vector.add("Status " + i);
      vector.add("Resolution " + i);
      vector.add("BuildNo " + i);
      vector.add("URL " + i);
      vector.add("BoardStatus " + i);
      return vector;
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
}
