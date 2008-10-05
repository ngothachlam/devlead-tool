/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public enum Column {
   // String Defaults
   Jira(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No), 
   Description(String.class, "", IsEditableColumn.No, IsJiraColumn.No), 
   Planned_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No), 
   Resolved_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No), 
   Closed_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No), 
   BoardStatus(BoardStatusValue.class, BoardStatusValue.UnKnown, IsEditableColumn.Yes, IsJiraColumn.No), 
   Dev_Estimate(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No), 
   QA_Estimate(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No), 
   Dev_Actual(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No), 
   Note(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No), 
   Release(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No),
   J_Status(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes), 
   J_Resolution(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes), 
   J_BuildNo(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes), 
   J_FixVersion(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes), 
   J_Type(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes), 
   J_Dev_Estimate(String.class,"", IsEditableColumn.No, IsJiraColumn.Yes), 
   J_Dev_Spent(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes),

   //Integer
   prio(Integer.class, null, IsEditableColumn.No, IsJiraColumn.Yes),

   // Boolean Defaults
   isInProgress(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No), 
   isOpen(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No), 
   isBug(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No), 
   isResolved(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No), 
   isComplete(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No);

   private final Class defaultClass;
   private final Object defaultValue;
   private final IsJiraColumn jiraColumn;
   private final IsEditableColumn isEditable;

   private <T> Column(Class<T> defaultClass, Object defaultValue, IsEditableColumn isEditable, IsJiraColumn isJiraColumn) {
      this.defaultClass = defaultClass;
      this.defaultValue = defaultValue;
      this.isEditable = isEditable;
      this.jiraColumn = isJiraColumn;
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

   public boolean isJiraColumn() {
      return jiraColumn.intValue();
   }

   public boolean isEditable() {
      return isEditable.intValue();
   }
}
