package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import com.jonas.testing.jxtreetable.Column;

public class Jira extends DefaultUserObject {

   private String description;
   private String key;
   private String fixVersion;
   private String sprint;

   public Jira(String key) {
      this.key = key;
   }

   public Jira(String key, String description) {
      super();
      this.description = description;
      this.key = key;
   }

   @Override
   public int compareTo(Jira o) {
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
      return new DataFlavor(Jira.class, "Jira");
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
      case JIRA:
         return key;
      case DESCRIPTION:
         return description;
      }
      return "";
   }

   @Override
   public void setValue(Column column, Object value) {
      switch (column) {
      case JIRA:
         setKey(value.toString());
         break;
      case DESCRIPTION:
         setDescription(value.toString());
         break;
      }

   }
}
