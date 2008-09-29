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

   private static final Column[] columns = { Column.Jira, Column.Description, Column.isOpen, Column.isBug, Column.isInProgress, Column.isResolved, Column.isComplete, Column.ListPrio };
   static Logger log = MyLogger.getLogger(BoardTableModel.class);


   public BoardTableModel() {
      super(columns);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   public List<BoardStatusValue> getStatus(String jira) {
      int row = getRowOfSameValueInColumn(jira, Column.Jira);
      log.debug("row: " + row + " for jira: " + jira);
      List<BoardStatusValue> list = new ArrayList<BoardStatusValue>();
      if (row >= 0) {
         BoardStatusValue result = null;
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
            list.add(BoardStatusValue.UnKnown);
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