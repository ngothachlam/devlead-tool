package com.jonas.agile.devleadtool.gui.component.table.column;

import java.util.HashSet;
import java.util.Set;

import com.jonas.jira.JiraStatus;

public enum BoardStatusValue {
   UnKnown(0, JiraStatus.Open),
   NA(0, JiraStatus.Open),
   Open(1, JiraStatus.Open),
   Bug(2, JiraStatus.Open, JiraStatus.ReOpened),
//   Parked(1, JiraStatus.Open, JiraStatus.InProgress, JiraStatus.Resolved),
   InProgress(3, JiraStatus.Open, JiraStatus.InProgress, JiraStatus.Resolved),
   Resolved(4, JiraStatus.Resolved),
   Complete(5, JiraStatus.Closed),
   ForShowCase(6, JiraStatus.Closed),
   Approved(7, JiraStatus.Closed);

   private String toString;
   private String name;
   private Set<JiraStatus> statuses = new HashSet<JiraStatus>();

   private BoardStatusValue(Integer number, JiraStatus... jiraStatuses) {
      StringBuffer sb = new StringBuffer(number.toString());
      sb.append(". ").append(super.toString());
      toString = sb.toString();
      name = super.toString();
      for (JiraStatus jiraStatus : jiraStatuses) {
         statuses.add(jiraStatus);
      }
   }

   @Override
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

   public Set<JiraStatus> getJiraStatuses() {
      return statuses;
   }

}
