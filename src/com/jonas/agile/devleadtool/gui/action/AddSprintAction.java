package com.jonas.agile.devleadtool.gui.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import com.jonas.agile.devleadtool.sprint.SprintCreationTarget;

public class AddSprintAction extends BasicAbstractGUIAction {

   private static final Set<SprintCacheListener> listeners = new HashSet<SprintCacheListener>();
   private static final long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000L;

   static final long ONE_HOUR = 60 * 60 * 1000L;
   static final long ONE_WEEK = ONE_HOUR * 24 * 7;

   private SprintCreationSource source;
   private SprintCreationTarget target;

   AddSprintAction() {
      super("Add Sprint", "Adding sprint to cache!", null);
   }

   public AddSprintAction(Frame parentFrame, SprintCreationSource source, SprintCreationTarget target) {
      super("Add Sprint", "Adding sprint to cache!", parentFrame);
      this.source = source;
      this.target = target;
   }

   public void addListener(SprintCacheListener listener) {
      listeners.add(listener);
   }

   long daysBetween(Calendar d1, Calendar d2) {
      long endL = d2.getTimeInMillis() + d2.getTimeZone().getOffset(d2.getTimeInMillis());
      long startL = d1.getTimeInMillis() + d1.getTimeZone().getOffset(d1.getTimeInMillis());
      return (endL - startL) / MILLISECS_PER_DAY;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      notifyListeners(SprintCacheNotification.ADDINGTOCACHE);
      try {
         String name = source.getName();
         Date startDate = source.getStart();
         Date endDate = source.getEnd();
         Integer length = source.getLength();

         Sprint sprint = new Sprint(name, startDate, endDate, length);
         System.out.println("Adding: " + sprint);

         getWorkingDays(source);

         try {
            target.addSprint(sprint);
         } catch (IOException e1) {
            throw new RuntimeException(e1);
         }
      } finally {
         notifyListeners(SprintCacheNotification.ADDEDTOCACHE);
      }
   }

   private Calendar getCalendar(Date date) {
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

   int getWorkingDays(SprintCreationSource source) {
      Calendar startCalendar = getCalendar(source.getStart());
      Calendar endCalendar = getCalendar(source.getEnd());

      int fullWeeksBetween = getFullWorkingWeeks(startCalendar, endCalendar);

      System.out.println("full weeks between: " + fullWeeksBetween);

      int startDayOfWeek = getDayOfWeek(startCalendar);
      int endDayOfWeek = getDayOfWeek(endCalendar);

      int additional = 0;
      if (startCalendar.get(Calendar.WEEK_OF_YEAR) == endCalendar.get(Calendar.WEEK_OF_YEAR)) {
         additional = endAdditional(endDayOfWeek) - startAdditional(startDayOfWeek) + 1;
      } else {
         additional = endAdditional(endDayOfWeek) + startAdditional(startDayOfWeek);
      }

      return fullWeeksBetween * 2 + additional;
   }

   int endAdditional(int endDayOfWeek) {
      return (endDayOfWeek / 6 < 1) ? endDayOfWeek % 6 : 5;
   }

   int getDayOfWeek(Calendar startCalendar) {
      int i = startCalendar.get(Calendar.DAY_OF_WEEK) - 1;
      return i == 0 ? 7 : i;
   }

   public void notifyListeners(SprintCacheNotification notification) {
      for (SprintCacheListener listener : listeners) {
         listener.notify(notification);
      }
   }

   public void removeListener(SprintCacheListener listener) {
      listeners.remove(listener);
   }

   int startAdditional(int startDayOfWeek) {
      int i = 6 - startDayOfWeek;
      return i < 0 ? 0 : i;
   }

}


interface SprintCacheListener {

   public void notify(SprintCacheNotification notification);

}


enum SprintCacheNotification {
   ADDEDTOCACHE, ADDINGTOCACHE
}