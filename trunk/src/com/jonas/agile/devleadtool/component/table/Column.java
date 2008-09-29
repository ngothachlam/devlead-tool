/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public enum Column {
   // String Defaults
   Jira(String.class, "", true), 
   Description(String.class, "", true), 
   J_Status(String.class, "", false), 
   J_Resolution(String.class, "", false), 
   J_BuildNo(String.class, "", false), 
   Note(String.class, "", true), 
   Planned_Sprint(String.class, "", true), 
   Resolved_Sprint(String.class, "", true), 
   Closed_Sprint(String.class, "", true), 
   BoardStatus(BoardStatusValue.class, BoardStatusValue.UnKnown, true), 
   Dev_Estimate(String.class, "", true), 
   QA_Estimate(String.class, "", true), 
   Dev_Actual(String.class, "", true), 
   J_FixVersion(String.class, "", false), 
   J_Type(String.class, "", false), 
   J_Dev_Estimate(String.class,"", false), 
   J_Dev_Spent(String.class, "", false),

   //Integer
   ListPrio(Integer.class, null, true),

   // Boolean Defaults
   isInProgress(Boolean.class, Boolean.FALSE, true), 
   isOpen(Boolean.class, Boolean.FALSE, true), 
   isBug(Boolean.class, Boolean.FALSE, true), 
   isResolved(Boolean.class, Boolean.FALSE, true), 
   isComplete(Boolean.class, Boolean.FALSE, true);

   private final Class defaultClass;
   private final Object defaultValue;
   private final boolean shouldLoad;

   private <T> Column(Class<T> defaultClass, Object defaultValue, boolean shouldLoad) {
      this.defaultClass = defaultClass;
      this.defaultValue = defaultValue;
      this.shouldLoad = shouldLoad;

   }

   public static Column getEnum(String columnName) {
      for (Column col : Column.values()) {
         if (col.toString().equals(columnName)) {
            return col;
         }
      }
      return null;
   }

   public Object getDefaultValue() {
      return defaultValue;
   }
   
   public Class getDefaultClass() {
      return defaultClass;
   }

   public boolean shouldLoad() {
      return shouldLoad;
   }
}