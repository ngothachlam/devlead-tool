package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;

public class SprintUserObject extends DefaultParentUserObject {

   public SprintUserObject() {
   }
   
   public SprintUserObject(String name) {
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
      return new DataFlavor(SprintUserObject.class, "Sprint");
   }
}