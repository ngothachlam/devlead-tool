package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;

public class FixVersionUserObject extends DefaultParentUserObject {

   public FixVersionUserObject() {
   }
   
   public FixVersionUserObject(String name) {
      super(name);
   }

   @Override
   public int compareTo(JiraUserObject o) {
      int res = 0;
      res = o.getKey().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(FixVersionUserObject.class, "FixVersion");
   }
}
