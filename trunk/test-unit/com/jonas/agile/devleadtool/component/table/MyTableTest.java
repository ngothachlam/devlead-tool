package com.jonas.agile.devleadtool.component.table;

import org.easymock.EasyMock;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.TestTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class MyTableTest extends JonasTestCase {

   MyTable table;

   protected void setUp() throws Exception {
      super.setUp();
      table = new MyTable("");
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldIdentifyColumnCorrectly() throws SecurityException, NoSuchMethodException {
      MyTableModel model = new TestTableModel();
      table.setModel(model);

      assertEquals(Column.Jira, table.getColumnEnum(0));
      assertEquals(Column.Description, table.getColumnEnum(1));
      assertEquals(Column.B_BoardStatus, table.getColumnEnum(2));
      
      assertEquals(0, table.getColumnIndex(Column.Jira));
      assertEquals(1, table.getColumnIndex(Column.Description));
      assertEquals(2, table.getColumnIndex(Column.B_BoardStatus));
   }

   public void testShouldIdentifyColumnInCorrectly() throws SecurityException, NoSuchMethodException {
      MyTableModel model = new TestTableModel();
      table.setModel(model);
      
      assertEquals(null, table.getColumnEnum(-1));
      
      assertEquals(-1, table.getColumnIndex(Column.B_Release));
   }
   
   public void testShouldGetCorrectColumnIndex(){
      MyTableModel model = new TestTableModel();
      table.setModel(model);
      
      assertEquals(0,table.getColumnIndex(Column.Jira));
      assertEquals(1,table.getColumnIndex(Column.Description));
      assertEquals(-1,table.getColumnIndex(Column.Resolved_Sprint));
   }
   
   public void testShouldSetValueUsingColumnDataCorrectly(){
      MyTableModel model = new TestTableModel();
      table.setModel(model);
      
      table.addEmptyRow();
      
      assertEquals(1, table.getRowCount());
      assertEquals(9, table.getColumnCount());
      assertEquals("", table.getValueAt(0, 0));
      assertEquals("", table.getValueAt(0, 1));
      assertEquals(BoardStatusValue.UnKnown, table.getValueAt(0, 2));
      assertEquals(null, table.getValueAt(0, 3));
      assertEquals("", table.getValueAt(0, 4));
      assertEquals("", table.getValueAt(0, 5));
      assertEquals("", table.getValueAt(0, 6));
      assertEquals(false, table.getValueAt(0, 7));
      assertEquals("", table.getValueAt(0, 8));
      
      table.setValueAt("newValue", 0, Column.Description);
      table.setValueAt(1f, 0, Column.Dev_Estimate);
      
      assertEquals(1, table.getRowCount());
      assertEquals(9, table.getColumnCount());
      assertEquals("", table.getValueAt(0, 0));
      assertEquals("newValue", table.getValueAt(0, 1));
      assertEquals(BoardStatusValue.UnKnown, table.getValueAt(0, 2));
      assertEquals(null, table.getValueAt(0, 3));
      assertEquals("", table.getValueAt(0, 4));
      assertEquals("", table.getValueAt(0, 5));
      assertEquals("", table.getValueAt(0, 6));
      assertEquals(false, table.getValueAt(0, 7));
      assertEquals(1.0f, table.getValueAt(0, 8));
   }
}
