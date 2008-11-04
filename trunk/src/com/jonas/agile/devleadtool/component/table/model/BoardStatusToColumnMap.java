package com.jonas.agile.devleadtool.component.table.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;

public class BoardStatusToColumnMap {

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
   public static Column getColumn(BoardStatusValue value) {
      Set<Column> columns = map.keySet();
      for (Column column : columns) {
         if (map.get(column).equals(value))
            return column;
      }
      return null;
   }
}