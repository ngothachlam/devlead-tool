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
   AUTOMATIC_BUILD_BREAK("Automatic Build Break", "45"),
   EXTERNAL("External");

   private Set<IssueType> statuses = new HashSet<IssueType>();
   private final String toString;
   private final String id;

   private IssueType(String toString, String id) {
      this.toString = toString;
      this.id = id;
      statuses.add(this);
   }
   
   private IssueType(String toString) {
      this(toString, null);
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
   
   public String getId(){
      return id;
   }

}
