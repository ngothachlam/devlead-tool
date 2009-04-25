/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;

public enum Column {
   // String Defaults
   Jira(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false) {

      @Override
      public String parseFromPersistanceStore(Object cellContents) {
         if (cellContents == null)
            return null;
         return cellContents.toString().toUpperCase();
      }
   },
   Description(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   Planned_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false),
   Merge(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false),
   Resolved_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false),
   Closed_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false),
   DevEst(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true),
   DevRem(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true),
   QAEst(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true),
   DevAct(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true),
   Note(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false),
   Release(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false),
   BoardStatus(BoardStatusValue.class, BoardStatusValue.UnKnown, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false) {
      @Override
      public BoardStatusValue parseFromPersistanceStore(Object cellContents) {
         return BoardStatusValue.get(cellContents.toString());
      }
   },

   // Complex Objects:
   Sprint(Sprint.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false) {
      @Override
      public String parseToPersistanceStore(Object value) {
         Sprint sprint = (Sprint) value;
         return sprint == null ? "" : sprint.getName();
      }

      @Override
      public Sprint parseFromPersistanceStore(Object cellContents) {
         return SprintCache.getInstance().getSprintWithName(cellContents.toString());
      }
   },

   // Jira Sync columns:
   Status(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   Resolution(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   BuildNo(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   FixVersion(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   Type(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   J_DevEst(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, true),
   J_DevAct(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, true),
   J_Sprint(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   Project(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   Delivery(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.No, false),
   Owner(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),
   Environment(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false),

   // Integer
   prio(Integer.class, null, IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, true) {
      @Override
      public Integer parseFromPersistanceStore(Object cellContents) {
         String cellContentsString = cellContents.toString();
         if (cellContentsString == null || cellContentsString.trim().length() == 0)
            return -1;
         // we do parse a double initially as Cell's in spreadsheet (xls) will become 10.0 - not 10, which will otherwise result in numberformat
         // exception.
         return (int) Double.parseDouble(cellContentsString);
      }
   },

   // Boolean Defaults
   isParked(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false) {
      @Override
      public Boolean parseFromPersistanceStore(Object cellContents) {
         if (cellContents == null)
            return Boolean.FALSE;
         String cellContentsString = cellContents.toString();
         return Boolean.parseBoolean(cellContentsString);
      }
   },
   Old(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false) {
      @Override
      public Boolean parseFromPersistanceStore(Object cellContents) {
         if (cellContents == null)
            return Boolean.FALSE;
         String cellContentsString = cellContents.toString();
         return Boolean.parseBoolean(cellContentsString);
      }
   };

   private final Class defaultClass;
   private final Object defaultValue;
   private final IsJiraColumn jiraColumn;
   private final IsEditableColumn isEditable;
   private final ToLoadColumn isToLoad;
   private final Boolean isNumeric;

   // private static Map<String, Column> columns;

   private <T> Column(Class<T> defaultClass, Object defaultValue, IsEditableColumn isEditable, IsJiraColumn isJiraColumn, ToLoadColumn isToLoad,
         Boolean isNumeric) {
      this.defaultClass = defaultClass;
      this.defaultValue = defaultValue;
      this.isEditable = isEditable;
      this.jiraColumn = isJiraColumn;
      this.isToLoad = isToLoad;
      this.isNumeric = isNumeric;
   }

   public static Column getEnum(Object columnName) {
      for (Column col : Column.values()) {
         if (col.toString().equals(columnName)) {
            return col;
         }
      }
      return null;
      // return Column.columns.get(columnName);
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

   public Object parseFromPersistanceStore(Object cellContents) {
      return cellContents == null ? "" : cellContents.toString();
   }

   /**
    * The idea is that anything that can be interpreted to a double should be converted to a double when saving the data So that SUM(A:A) can be
    * done for instance. Loading it back again needs to display it as a string.
    * 
    * @param value
    * @return
    */
   public Object parseToPersistanceStore(Object value) {
      if (value == null)
         return "";

      if (isNumeric && value != null && value instanceof String) {
         String string = (String) value;
         if (string.trim().isEmpty()) {
            return string;
         }
         try {
            Double doubleV = Double.valueOf(string);
            return doubleV;
         } catch (NumberFormatException e) {
            return string;
         }
      }
      return value;
   }

}
