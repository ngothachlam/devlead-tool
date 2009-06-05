package com.jonas.agile.devleadtool.gui.component.table.column;

import java.util.HashSet;
import java.util.Set;

public enum IssueType {

   TBD("<TBD>"),
   BUG("Bug"),
   STORY("Story"),
   DEV("Dev"),
   DATAFIX("Datafix"),
   TEST("Test"),
   MERGE("Merge"),
   PRODISSUE("Issue"),
   EXTERNAL("External");

   private Set<IssueType> statuses = new HashSet<IssueType>();
   private final String toString;

   private IssueType(String toString) {
      this.toString = toString;
      statuses.add(this);
   }

   public String toString() {
      return toString;
   }

   public static IssueType get(String toString) {
      for (IssueType issueType : IssueType.values()) {
         if (issueType.toString().equals(toString))
            return issueType;
      }
      return TBD;
   }

}
