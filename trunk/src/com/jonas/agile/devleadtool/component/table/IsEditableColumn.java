package com.jonas.agile.devleadtool.component.table;

public enum IsEditableColumn {
   Yes(true), No(false);

   private final boolean intValue;

   IsEditableColumn(boolean intValue) {
      this.intValue = intValue;
   }

   public boolean intValue() {
      return intValue;
   }
}