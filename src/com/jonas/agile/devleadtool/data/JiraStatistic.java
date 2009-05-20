package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;

public class JiraStatistic {

   private DevStatus devStatus;

   public JiraStatistic(BoardStatusValue object) {
      switch (object) {
         case UnKnown:
            devStatus = DevStatus.jiraIsInUnKnownState;
            break;
         case NA:
            devStatus = DevStatus.jiraIsInPreDevelopmentState;
            break;
         case Open:
            devStatus = DevStatus.jiraIsInDevelopmentOpenState;
            break;
         case Failed:
         case InProgress:
            devStatus = DevStatus.jiraIsInDevelopmentProgressState;
            break;
         case Resolved:
            devStatus = DevStatus.jiraIsInDevelopmentResolvedState;
            break;
         case Complete:
         case Approved:
         case ForShowCase:
            devStatus = DevStatus.jiraIsInPostDevelopmentState;
            break;
      }
   }

   public DevStatus devStatus() {
      return devStatus;
   }

}
