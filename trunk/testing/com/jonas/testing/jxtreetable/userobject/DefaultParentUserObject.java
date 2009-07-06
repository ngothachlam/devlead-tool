package com.jonas.testing.jxtreetable.userobject;

import com.jonas.testing.jxtreetable.column.Column;

public abstract class DefaultParentUserObject<T> extends DefaultUserObject<T> {
   private String uiName;

   public final String getName() {
      return name;
   }

   public DefaultParentUserObject(String name, String typeOfParent) {
      super();
      this.name = name;
      this.uiName = typeOfParent + ": " + name;
   }

   public final void setName(String name) {
      this.name = name;
   }

   private String name;

   @Override
   public final String getValueForColumn(Column column) {
      switch (column) {
      case REF:
         return uiName;
      }
      return "";
   }

   @Override
   public final void setValue(Column column, Object value) {
   }

   @Override
   public final boolean isLeaf() {
      return false;
   }

   @Override
   public final boolean isEditable(Column column) {
      return false;
   }
}