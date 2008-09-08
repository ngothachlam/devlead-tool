package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.ColumnValue;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

   private final static Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();

   private static Column[] tableHeader = { Column.Jira, Column.Open, Column.Bugs, Column.InProgress, Column.Resolved, Column.Complete, Column.inPlan };

   static Logger log = MyLogger.getLogger(BoardTableModel.class);
   static {
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Open, 1);
      columnNames.put(Column.Bugs, 2);
      columnNames.put(Column.InProgress, 3);
      columnNames.put(Column.Resolved, 4);
      columnNames.put(Column.Complete, 5);
      columnNames.put(Column.inPlan, 6);
   }

   public BoardTableModel() {
      super(columnNames, 1);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(contents, header);
   }

   public List<BoardStatus> getStatus(String jira) {
      int row = getRowOfSameValueInColumn(jira, getColumnNo(Column.Jira));
      log.debug("row: " + row + " for jira: " + jira);
      List<BoardStatus> list = new ArrayList<BoardStatus>();
      if (row >= 0) {
         BoardStatus result = null;
         for (Column column : tableHeader) {
            switch (column) {
            case Open:
            case Bugs:
            case InProgress:
            case Resolved:
            case Complete:
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

   @Override
   public boolean isCellEditable(int row, int column) {
      return isEditable() ? column < 6 : false;
   }

   private boolean getBoardStatus(int row, Column column) {
      int columnTemp = getColumnNo(column);
      Boolean valueAt = (Boolean) getValueAt(row, columnTemp);
      return valueAt == null ? false : valueAt.booleanValue();
   }

   public Object[] getEmptyRow() {
      return new Object[] { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, ColumnValue.NA };
   }

   @Override
   public
   Map<Column, Integer> getColumnNames() {
      return columnNames;
   }
}


class BoardStatusToColumnMap {

   private static final Map<Column, BoardStatus> map = new HashMap<Column, BoardStatus>();
   private static final BoardStatusToColumnMap mapping1 = new BoardStatusToColumnMap(Column.Open, BoardStatus.Open);
   private static final BoardStatusToColumnMap mapping2 = new BoardStatusToColumnMap(Column.Bugs, BoardStatus.Bugs);
   private static final BoardStatusToColumnMap mapping3 = new BoardStatusToColumnMap(Column.InProgress, BoardStatus.InProgress);
   private static final BoardStatusToColumnMap mapping4 = new BoardStatusToColumnMap(Column.Resolved, BoardStatus.Resolved);
   private static final BoardStatusToColumnMap mapping5 = new BoardStatusToColumnMap(Column.Complete, BoardStatus.Complete);

   public BoardStatusToColumnMap(Column column, BoardStatus status) {
      map.put(column, status);
   }

   public static BoardStatus getBoardStatus(Column column) {
      return map.get(column);
   }

}