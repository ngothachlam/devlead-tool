package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.TestTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class MyTableTest extends JonasTestCase {

   MyTable table;

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      table = new MyTable("", null, false);
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldIdentifyColumnCorrectly() throws SecurityException, NoSuchMethodException {
      MyTableModel model = new TestTableModel();
      table.setModel(model);

      assertEquals(ColumnType.Jira, table.getColumnEnum(0));
      assertEquals(ColumnType.Description, table.getColumnEnum(1));
      
      assertEquals(0, table.getColumnIndex(ColumnType.Jira));
      assertEquals(1, table.getColumnIndex(ColumnType.Description));
   }

   public void testShouldIdentifyColumnInCorrectly() throws SecurityException, NoSuchMethodException {
      MyTableModel model = new TestTableModel();
      table.setModel(model);
      
      assertEquals(null, table.getColumnEnum(-1));
      
      assertEquals(-1, table.getColumnIndex(ColumnType.Merge));
   }
   
   public void testShouldGetCorrectColumnIndex(){
      MyTableModel model = new TestTableModel();
      table.setModel(model);
      
      assertEquals(0,table.getColumnIndex(ColumnType.Jira));
      assertEquals(1,table.getColumnIndex(ColumnType.Description));
      assertEquals(-1,table.getColumnIndex(ColumnType.Resolved_Sprint));
   }
   
   public void testShouldSetValueUsingColumnDataCorrectly(){
      MyTableModel model = new TestTableModel();
      table.setModel(model);
      
      table.addEmptyRow();
      
      assertEquals(1, table.getRowCount());
      assertEquals(7, table.getColumnCount());
      assertEquals("", table.getValueAt(0, 0));
      assertEquals("", table.getValueAt(0, 1));
      assertEquals(null, table.getValueAt(0, 2));
      assertEquals("", table.getValueAt(0, 3));
      assertEquals("", table.getValueAt(0, 4));
      assertEquals("", table.getValueAt(0, 5));
      assertEquals("", table.getValueAt(0, 6));
      
      table.setValueAt("newValue", 0, ColumnType.Description);
      table.setValueAt(1f, 0, ColumnType.DEst);
      
      assertEquals(1, table.getRowCount());
      assertEquals(7, table.getColumnCount());
      assertEquals("", table.getValueAt(0, 0));
      assertEquals("newValue", table.getValueAt(0, 1));
      assertEquals(null, table.getValueAt(0, 2));
      assertEquals("", table.getValueAt(0, 3));
      assertEquals("", table.getValueAt(0, 4));
      assertEquals("", table.getValueAt(0, 5));
      assertEquals(1.0f, table.getValueAt(0, 6));
   }
}
