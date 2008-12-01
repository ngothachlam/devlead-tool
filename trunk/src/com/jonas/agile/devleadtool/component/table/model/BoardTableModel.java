package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {


   private static final Column[] columns = { Column.Jira, Column.Description, Column.J_Resolution, Column.Release, Column.Merge, Column.BoardStatus, Column.Dev_Estimate, Column.Dev_Actual, Column.prio };
   static Logger log = MyLogger.getLogger(BoardTableModel.class);


   public BoardTableModel() {
      super(columns, true);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header, true);
   }

   public BoardStatusValue getStatus(String jira) {
      int row = getRowWithJira(jira);
      log.debug("row: " + row + " for jira: " + jira);
      if (row >= 0) {
         BoardStatusValue valueAt = (BoardStatusValue) getValueAt(Column.BoardStatus, jira);
         log.debug("valueat: " + valueAt);
         return valueAt;
      }
      return BoardStatusValue.NA;
   }

   private boolean isColumnSet(int row, Column column) {
      int colIndex = getColumnIndex(column);
      Boolean valueAt = (Boolean) getValueAt(row, colIndex);
      return valueAt == null ? false : valueAt.booleanValue();
   }

   @Override
   public boolean isRed(Object value, int row, int column) {
      boolean result = false;
      Column columnEnum = getColumn(column);
      if (columnEnum == null) {
         return false;
      }
      if (getColumnIndex(Column.BoardStatus) >= 0 ) {
            String stringValue = "";
            switch (columnEnum) {
            case Dev_Estimate:
               log.debug("value: " + value);
               stringValue = (String) value;
               if (stringValue == null || stringValue.trim().length() <= 0) {
                  if (isBoardValueEither(row, BoardStatusValue.Open, BoardStatusValue.InProgress, BoardStatusValue.Resolved, BoardStatusValue.Complete)) {
                     return true;
                  }
               }
               break;
            case Dev_Actual:
               stringValue = (String) value;
               if (stringValue == null || stringValue.trim().length() <= 0) {
                  if (isBoardValueEither(row, BoardStatusValue.Resolved, BoardStatusValue.Complete)) {
                     return true;
                  }
               } else {
                  if (isBoardValueEither(row, BoardStatusValue.Bug, BoardStatusValue.Open, BoardStatusValue.InProgress)) {
                     return true;
                  }
               }
               break;
            default:
               break;
            }
      }
      return result;
   }

   boolean isBoardValueEither(int row, BoardStatusValue... values) {
      Object value = getValueAt(Column.BoardStatus, row);
      log.debug("board status: " + value);
      for (int i = 0; i < values.length; i++) {
         BoardStatusValue boardStatusValue = values[i];
         log.debug("checking against : " + boardStatusValue);
         if(boardStatusValue.equals(value)){
            log.debug("match!");
            return true;
         }
      }
      return false;
   }

   public String getRelease(String jira) {
      if (doesJiraExist(jira))
         return (String) getValueAt(Column.Release, jira);
      return "";
   }
}
