package com.jonas.agile.devleadtool.component.table;

import org.easymock.EasyMock;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class MyTableTest extends JonasTestCase {

   MyTable table;

   protected void setUp() throws Exception {
      super.setUp();
      table = new MyTable();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldIdentifyColumnCorrectly() throws SecurityException, NoSuchMethodException {
      MyTableModel mock_table_model = createClassMock(MyTableModel.class);
      EasyMock.expect(mock_table_model.getColumn(0)).andReturn(Column.Jira);

      // mock out table methods (jtable is a big class!)
      MyTable newtable = createMock(MyTable.class, MyTable.class.getMethod("getModel", MyTableModel.class), MyTable.class.getMethod("convertColumnIndexToModel", int.class));
      EasyMock.expect(newtable.getModel()).andReturn(mock_table_model).anyTimes();
      EasyMock.expect(newtable.convertColumnIndexToModel(0)).andReturn(0).anyTimes();

      replay();

      assertTrue(newtable.isColumn(Column.Jira, 0));

      verify();
   }

   public void testShouldIdentifyColumnInCorrectly() throws SecurityException, NoSuchMethodException {
      MyTableModel mock_table_model = createClassMock(MyTableModel.class);
      EasyMock.expect(mock_table_model.getColumn(0)).andReturn(Column.Dev_Actual);

      // mock out table methods (jtable is a big class!)
      MyTable newtable = createMock(MyTable.class, MyTable.class.getMethod("getModel", null), MyTable.class.getMethod("convertColumnIndexToModel", int.class));
      EasyMock.expect(newtable.getModel()).andReturn(mock_table_model).anyTimes();
      EasyMock.expect(newtable.convertColumnIndexToModel(0)).andReturn(0).anyTimes();

      replay();

      assertFalse(newtable.isColumn(Column.Jira, 0));

      verify();
   }
   
   public void testShouldGetCorrectColumnIndex(){
      MyTableModel model = new BoardTableModel();
      table.setModel(model);
      
      assertEquals(0,table.getColumnIndex(Column.Jira));
      assertEquals(1,table.getColumnIndex(Column.Description));
      assertEquals(-1,table.getColumnIndex(Column.Resolved_Sprint));
   }
   
   public void testShouldSetValueUsingColumnDataCorrectly(){
      MyTableModel model = new PlanTableModel();
      table.setModel(model);
      
      table.addEmptyRow();
      
      assertEquals(1, table.getRowCount());
      assertEquals(10, table.getColumnCount());
      assertEquals("", table.getValueAt(0, 0));
      assertEquals("", table.getValueAt(0, 1));
      assertEquals("", table.getValueAt(0, 2));
      assertEquals("", table.getValueAt(0, 3));
      assertEquals("", table.getValueAt(0, 4));
      assertEquals("", table.getValueAt(0, 5));
      assertEquals("", table.getValueAt(0, 6));
      assertEquals("", table.getValueAt(0, 7));
      assertEquals("", table.getValueAt(0, 8));
      assertEquals("", table.getValueAt(0, 9));
      
      table.setValueAt("newValue", 0, Column.Description);
      table.setValueAt(1f, 0, Column.Dev_Estimate);
      
      assertEquals(1, table.getRowCount());
      assertEquals(10, table.getColumnCount());
      assertEquals("", table.getValueAt(0, 0));
      assertEquals("newValue", table.getValueAt(0, 1));
      assertEquals("", table.getValueAt(0, 2));
      assertEquals("", table.getValueAt(0, 3));
      assertEquals("", table.getValueAt(0, 4));
      assertEquals("", table.getValueAt(0, 5));
      assertEquals(1f, table.getValueAt(0, 6));
      assertEquals("", table.getValueAt(0, 7));
      assertEquals("", table.getValueAt(0, 8));
      assertEquals("", table.getValueAt(0, 9));
      
   }
}
