/**
 * 
 */
package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.data.Cache;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;

public enum Column {
   
   // String Defaults
   Jira(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache) {

      @Override
      public String parseFromPersistanceStore(Object cellContents) {
         if (cellContents == null)
            return null;
         return cellContents.toString().toUpperCase();
      }
   },
   Description(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache, false),
   Planned_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache),
   Merge(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache),
   Resolved_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache),
   Closed_Sprint(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache),
   DEst(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true, CacheMode.noCache),
   QEst(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true, CacheMode.noCache),
   DRem(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true, CacheMode.noCache),
   QRem(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true, CacheMode.noCache),
   DAct(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, true, CacheMode.noCache),
   Note(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache),
   Release(String.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache),
   BoardStatus(BoardStatusValue.class, BoardStatusValue.UnKnown, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache) {
      @Override
      public BoardStatusValue parseFromPersistanceStore(Object cellContents) {
         return BoardStatusValue.get(cellContents.toString());
      }
   },

   // Complex Objects:
   Sprint(Sprint.class, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.usesCache) {
      @Override
      public String parseToPersistanceStore(Object value) {
         return value == null ? "" : value.toString();
      }

      @Override
      public Sprint parseFromPersistanceStore(Object cellContents, Cache cache) {
         if (cache instanceof SprintCache) {
            SprintCache sCache = (SprintCache) cache;
            return sCache.getSprintWithName(cellContents.toString());
         }
         throw new RuntimeException("Not sprintcache!");
      }
   },

   // Jira Sync columns:
   Status(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),
   Resolution(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),
   BuildNo(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),
   FixVersion(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),
   Type(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),
   J_DevEst(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, true, CacheMode.noCache),
   J_DevAct(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, true, CacheMode.noCache),
   J_Sprint(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),
   Project(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),
   Delivery(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.No, false, CacheMode.noCache),
   Owner(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),
   Environment(String.class, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, false, CacheMode.noCache),

   // Integer
   prio(Integer.class, null, IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, true, CacheMode.noCache) {
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
   isParked(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache) {
      @Override
      public Boolean parseFromPersistanceStore(Object cellContents) {
         if (cellContents == null)
            return Boolean.FALSE;
         String cellContentsString = cellContents.toString();
         return Boolean.parseBoolean(cellContentsString);
      }
   },
   Old(Boolean.class, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, false, CacheMode.noCache) {
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
   private final CacheMode cacheMode;
   private boolean isToAutoResize;

   // private static Map<String, Column> columns;

   private <T> Column(Class<T> defaultClass, Object defaultValue, IsEditableColumn isEditable, IsJiraColumn isJiraColumn, ToLoadColumn isToLoad,
         Boolean isNumeric, CacheMode cacheMode, boolean isToAutoResize) {
      this(defaultClass, defaultValue, isEditable, isJiraColumn, isToLoad, isNumeric, cacheMode);
      this.isToAutoResize = isToAutoResize;
   }
   private <T> Column(Class<T> defaultClass, Object defaultValue, IsEditableColumn isEditable, IsJiraColumn isJiraColumn, ToLoadColumn isToLoad,
         Boolean isNumeric, CacheMode cacheMode) {
      this.defaultClass = defaultClass;
      this.defaultValue = defaultValue;
      this.isEditable = isEditable;
      this.jiraColumn = isJiraColumn;
      this.isToLoad = isToLoad;
      this.isNumeric = isNumeric;
      this.cacheMode = cacheMode;
      this.isToAutoResize = true;
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

   public Object parseFromPersistanceStore(Object cellContents, Cache cache) {
      return null;
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


   public boolean isUsingCache() {
      return cacheMode.equals(CacheMode.usesCache);
   }

   public boolean isToAutoResize() {
      return isToAutoResize;
   }

}
