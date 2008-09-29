package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

   private static final ColumnDataType[] columns = { ColumnDataType.Jira, ColumnDataType.Description, ColumnDataType.isOpen, ColumnDataType.isBug, ColumnDataType.isInProgress, ColumnDataType.isResolved, ColumnDataType.isComplete, ColumnDataType.isInPlan, ColumnDataType.ListPrio };
   static Logger log = MyLogger.getLogger(BoardTableModel.class);


   public BoardTableModel() {
      super(columns);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<ColumnDataType> header) {
      super(columns, contents, header);
   }

   public List<BoardStatus> getStatus(String jira) {
      int row = getRowOfSameValueInColumn(jira, ColumnDataType.Jira);
      log.debug("row: " + row + " for jira: " + jira);
      List<BoardStatus> list = new ArrayList<BoardStatus>();
      if (row >= 0) {
         BoardStatus result = null;
         for (ColumnDataType column : columns) {
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

   private boolean getBoardStatus(int row, ColumnDataType column) {
      int columnTemp = getColumnNo(column);
      Boolean valueAt = (Boolean) getValueAt(row, columnTemp);
      return valueAt == null ? false : valueAt.booleanValue();
   }
}

class BoardStatusToColumnMap {

   private static final Map<ColumnDataType, BoardStatus> map = new HashMap<ColumnDataType, BoardStatus>();
   private static final BoardStatusToColumnMap mapping1 = new BoardStatusToColumnMap(ColumnDataType.isOpen, BoardStatus.Open);
   private static final BoardStatusToColumnMap mapping2 = new BoardStatusToColumnMap(ColumnDataType.isBug, BoardStatus.Bugs);
   private static final BoardStatusToColumnMap mapping3 = new BoardStatusToColumnMap(ColumnDataType.isInProgress, BoardStatus.InProgress);
   private static final BoardStatusToColumnMap mapping4 = new BoardStatusToColumnMap(ColumnDataType.isResolved, BoardStatus.Resolved);
   private static final BoardStatusToColumnMap mapping5 = new BoardStatusToColumnMap(ColumnDataType.isComplete, BoardStatus.Complete);

   public BoardStatusToColumnMap(ColumnDataType column, BoardStatus status) {
      map.put(column, status);
   }

   public static BoardStatus getBoardStatus(ColumnDataType column) {
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