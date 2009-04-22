package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import com.jonas.agile.devleadtool.sprint.SprintCreationTarget;

public class AddSprintAction extends BasicAbstractGUIAction {

   private SprintCreationTarget target;
   private SprintCreationSource source;

   AddSprintAction() {
      super("Add Sprint", "Adding a sprint to cache!", null);
   }

   public AddSprintAction(Frame parentFrame, SprintCreationSource source, SprintCreationTarget target) {
      super("Add Sprint", "Adding a sprint to cache!", parentFrame);
      this.source = source;
      this.target = target;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {

      String name = source.getName();
      Date startDate = source.getStart();
      Date endDate = source.getEnd();
      int length = source.getLength();

      Sprint sprint = new Sprint(name, startDate, endDate, length);
      System.out.println("Adding: " + sprint);

      getWorkingDays(source);

      try {
         target.addSprint(sprint);
      } catch (IOException e1) {
         throw new RuntimeException(e1);
      }
   }

   int getWorkingDays(SprintCreationSource source) {
      Calendar startCalendar = getCalendar(source.getStart());
      Calendar endCalendar = getCalendar(source.getEnd());

      int fullWeeksBetween = getFullWorkingWeeks(startCalendar, endCalendar);

      System.out.println("full weeks between: " + fullWeeksBetween);
      
      return 0;
   }

   int getFullWorkingWeeks(Calendar startCalendar, Calendar endCalendar) {
      int firstWeek = startCalendar.get(Calendar.WEEK_OF_YEAR);
      int endWeek = endCalendar.get(Calendar.WEEK_OF_YEAR);

      int fullWeeksBetween = endWeek - firstWeek - 1;
      fullWeeksBetween = fullWeeksBetween < 0 ? 0 : fullWeeksBetween;
      return fullWeeksBetween;
   }

   private Calendar getCalendar(Date date) {
      Calendar startCalendar = Calendar.getInstance();
      startCalendar.setTime(date);
      return startCalendar;
   }

   static final long ONE_HOUR = 60 * 60 * 1000L;
   static final long ONE_WEEK = ONE_HOUR * 24 * 7;
   private static final long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000L;

   long daysBetween(Calendar d1, Calendar d2) {
      long endL = d2.getTimeInMillis() + d2.getTimeZone().getOffset(d2.getTimeInMillis());
      long startL = d1.getTimeInMillis() + d1.getTimeZone().getOffset(d1.getTimeInMillis());
      return (endL - startL) / MILLISECS_PER_DAY;
   }

}
