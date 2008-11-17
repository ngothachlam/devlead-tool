package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

   private static final List<Column> mutuallyExclusive = new ArrayList<Column>(5);

   private static final Column[] columns = { Column.Jira, Column.Description, Column.J_Resolution, Column.Release, Column.isOpen, Column.isBug,
         Column.isInProgress, Column.isResolved, Column.isComplete, Column.Dev_Estimate, Column.Dev_Actual, Column.prio };
   static Logger log = MyLogger.getLogger(BoardTableModel.class);


   static {
      mutuallyExclusive.add(Column.isOpen);
      mutuallyExclusive.add(Column.isBug);
      mutuallyExclusive.add(Column.isInProgress);
      mutuallyExclusive.add(Column.isResolved);
      mutuallyExclusive.add(Column.isComplete);
   }

   public BoardTableModel() {
      super(columns, true);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header, true);
   }

   public BoardStatusValue getStatus(String jira) {
      int row = getRowWithJira(jira);
      log.debug("row: " + row + " for jira: " + jira);
      int count = 0;
      Column col = null;
      if (row >= 0) {
         for (Column column : columns) {
            switch (column) {
            case isOpen:
            case isBug:
            case isInProgress:
            case isResolved:
            case isComplete:
               if (isColumnSet(row, column)) {
                  count++;
                  col = column;
               }
            default:
               break;
            }
         }
         if (count == 1) {
            return BoardStatusToColumnMap.getBoardStatus(col);
         }
         return BoardStatusValue.UnKnown;
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
      if (mutuallyExclusive != null) {
         if (mutuallyExclusive.contains(columnEnum)) {
            if (value != null && value instanceof Boolean) {
               boolean isThisSet = ((Boolean) value).booleanValue();
               int countOfMutuallyExclusiveSet = isThisSet ? 1 : 0;
               for (Column col : mutuallyExclusive) {
                  Object valueAt = getValueAt(col, row);
                  if (valueAt != null && valueAt instanceof Boolean && col != columnEnum && ((Boolean) valueAt).booleanValue()) {
                     countOfMutuallyExclusiveSet++;
                  }
                  if (countOfMutuallyExclusiveSet >= 2)
                     break;
               }

               if (isThisSet && countOfMutuallyExclusiveSet > 1) {
                  result = true;
               } else if (!isThisSet && countOfMutuallyExclusiveSet == 0)
                  result = true;
            }
         } else {
            String stringValue = "";
            switch (columnEnum) {
            case Dev_Estimate:
               stringValue = (String) value;
               if (stringValue == null || stringValue.trim().length() <= 0) {
                  if (howManyTrue(row, Column.isOpen, Column.isInProgress, Column.isResolved, Column.isParked, Column.isComplete) == 1) {
                     return true;
                  }
               }
               break;
            case Dev_Actual:
               stringValue = (String) value;
               if (stringValue == null || stringValue.trim().length() <= 0) {
                  if (howManyTrue(row, Column.isResolved, Column.isComplete) == 1) {
                     return true;
                  }
               } else {
                  if (howManyTrue(row, Column.isBug, Column.isOpen, Column.isParked, Column.isInProgress) == 1) {
                     return true;
                  }
               }
               break;
            default:
               break;
            }
         }
      }
      return result;
   }

   int howManyTrue(int row, Column... boolColumns) {
      int j = 0;
      for (int i = 0; i < boolColumns.length; i++) {
         Column column = boolColumns[i];
         if (getColumnIndex(column) >= 0) {
            Boolean value = (Boolean) getValueAt(column, row);
            if (value != null && value.booleanValue() == true)
               j++;
         }
      }
      return j;
   }

   public String getRelease(String jira) {
      if (doesJiraExist(jira))
         return (String) getValueAt(Column.Release, jira);
      return "";
   }
}
