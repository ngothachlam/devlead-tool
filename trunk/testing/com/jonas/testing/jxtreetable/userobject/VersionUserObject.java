package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class VersionUserObject extends DefaultParentUserObject<VersionUserObject> {

   public VersionUserObject(String name, DefaultMutableTreeTableNode parent) {
      super(name, "Version", parent);
   }

   @Override
   public int compareTo(VersionUserObject o) {
      int res = 0;
      res = o.getName().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(VersionUserObject.class, "Version");
   }

}
