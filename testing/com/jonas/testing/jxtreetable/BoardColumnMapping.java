package com.jonas.testing.jxtreetable;

import java.util.HashMap;
import java.util.Map;

public class BoardColumnMapping {
   private final Map<Integer, Column> indexMap = new HashMap<Integer, Column>();

   private static int columnIndex = 0;
   public static final BoardColumnMapping boardColumnMapping = new BoardColumnMapping();
   
   private BoardColumnMapping() {
      add(Column.JIRA);
      add(Column.DESCRIPTION);
   }

   private void add(Column column){
      indexMap.put(columnIndex++, column);
   }
   
   public Column getColumnForIndex(int columnIndex) {
      return indexMap.get(columnIndex);
   }
   
}
