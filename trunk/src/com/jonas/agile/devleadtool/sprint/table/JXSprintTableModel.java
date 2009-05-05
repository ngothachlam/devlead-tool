package com.jonas.agile.devleadtool.sprint.table;

import java.util.Date;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class JXSprintTableModel extends DefaultTableModel {

   private SprintCache sprintCache;

   public JXSprintTableModel() {
   }

   @Override
   public int getRowCount() {
      if (sprintCache == null)
         return 0;
      return sprintCache.getSprints().size();
   }

   private static final Logger log = MyLogger.getLogger(JXSprintTableModel.class);

   @Override
   public void setValueAt(Object value, int row, int column) {
      if (sprintCache == null)
         return;
      Sprint sprint = sprintCache.getSprints().get(row);
      sprintCache.setValueAt(value, sprint, (short) column);
   }

   @Override
   public Object getValueAt(int row, int column) {
      if (sprintCache == null)
         return null;
      if (log.isDebugEnabled())
         log.debug("getting row: " + row + " and col " + column + " size: " + sprintCache.getSprints().size());

      Sprint sprint = sprintCache.getSprintFromRow(row);
      Object valueAt = sprintCache.getValueAt(sprint, (short) column);
      if (valueAt instanceof Date) {
         valueAt = DateHelper.getDateAsSimpleOrderableString((Date) valueAt);
      }

      if (log.isDebugEnabled())
         log.debug("valueat: row: " + row + " col: " + column + " is " + valueAt);
      return valueAt;
   }

   @Override
   public boolean isCellEditable(int row, int column) {
      return false;
   }

   @Override
   public int getColumnCount() {
      if (sprintCache == null)
         return 0;

      int columnCount = sprintCache.getColumnCount();
      log.debug(columnCount + "");
      return columnCount;
   }

   @Override
   public String getColumnName(int column) {
      if (sprintCache == null)
         return null;

      String columnName = sprintCache.getColumnName(column);
      log.debug(columnName);
      return columnName;
   }

   public void setSprintCache(SprintCache sprintCache) {
      this.sprintCache = sprintCache;
   }

}
