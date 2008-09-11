/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public class ColumnNew {
   
   public static final ColumnNew URL = new ColumnNew("", Column.URL);
   public static final ColumnNew Jira = new ColumnNew("", Column.Jira);
   public static final ColumnNew Description = new ColumnNew("", Column.Description);
   public static final ColumnNew FixVersion = new ColumnNew("", Column.FixVersion);
   public static final ColumnNew Status = new ColumnNew("", Column.Status);
   public static final ColumnNew Resolution = new ColumnNew("", Column.Resolution);
   public static final ColumnNew BuildNo = new ColumnNew("", Column.BuildNo);
   public static final ColumnNew Type = new ColumnNew("", Column.Type);
   public static final ColumnNew Planned_Sprint = new ColumnNew("", Column.Planned_Sprint);
   public static final ColumnNew Resolved_Sprint = new ColumnNew("", Column.Resolved_Sprint);
   public static final ColumnNew Closed_Sprint = new ColumnNew("", Column.Closed_Sprint);
   public static final ColumnNew Note = new ColumnNew("", Column.Note);
   
   public static final ColumnNew Estimate = new ColumnNew(0f, Column.Estimate);
   public static final ColumnNew Actual = new ColumnNew(0f, Column.Actual);
   
   public static final ColumnNew isOpen = new ColumnNew(Boolean.FALSE, Column.isOpen);
   public static final ColumnNew isBug = new ColumnNew(Boolean.FALSE, Column.isBug);
   public static final ColumnNew isInProgress = new ColumnNew(Boolean.FALSE, Column.Remaining_Progress);
   public static final ColumnNew isResolved = new ColumnNew(Boolean.FALSE, Column.isResolved);
   public static final ColumnNew isComplete = new ColumnNew(Boolean.FALSE, Column.isComplete);
   
   public static final ColumnNew isInPlan = new ColumnNew(ColumnValue.NA, Column.isInPlan);
   
   private final Object defaultValue;
   private final Column column;

   public ColumnNew(Object defaultValue, Column column) {
      this.defaultValue = defaultValue;
      this.column = column;
   }

   public static Column getEnum(String columnName) {
      for (Column col : Column.values()) {
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