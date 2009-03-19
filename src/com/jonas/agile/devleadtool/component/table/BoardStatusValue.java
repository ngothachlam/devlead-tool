package com.jonas.agile.devleadtool.component.table;

import java.util.HashSet;
import java.util.Set;

public enum BoardStatusValue {
   UnKnown(0, "Open"), NA(0), Open(1, "Open"), Bug(1, "Open", "Reopened"), Parked(1), InDevProgress(2), Resolved(3), InQAProgress(4), Complete(5), ForShowCase(6), Approved(7);

   private String toString;
   private String name;
   private Set<String> statuses = new HashSet<String>();

   private BoardStatusValue(int number, String... jiraStatuses) {
      StringBuffer sb = new StringBuffer();
      sb.append(number).append(". ").append(super.toString());
      toString = sb.toString();
      name = super.toString();
      for (String jiraStatus : jiraStatuses) {
         statuses.add(jiraStatus);
      }
   }

   public String toString() {
      return toString;
   }
   public String getName() {
      return name;
   }

   public static BoardStatusValue get(String cellContents) {
      for (BoardStatusValue boardStatusValue : values()) {
         if (boardStatusValue.toString.equalsIgnoreCase(cellContents))
            return boardStatusValue;
      }
      for (BoardStatusValue boardStatusValue : values()) {
         if (boardStatusValue.name.equalsIgnoreCase(cellContents))
            return boardStatusValue;
      }
      return null;
   }

   public Set<String> getJiraStatuses() {
      return statuses;
   }

}
