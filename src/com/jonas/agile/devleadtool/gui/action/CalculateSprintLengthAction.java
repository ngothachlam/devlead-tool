package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.sprint.SprintLengthSource;
import com.jonas.agile.devleadtool.sprint.SprintLengthTarget;
import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class CalculateSprintLengthAction extends BasicAbstractGUIAction {

   private SprintLengthSource lengthSource;
   private SprintLengthTarget lengthTarget;
   private static final Logger log = MyLogger.getLogger(CalculateSprintLengthAction.class);

   public CalculateSprintLengthAction(Frame parentFrame) {
      super("Calculate Length", "Calculate Sprint Length based on start and end", parentFrame);
   }
   @Override
   public void doActionPerformed(ActionEvent e) {
      int days = getWorkingDaysBetweenStartAndEnd(lengthSource);
      lengthTarget.setWorkingDays(days);
   }

   public void setLengthSource(SprintLengthSource lengthSource) {
      this.lengthSource = lengthSource;
   }

   public void setLengthTarget(SprintLengthTarget lengthTarget) {
      this.lengthTarget = lengthTarget;
   }

   int endAdditional(int endDayOfWeek) {
      return (endDayOfWeek / 6 < 1) ? endDayOfWeek % 6 : 5;
   }

   Calendar getCalendar(Date date) {
      Calendar startCalendar = Calendar.getInstance();
      startCalendar.setTime(date);
      return startCalendar;
   }

   int getFullWorkingWeeks(Calendar startCalendar, Calendar endCalendar) {
      int firstWeek = startCalendar.get(Calendar.WEEK_OF_YEAR);
      int endWeek = endCalendar.get(Calendar.WEEK_OF_YEAR);

      int fullWeeksBetween = endWeek - firstWeek - 1;
      fullWeeksBetween = fullWeeksBetween < 0 ? 0 : fullWeeksBetween;
      return fullWeeksBetween;
   }

   int getWorkingDaysBetweenStartAndEnd(SprintLengthSource source) {
      Date startDate = source.getStart();
      Date endDate = source.getEnd();
      
      if(startDate == null || endDate == null){
         log.warn("startDate or endDate are null!");
         return -1;
      }
      
      Calendar startCalendar = getCalendar(startDate);
      Calendar endCalendar = getCalendar(endDate);

      int fullWeeksBetween = getFullWorkingWeeks(startCalendar, endCalendar);

      System.out.println("full weeks between: " + fullWeeksBetween);

      int startDayOfWeek = DateHelper.getRealDayOfWeek(startCalendar);
      int endDayOfWeek = DateHelper.getRealDayOfWeek(endCalendar);

      int additional = 0;
      int endAdditional = endAdditional(endDayOfWeek);
      if (startCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar.get(Calendar.WEEK_OF_YEAR)) {
         additional = endAdditional - endAdditional(startDayOfWeek) + (DateHelper.isWorkingDay(startCalendar) ? 1 : 0);
         System.out.println("1: " + additional + " =  " + endAdditional + " - " + endAdditional(startDayOfWeek) + " + 1");
      } else {
         additional = endAdditional + startAdditional(startDayOfWeek);
         System.out.println("2: " + additional);
      }

      return fullWeeksBetween * 5 + additional;
   }

   int startAdditional(int startDayOfWeek) {
      int i = 6 - startDayOfWeek;
      return i < 0 ? 0 : i;
   }

}
