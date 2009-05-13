package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;

public class JiraStatistic {

   private DevStatus devStatus;

   public JiraStatistic(BoardStatusValue object) {
      switch (object) {
         case UnKnown:
         case NA:
            devStatus = DevStatus.preDevelopment;
            break;
         case Open:
         case Failed:
         case InProgress:
         case Resolved:
            devStatus = DevStatus.inDevelopment;
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
