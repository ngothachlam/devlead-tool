package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;

public class FixVersionUserObject extends DefaultParentUserObject<FixVersionUserObject> {

   public FixVersionUserObject(String name) {
      super(name, "FixVersion");
   }

   @Override
   public int compareTo(FixVersionUserObject o) {
      int res = 0;
      res = o.getName().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(FixVersionUserObject.class, "FixVersion");
   }

}
