package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;

public class JiraStatistic {

   private DevStatus devStatus;

   public JiraStatistic(BoardStatusValue object) {
      switch (object) {
      case UnKnown:
      case NA:
         devStatus = DevStatus.preDevelopment;
         break;
      case Open:
      case Bug:
      case InProgress:
         devStatus = DevStatus.inDevelopment;
         break;
      case Resolved:
         devStatus = DevStatus.inTesting;
         break;
      case Complete:
      case Approved:
      case ForShowCase:
         devStatus = DevStatus.closed;
         break;
      }
   }

   public DevStatus devStatus() {
      return devStatus;
   }

}
