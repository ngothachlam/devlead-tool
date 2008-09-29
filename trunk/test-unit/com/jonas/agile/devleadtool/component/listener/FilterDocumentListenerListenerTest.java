package com.jonas.agile.devleadtool.component.listener;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;

public class FilterDocumentListenerListenerTest extends TestCase {

   public void testShouldAddToArrayOnlyPositiveNumbers() {
      FilterDocumentListenerListener listener = new FilterDocumentListenerListener();
      List<Integer> array = new ArrayList<Integer>();
      assertEquals(0, array.size());
      listener.addToArrayIfPositive(-1, array);
      assertEquals(0, array.size());
      listener.addToArrayIfPositive(0, array);
      assertEquals(1, array.size());
      listener.addToArrayIfPositive(-1, array);
      assertEquals(1, array.size());
      listener.addToArrayIfPositive(1, array);
      assertEquals(2, array.size());
   }

   public void testShouldCalculateArrayCorrectly() {
      FilterDocumentListenerListener listener = new FilterDocumentListenerListener(null, new MyTable(new BoardTableModel()), null, ColumnDataType.Jira);
      // FilterDocumentListenerListenerImpl listener = new FilterDocumentListenerListenerImpl();
      assertEquals(0, listener.getArray(ColumnDataType.Closed_Sprint).length);
      int[] array = listener.getArray(ColumnDataType.Jira, ColumnDataType.Closed_Sprint, ColumnDataType.Description);
      assertEquals(2, array.length);
      assertEquals(0, array[0]);
      assertEquals(1, array[1]);
   }
   // public void testShouldCalculateArrayCorrectly() {
   // FilterDocumentListenerListenerImpl listener = new FilterDocumentListenerListenerImpl();
   // assertEquals(0, listener.getArray(-1, -1).length);
   // int[] array = listener.getArray(1, -1, 2, -1, 3);
   // assertEquals(3, array.length);
   // assertEquals(1, array[0]);
   // assertEquals(2, array[1]);
   // assertEquals(3, array[2]);
   // }
   // public void testShouldIncludeClumnsInTableOk() {
   // FilterDocumentListenerListenerImpl listener = new FilterDocumentListenerListenerImpl(null, new MyTable(new BoardTableModel()), null, Column.Jira);
   // assertEquals(0, listener.buildArrayOfColumnsContainedInTable().length);
   // }

}
