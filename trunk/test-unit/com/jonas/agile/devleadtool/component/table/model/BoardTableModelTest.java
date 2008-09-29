package com.jonas.agile.devleadtool.component.table.model;

import java.util.Map;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
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
      Map<Column, Integer> columnNames = model.getColumnNames();
      assertEquals(new Integer(0).intValue(), columnNames.get(Column.Jira).intValue());
      assertEquals(new Integer(1).intValue(), columnNames.get(Column.Description).intValue());
      assertEquals(new Integer(2).intValue(), columnNames.get(Column.isOpen).intValue());
      assertEquals(new Integer(3).intValue(), columnNames.get(Column.isBug).intValue());
      assertEquals(new Integer(4).intValue(), columnNames.get(Column.isInProgress).intValue());
      assertEquals(new Integer(5).intValue(), columnNames.get(Column.isResolved).intValue());
      assertEquals(new Integer(6).intValue(), columnNames.get(Column.isComplete).intValue());
      assertEquals(new Integer(7).intValue(), columnNames.get(Column.ListPrio).intValue());
      assertEquals(8, columnNames.size());
   }
   
   public void testShouldCreateFromConstructorOk() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Jira);
      contents.add(getTestContentRow(1, "row0-", 0));
      contents.add(getTestContentRow(1, "row1-", 0));
      MyTableModel model = new BoardTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(8, model.getColumnCount());
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
      assertEquals("Description", model.getColumnName(1));
      assertEquals("isOpen", model.getColumnName(2));
      assertEquals("isBug", model.getColumnName(3));
      assertEquals("isInProgress", model.getColumnName(4));
      assertEquals("isResolved", model.getColumnName(5));
      assertEquals("isComplete", model.getColumnName(6));
      assertEquals("ListPrio", model.getColumnName(7));
      
   }
}
