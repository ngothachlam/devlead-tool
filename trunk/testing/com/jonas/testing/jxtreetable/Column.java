package com.jonas.testing.jxtreetable;

public enum Column {
   REF("Ref"), DESCRIPTION("Description");

   @Override
   public String toString() {
      return name;
   }

   private final String name;

   Column(String name) {
      this.name = name;
   }

}