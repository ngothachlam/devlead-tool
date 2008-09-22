/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public enum Column {
   // String Defaults
   URL, Jira, Description, FixVersion, JiraStatus, Resolution, BuildNo, Type, Note, Planned_Sprint, Resolved_Sprint, Closed_Sprint, BoardStatus, Dev_Estimate, QA_Estimate, Dev_Actual,

   // Boolean Defaults
   isInProgress {
      @Override
      public Object getCellContentDefault() {
         return Boolean.FALSE;
      }
   },
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