/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

import java.util.ArrayList;
import java.util.List;

public enum Column {
   // String Defaults
   Jira(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {

      public String parseFromData(Object cellContents) {
         if (cellContents == null)
            return null;
         return cellContents.toString().toUpperCase();
      }
   },
   Description(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   Planned_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   Merge(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   Resolved_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   Closed_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),

   Dev_Estimate(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   QA_Estimate(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   Dev_Actual(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   Note(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   Release(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   BoardStatus(BoardStatusValue.class, BoardStatusValue.UnKnown, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
      public BoardStatusValue parseFromData(String cellContents) {
         return BoardStatusValue.get(cellContents);
      }
   },

   // Jira Sync columns:
   J_Status(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   J_Resolution(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   J_BuildNo(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   J_FixVersion(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes) {
      @Override
      public Object parseFromData(Object cellContents) {
         List<String> arrayList = new ArrayList<String>();
         if (cellContents instanceof String) {
            String cellContentsAsString = cellContents.toString();
            if (cellContentsAsString == null || cellContentsAsString.trim().length() == 0) {
               return arrayList;
            }
            cellContentsAsString = cellContentsAsString.substring(1, cellContentsAsString.length() - 1);
            String[] split = cellContentsAsString.split("(, )");
            for (String string : split) {
               arrayList.add(string);
            }
         }
         return arrayList;
      }
   },
   J_Type(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   J_Dev_Estimate(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   J_Dev_Spent(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   J_Sprint(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   J_Project(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),
   J_Delivery(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.No),

   // Integer
   prio(Integer.class, null, IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes) {
      public Integer parseFromData(String cellContents) {
         if (cellContents == null || cellContents.trim().length() == 0)
            return -1;
         // we do parse a double initially as Cell's in spreadsheet (xls) will become 10.0 - not 10, which will otherwise result in numberformat
         // exception.
         return (int) Double.parseDouble(cellContents);
      }
   },

   // Boolean Defaults
   isParked(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
      public Boolean parseFromData(String cellContents) {
         return Boolean.parseBoolean(cellContents);
      }
   };

   private final Class defaultClass;
   private final Object defaultValue;
   private final IsJiraColumn jiraColumn;
   private final IsEditableColumn isEditable;
   private final ToLoadColumn isToLoad;

   // private static Map<String, Column> columns;

   private <T> Column(Class<T> defaultClass, Object defaultValue, IsEditableColumn isEditable, IsJiraColumn isJiraColumn, ToLoadColumn isToLoad) {
      this.defaultClass = defaultClass;
      this.defaultValue = defaultValue;
      this.isEditable = isEditable;
      this.jiraColumn = isJiraColumn;
      this.isToLoad = isToLoad;
      // if (Column.columns == null) {
      // synchronized (Column.class) {
      // if (Column.columns == null) {
      // Column.columns = new HashMap<String, Column>(30);
      // }
      // }
      // }
      // Column.columns.put(this.toString(), this);
   }

   public static Column getEnum(String columnName) {
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

   public Object parseFromData(Object cellContents) {
      return cellContents == null ? "" : cellContents.toString();
   }

   /**
    * The idea is that anything that can be interpreted to a double should be converted to a double when saving the data
    * So that SUM(A:A) can be done for instance. Loading it back again needs to display it as a string.
    * @param value
    * @return
    */
   public Object parseToData(Object value) {
      if(value != null && value instanceof String ){
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
