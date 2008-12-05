package com.jonas.agile.devleadtool.component.table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableModel;
import junit.framework.TestCase;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class MyTableFunctionalUnitTest extends TestCase {
   private Column[] columns = { Column.Jira };
   private MyTable table;

   protected void setUp() throws Exception {
      super.setUp();
      MyTableModel modelModel = new ATestTableModel(columns);
      table = new MyTable("table test", modelModel, true);
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldNotMarkAddedFieldOrEmptyTableOrRowsOutofBounds() {
      assertEquals("should not have any rows", 0, table.getRowCount());
      assertFalse(table.isMarked(0));

      table.addJira("test1");

      assertEquals("should only have one row", 1, table.getRowCount());
      assertFalse(table.isMarked(0));
      assertFalse(table.isMarked(1));
   }

   public void testShouldOnlyNeedOneUnMarkIfSeveralMarksOccur() {
      table.addJira("test1");
      table.addJira("test2");

      table.addRowSelectionInterval(0, 0);
      table.markSelected();
      table.addRowSelectionInterval(0, 1);

      assertEquals(2, table.getSelectedRowCount());

      table.markSelected();
      table.unMarkSelection();

      assertFalse(table.isMarked(0));
      assertFalse(table.isMarked(1));
   }

   public void testShouldMarkAndUnMarkOnSelectionOkWhenNothingAddedOrResorted_Basic() {
      table.addJira("test1");
      table.addJira("test5");
      table.addJira("test3");
      table.addRowSelectionInterval(1, 1);
      table.markSelected();

      assertEquals("We should have one selected!", 1, table.getSelectedRowCount());
      assertEquals("We should have one selected and the selected row should be...", 1, table.getSelectedRow());

      assertFalse("middle should be row marked", table.isMarked(0));
      assertTrue("middle row should be marked", table.isMarked(1));
      assertFalse("middle row should be marked", table.isMarked(2));

      table.unMarkSelection();

      assertFalse("none should be marked", table.isMarked(0));
      assertFalse("none should be marked", table.isMarked(1));
      assertFalse("none should be marked", table.isMarked(2));

      table.clearSelection();
      table.addRowSelectionInterval(0, 0);
      table.addRowSelectionInterval(2, 2);
      table.markSelected();

      assertTrue("first and second row should be marked", table.isMarked(0));
      assertFalse("first and second row should be marked", table.isMarked(1));
      assertTrue("first and second row should be marked", table.isMarked(2));

   }

   public void testShouldMarkOkWhenRemovingRows() {
      table.addJira("test1");
      table.addJira("test5");
      table.addJira("test3");

      table.addRowSelectionInterval(0, 0);
      table.addRowSelectionInterval(2, 2);
      table.markSelected();

      assertEquals("TEST1", table.getValueAt(0, 0));
      assertEquals("TEST5", table.getValueAt(1, 0));
      assertEquals("TEST3", table.getValueAt(2, 0));

      assertTrue("first and third row should be marked", table.isMarked(0));
      assertFalse("first and third row should be marked", table.isMarked(1));
      assertTrue("first and third row should be marked", table.isMarked(2));

      table.clearSelection();
      table.addRowSelectionInterval(0, 0);
      System.out.println("removing started!!");
      table.removeSelectedRows();
      System.out.println("removing finished!!");

      assertEquals(2, table.getRowCount());
      assertEquals("TEST5", table.getValueAt(0, 0));
      assertEquals("TEST3", table.getValueAt(1, 0));

      assertFalse("second row should be marked", table.isMarked(0));
      assertTrue("second row should be marked", table.isMarked(1));
   }

   public void testShouldMarkOkIfReSortingTable() {
      table.clearSelection();
      table.addJira("test3");
      table.addJira("test1");
      table.addJira("test2");

      table.addRowSelectionInterval(0, 0);
      table.addRowSelectionInterval(2, 2);
      table.markSelected();

      assertEquals("TEST3", table.getValueAt(0, 0));
      assertEquals("TEST1", table.getValueAt(1, 0));
      assertEquals("TEST2", table.getValueAt(2, 0));
      assertTrue("two should be marked", table.isMarked(0));
      assertFalse("two should be marked", table.isMarked(1));
      assertTrue("two should be marked", table.isMarked(2));

      table.clearSelection();
      sortTable(table, 0, SortOrder.ASCENDING);

      assertEquals("TEST1", table.getValueAt(0, 0));
      assertEquals("TEST2", table.getValueAt(1, 0));
      assertEquals("TEST3", table.getValueAt(2, 0));

      assertFalse("two should be marked", table.isMarked(0));
      assertTrue("two should be marked", table.isMarked(1));
      assertTrue("two should be marked", table.isMarked(2));
   }

   public void testShouldMarkOkWhenRemovingRowsThatAreSorted() {
      table.addJira("test3");
      table.addJira("test1");
      table.addJira("test5");
      table.addJira("test2");

      table.addRowSelectionInterval(0, 0);
      table.addRowSelectionInterval(2, 2);
      table.markSelected();

      assertEquals("TEST3", table.getValueAt(0, 0));
      assertEquals("TEST1", table.getValueAt(1, 0));
      assertEquals("TEST5", table.getValueAt(2, 0));
      assertEquals("TEST2", table.getValueAt(3, 0));

      table.clearSelection();
      sortTable(table, 0, SortOrder.ASCENDING);

      table.clearSelection();
      table.addRowSelectionInterval(0, 0);
      table.removeSelectedRows();

      assertEquals("TEST2", table.getValueAt(0, 0));
      assertEquals("TEST3", table.getValueAt(1, 0));
      assertEquals("TEST5", table.getValueAt(2, 0));

      assertFalse("two should be marked", table.isMarked(0));
      assertTrue("two should be marked", table.isMarked(1));
      assertTrue("two should be marked", table.isMarked(2));
   }

   public void testShouldAddRowOkBeforeAndAfterMarkedRowWhenItsSorted() {
      table.addJira("test2");
      table.addJira("test1");
      table.addJira("test3");

      table.addRowSelectionInterval(0, 0);
      table.addRowSelectionInterval(2, 2);
      table.markSelected();
      sortTable(table, 0, SortOrder.ASCENDING);

      table.addJira("test0");

      assertEquals("TEST0", table.getValueAt(0, 0));
      assertEquals("TEST1", table.getValueAt(1, 0));
      assertEquals("TEST2", table.getValueAt(2, 0));
      assertEquals("TEST3", table.getValueAt(3, 0));

      assertFalse("test2 and 3 should be marked", table.isMarked(0));
      assertFalse("test2 and 3 should be marked", table.isMarked(1));
      assertTrue("test2 and 3 should be marked", table.isMarked(2));
      assertTrue("test2 and 3 should be marked", table.isMarked(3));
   }

   private void sortTable(MyTable table, int colToSort, SortOrder sortOrder) {
      RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
      List<SortKey> sortKeys = new ArrayList<SortKey>();
      sortKeys.add(new SortKey(colToSort, sortOrder));
      rowSorter.setSortKeys(sortKeys);
   }

   private class ATestTableModel extends MyTableModel {
      private ATestTableModel(Column[] columns) {
         super(columns, true);
      }

      @Override
      public Color getColor(Object value, int row, Column column) {
         return null;
      }
   }

}
