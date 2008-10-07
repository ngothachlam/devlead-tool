package com.jonas.agile.devleadtool.component.table.model;

import java.util.Map;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class BoardTableModelTest extends JonasTestCase {

   String[] tableHeader;
   Object[][] tableContents;
   MyTableModel model;

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
      assertTrue(model.isCellEditable(0, 2));
      assertTrue(model.isCellEditable(0, 3));
      assertTrue(model.isCellEditable(0, 4));
      assertTrue(model.isCellEditable(0, 5));
      assertTrue(model.isCellEditable(0, 6));
   }

   private Object[] getEmptyRow() {
      Object[] newRow = { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
      return newRow;
   }

   public void testShouldHaveEmptyRowAndColumnNamesOfSameSize() {
      assertEquals(model.getColumnNames().size(), model.getEmptyRow().length);
   }

   public void testColumnNames() {
      Map<Column, Integer> columnNames = model.getColumnNames();
      assertEquals(new Integer(0).intValue(), columnNames.get(Column.Jira).intValue());
      assertEquals(new Integer(1).intValue(), columnNames.get(Column.Description).intValue());
      assertEquals(new Integer(2).intValue(), columnNames.get(Column.isOpen).intValue());
      assertEquals(new Integer(3).intValue(), columnNames.get(Column.isBug).intValue());
      assertEquals(new Integer(4).intValue(), columnNames.get(Column.isInProgress).intValue());
      assertEquals(new Integer(5).intValue(), columnNames.get(Column.isResolved).intValue());
      assertEquals(new Integer(6).intValue(), columnNames.get(Column.isComplete).intValue());
      assertEquals(new Integer(7).intValue(), columnNames.get(Column.Dev_Estimate).intValue());
      assertEquals(new Integer(8).intValue(), columnNames.get(Column.Dev_Actual).intValue());
      assertEquals(new Integer(9).intValue(), columnNames.get(Column.prio).intValue());
      assertEquals(10, columnNames.size());
   }
   
   public void testShouldSetRedOk() {
      model.addJira(new JiraIssue("LLU-1", "llu-1 summary", "llu-1 status", "llu-1 resolution", "llu-1 type"));
      
      assertEquals(1, model.getRowCount());
      
      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(false, model.isRed(true, 0, Column.isOpen));
      assertEquals(false, model.isRed(true, 0, Column.isBug));
      assertEquals(false, model.isRed(true, 0, Column.isInProgress));
      assertEquals(false, model.isRed(true, 0, Column.isResolved));
      assertEquals(false, model.isRed(true, 0, Column.isComplete));
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed(null, 0, Column.prio));
      
      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(true, model.isRed(false, 0, Column.isOpen));
      assertEquals(true, model.isRed(false, 0, Column.isBug));
      assertEquals(true, model.isRed(false, 0, Column.isInProgress));
      assertEquals(true, model.isRed(false, 0, Column.isResolved));
      assertEquals(true, model.isRed(false, 0, Column.isComplete));
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed(null, 0, Column.prio));
      
      model.setValueAt(true, 0, Column.isBug);
      
      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(true, model.isRed(true, 0, Column.isOpen));
      assertEquals(false, model.isRed(true, 0, Column.isBug));
      assertEquals(true, model.isRed(true, 0, Column.isInProgress));
      assertEquals(true, model.isRed(true, 0, Column.isResolved));
      assertEquals(true, model.isRed(true, 0, Column.isComplete));
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed(null, 0, Column.prio));
      
      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(false, model.isRed(false, 0, Column.isOpen));
      assertEquals(true, model.isRed(false, 0, Column.isBug));
      assertEquals(false, model.isRed(false, 0, Column.isInProgress));
      assertEquals(false, model.isRed(false, 0, Column.isResolved));
      assertEquals(false, model.isRed(false, 0, Column.isComplete));
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed(null, 0, Column.prio));

      model.setValueAt(true, 0, Column.isInProgress);
      
      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(true, model.isRed(true, 0, Column.isOpen));
      assertEquals(true, model.isRed(true, 0, Column.isBug));
      assertEquals(true, model.isRed(true, 0, Column.isInProgress));
      assertEquals(true, model.isRed(true, 0, Column.isResolved));
      assertEquals(true, model.isRed(true, 0, Column.isComplete));
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed(null, 0, Column.prio));
      
      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(false, model.isRed(false, 0, Column.isOpen));
      assertEquals(false, model.isRed(false, 0, Column.isBug));
      assertEquals(false, model.isRed(false, 0, Column.isInProgress));
      assertEquals(false, model.isRed(false, 0, Column.isResolved));
      assertEquals(false, model.isRed(false, 0, Column.isComplete));
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed(null, 0, Column.prio));
      
      model.setValueAt(true, 0, Column.isResolved);
      
      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(true, model.isRed(true, 0, Column.isOpen));
      assertEquals(true, model.isRed(true, 0, Column.isBug));
      assertEquals(true, model.isRed(true, 0, Column.isInProgress));
      assertEquals(true, model.isRed(true, 0, Column.isResolved));
      assertEquals(true, model.isRed(true, 0, Column.isComplete));
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed(null, 0, Column.prio));
      
      assertEquals(false, model.isRed("", 0, Column.Jira));
      assertEquals(false, model.isRed("", 0, Column.Description));
      assertEquals(false, model.isRed(false, 0, Column.isOpen));
      assertEquals(false, model.isRed(false, 0, Column.isBug));
      assertEquals(false, model.isRed(false, 0, Column.isInProgress));
      assertEquals(false, model.isRed(false, 0, Column.isResolved));
      assertEquals(false, model.isRed(false, 0, Column.isComplete));
      assertEquals(false, model.isRed("", 0, Column.Dev_Estimate));
      assertEquals(false, model.isRed("", 0, Column.Dev_Actual));
      assertEquals(false, model.isRed(null, 0, Column.prio));
   }
   
   public void testShouldCreateFromConstructorOk() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();

      Vector<Column> header = new Vector<Column>();
      header.add(Column.Jira);
      header.add(Column.Description);
      header.add(Column.isOpen);
      header.add(Column.isBug);
      header.add(Column.isInProgress);
      header.add(Column.isResolved);
      header.add(Column.isComplete);
      header.add(Column.Dev_Estimate);
      header.add(Column.Dev_Actual);
      header.add(Column.prio);
      contents.add(getTestContentRow(10, "0."));
      contents.add(getTestContentRow(10, "1."));
      
      MyTableModel model = new BoardTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(10, model.getColumnCount());
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
   }
   
   public void testGetEmptyRowFromBoard() {
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
   
   
}
