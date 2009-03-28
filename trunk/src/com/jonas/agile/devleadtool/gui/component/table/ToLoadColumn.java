package com.jonas.agile.devleadtool.gui.component.table;

public enum ToLoadColumn {
   Yes(true), No(false);

   private final boolean boolValue;
   ToLoadColumn(boolean boolValue){
      this.boolValue = boolValue;
   }
   
   public boolean boolValue() {
      return boolValue;
   }
}
