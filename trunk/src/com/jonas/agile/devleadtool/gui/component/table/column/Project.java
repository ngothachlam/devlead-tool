package com.jonas.agile.devleadtool.gui.component.table.column;

import java.util.HashSet;
import java.util.Set;

public enum Project {

   TBD("<TBD>"),
   PRODBUG("Bug in Prod"),
   DEVBUG("Bug in Dev"),
   SMALLBUSCHANGE("Small Business Change");

   private Set<Project> statuses = new HashSet<Project>();
   private final String toString;

   private Project(String toString) {
      this.toString = toString;
      statuses.add(this);
   }

   public String toString() {
      return toString;
   }

   public static Project get(String toString) {
      for (Project issueType : Project.values()) {
         if (issueType.toString().equals(toString))
            return issueType;
      }
      return TBD;
   }

}
