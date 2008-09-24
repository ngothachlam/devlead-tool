/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import com.jonas.agile.devleadtool.component.listener.FilterDocumentListenerListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;

public class FilterDocumentListenerListenerImpl implements FilterDocumentListenerListener {

   private JTextField filterText;
   private MyTable table;
   private TableRowSorter<TableModel> tableRowSorter;
   private final Column[] columns;

   FilterDocumentListenerListenerImpl(Column... columns) {
      this.columns = columns;
   }

   public FilterDocumentListenerListenerImpl(JTextField filterText, MyTable table, TableRowSorter<TableModel> tableRowSorter, Column... columns) {
      this(columns);
      this.filterText = filterText;
      this.table = table;
      this.tableRowSorter = tableRowSorter;
   }

   @Override
   public void newFilter() {
      RowFilter<TableModel, Object> rf = null;
      try {
         int[] intArr = getArray(columns);
         rf = RowFilter.regexFilter(filterText.getText(), intArr);
      } catch (java.util.regex.PatternSyntaxException e) {
         return;
      }
      tableRowSorter.setRowFilter(rf);
   }

   int[] getArray(Column... columns) {
      List<Integer> array = new ArrayList<Integer>();
      for (Column column : columns) {
         int columnIndex = table.getColumnIndex(column);
         if (columnIndex >= 0) {
            addToArrayIfPositive(columnIndex, array);
         }
      }
      int[] intArray = new int[array.size()];
      for (int i = 0; i < intArray.length; i++) {
         intArray[i] = array.get(i);
      }
      return intArray;
   }

   void addToArrayIfPositive(Integer columnIndexOne, List<Integer> array) {
      if (columnIndexOne >= 0)
         array.add(columnIndexOne);
   }
}