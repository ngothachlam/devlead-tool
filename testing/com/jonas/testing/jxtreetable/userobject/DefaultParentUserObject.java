package com.jonas.testing.jxtreetable.userobject;

import com.jonas.testing.jxtreetable.Column;

public abstract class DefaultParentUserObject extends DefaultUserObject {
   public final String getName() {
      return name;
   }

   public DefaultParentUserObject(String name) {
      super();
      this.name = name;
   }

   public final void setName(String name) {
      this.name = name;
   }

   private String name;

   @Override
   public final String getValueForColumn(Column column) {
      switch (column) {
      case JIRA:
         return name;
      }
      return "";
   }
}