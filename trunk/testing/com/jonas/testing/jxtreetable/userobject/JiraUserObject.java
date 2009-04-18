package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import com.jonas.testing.jxtreetable.column.Column;

public class JiraUserObject extends DefaultUserObject {

   private String description;
   private String key;
   private String fixVersion;
   private String sprint;

   public JiraUserObject() {
   }
   
   public JiraUserObject(String key) {
      this.key = key;
   }

   public JiraUserObject(String key, String description) {
      super();
      this.description = description;
      this.key = key;
   }

   @Override
   public int compareTo(JiraUserObject o) {
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
      return new DataFlavor(JiraUserObject.class, "Jira");
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
         return description;
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
         setDescription(value.toString());
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
         return false;
      }
      return false;
   }
}
