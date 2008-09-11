/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public enum Column {
   // String Defaults
   URL, Jira, Description, FixVersion, Status, Resolution, BuildNo, Type, Note, Planned_Sprint, Resolved_Sprint, Closed_Sprint, Remaining_Progress,

   // Float Defaults
   Estimate {
      @Override
      public Object getCellContentDefault() {
         return 0f;
      }
   },
   Actual {
      @Override
      public Object getCellContentDefault() {
         return 0f;
      }
   },

   // Boolean Defaults
   isOpen {
      @Override
      public Object getCellContentDefault() {
         return Boolean.FALSE;
      }
   },
   isBug {
      @Override
      public Object getCellContentDefault() {
         return Boolean.FALSE;
      }
   },
   isResolved {
      @Override
      public Object getCellContentDefault() {
         return Boolean.FALSE;
      }
   },
   isComplete {
      @Override
      public Object getCellContentDefault() {
         return Boolean.FALSE;
      }
   },

   // Other Defaults
   isInPlan {
      @Override
      public Object getCellContentDefault() {
         return ColumnValue.NA;
      }

   };

   public static Column getEnum(String columnName) {
      for (Column col : Column.values()) {
         if (col.toString().equals(columnName)) {
            return col;
         }
      }
      return null;

   }

   public Object getCellContentDefault() {
      return "";
   }
}