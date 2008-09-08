package com.jonas.agile.devleadtool.component.table;

import org.easymock.EasyMock;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class MyTableTest extends JonasTestCase {

   MyTable table = null;

   protected void setUp() throws Exception {
      super.setUp();
      table = new MyTable();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldIdentifyColumnCorrectly() throws SecurityException, NoSuchMethodException {
      MyTableModel mock_table_model = createClassMock(JiraTableModel.class);
      EasyMock.expect(mock_table_model.getColumnEnum(0)).andReturn(Column.Jira);

      // mock out table methods (jtable is a big class!)
      MyTable newtable = createMock(MyTable.class, MyTable.class.getMethod("getModel", null), MyTable.class.getMethod("convertColumnIndexToModel", int.class));
      EasyMock.expect(newtable.getModel()).andReturn(mock_table_model).anyTimes();
      EasyMock.expect(newtable.convertColumnIndexToModel(0)).andReturn(0).anyTimes();

      replay();

      assertTrue(newtable.isColumn(Column.Jira, 0));

      verify();
   }

   public void testShouldIdentifyColumnInCorrectly() throws SecurityException, NoSuchMethodException {
      MyTableModel mock_table_model = createClassMock(JiraTableModel.class);
      EasyMock.expect(mock_table_model.getColumnEnum(0)).andReturn(Column.Actual);

      // mock out table methods (jtable is a big class!)
      MyTable newtable = createMock(MyTable.class, MyTable.class.getMethod("getModel", null), MyTable.class.getMethod("convertColumnIndexToModel", int.class));
      EasyMock.expect(newtable.getModel()).andReturn(mock_table_model).anyTimes();
      EasyMock.expect(newtable.convertColumnIndexToModel(0)).andReturn(0).anyTimes();

      replay();

      assertFalse(newtable.isColumn(Column.Jira, 0));

      verify();
   }

}
