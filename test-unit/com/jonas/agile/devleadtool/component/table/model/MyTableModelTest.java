package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.easymock.classextension.EasyMock;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.TestObjects;

public class MyTableModelTest extends JonasTestCase {

   public MyTableModelTest() {
      super();
   }

   private MyTableModel model;

   protected void setUp() throws Exception {
      super.setUp();
      model = new JiraTableModel();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldBeCreatedOk() {
      assertEquals(0, model.getRowCount());
      assertTrue(model.getColumnCount() > 0);
   }

   public void testShouldConvertionNumbersWithStandardColumnsOk() {
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
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);

      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Note, 1);
      columnNames.put(Column.J_BuildNo, 2);
      columnNames.put(Column.Description, 3);

      List convert = model.getConvertionNumbers(header, columnNames);

      assertEquals(4, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(-1, convert.get(1));
      assertEquals(-1, convert.get(2));
      assertEquals(0, convert.get(3));
   }

   public void testShouldConvertionNumbersWithMoreColumnsOk() {
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);
      header.add(Column.isBug);

      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Description, 1);

      List convert = model.getConvertionNumbers(header, columnNames);

      assertEquals(1, convert.get(0));
      assertEquals(0, convert.get(1));
      assertEquals(2, convert.size());
   }

   public void testShouldSortVectorBasedOnListWithStandardColumnsOk() {
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

   public void testShouldReturnFirstOccurenceInSetThatDoesNotExistInVector() {
      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Dev_Actual, 0);
      columnNames.put(Column.J_BuildNo, 1);
      columnNames.put(Column.Closed_Sprint, 2);

      Vector<Column> vector = new Vector<Column>();
      vector.add(Column.J_BuildNo);
      assertEquals(Column.Dev_Actual, model.findIndexThatDoesNotExist(columnNames, vector, 0));
      assertEquals(Column.Closed_Sprint, model.findIndexThatDoesNotExist(columnNames, vector, 1));
      assertEquals(null, model.findIndexThatDoesNotExist(columnNames, vector, 2));
   }

   public void testShouldSortHeaderBasedOnListWithFewerColumnsOk() {
      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(-1);
      originalList.add(0);
      originalList.add(-1);

      Vector<Column> mixedRowVector = new Vector<Column>();
      mixedRowVector.add(Column.Description);
      mixedRowVector.add(Column.Jira);

      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Description, 1);
      columnNames.put(Column.J_BuildNo, 2);
      columnNames.put(Column.J_FixVersion, 3);

      Vector<Column> result = model.sortHeaderBasedOnList(originalList, mixedRowVector, columnNames);
      assertEquals(Column.Jira, result.get(0));
      assertEquals(Column.J_BuildNo, result.get(1));
      assertEquals(Column.Description, result.get(2));
      assertEquals(Column.J_FixVersion, result.get(3));
      assertEquals(4, result.size());

   }

   public void testShouldAddRowsWhilstEditingCorrectly() {
      assertEquals(0, model.getRowCount());
      model.addEmptyRow();
      assertEquals(1, model.getRowCount());
      assertTrue(model.getColumnCount() > 1);
      model.setValueAt("123", 0, 0);
      assertEquals(1, model.getRowCount());
      assertEquals("123", model.getValueAt(0, 0));

      model.setValueAt("1234", 0, 0);
      assertEquals(1, model.getRowCount());
      assertEquals("1234", model.getValueAt(0, 0));

      model.addEmptyRow();
      model.setValueAt("1235", 1, 0);
      assertEquals(2, model.getRowCount());
      assertEquals("1234", model.getValueAt(0, 0));
      assertEquals("1235", model.getValueAt(1, 0));
   }

   public void testGetEmptyRowFromBoard() {
      // Column.Jira;
      // Column.Description;
      // Column.isOpen;
      // Column.isBug;
      // Column.isInProgress;
      // Column.isResolved;
      // Column.isComplete;
      // Column.ListPrio;
      
      model = new BoardTableModel();
      Object[] emptyRow = model.getEmptyRow();
      assertEquals(10, emptyRow.length);
      assertEquals("", emptyRow[0]);
      assertEquals("", emptyRow[1]);
      assertEquals(Boolean.FALSE, emptyRow[2]);
      assertEquals(Boolean.FALSE, emptyRow[3]);
      assertEquals(Boolean.FALSE, emptyRow[4]);
      assertEquals(Boolean.FALSE, emptyRow[5]);
      assertEquals(Boolean.FALSE, emptyRow[6]);
      assertEquals("", emptyRow[7]);
      assertEquals("", emptyRow[8]);
      assertEquals(null, emptyRow[9]);
   }

   public void testGetEmptyRowFromPlan() {
//      Column.Jira; 
//      Column.Description; 
//      Column.J_Type; 
//      Column.Planned_Sprint; 
//      Column.Resolved_Sprint; 
//      Column.Closed_Sprint; 
//      Column.Dev_Estimate; 
//      Column.QA_Estimate; 
//      Column.Dev_Actual; 
//      Column.Note;
      
      model = new PlanTableModel();

      Object[] emptyRow = model.getEmptyRow();
      assertEquals("", emptyRow[0]);
      assertEquals("", emptyRow[1]);
      assertEquals("", emptyRow[2]);
      assertEquals("", emptyRow[3]);
      assertEquals("", emptyRow[4]);
      assertEquals("", emptyRow[5]);
      assertEquals("", emptyRow[6]);
      assertEquals("", emptyRow[7]);
      assertEquals("", emptyRow[8]);
      assertEquals("", emptyRow[9]);
      assertEquals("", emptyRow[10]);
      assertEquals(11, emptyRow.length);
   }

   public void testShouldAddJiraOk() {
      model.addJira("llu-1");

      assertEquals("LLU-1", model.getValueAt(0, 0));
      assertEquals("", model.getValueAt(0, 1));
//      assertEquals("", model.getValueAt(0, 2));
      assertEquals("", model.getValueAt(0, 3));
      assertEquals("", model.getValueAt(0, 4));
      assertEquals("", model.getValueAt(0, 5));
      assertEquals("", model.getValueAt(0, 6));
      assertEquals("", model.getValueAt(0, 7));
      assertEquals("", model.getValueAt(0, 8));
      assertEquals("", model.getValueAt(0, 9));
      assertEquals("", model.getValueAt(0, 10));
      assertEquals("", model.getValueAt(0, 11));
   }

   public void testShouldCalculateAlreadyExistsOk() {
      String jira = "Jira-1";
      assertFalse(model.doesJiraExist(jira));
      model.addJira(jira);
      assertTrue(model.doesJiraExist(jira));
   }

   public void testShouldGetColumnInfoOk() {
      assertEquals(0, model.getColumnIndex(Column.Jira));
      assertEquals(1, model.getColumnIndex(Column.Description));
      assertEquals(5, model.getColumnIndex(Column.J_FixVersion));
      assertEquals(-1, model.getColumnIndex(Column.isBug));
   }

   public void testShouldAddJiraComplexObjectOk() {
      JiraIssue mock_jiraIssue = createClassMock(JiraIssue.class);
      EasyMock.expect(mock_jiraIssue.getKey()).andReturn("LLU-1").anyTimes();
      EasyMock.expect(mock_jiraIssue.getSummary()).andReturn("Summary1").anyTimes();
      List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
      fixVersions.add(TestObjects.Version_10);
      fixVersions.add(TestObjects.Version_11);
      EasyMock.expect(mock_jiraIssue.getFixVersions()).andReturn(fixVersions).anyTimes();
      EasyMock.expect(mock_jiraIssue.getStatus()).andReturn("Status1").anyTimes();
      EasyMock.expect(mock_jiraIssue.getResolution()).andReturn("Resolution1").anyTimes();
      EasyMock.expect(mock_jiraIssue.getBuildNo()).andReturn("BuildNo1").anyTimes();
      EasyMock.expect(mock_jiraIssue.getEstimate()).andReturn("1.4").anyTimes();
      replay();

      model.addJira(mock_jiraIssue);

      verify();
      assertEquals(1, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertModelRow("LLU-1", Column.Jira, 0, 0);
      assertModelRow("Summary1", Column.Description, 1, 0);
      assertModelRow("Status1", Column.J_Status, 3, 0);
      assertModelRow("Resolution1", Column.J_Resolution, 4, 0);
      assertModelRow("BuildNo1", Column.J_BuildNo, 5, 0);
      assertModelRow(1.4f, Column.Dev_Estimate, 6, 0);

      reset();
      model = new PlanTableModel();
      EasyMock.expect(mock_jiraIssue.getKey()).andReturn("LLU-2").anyTimes();
      EasyMock.expect(mock_jiraIssue.getSummary()).andReturn("Summary2").anyTimes();
      EasyMock.expect(mock_jiraIssue.getType()).andReturn("Type2").anyTimes();
      EasyMock.expect(mock_jiraIssue.getEstimate()).andReturn("2.4").anyTimes();
      replay();

      model.addJira(mock_jiraIssue);

      verify();
      assertEquals(1, model.getRowCount());
      assertEquals(10, model.getColumnCount());
      // Column.Jira, Column.Description, Column.Type, Column.Planned_Sprint, Column.Resolved_Sprint, Column.Closed_Sprint, Column.Estimate, Column.Actual,
      // Column.Note
      assertModelRow("LLU-2", Column.Jira, 0, 0);
      assertModelRow("Summary2", Column.Description, 1, 0);
      assertModelRow("Type2", Column.J_Type, 2, 0);
      assertModelRow("", Column.Planned_Sprint, 3, 0);
      assertModelRow("", Column.Resolved_Sprint, 4, 0);
      assertModelRow("", Column.Closed_Sprint, 5, 0);
      assertModelRow(2.4f, Column.Dev_Estimate, 6, 0);
      assertModelRow(0f, Column.QA_Estimate, 7, 0);
      assertModelRow(0f, Column.Dev_Actual, 8, 0);
      assertModelRow("", Column.Note, 9, 0);
   }

   private void assertModelRow(Object string, Column column, int col, int row) {
      assertEquals(string, model.getValueAt(column, row));
      assertEquals(string, model.getValueAt(row, col));
   }
}
