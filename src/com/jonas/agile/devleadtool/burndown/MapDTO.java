package com.jonas.agile.devleadtool.burndown;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class MapDTO {

   public Vector<Integer> getCriteriaCols() {
      return criteriaCols;
   }

   public Map<String, Integer> getColumnNameAndItsLocationMap() {
      return columnNameAndItsLocationMap;
   }

   private final Vector<Integer> criteriaCols;
   private final Map<String, Integer> columnNameAndItsLocationMap;

   public MapDTO(Map<String, Integer> columnNameAndItsLocationMap, Vector<Integer> criteriaCols) {
      this.columnNameAndItsLocationMap = columnNameAndItsLocationMap;
      this.criteriaCols = criteriaCols;
   }
   
   
}
