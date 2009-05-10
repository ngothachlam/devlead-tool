package com.jonas.agile.devleadtool.gui.component.table.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.easymock.classextension.EasyMock;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.TestObjects;

public class MyTableModelTest extends JonasTestCase {

   public MyTableModelTest() {
      super();
   }

   private MyTableModel model;

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      model = new TestTableModel();
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldBeCreatedOk() {
      assertEquals(0, model.getRowCount());
      assertTrue(model.getColumnCount() > 0);
   }

   public void testShouldConvertionNumbersWithStandardColumnsOk() {
      Vector<ColumnType> header = new Vector<ColumnType>();
      header.add(ColumnType.Description);
      header.add(ColumnType.Jira);

      Map<ColumnType, Integer> columnNames = new LinkedHashMap<ColumnType, Integer>();
      columnNames.put(ColumnType.Jira, 0);
      columnNames.put(ColumnType.Description, 1);

      List convert = model.getConvertionNumbers(header, columnNames);

      assertEquals(2, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(0, convert.get(1));
   }

   public void testShouldConvertionNumbersWithFewerColumnsOk() {
      Vector<ColumnType> header = new Vector<ColumnType>();
      header.add(ColumnType.Description);
      header.add(ColumnType.Jira);

      Map<ColumnType, Integer> columnNames = new LinkedHashMap<ColumnType, Integer>();
      columnNames.put(ColumnType.Jira, 0);
      columnNames.put(ColumnType.Note, 1);
      columnNames.put(ColumnType.BuildNo, 2);
      columnNames.put(ColumnType.Description, 3);

      List convert = model.getConvertionNumbers(header, columnNames);

      assertEquals(4, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(-1, convert.get(1));
      assertEquals(-1, convert.get(2));
      assertEquals(0, convert.get(3));
   }

   public void testShouldConvertionNumbersWithMoreColumnsOk() {
      Vector<ColumnType> header = new Vector<ColumnType>();
      header.add(ColumnType.Description);
      header.add(ColumnType.Jira);
      header.add(ColumnType.BoardStatus);

      Map<ColumnType, Integer> columnNames = new LinkedHashMap<ColumnType, Integer>();
      columnNames.put(ColumnType.Jira, 0);
      columnNames.put(ColumnType.Description, 1);

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
      Map<ColumnType, Integer> columnNames = new LinkedHashMap<ColumnType, Integer>();
      columnNames.put(ColumnType.DAct, 0);
      columnNames.put(ColumnType.BuildNo, 1);
      columnNames.put(ColumnType.Closed_Sprint, 2);

      Vector<ColumnType> vector = new Vector<ColumnType>();
      vector.add(ColumnType.BuildNo);
      assertEquals(ColumnType.DAct, model.findIndexThatDoesNotExist(columnNames, vector, 0));
      assertEquals(ColumnType.Closed_Sprint, model.findIndexThatDoesNotExist(columnNames, vector, 1));
      assertEquals(null, model.findIndexThatDoesNotExist(columnNames, vector, 2));
   }

   public void testShouldSortHeaderBasedOnListWithFewerColumnsOk() {
      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(-1);
      originalList.add(0);
      originalList.add(-1);

      Vector<ColumnType> mixedRowVector = new Vector<ColumnType>();
      mixedRowVector.add(ColumnType.Description);
      mixedRowVector.add(ColumnType.Jira);

      Map<ColumnType, Integer> columnNames = new LinkedHashMap<ColumnType, Integer>();
      columnNames.put(ColumnType.Jira, 0);
      columnNames.put(ColumnType.Description, 1);
      columnNames.put(ColumnType.BuildNo, 2);
      columnNames.put(ColumnType.FixVersion, 3);

      Vector<ColumnType> result = model.sortHeaderBasedOnList(originalList, mixedRowVector, columnNames);
      assertEquals(ColumnType.Jira, result.get(0));
      assertEquals(ColumnType.BuildNo, result.get(1));
      assertEquals(ColumnType.Description, result.get(2));
      assertEquals(ColumnType.FixVersion, result.get(3));
      assertEquals(4, result.size());

   }

   public void testShouldAddJiraOk() {
      model.addJira("llu-1");

      assertEquals("llu-1", model.getValueAt(0, 0));
      assertEquals("llu-1", model.getValueAt(ColumnType.Jira, 0));
      
      assertEquals("", model.getValueAt(0, 1));
      assertEquals("", model.getValueAt(ColumnType.Description, 0));
      
      assertEquals(null, model.getValueAt(ColumnType.prio, 0));
      
      assertEquals("", model.getValueAt(0, 3));
      assertEquals("", model.getValueAt(ColumnType.Note, 0));
   }

   public void testShouldCalculateAlreadyExistsOk() {
      String jira = "Jira-1";
      assertFalse(model.isJiraPresent(jira));
      model.addJira(jira);
      assertTrue(model.isJiraPresent(jira));
   }

   public void testShouldGetColumnInfoOk() {
      assertEquals(0, model.getColumnIndex(ColumnType.Jira));
      assertEquals(1, model.getColumnIndex(ColumnType.Description));
      assertEquals(2, model.getColumnIndex(ColumnType.prio));
      assertEquals(3, model.getColumnIndex(ColumnType.Note));
      assertEquals(4, model.getColumnIndex(ColumnType.BuildNo));
      assertEquals(5, model.getColumnIndex(ColumnType.J_DevEst));
      assertEquals(6, model.getColumnIndex(ColumnType.DEst));
      assertEquals(-1, model.getColumnIndex(ColumnType.BoardStatus));
   }

   public void testShouldAddJiraComplexObjectOk() {
      JiraIssue mock_jiraIssue = createClassMock(JiraIssue.class);

      org.easymock.EasyMock.expect(mock_jiraIssue.getKey()).andReturn("LLU-1").anyTimes();
      org.easymock.EasyMock.expect(mock_jiraIssue.getSummary()).andReturn("Summary1").anyTimes();
      List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
      fixVersions.add(TestObjects.Version_10);
      fixVersions.add(TestObjects.Version_11);
      org.easymock.EasyMock.expect(mock_jiraIssue.getFixVersions()).andReturn(fixVersions).anyTimes();
      org.easymock.EasyMock.expect(mock_jiraIssue.getStatus()).andReturn("Status1").anyTimes();
      org.easymock.EasyMock.expect(mock_jiraIssue.getResolution()).andReturn("Resolution1").anyTimes();
      org.easymock.EasyMock.expect(mock_jiraIssue.getBuildNo()).andReturn("BuildNo1").anyTimes();
      org.easymock.EasyMock.expect(mock_jiraIssue.getEstimate()).andReturn("1.4").anyTimes();
      org.easymock.EasyMock.expect(mock_jiraIssue.getLLUListPriority()).andReturn(5800).anyTimes();
      replay();

      boolean isAdded = model.addJira(mock_jiraIssue);

      verify();
      assertEquals(1, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertModelRow("LLU-1", ColumnType.Jira, 0, 0);
      assertModelRow("Summary1", ColumnType.Description, 1, 0);
      assertModelRow(5800, ColumnType.prio, 2, 0);
      assertModelRow("", ColumnType.Note, 3, 0);
      assertModelRow("BuildNo1", ColumnType.BuildNo, 4, 0);
      assertModelRow("1.4", ColumnType.J_DevEst, 5, 0);
      assertModelRow("", ColumnType.DEst, 6, 0);
      assertTrue(isAdded);
      
      isAdded = model.addJira(mock_jiraIssue);
      assertFalse(isAdded);
   }

   private void assertModelRow(Object string, ColumnType column, int col, int row) {
      assertEquals("col " + col + " is " + model.getColumnType(col) + " not " + column + "!", col, model.getColumnIndex(column));
      assertEquals(string, model.getValueAt(column, row));
      assertEquals(string, model.getValueAt(row, col));
   }

   public void testShouldGetValueOfSameClassAsColumnType() {
      MyTableModel model = new TestTableModel();

      JiraIssue jiraIssue = new JiraIssue("key", "summary", "status", "resolution", "type", "buildNo", "estimate", 1, "sprint", "project", "environment", "owner", "created");
      JiraVersion fixVersion = new JiraVersion("",  JiraProject.LLU, "", false);
      jiraIssue.addFixVersions(fixVersion);
      assertEquals("KEY", model.getValueFromIssue(jiraIssue, ColumnType.Jira));
      assertEquals("type", model.getValueFromIssue(jiraIssue, ColumnType.Type));
      assertEquals("summary", model.getValueFromIssue(jiraIssue, ColumnType.Description));
      Object fixVersions = model.getValueFromIssue(jiraIssue, ColumnType.FixVersion);
      assertTrue(fixVersions instanceof String);
      assertEquals("", fixVersions.toString());
      assertEquals("status", model.getValueFromIssue(jiraIssue, ColumnType.Status));
      assertEquals("status (resolution)", model.getValueFromIssue(jiraIssue, ColumnType.Resolution));
      assertEquals("buildNo", model.getValueFromIssue(jiraIssue, ColumnType.BuildNo));
      assertEquals(1, model.getValueFromIssue(jiraIssue, ColumnType.prio));
      assertEquals("estimate", model.getValueFromIssue(jiraIssue, ColumnType.J_DevEst));
      assertEquals("sprint", model.getValueFromIssue(jiraIssue, ColumnType.J_Sprint));
      assertEquals(null, model.getValueFromIssue(jiraIssue, ColumnType.J_DevAct));
      assertEquals("environment", model.getValueFromIssue(jiraIssue, ColumnType.Environment));
      assertEquals("owner", model.getValueFromIssue(jiraIssue, ColumnType.Owner));
   }
}
