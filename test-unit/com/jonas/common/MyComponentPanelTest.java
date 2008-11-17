package com.jonas.common;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;

public class MyComponentPanelTest extends TestCase {

   
   public void testShouldAddToArrayOnlyPositiveNumbers() {
      MyComponentPanel listener = new MyComponentPanel(null);
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
      MyTable myTable = new MyTable("Board", new BoardTableModel(), true);
      MyComponentPanel listener = new MyComponentPanel(null);
      // FilterDocumentListenerListenerImpl listener = new FilterDocumentListenerListenerImpl();
      assertEquals(0, listener.getArrayOfIndices(myTable, Column.Closed_Sprint).length);
      int[] array = listener.getArrayOfIndices(myTable,Column.Jira, Column.Closed_Sprint, Column.Description);
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
