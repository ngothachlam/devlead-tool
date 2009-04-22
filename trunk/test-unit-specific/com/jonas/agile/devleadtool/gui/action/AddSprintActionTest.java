package com.jonas.agile.devleadtool.gui.action;

import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;

public class AddSprintActionTest extends TestCase {

   AddSprintAction action = new AddSprintAction();

   public void testStartAdditional() {
      assertEquals(5, action.startAdditional(1));
      assertEquals(4, action.startAdditional(2));
      assertEquals(3, action.startAdditional(3));
      assertEquals(2, action.startAdditional(4));
      assertEquals(1, action.startAdditional(5));
      assertEquals(0, action.startAdditional(6));
      assertEquals(0, action.startAdditional(7));
   }

   public void testEndAdditional() {
      assertEquals(1, action.endAdditional(1));
      assertEquals(2, action.endAdditional(2));
      assertEquals(3, action.endAdditional(3));
      assertEquals(4, action.endAdditional(4));
      assertEquals(5, action.endAdditional(5));
      assertEquals(5, action.endAdditional(6));
      assertEquals(5, action.endAdditional(7));
   }

   public void testDayOfWeek() {
      assertEquals(4, action.getDayOfWeek(getCalendar(1, 1, 2009)));
      assertEquals(5, action.getDayOfWeek(getCalendar(2, 1, 2009)));
      assertEquals(6, action.getDayOfWeek(getCalendar(3, 1, 2009)));
      assertEquals(7, action.getDayOfWeek(getCalendar(4, 1, 2009)));
      assertEquals(1, action.getDayOfWeek(getCalendar(5, 1, 2009)));
      assertEquals(2, action.getDayOfWeek(getCalendar(6, 1, 2009)));
      assertEquals(3, action.getDayOfWeek(getCalendar(7, 1, 2009)));
   }

   public void testDaysBetween() {
      assertEquals(0L, action.daysBetween(getCalendar(1, 1, 2009), getCalendar(1, 1, 2009)));
      assertEquals(1L, action.daysBetween(getCalendar(1, 1, 2009), getCalendar(2, 1, 2009)));
      assertEquals(2L, action.daysBetween(getCalendar(1, 1, 2009), getCalendar(3, 1, 2009)));
      assertEquals(-1L, action.daysBetween(getCalendar(2, 1, 2009), getCalendar(1, 1, 2009)));
   }

   public void testFullWorkingWeeks() {
      Calendar startCalendar = getCalendar(5, 4, 2009); // sunday
      System.out.println("Sunday: " + startCalendar.get(Calendar.DAY_OF_WEEK)); // 1
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, startCalendar));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(6, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(7, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(8, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(9, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(10, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(11, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(12, 4, 2009)));
      assertEquals(1, action.getFullWorkingWeeks(startCalendar, getCalendar(13, 4, 2009)));

      startCalendar = getCalendar(6, 4, 2009); // monday
      System.out.println("Monday: " + startCalendar.get(Calendar.DAY_OF_WEEK));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, startCalendar));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(6, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(7, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(8, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(9, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(10, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(11, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(12, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(13, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(14, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(15, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(16, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(17, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(18, 4, 2009)));
      assertEquals(0, action.getFullWorkingWeeks(startCalendar, getCalendar(19, 4, 2009)));
      assertEquals(1, action.getFullWorkingWeeks(startCalendar, getCalendar(20, 4, 2009)));

      startCalendar = getCalendar(4, 4, 2009); // saturday
      System.out.println("Sat: " + startCalendar.get(Calendar.DAY_OF_WEEK)); // 1
   }

   public void testWorkingDays() {
      assertEquals(0, action.getWorkingDays(setUpResponse(23, 4, 2009, 23, 4, 2009)));
      assertEquals(1, action.getWorkingDays(setUpResponse(26, 4, 2009, 27, 4, 2009)));
      assertEquals(1, action.getWorkingDays(setUpResponse(23, 4, 2009, 24, 4, 2009)));
      assertEquals(1, action.getWorkingDays(setUpResponse(23, 4, 2009, 25, 4, 2009)));
      assertEquals(1, action.getWorkingDays(setUpResponse(23, 4, 2009, 26, 4, 2009)));
      assertEquals(2, action.getWorkingDays(setUpResponse(23, 4, 2009, 27, 4, 2009)));
      assertEquals(3, action.getWorkingDays(setUpResponse(23, 4, 2009, 28, 4, 2009)));
   }

   private SprintCreationSource setUpResponse(final int startDay, final int startMonth, final int startYear, final int endDay,
         final int endMonth, final int endYear) {
      return new SprintCreationSource() {

         @Override
         public void clear() {
         }

         @Override
         public Date getEnd() {
            return getDate(endDay, endMonth, endYear);
         }

         @Override
         public Integer getLength() {
            return 0;
         }

         @Override
         public String getName() {
            return null;
         }

         @Override
         public Date getStart() {
            return getDate(startDay, startMonth, startYear);
         }

      };
   }

   private Date getDate(int dayOfMonth, int month, int year) {
      Calendar calendar = getCalendar(dayOfMonth, month, year);
      return calendar.getTime();
   }

   private Calendar getCalendar(int dayOfMonth, int month, int year) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
      calendar.set(Calendar.MONTH, month - 1);
      calendar.set(Calendar.YEAR, year);
      return calendar;
   }
}
