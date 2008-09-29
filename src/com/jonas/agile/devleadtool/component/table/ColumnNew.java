/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public class ColumnNew {
   
   public static final ColumnNew URL = new ColumnNew("", ColumnDataType.URL);
   public static final ColumnNew Jira = new ColumnNew("", ColumnDataType.Jira);
   public static final ColumnNew Description = new ColumnNew("", ColumnDataType.Description);
   public static final ColumnNew FixVersion = new ColumnNew("", ColumnDataType.J_FixVersion);
   public static final ColumnNew Status = new ColumnNew("", ColumnDataType.J_Status);
   public static final ColumnNew Resolution = new ColumnNew("", ColumnDataType.J_Resolution);
   public static final ColumnNew BuildNo = new ColumnNew("", ColumnDataType.J_BuildNo);
   public static final ColumnNew Type = new ColumnNew("", ColumnDataType.J_Type);
   public static final ColumnNew Planned_Sprint = new ColumnNew("", ColumnDataType.Planned_Sprint);
   public static final ColumnNew Resolved_Sprint = new ColumnNew("", ColumnDataType.Resolved_Sprint);
   public static final ColumnNew Closed_Sprint = new ColumnNew("", ColumnDataType.Closed_Sprint);
   public static final ColumnNew Note = new ColumnNew("", ColumnDataType.Note);
   
   public static final ColumnNew Estimate = new ColumnNew(0f, ColumnDataType.Dev_Estimate);
   public static final ColumnNew Actual = new ColumnNew(0f, ColumnDataType.Dev_Actual);
   
   public static final ColumnNew isOpen = new ColumnNew(Boolean.FALSE, ColumnDataType.isOpen);
   public static final ColumnNew isBug = new ColumnNew(Boolean.FALSE, ColumnDataType.isBug);
   public static final ColumnNew isInProgress = new ColumnNew(Boolean.FALSE, ColumnDataType.isInProgress);
   public static final ColumnNew isResolved = new ColumnNew(Boolean.FALSE, ColumnDataType.isResolved);
   public static final ColumnNew isComplete = new ColumnNew(Boolean.FALSE, ColumnDataType.isComplete);
   
   public static final ColumnNew isInPlan = new ColumnNew(ColumnValue.NA, ColumnDataType.isInPlan);
   
   private final Object defaultValue;
   private final ColumnDataType column;

   public ColumnNew(Object defaultValue, ColumnDataType column) {
      this.defaultValue = defaultValue;
      this.column = column;
   }

   public static ColumnDataType getEnum(String columnName) {
      for (ColumnDataType col : ColumnDataType.values()) {
         if (col.toString().equals(columnName)) {
            return col;
         }
      }
      return null;
   }

   public Object getCellContentDefault() {
      return defaultValue;
   }
}