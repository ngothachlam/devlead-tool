package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import com.jonas.testing.jxtreetable.column.Column;

public class StoryUserObject extends DefaultParentUserObject<StoryUserObject> {

   private String description;

//   @Override
//   public boolean isLeaf() {
//      return !hasChildren();
//   }

   public StoryUserObject(String name, String description, SprintUserObject parent) {
      super(name, "Story", parent);
      this.description = description;
   }

   @Override
   public int compareTo(StoryUserObject o) {
      int res = 0;
      res = o.getName().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(StoryUserObject.class, "Story");
   }

   public String getValueForColumn(Column column) {
      switch (column) {
      case REF:
         return getUiName();
      case DESCRIPTION:
         return description;
      }
      return "";
   }

}