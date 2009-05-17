package com.jonas.agile.devleadtool.burndown;


public class Category {

   private final String name;
   private final BurnType burnType;

   public Category(String name, BurnType burnType) {
      this.name = name;
      this.burnType = burnType;
   }

   public BurnType getBurnType() {
      return burnType;
   }

   public String getName() {
      return name;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((burnType == null) ? 0 : burnType.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      final Category other = (Category) obj;
      if (burnType == null) {
         if (other.burnType != null)
            return false;
      } else if (!burnType.equals(other.burnType))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

}
