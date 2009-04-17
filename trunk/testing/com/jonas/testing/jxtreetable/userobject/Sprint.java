package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import com.jonas.testing.jxtreetable.Column;

public class Sprint extends DefaultParentUserObject {

   public Sprint(String name) {
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
      return new DataFlavor(Sprint.class, "Sprint");
   }

   @Override
   public void setValue(Column column, Object value) {
   }

   @Override
   public boolean isLeaf() {
      return false;
   }

}