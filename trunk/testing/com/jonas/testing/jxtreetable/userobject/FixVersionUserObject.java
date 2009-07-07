package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class FixVersionUserObject extends DefaultParentUserObject<FixVersionUserObject> {

   public FixVersionUserObject(String name, DefaultMutableTreeTableNode parent) {
      super(name, "Version", parent);
   }

   @Override
   public int compareTo(FixVersionUserObject o) {
      int res = 0;
      res = o.getName().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(FixVersionUserObject.class, "Version");
   }

}
