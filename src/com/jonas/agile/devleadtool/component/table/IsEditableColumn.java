package com.jonas.agile.devleadtool.component.table;

public enum IsEditableColumn {
   Yes(true), No(false);

   private final boolean boolValue;

   IsEditableColumn(boolean boolValue) {
      this.boolValue = boolValue;
   }

   public boolean boolValue() {
      return boolValue;
   }
}