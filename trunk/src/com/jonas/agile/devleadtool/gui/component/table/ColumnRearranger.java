package com.jonas.agile.devleadtool.gui.component.table;

import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.table.TableColumnExt;

import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;

public class ColumnRearranger {

   private MyTable table;
   private Logger log = MyLogger.getLogger(ColumnRearranger.class);

   public ColumnRearranger(MyTable table) {
      this.table = table;
   }

   private int getNewColumn(String string) {
      for (int colIndex = 0; colIndex < table.getColumnCount(); colIndex++) {
         if (table.getColumnName(colIndex).equals(string))
            return colIndex;
      }
      return -1;
   }

   private void hideAllColumns() {
      for (int col = table.getColumnCount() - 1; col >= 0; col--) {
         TableColumnExt columnExt = table.getColumnExt(col);
         columnExt.setVisible(false);
      }
   }

   void showAndRearrangeColumns(String... colsToShowAndRearrange) {
      hideAllColumns();
      for (String columnName : colsToShowAndRearrange) {
         showAndRearrangeColumnToCurrentLastColumn(columnName);
      }
   }

   private void showAndRearrangeColumnToCurrentLastColumn(String string) {
      TableColumnModel columnModel = table.getColumnModel();
      TableColumnExt columnExt;
      columnExt = table.getColumnExt(string);
      if (columnExt == null) {
         log.warn(string + " does not exist in table (maybe hidden already? or it just doesn't exist even in the model?");
         return;
      }
      columnExt.setVisible(true);
      int addedColIndex = getNewColumn(string);
      int newIndex = table.getColumnCount() - 1;
      if (newIndex != addedColIndex)
         columnModel.moveColumn(addedColIndex, newIndex);
   }

   public void resetColumns() {
      hideAllColumns();
      MyTableModel model = table.getMyModel();
      for (int col = 0; col < model.getColumnCount(); col++) {
         String colName = model.getColumnName(col);
         showAndRearrangeColumnToCurrentLastColumn(colName);
      }
   }

   
}
