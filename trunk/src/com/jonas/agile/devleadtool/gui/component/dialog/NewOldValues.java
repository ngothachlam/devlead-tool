package com.jonas.agile.devleadtool.gui.component.dialog;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;

public class NewOldValues {

   public Object getOldValue() {
      return oldValue;
   }

   public ColumnType getColumn() {
      return column;
   }

   private final ColumnType column;
   private final String oldValue;
   private final Object newValue;
   private boolean isDifferent;

   public NewOldValues(ColumnType column, String oldValue, Object newValue) {
      this.column = column;
      this.newValue = newValue;
      this.oldValue = oldValue;
      if (oldValue == null) {
         isDifferent = newValue == null ? false : true;
         return;
      }
      if (!oldValue.equals(newValue)) {
         isDifferent = true;
      }
   }

   public boolean isValueNew() {
      return isDifferent;
   }

   public Object getNewValue() {
      return newValue;
   }
}