package com.jonas.agile.devleadtool.component.table.model;

import java.util.Map;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class BoardTableModelTest extends JonasTestCase {

   String[] tableHeader;
   Object[][] tableContents;
   MyTableModel model;

   protected void setUp() throws Exception {
      tableHeader = new String[] { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete" };
      tableContents = new Object[][] { getEmptyRow() };
      super.setUp();
      model = new BoardTableModel();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldSetEditableCorrectly() {
      assertTrue(model.isCellEditable(0, 0));
      assertTrue(model.isCellEditable(0, 1));
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
      Map<ColumnDataType, Integer> columnNames = model.getColumnNames();
      assertEquals(new Integer(0).intValue(), columnNames.get(ColumnDataType.Jira).intValue());
      assertEquals(new Integer(1).intValue(), columnNames.get(ColumnDataType.isOpen).intValue());
      assertEquals(new Integer(2).intValue(), columnNames.get(ColumnDataType.isBug).intValue());
      assertEquals(new Integer(3).intValue(), columnNames.get(ColumnDataType.isInProgress).intValue());
      assertEquals(new Integer(4).intValue(), columnNames.get(ColumnDataType.isResolved).intValue());
      assertEquals(new Integer(5).intValue(), columnNames.get(ColumnDataType.isComplete).intValue());
      assertEquals(new Integer(6).intValue(), columnNames.get(ColumnDataType.isInPlan).intValue());
      assertEquals(7, columnNames.size());
   }
   
   public void testShouldCreateFromConstructorOk() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<ColumnDataType> header = new Vector<ColumnDataType>();
      header.add(ColumnDataType.Jira);
      contents.add(getTestContentRow(1, "row0-", 0));
      contents.add(getTestContentRow(1, "row1-", 0));
      MyTableModel model = new BoardTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(7, model.getColumnCount());
      assertEquals("row0-0", model.getValueAt(0, 0));
      assertEquals(null, model.getValueAt(0, 1));
      assertEquals(null, model.getValueAt(0, 2));
      assertEquals(null, model.getValueAt(0, 3));
      assertEquals(null, model.getValueAt(0, 4));
      assertEquals(null, model.getValueAt(0, 5));
      assertEquals(null, model.getValueAt(0, 6));
      assertEquals("row1-0", model.getValueAt(1, 0));
      assertEquals(null, model.getValueAt(1, 1));
      assertEquals(null, model.getValueAt(1, 2));
      assertEquals(null, model.getValueAt(1, 3));
      assertEquals(null, model.getValueAt(1, 4));
      assertEquals(null, model.getValueAt(1, 5));
      assertEquals(null, model.getValueAt(1, 6));
      
      assertEquals("Jira", model.getColumnName(0));
      assertEquals("isOpen", model.getColumnName(1));
      assertEquals("isBug", model.getColumnName(2));
      assertEquals("Remaining_Progress", model.getColumnName(3));
      assertEquals("isResolved", model.getColumnName(4));
      assertEquals("isComplete", model.getColumnName(5));
      assertEquals("isInPlan", model.getColumnName(6));
      
   }
}
