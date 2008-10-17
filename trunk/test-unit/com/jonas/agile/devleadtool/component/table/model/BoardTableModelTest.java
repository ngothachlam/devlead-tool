package com.jonas.agile.devleadtool.component.table.model;

import java.util.Map;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class BoardTableModelTest extends JonasTestCase {

   String[] tableHeader;
   Object[][] tableContents;
   BoardTableModel model;

   protected void setUp() throws Exception {
      super.setUp();
      model = new BoardTableModel();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldSetEditableCorrectly() {
      assertTrue(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(0, 2));
      assertTrue(model.isCellEditable(0, 3));
      assertTrue(model.isCellEditable(0, 4));
      assertTrue(model.isCellEditable(0, 5));
      assertTrue(model.isCellEditable(0, 6));
      assertTrue(model.isCellEditable(0, 7));
   }

   public void testShouldHaveEmptyRowAndColumnNamesOfSameSize() {
      assertEquals(model.getColumnNames().size(), model.getEmptyRow().length);
   }

   public void testColumnNames() {
      Map<Column, Integer> columnNames = model.getColumnNames();
      assertEquals(new Integer(0).intValue(), columnNames.get(Column.Jira).intValue());
      assertEquals(new Integer(1).intValue(), columnNames.get(Column.Description).intValue());
      assertEquals(new Integer(2).intValue(), columnNames.get(Column.J_Resolution).intValue());
      assertEquals(new Integer(3).intValue(), columnNames.get(Column.Release).intValue());
      assertEquals(new Integer(4).intValue(), columnNames.get(Column.isOpen).intValue());
      assertEquals(new Integer(5).intValue(), columnNames.get(Column.isBug).intValue());
      assertEquals(new Integer(6).intValue(), columnNames.get(Column.isInProgress).intValue());
      assertEquals(new Integer(7).intValue(), columnNames.get(Column.isResolved).intValue());
      assertEquals(new Integer(8).intValue(), columnNames.get(Column.isComplete).intValue());
      assertEquals(new Integer(9).intValue(), columnNames.get(Column.Dev_Estimate).intValue());
      assertEquals(new Integer(10).intValue(), columnNames.get(Column.Dev_Actual).intValue());
      assertEquals(new Integer(11).intValue(), columnNames.get(Column.prio).intValue());
      assertEquals(12, columnNames.size());
   }

   public void testShouldSetBoolColumnsMutuallyExlusiveRedOk() {
      model.addJira(new JiraIssue("LLU-1", "llu-1 summary", "llu-1 status", "llu-1 resolution", "llu-1 type"));

      assertEquals(1, model.getRowCount());
      // none set

      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(false, model.isRed(true, 0, Column.isOpen));
      assertEquals(false, model.isRed(true, 0, Column.isBug));
      assertEquals(false, model.isRed(true, 0, Column.isInProgress));
      assertEquals(false, model.isRed(true, 0, Column.isResolved));
      assertEquals(false, model.isRed(true, 0, Column.isComplete));

      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(true, model.isRed(false, 0, Column.isOpen));
      assertEquals(true, model.isRed(false, 0, Column.isBug));
      assertEquals(true, model.isRed(false, 0, Column.isInProgress));
      assertEquals(true, model.isRed(false, 0, Column.isResolved));
      assertEquals(true, model.isRed(false, 0, Column.isComplete));

      // set to isBug
      model.setValueAt(true, 0, Column.isBug);

      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(true, model.isRed(true, 0, Column.isOpen));
      assertEquals(false, model.isRed(true, 0, Column.isBug));
      assertEquals(true, model.isRed(true, 0, Column.isInProgress));
      assertEquals(true, model.isRed(true, 0, Column.isResolved));
      assertEquals(true, model.isRed(true, 0, Column.isComplete));

      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(false, model.isRed(false, 0, Column.isOpen));
      assertEquals(true, model.isRed(false, 0, Column.isBug));
      assertEquals(false, model.isRed(false, 0, Column.isInProgress));
      assertEquals(false, model.isRed(false, 0, Column.isResolved));
      assertEquals(false, model.isRed(false, 0, Column.isComplete));

      // set to isBug and inProgress
      model.setValueAt(true, 0, Column.isInProgress);

      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(true, model.isRed(true, 0, Column.isOpen));
      assertEquals(true, model.isRed(true, 0, Column.isBug));
      assertEquals(true, model.isRed(true, 0, Column.isInProgress));
      assertEquals(true, model.isRed(true, 0, Column.isResolved));
      assertEquals(true, model.isRed(true, 0, Column.isComplete));

      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(false, model.isRed(false, 0, Column.isOpen));
      assertEquals(false, model.isRed(false, 0, Column.isBug));
      assertEquals(false, model.isRed(false, 0, Column.isInProgress));
      assertEquals(false, model.isRed(false, 0, Column.isResolved));
      assertEquals(false, model.isRed(false, 0, Column.isComplete));

      // set to isBug and inProgress and isResolved
      model.setValueAt(true, 0, Column.isResolved);

      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(true, model.isRed(true, 0, Column.isOpen));
      assertEquals(true, model.isRed(true, 0, Column.isBug));
      assertEquals(true, model.isRed(true, 0, Column.isInProgress));
      assertEquals(true, model.isRed(true, 0, Column.isResolved));
      assertEquals(true, model.isRed(true, 0, Column.isComplete));

      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(false, model.isRed(false, 0, Column.isOpen));
      assertEquals(false, model.isRed(false, 0, Column.isBug));
      assertEquals(false, model.isRed(false, 0, Column.isInProgress));
      assertEquals(false, model.isRed(false, 0, Column.isResolved));
      assertEquals(false, model.isRed(false, 0, Column.isComplete));
   }

   public void testShouldSetRedEstimatesAndActualsOk() {
      model.addJira(new JiraIssue("LLU-1", "llu-1 summary", "llu-1 status", "llu-1 resolution", "llu-1 type"));

      assertEquals(1, model.getRowCount());
      // none set

      // set to isOpen
      model.setValueAt(true, 0, Column.isOpen);
      model.setValueAt(false, 0, Column.isBug);
      model.setValueAt(false, 0, Column.isInProgress);
      model.setValueAt(false, 0, Column.isResolved);
      model.setValueAt(false, 0, Column.isComplete);

      assertEquals(true, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Estimate));
      assertEquals(true, model.isRed("1", 0, Column.Dev_Actual));
      
      // set to isBug
      model.setValueAt(false, 0, Column.isOpen);
      model.setValueAt(true, 0, Column.isBug);
      model.setValueAt(false, 0, Column.isInProgress);
      model.setValueAt(false, 0, Column.isResolved);
      model.setValueAt(false, 0, Column.isComplete);
      
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Estimate));
      assertEquals(true, model.isRed("1", 0, Column.Dev_Actual));

      // set to isInProgress
      model.setValueAt(false, 0, Column.isOpen);
      model.setValueAt(false, 0, Column.isBug);
      model.setValueAt(true, 0, Column.isInProgress);
      model.setValueAt(false, 0, Column.isResolved);
      model.setValueAt(false, 0, Column.isComplete);
      
      assertEquals(true, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Estimate));
      assertEquals(true, model.isRed("1", 0, Column.Dev_Actual));
      
      // set to isResolved
      model.setValueAt(false, 0, Column.isOpen);
      model.setValueAt(false, 0, Column.isBug);
      model.setValueAt(false, 0, Column.isInProgress);
      model.setValueAt(true, 0, Column.isResolved);
      model.setValueAt(false, 0, Column.isComplete);
      
      assertEquals(true, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(true, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Actual));
      
      // set to isComplete
      model.setValueAt(false, 0, Column.isOpen);
      model.setValueAt(false, 0, Column.isBug);
      model.setValueAt(false, 0, Column.isInProgress);
      model.setValueAt(false, 0, Column.isResolved);
      model.setValueAt(true, 0, Column.isComplete);
      
      assertEquals(true, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(true, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Actual));
      
      // set to isOpen and inProgress
      model.setValueAt(true, 0, Column.isOpen);
      model.setValueAt(false, 0, Column.isBug);
      model.setValueAt(true, 0, Column.isInProgress);
      model.setValueAt(false, 0, Column.isResolved);
      model.setValueAt(false, 0, Column.isComplete);
      
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("1", 0, Column.Dev_Actual));
   }

   public void testShouldGetAnyBoolColumnBackOk() {
      model.addJira(new JiraIssue("LLU-1", "llu-1 summary", "llu-1 status", "llu-1 resolution", "llu-1 type"));
      
      assertEquals(1, model.getRowCount());
      
      assertEquals(false, model.isAnyTrue(0, Column.isBug, Column.isComplete, Column.isInProgress, Column.isParked));

      //setToOpen
      model.setValueAt(Boolean.TRUE, 0, Column.isOpen);
      assertEquals(true, model.isAnyTrue(0, Column.isOpen));
      assertEquals(false, model.isAnyTrue(0, Column.isBug, Column.isComplete, Column.isInProgress, Column.isParked, Column.isResolved));
      assertEquals(true, model.isAnyTrue(0, Column.isBug, Column.isComplete, Column.isInProgress, Column.isParked, Column.isResolved, Column.isOpen));
      assertEquals(true, model.isAnyTrue(0, Column.isOpen, Column.isBug, Column.isComplete, Column.isInProgress, Column.isParked, Column.isResolved));
      assertEquals(true, model.isAnyTrue(0, Column.isBug, Column.isComplete, Column.isOpen, Column.isInProgress, Column.isParked, Column.isResolved));
   }
   
   
   public void testShouldCreateFromConstructorOk() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();

      Vector<Column> header = new Vector<Column>();
      header.add(Column.Jira);
      header.add(Column.Description);
      header.add(Column.J_Resolution);
      header.add(Column.Release);
      header.add(Column.isOpen);
      header.add(Column.isBug);
      header.add(Column.isInProgress);
      header.add(Column.isResolved);
      header.add(Column.isComplete);
      header.add(Column.Dev_Estimate);
      header.add(Column.Dev_Actual);
      header.add(Column.prio);
      contents.add(getTestContentRow(header.size(), "0."));
      contents.add(getTestContentRow(header.size(), "1."));

      MyTableModel model = new BoardTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(12, model.getColumnCount());
      assertEquals("0.0", model.getValueAt(0, 0));
      assertEquals("0.1", model.getValueAt(0, 1));
      assertEquals("0.2", model.getValueAt(0, 2));
      assertEquals("0.3", model.getValueAt(0, 3));
      assertEquals("0.4", model.getValueAt(0, 4));
      assertEquals("0.5", model.getValueAt(0, 5));
      assertEquals("0.6", model.getValueAt(0, 6));
      assertEquals("1.0", model.getValueAt(1, 0));
      assertEquals("1.1", model.getValueAt(1, 1));
      assertEquals("1.2", model.getValueAt(1, 2));
      assertEquals("1.3", model.getValueAt(1, 3));
      assertEquals("1.4", model.getValueAt(1, 4));
      assertEquals("1.5", model.getValueAt(1, 5));
      assertEquals("1.6", model.getValueAt(1, 6));
      assertEquals("1.7", model.getValueAt(1, 7));
   }

   public void testGetEmptyRowFromBoard() {
      Object[] emptyRow = model.getEmptyRow();
      assertEquals(12, emptyRow.length);
      assertEquals("", emptyRow[0]);
      assertEquals("", emptyRow[1]);
      assertEquals("", emptyRow[2]);
      assertEquals("", emptyRow[3]);
      assertEquals(Boolean.FALSE, emptyRow[4]);
      assertEquals(Boolean.FALSE, emptyRow[5]);
      assertEquals(Boolean.FALSE, emptyRow[6]);
      assertEquals(Boolean.FALSE, emptyRow[7]);
      assertEquals(Boolean.FALSE, emptyRow[8]);
      assertEquals("", emptyRow[9]);
      assertEquals("", emptyRow[10]);
      assertEquals(null, emptyRow[11]);
   }

}
