/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public enum ColumnDataType {
   // String Defaults
   URL, Jira, Description, J_Status, J_Resolution, J_BuildNo, Note, Planned_Sprint, Resolved_Sprint, Closed_Sprint, BoardStatus, Dev_Estimate, Dev_Spent, QA_Estimate, Dev_Actual,
   J_FixVersion, J_Type, J_Dev_Estimate, J_Dev_Spent,

   ListPrio {
      @Override
      public Object getCellContentDefault() {
         return new Integer(-1);
      }
   },

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

   public static ColumnDataType getEnum(String columnName) {
      for (ColumnDataType col : ColumnDataType.values()) {
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