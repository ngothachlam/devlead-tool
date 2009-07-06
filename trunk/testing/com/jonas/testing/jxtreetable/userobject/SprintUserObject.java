package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;

public class SprintUserObject extends DefaultParentUserObject<SprintUserObject> {

   public SprintUserObject(String name) {
      super(name, "Sprint");
   }

   @Override
   public int compareTo(SprintUserObject o) {
      int res = 0;
      res = o.getName().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(SprintUserObject.class, "Sprint");
   }
}