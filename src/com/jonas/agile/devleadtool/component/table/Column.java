/**
 * 
 */
package com.jonas.agile.devleadtool.component.table;

import java.util.ArrayList;


public enum Column {
   // String Defaults
   Jira(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes){
         public Object parse(String cellContents) {
            if (cellContents == null )
               return null;
            String upperCase = cellContents.toUpperCase();
            System.out.println("parsing Column Jira: " + cellContents + " to become " + upperCase);
            return upperCase;
         }
   },
   Description(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   Planned_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   Merge(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   Resolved_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), 
   Closed_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),

   Dev_Estimate(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), QA_Estimate(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No,
         ToLoadColumn.Yes), Dev_Actual(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes), Note(String.class, "", IsEditableColumn.Yes,
         IsJiraColumn.No, ToLoadColumn.Yes), Release(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes),
   BoardStatus(BoardStatusValue.class, BoardStatusValue.UnKnown, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
      public Object parse(String cellContents) {
         return BoardStatusValue.get(cellContents);
      }
   },
   
   // Board Sync columns:
   B_BoardStatus(BoardStatusValue.class, BoardStatusValue.NA, IsEditableColumn.No, IsJiraColumn.No, ToLoadColumn.Yes) {
      public Object parse(String cellContents) {
         return BoardStatusValue.get(cellContents);
      }
   },
   B_Release(String.class, "", IsEditableColumn.No, IsJiraColumn.No, ToLoadColumn.Yes),

   // Jira Sync columns:
   J_Status(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_Resolution(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes,ToLoadColumn.Yes), 
   J_BuildNo(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_FixVersion(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes) {
      @Override
      public Object parse(String cellContents) {
         ArrayList<String> arrayList = new ArrayList<String>();
         if (cellContents == null ){
            return arrayList;
         }
         cellContents = cellContents.substring(1, cellContents.length()-1);
         String[] split = cellContents.split("(, )");
         for (String string : split) {
            arrayList.add(string);
         }
         return arrayList;
      }
   },
   J_Type(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_Dev_Estimate( String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_Dev_Spent(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes), 
   J_Sprint(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes),

   // Integer
   prio(Integer.class, null, IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes) {
      public Object parse(String cellContents) {
         if (cellContents == null || cellContents.trim().length() == 0)
            return -1;
         return Integer.parseInt(cellContents);
      }
   },

   // Boolean Defaults
   isParked(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
      public Object parse(String cellContents) {
         return Boolean.parseBoolean(cellContents);
      }
   }
//   ,
//   isInProgress(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
//      public Object parse(String cellContents) {
//         return Boolean.parseBoolean(cellContents);
//      }
//   },
//   isOpen(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
//      public Object parse(String cellContents) {
//         return Boolean.parseBoolean(cellContents);
//      }
//   },
//   isBug(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
//      public Object parse(String cellContents) {
//         return Boolean.parseBoolean(cellContents);
//      }
//   },
//   isResolved(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
//      public Object parse(String cellContents) {
//         return Boolean.parseBoolean(cellContents);
//      }
//   },
//   isComplete(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes) {
//      public Object parse(String cellContents) {
//         return Boolean.parseBoolean(cellContents);
//      }
//   }
   ;

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

   public Object parse(String cellContents) {
      return cellContents;
   }

}
