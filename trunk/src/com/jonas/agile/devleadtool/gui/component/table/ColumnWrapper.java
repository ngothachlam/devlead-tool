package com.jonas.agile.devleadtool.gui.component.table;

import java.util.HashMap;
import java.util.Map;

import com.jonas.agile.devleadtool.data.Cache;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;

public class ColumnWrapper<T> {

   private static Map<ColumnType, ColumnWrapper> mapOfTypes = new HashMap<ColumnType, ColumnWrapper>();
   private static Map<String, ColumnWrapper> mapOfNames = new HashMap<String, ColumnWrapper>();

   public static final ColumnWrapper<String> JIRA = new ColumnWrapper<String>(String.class, ColumnType.Jira, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache) {
      @Override
      public String parseFromPersistanceStore(Object cellContents) {
         if (cellContents == null)
            return null;
         return cellContents.toString().toUpperCase();
      }
   };

   public static final ColumnWrapper<String> DESCRIPTION = new ColumnWrapper<String>(String.class, ColumnType.Description, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache).autoResize();
   public static final ColumnWrapper<String> MERGE = new ColumnWrapper<String>(String.class, ColumnType.Merge, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Resolved_Sprint = new ColumnWrapper<String>(String.class, ColumnType.Resolved_Sprint, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Closed_Sprint = new ColumnWrapper<String>(String.class, ColumnType.Closed_Sprint, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> DEst = new ColumnWrapper<String>(String.class, ColumnType.DEst, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.Yes, CacheMode.noCache);
   public static final ColumnWrapper<String> QEst = new ColumnWrapper<String>(String.class, ColumnType.QEst, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.Yes, CacheMode.noCache);
   public static final ColumnWrapper<String> DRem = new ColumnWrapper<String>(String.class, ColumnType.DRem, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.Yes, CacheMode.noCache);
   public static final ColumnWrapper<String> QRem = new ColumnWrapper<String>(String.class, ColumnType.QRem, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.Yes, CacheMode.noCache);
   public static final ColumnWrapper<String> DAct = new ColumnWrapper<String>(String.class, ColumnType.DAct, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.Yes, CacheMode.noCache);
   public static final ColumnWrapper<String> Note = new ColumnWrapper<String>(String.class, ColumnType.Note, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Release = new ColumnWrapper<String>(String.class, ColumnType.Release, "", IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);

   // Jira Sync columns:
   public static final ColumnWrapper<String> Status = new ColumnWrapper<String>(String.class, ColumnType.Status, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Resolution = new ColumnWrapper<String>(String.class, ColumnType.Resolution, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> BuildNo = new ColumnWrapper<String>(String.class, ColumnType.BuildNo, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> FixVersion = new ColumnWrapper<String>(String.class, ColumnType.FixVersion, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Type = new ColumnWrapper<String>(String.class, ColumnType.Type, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> J_DevEst = new ColumnWrapper<String>(String.class, ColumnType.J_DevEst, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.Yes, CacheMode.noCache);
   public static final ColumnWrapper<String> J_DevAct = new ColumnWrapper<String>(String.class, ColumnType.J_DevAct, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.Yes, CacheMode.noCache);
   public static final ColumnWrapper<String> J_Sprint = new ColumnWrapper<String>(String.class, ColumnType.J_Sprint, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Project = new ColumnWrapper<String>(String.class, ColumnType.Project, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Delivery = new ColumnWrapper<String>(String.class, ColumnType.Delivery, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.No, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Owner = new ColumnWrapper<String>(String.class, ColumnType.Owner, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);
   public static final ColumnWrapper<String> Environment = new ColumnWrapper<String>(String.class, ColumnType.Environment, "", IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache);

   // Complex columns
   public static final ColumnWrapper<BoardStatusValue> BoardStatus = new ColumnWrapper<BoardStatusValue>(BoardStatusValue.class, ColumnType.BoardStatus, BoardStatusValue.UnKnown, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes,
         IsNumberic.No, CacheMode.noCache) {
      @Override
      public BoardStatusValue parseFromPersistanceStore(Object cellContents) {
         return BoardStatusValue.get(cellContents.toString());
      }
   };
   public static final ColumnWrapper<Sprint> Sprint = new ColumnWrapper<Sprint>(Sprint.class, ColumnType.Sprint, SprintCache.EMPTYSPRINT, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.usesCache) {
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
   };
   // Integer
   public static final ColumnWrapper<Integer> prio = new ColumnWrapper<Integer>(Integer.class, ColumnType.prio, null, IsEditableColumn.No, IsJiraColumn.Yes, ToLoadColumn.Yes, IsNumberic.Yes, CacheMode.noCache) {
      @Override
      public Integer parseFromPersistanceStore(Object cellContents) {
         String cellContentsString = cellContents.toString();
         if (cellContentsString == null || cellContentsString.trim().length() == 0)
            return -1;
         // we do parse a double initially as Cell's in spreadsheet (xls) will
         // become 10.0 - not 10, which will otherwise result in numberformat
         // exception.
         return (int) Double.parseDouble(cellContentsString);
      }
   };

   // Boolean Defaults
   public static final ColumnWrapper<Boolean> isParked = new ColumnWrapper<Boolean>(Boolean.class, ColumnType.isParked, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache) {
      @Override
      public Boolean parseFromPersistanceStore(Object cellContents) {
         if (cellContents == null)
            return Boolean.FALSE;
         String cellContentsString = cellContents.toString();
         return Boolean.parseBoolean(cellContentsString);
      }
   };
   public static final ColumnWrapper<Boolean> Old = new ColumnWrapper<Boolean>(Boolean.class, ColumnType.Old, Boolean.FALSE, IsEditableColumn.Yes, IsJiraColumn.No, ToLoadColumn.Yes, IsNumberic.No, CacheMode.noCache) {
      @Override
      public Boolean parseFromPersistanceStore(Object cellContents) {
         if (cellContents == null)
            return Boolean.FALSE;
         String cellContentsString = cellContents.toString();
         return Boolean.parseBoolean(cellContentsString);
      }
   };

   private final Object defaultValue;
   private final IsJiraColumn jiraColumn;
   private final IsEditableColumn isEditable;
   private final ToLoadColumn isToLoad;
   private final IsNumberic isNumeric;
   private final CacheMode cacheMode;
   private boolean isToAutoResize;
   private final ColumnType type;
   private Class<T> defaultClass;

   // private static Map<String, Column> columns;

   private ColumnWrapper autoResize() {
      isToAutoResize = true;
      return this;
   }

   private ColumnWrapper(Class<T> defaultClass, ColumnType type, T defaultValue, IsEditableColumn isEditable, IsJiraColumn isJiraColumn, ToLoadColumn isToLoad, IsNumberic isNumeric, CacheMode cacheMode) {
      this.defaultClass = defaultClass;
      this.type = type;
      this.defaultValue = defaultValue;
      this.isEditable = isEditable;
      this.jiraColumn = isJiraColumn;
      this.isToLoad = isToLoad;
      this.isNumeric = isNumeric;
      this.cacheMode = cacheMode;
      this.isToAutoResize = true;

      mapOfTypes.put(type, this);
      mapOfNames.put(type.toString(), this);
   }

   public static ColumnType getEnum(Object columnName) {
      for (ColumnType col : ColumnType.values()) {
         if (col.toString().equals(columnName)) {
            return col;
         }
      }
      return null;
   }

   public Object getDefaultValue() {
      return defaultValue;
   }

   public Class<T> getDefaultClass() {
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
    * The idea is that anything that can be interpreted to a double should be
    * converted to a double when saving the data So that SUM(A:A) can be done
    * for instance. Loading it back again needs to display it as a string.
    * 
    * @param value
    * @return
    */
   public Object parseToPersistanceStore(Object value) {
      if (value == null)
         return "";

      if (isNumeric == IsNumberic.Yes && value != null && value instanceof String) {
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

   public static ColumnWrapper get(ColumnType column) {
      return mapOfTypes.get(column);
   }

   public static ColumnWrapper get(String columnName) {
      return mapOfNames.get(columnName);
   }

   public ColumnType getType() {
      return type;
   }

}
