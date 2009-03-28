package com.jonas.agile.devleadtool.gui.component.table;

public enum IsJiraColumn{
   Yes(true), No(false);

   private final boolean intValue;
   IsJiraColumn(boolean intValue){
      this.intValue = intValue;
   }
   
   public boolean intValue() {
      return intValue;
   }
}
