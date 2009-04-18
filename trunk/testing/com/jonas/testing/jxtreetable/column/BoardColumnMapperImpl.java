package com.jonas.testing.jxtreetable.column;

import java.util.HashMap;
import java.util.Map;

public class BoardColumnMapperImpl implements ColumnMapper {
   final Map<Integer, Column> indexMap = new HashMap<Integer, Column>();

   private static int columnIndex = 0;
   public static final ColumnMapper boardColumnMapping = new BoardColumnMapperImpl();
   
   private BoardColumnMapperImpl() {
      add(Column.REF);
      add(Column.DESCRIPTION);
   }

   private void add(Column column){
      indexMap.put(columnIndex++, column);
   }
   
   @Override
   public Column getColumn(int columnIndex) {
      return indexMap.get(columnIndex);
   }

   @Override
   public int getColumnCount() {
      return indexMap.size();
   }
   
}
