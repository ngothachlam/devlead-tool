package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import com.jonas.testing.jxtreetable.column.Column;

public class TaskUserObject extends DefaultUserObject<TaskUserObject> {

   private String description;
   private String key;
   private String fixVersion;
   private String sprint;

   public TaskUserObject(String key, DefaultParentUserObject<StoryUserObject> parent) {
      super(parent);
      this.key = key;
   }

   public TaskUserObject(String key, String description, StoryUserObject parent) {
      this(key, parent);
      this.description = description;
   }

   @Override
   public int compareTo(TaskUserObject o) {
      int res = 0;
      res = o.getKey().compareTo(getKey());
      if (0 == res) {
         res = o.getDescription().compareTo(getDescription());
      }
      return res;
   }

   public String getDescription() {
      return description;
   }

   public String getKey() {
      return key;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setKey(String key) {
      this.key = key;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(TaskUserObject.class, "Task");
   }

   public String getFixVersion() {
      return fixVersion;
   }

   public String getSprint() {
      return sprint;
   }

   @Override
   public String getValueForColumn(Column column) {
      switch (column) {
      case REF:
         return key;
      case DESCRIPTION:
         return " +" + description;
      }
      return "";
   }

   @Override
   public void setValue(Column column, Object value) {
      switch (column) {
      case REF:
         setKey(value.toString());
         break;
      case DESCRIPTION:
         setDescription(value.toString().substring(2));
         break;
      }
   }

   @Override
   public boolean isLeaf() {
      return true;
   }

   @Override
   public boolean isEditable(Column column) {
      switch (column) {
      case REF:
         return true;
      case DESCRIPTION:
         return true;
      }
      return false;
   }
}
