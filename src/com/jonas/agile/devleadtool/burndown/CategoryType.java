package com.jonas.agile.devleadtool.burndown;


public class CategoryType {

   private final String name;
   private final BurnType burnType;

   public CategoryType(String name, BurnType burnType) {
      this.name = name;
      this.burnType = burnType;
   }

   public String getName() {
      return name;
   }

   public BurnType getType() {
      return burnType;
   }

}
