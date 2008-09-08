package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.isOpen, Column.isBug, Column.isInProgress, Column.isResolved, Column.isComplete, Column.isInPlan };
   static Logger log = MyLogger.getLogger(BoardTableModel.class);


   public BoardTableModel() {
      super(columns);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   public List<BoardStatus> getStatus(String jira) {
      int row = getRowOfSameValueInColumn(jira, Column.Jira);
      log.debug("row: " + row + " for jira: " + jira);
      List<BoardStatus> list = new ArrayList<BoardStatus>();
      if (row >= 0) {
         BoardStatus result = null;
         for (Column column : columns) {
            switch (column) {
            case isOpen:
            case isBug:
            case isInProgress:
            case isResolved:
            case isComplete:
               if (getBoardStatus(row, column)) {
                  list.add(BoardStatusToColumnMap.getBoardStatus(column));
               }
            default:
               break;
            }
         }
         if (list.size() == 0)
            list.add(BoardStatus.Empty);
      }
      return list;
   }

   private boolean getBoardStatus(int row, Column column) {
      int columnTemp = getColumnNo(column);
      Boolean valueAt = (Boolean) getValueAt(row, columnTemp);
      return valueAt == null ? false : valueAt.booleanValue();
   }
}

class BoardStatusToColumnMap {

   private static final Map<Column, BoardStatus> map = new HashMap<Column, BoardStatus>();
   private static final BoardStatusToColumnMap mapping1 = new BoardStatusToColumnMap(Column.isOpen, BoardStatus.Open);
   private static final BoardStatusToColumnMap mapping2 = new BoardStatusToColumnMap(Column.isBug, BoardStatus.Bugs);
   private static final BoardStatusToColumnMap mapping3 = new BoardStatusToColumnMap(Column.isInProgress, BoardStatus.InProgress);
   private static final BoardStatusToColumnMap mapping4 = new BoardStatusToColumnMap(Column.isResolved, BoardStatus.Resolved);
   private static final BoardStatusToColumnMap mapping5 = new BoardStatusToColumnMap(Column.isComplete, BoardStatus.Complete);

   public BoardStatusToColumnMap(Column column, BoardStatus status) {
      map.put(column, status);
   }

   public static BoardStatus getBoardStatus(Column column) {
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