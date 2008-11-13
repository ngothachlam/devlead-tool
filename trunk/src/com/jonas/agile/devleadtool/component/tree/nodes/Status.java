package com.jonas.agile.devleadtool.component.tree.nodes;

public enum Status {
   UnKnown(100), Closed(40), Resolved(30), Reopened(20), InProgress(10), Open(0);

   private int value;

   private Status(int value) {
      this.value = value;
   }

   public boolean isLowerThan(Status comparing) {
      if (getValue() < comparing.getValue()) {
         return true;
      }
      return false;
   }

   private int getValue() {
      return value;
   }

   public static Status get(String name) {
      for (Status resolution : values()) {
         if (resolution.toString().equalsIgnoreCase(name)) {
            return resolution;
         }
      }
      return UnKnown;
   }
}
