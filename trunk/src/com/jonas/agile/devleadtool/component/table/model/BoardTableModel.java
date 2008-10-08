package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

   private static final List<Column> mutuallyExclusive = new ArrayList<Column>(5);

   private static final Column[] columns = { Column.Jira, Column.Description, Column.J_Resolution, Column.isOpen, Column.isBug, Column.isInProgress,
         Column.isResolved, Column.isComplete, Column.Dev_Estimate, Column.Dev_Actual, Column.prio };
   static Logger log = MyLogger.getLogger(BoardTableModel.class);

   static {
      mutuallyExclusive.add(Column.isOpen);
      mutuallyExclusive.add(Column.isBug);
      mutuallyExclusive.add(Column.isInProgress);
      mutuallyExclusive.add(Column.isResolved);
      mutuallyExclusive.add(Column.isComplete);
   }

   public BoardTableModel() {
      super(columns);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   public List<BoardStatusValue> getStatus(String jira) {
      int row = getRowWithJira(jira, Column.Jira);
      log.debug("row: " + row + " for jira: " + jira);
      List<BoardStatusValue> list = new ArrayList<BoardStatusValue>();
      if (row >= 0) {
         for (Column column : columns) {
            switch (column) {
            case isOpen:
            case isBug:
            case isInProgress:
            case isResolved:
            case isComplete:
            default:
               if (getBoardStatus(row, column)) {
                  list.add(BoardStatusToColumnMap.getBoardStatus(column));
               }
               break;
            }
         }
         if (list.size() == 0)
            list.add(BoardStatusValue.UnKnown);
      }
      return list;
   }

   private boolean getBoardStatus(int row, Column column) {
      int columnTemp = getColumnIndex(column);
      Boolean valueAt = (Boolean) getValueAt(row, columnTemp);
      return valueAt == null ? false : valueAt.booleanValue();
   }

   @Override
   public boolean isRed(Object value, int row, int column) {
      boolean result = false;
      Column columnEnum = getColumn(column);
      if (columnEnum != null) {
         if (mutuallyExclusive != null && mutuallyExclusive.contains(columnEnum)) {
            if (value != null && value instanceof Boolean) {
               boolean isThisSet = ((Boolean) value).booleanValue();
               int countOfMutuallyExclusiveSet = isThisSet ? 1 : 0;
               for (Column col : mutuallyExclusive) {
                  Object valueAt = getValueAt(col, row);
                  if (valueAt != null && valueAt instanceof Boolean && col != columnEnum && ((Boolean) valueAt).booleanValue()) {
                     countOfMutuallyExclusiveSet++;
                  }
                  if (countOfMutuallyExclusiveSet == 2)
                     break;
               }

               if (isThisSet && countOfMutuallyExclusiveSet > 1)
                  result = true;
               else if (!isThisSet && countOfMutuallyExclusiveSet == 0)
                  result = true;
               fireTableRowsUpdated(row, row);
            }
         }
      }
      return result;
   }
}


class BoardStatusToColumnMap {

   private static final Map<Column, BoardStatusValue> map = new HashMap<Column, BoardStatusValue>();
   private static final BoardStatusToColumnMap mapping1 = new BoardStatusToColumnMap(Column.isOpen, BoardStatusValue.Open);
   private static final BoardStatusToColumnMap mapping2 = new BoardStatusToColumnMap(Column.isBug, BoardStatusValue.Bugs);
   private static final BoardStatusToColumnMap mapping3 = new BoardStatusToColumnMap(Column.isInProgress, BoardStatusValue.InProgress);
   private static final BoardStatusToColumnMap mapping4 = new BoardStatusToColumnMap(Column.isResolved, BoardStatusValue.Resolved);
   private static final BoardStatusToColumnMap mapping5 = new BoardStatusToColumnMap(Column.isComplete, BoardStatusValue.Complete);

   public BoardStatusToColumnMap(Column column, BoardStatusValue status) {
      map.put(column, status);
   }

   public static BoardStatusValue getBoardStatus(Column column) {
      return map.get(column);
   }
}


class Counter {
   private int i = 0;

   public int getValueAndIncrease() {
      return i++;
   }

   public void reset() {
      i = 0;
   }
}