package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;

public class JiraStatistic {

   private DevStatus devStatus;

   public JiraStatistic(BoardStatusValue boardStatus) {
      switch (boardStatus) {
         case UnKnown:
            devStatus = DevStatus.jiraIsInUnKnownState;
            break;
         case NA:
            devStatus = DevStatus.jiraIsInPreDevelopment;
            break;
         case Open:
            devStatus = DevStatus.jiraIsInDevelopmentAndOpen;
            break;
         case Failed:
         case InProgress:
            devStatus = DevStatus.jiraIsInDevelopmentAndInProgress;
            break;
         case Resolved:
            devStatus = DevStatus.jiraIsInDevelopmentAndResolved;
            break;
         case Complete:
         case Approved:
         case ForShowCase:
            devStatus = DevStatus.jiraIsInPostDevelopment;
            break;
      }
   }

   public DevStatus devStatus() {
      return devStatus;
   }

}
