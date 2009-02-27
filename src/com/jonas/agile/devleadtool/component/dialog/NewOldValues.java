package com.jonas.agile.devleadtool.component.dialog;

import com.jonas.agile.devleadtool.component.table.Column;

public class NewOldValues {

   public Object getOldValue() {
      return oldValue;
   }

   public Column getColumn() {
      return column;
   }

   private final String jira;
   private final Column column;
   private final String oldValue;
   private final Object newValue;
   private boolean isDifferent;

   public NewOldValues(String jira, Column column, String oldValue, Object newValue) {
      this.jira = jira;
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