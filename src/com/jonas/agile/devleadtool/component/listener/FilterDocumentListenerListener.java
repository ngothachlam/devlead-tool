/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.MyStringParser;

public class FilterDocumentListenerListener {

   private Logger log = MyLogger.getLogger(FilterDocumentListenerListener.class);
   private JTextField filterText;
   private MyTable table;
   private TableRowSorter<TableModel> tableRowSorter;
   private final ColumnDataType[] columns;
   private int searches = 0;

   FilterDocumentListenerListener(ColumnDataType... columns) {
      this.columns = columns;
   }

   public FilterDocumentListenerListener(JTextField filterText, MyTable table, TableRowSorter<TableModel> tableRowSorter, ColumnDataType... columns) {
      this(columns);
      this.filterText = filterText;
      this.table = table;
      this.tableRowSorter = tableRowSorter;
   }

   public void newFilter(FilterType filterType) {
      RowFilter<Object, Object> rf = null;
      try {
         int[] intArr = getArray(columns);
         log.debug("FilterType " + filterType);
         switch (filterType) {
         case REGEX: {
            rf = getFilter(filterText.getText());
            searches = 1;
         }
            break;
         case PIPESEPARATOR: {
            MyStringParser parser = new MyStringParser();
            List<String> list = parser.separateString(filterText.getText(), "|");
            searches = list.size();

            List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(10);
            for (String string : list) {
               filters.add(getFilter(string));
            }
            rf = RowFilter.orFilter(filters);
            break;
         }
         default:
            break;
         }
      } catch (PatternSyntaxException e) {
         log.debug(e);
      }
      if (filterText.getText().length() == 0) {
         rf = getFilter("");
         searches = -1;
      }

      if (table.getModel().getRowCount() > 0) {
         // FIXME Doesn't seem to work when adding a row to empty table that already has a filter set!!
         tableRowSorter.setRowFilter(rf);
      }
   }

   private RowFilter<Object, Object> getFilter(String text) {
      List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(2);
      filters.add(RowFilter.regexFilter(text));
      return RowFilter.andFilter(filters);
   }

   int[] getArray(ColumnDataType... columns) {
      List<Integer> array = new ArrayList<Integer>();
      for (ColumnDataType column : columns) {
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

   public String getSearches() {
      return searches >= 0 ? String.valueOf(searches) : "All";
   }
}