package com.jonas.agile.devleadtool.gui.component.table.column;

import java.util.HashSet;
import java.util.Set;

public enum Environment {

   TBD("<TBD>"),
   PROD("Production"),
   DEV("Development"),
   QA("QA"),
   NA("NA");

   private Set<Environment> statuses = new HashSet<Environment>();
   private final String toString;

   private Environment(String toString) {
      this.toString = toString;
      statuses.add(this);
   }

   public String toString() {
      return toString;
   }

   public static Environment get(String toString) {
      for (Environment issueType : Environment.values()) {
         if (issueType.toString().equals(toString))
            return issueType;
      }
      return TBD;
   }

}
