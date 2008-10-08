/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

public enum Column {
   // String Defaults
   Jira(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   Description(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   Planned_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   Resolved_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   Closed_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   BoardStatus(BoardStatusValue.class, BoardStatusValue.UnKnown, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes){
      public Object parse(String cellContents) {
         return BoardStatusValue.get(cellContents);
      }
   },
   Dev_Estimate(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   QA_Estimate(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   Dev_Actual(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   Note(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   Release(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   
   J_Status(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_Resolution(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_BuildNo(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_FixVersion(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_Type(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_Dev_Estimate(String.class,"", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_Dev_Spent(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),

   //Integer
   prio(Integer.class, null, IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes) {
      public Object parse(String cellContents) {
         return Integer.parseInt(cellContents);
      }
   },

   // Boolean Defaults
   isInProgress(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes){
      public Object parse(String cellContents) {
         return Boolean.parseBoolean(cellContents);
      }
   }, 
   isOpen(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes){
      public Object parse(String cellContents) {
         return Boolean.parseBoolean(cellContents);
      }
   }, 
   isBug(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes){
      public Object parse(String cellContents) {
         return Boolean.parseBoolean(cellContents);
      }
   }, 
   isResolved(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes){
      public Object parse(String cellContents) {
         return Boolean.parseBoolean(cellContents);
      }
   }, 
   isComplete(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes){
      public Object parse(String cellContents) {
         return Boolean.parseBoolean(cellContents);
      }
   };

   private final Class defaultClass;
   private final Object defaultValue;
   private final IsJiraColumn jiraColumn;
   private final IsEditableColumn isEditable;
   private final ToLoadColumn isToLoad;

   private <T> Column(Class<T> defaultClass, Object defaultValue, IsEditableColumn isEditable, IsJiraColumn isJiraColumn, ToLoadColumn isToLoad) {
      this.defaultClass = defaultClass;
      this.defaultValue = defaultValue;
      this.isEditable = isEditable;
      this.jiraColumn = isJiraColumn;
      this.isToLoad = isToLoad;
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
      return isEditable.boolValue();
   }
   
   public boolean isToLoad() {
      return isToLoad.boolValue();
   }

   public Object parse(String cellContents) {
      return cellContents;
   }

   public Column getUnEditable() {
      // FIXME a way to just get this column, but as uneditable format - how?
      return this;
   }
   

}
