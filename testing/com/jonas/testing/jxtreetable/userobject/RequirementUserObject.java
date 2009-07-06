package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;

public class RequirementUserObject extends DefaultParentUserObject<RequirementUserObject> {

   public RequirementUserObject(String name) {
      super(name, "Req");
   }

   @Override
   public int compareTo(RequirementUserObject o) {
      int res = 0;
      res = o.getName().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(RequirementUserObject.class, "Requirement");
   }
}