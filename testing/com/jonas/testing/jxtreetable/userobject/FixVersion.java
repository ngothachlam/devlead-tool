package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import com.jonas.testing.jxtreetable.Column;

public class FixVersion extends DefaultParentUserObject {

   public FixVersion(String name) {
      super(name);
   }

   @Override
   public int compareTo(Jira o) {
      int res = 0;
      res = o.getKey().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(FixVersion.class, "FixVersion");
   }

   @Override
   public void setValue(Column column, Object value) {
   }
}
