package com.jonas.agile.devleadtool.component.table;

public enum ToLoadColumn {
   Yes(true), No(false);

   private final boolean intValue;
   ToLoadColumn(boolean intValue){
      this.intValue = intValue;
   }
   
   public boolean intValue() {
      return intValue;
   }
}
