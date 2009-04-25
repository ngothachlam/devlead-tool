package com.jonas.agile.devleadtool.sprint.table;

import java.util.Date;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class JXSprintTableModel extends DefaultTableModel {

   @Override
   public int getRowCount() {
      return SprintCache.getInstance().getSprints().size();
   }

   private static final Logger log = MyLogger.getLogger(JXSprintTableModel.class);

   @Override
   public void setValueAt(Object value, int row, int column) {
      SprintCache instance = SprintCache.getInstance();
      Sprint sprint = instance.getSprints().get(row);
      instance.setValueAt(value, sprint, (short) column);
   }

   @Override
   public Object getValueAt(int row, int column) {
      SprintCache instance = SprintCache.getInstance();
      log.debug("getting row: " + row + " and col " + column + " size: " + instance.getSprints().size());

      Sprint sprint = instance.getSprintFromRow(row);
      Object valueAt = instance.getValueAt(sprint, (short) column);
      if (valueAt instanceof Date) {
         valueAt = DateHelper.getDateAsSimpleOrderableString((Date) valueAt);
      }

      log.debug("valueat: row: " + row + " col: " + column + " is " + valueAt);
      return valueAt;
   }

   @Override
   public boolean isCellEditable(int row, int column) {
      return false;
   }

   @Override
   public int getColumnCount() {
      int columnCount = SprintCache.getInstance().getColumnCount();
      log.debug(columnCount + "");
      return columnCount;
   }

   @Override
   public String getColumnName(int column) {
      String columnName = SprintCache.getInstance().getColumnName(column);
      log.debug(columnName);
      return columnName;
   }

}
