package com.jonas.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.TestCase;

public class DateHelperTest extends TestCase {

   DateHelper helper;

   public void testGetDate() {
      SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
      Calendar calendar = DateHelper.getDate(format, "Mon, 2 Mar 2009 18:28:25 +0000 (GMT)");
      System.out.println("Calendar: " + calendar.getTime());
   }

   public void testSameDay() {
      assertTrue(DateHelper.isSameDay(getCalendar(27, 4, 2009).getTime(), getCalendar(27, 4, 2009).getTime()));
      assertFalse(DateHelper.isSameDay(getCalendar(26, 4, 2009).getTime(), getCalendar(27, 4, 2009).getTime()));
      assertFalse(DateHelper.isSameDay(getCalendar(28, 4, 2009).getTime(), getCalendar(27, 4, 2009).getTime()));

      Calendar cal28th = getCalendar(28, 4, 2009, 1, 2, 4, 5);
      assertFalse("Should not have same day", DateHelper.isSameDay(cal28th.getTime(), getCalendar(27, 4, 2009).getTime()));
      assertFalse("Should not have same day", DateHelper.isSameDay(getCalendar(27, 4, 2009).getTime(), cal28th.getTime()));

      cal28th = getCalendar(28, 4, 2009, 0, 0, 0, 1);
      assertFalse(DateHelper.isSameDay(cal28th.getTime(), getCalendar(28, 4, 2009, 0, 0, 0, -1).getTime()));
      assertTrue(DateHelper.isSameDay(cal28th.getTime(), getCalendar(28, 4, 2009, 0, 0, 0, 0).getTime()));
      assertTrue(DateHelper.isSameDay(cal28th.getTime(), getCalendar(28, 4, 2009, 0, 0, 0, 1).getTime()));
      assertTrue(DateHelper.isSameDay(cal28th.getTime(), getCalendar(28, 4, 2009, 23, 59, 59, 999).getTime()));
      assertFalse(DateHelper.isSameDay(cal28th.getTime(), getCalendar(28, 4, 2009, 23, 59, 59, 1000).getTime()));
   }

   public void testStartAdditional() {
      assertEquals(5, DateHelper.startAdditional(1));
      assertEquals(4, DateHelper.startAdditional(2));
      assertEquals(3, DateHelper.startAdditional(3));
      assertEquals(2, DateHelper.startAdditional(4));
      assertEquals(1, DateHelper.startAdditional(5));
      assertEquals(0, DateHelper.startAdditional(6));
      assertEquals(0, DateHelper.startAdditional(7));
   }

   public void testEndAdditional() {
      assertEquals(1, DateHelper.endAdditional(1));
      assertEquals(2, DateHelper.endAdditional(2));
      assertEquals(3, DateHelper.endAdditional(3));
      assertEquals(4, DateHelper.endAdditional(4));
      assertEquals(5, DateHelper.endAdditional(5));
      assertEquals(5, DateHelper.endAdditional(6));
      assertEquals(5, DateHelper.endAdditional(7));
   }

   public void testFullWorkingWeeks() {
      Calendar startCalendar = getCalendar(5, 4, 2009); // sunday
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, startCalendar));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(6, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(7, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(8, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(9, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(10, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(11, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(12, 4, 2009)));
      assertEquals(1, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(13, 4, 2009)));

      startCalendar = getCalendar(6, 4, 2009); // monday
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, startCalendar));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(6, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(7, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(8, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(9, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(10, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(11, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(12, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(13, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(14, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(15, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(16, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(17, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(18, 4, 2009)));
      assertEquals(0, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(19, 4, 2009)));
      assertEquals(1, DateHelper.getFullWorkingWeeksBetween(startCalendar, getCalendar(20, 4, 2009)));

      startCalendar = getCalendar(4, 4, 2009); // saturday
   }

   public void testDayOfWeek() {
      assertEquals(4, DateHelper.getRealDayOfWeek(getCalendar(1, 1, 2009)));
      assertEquals(5, DateHelper.getRealDayOfWeek(getCalendar(2, 1, 2009)));
      assertEquals(6, DateHelper.getRealDayOfWeek(getCalendar(3, 1, 2009)));
      assertEquals(7, DateHelper.getRealDayOfWeek(getCalendar(4, 1, 2009)));
      assertEquals(1, DateHelper.getRealDayOfWeek(getCalendar(5, 1, 2009)));
      assertEquals(2, DateHelper.getRealDayOfWeek(getCalendar(6, 1, 2009)));
      assertEquals(3, DateHelper.getRealDayOfWeek(getCalendar(7, 1, 2009)));
   }

   public void testIsWorkingDay() {
      assertTrue(DateHelper.isWorkingDay(getCalendar(5, 1, 2009)));
      assertTrue(DateHelper.isWorkingDay(getCalendar(6, 1, 2009)));
      assertTrue(DateHelper.isWorkingDay(getCalendar(7, 1, 2009)));
      assertTrue(DateHelper.isWorkingDay(getCalendar(8, 1, 2009)));
      assertTrue(DateHelper.isWorkingDay(getCalendar(9, 1, 2009)));
      assertFalse(DateHelper.isWorkingDay(getCalendar(10, 1, 2009)));
      assertFalse(DateHelper.isWorkingDay(getCalendar(11, 1, 2009)));
   }

   private Calendar getCalendar(int dayOfMonth, int month, int year) {
      return getCalendar(dayOfMonth, month, year, 0, 0, 0, 0);
   }

   private Calendar getCalendar(int dayOfMonth, int month, int year, int hour, int minute, int second, int millisecond) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
      calendar.set(Calendar.MONTH, month - 1);
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.HOUR, hour);
      calendar.set(Calendar.MINUTE, minute);
      calendar.set(Calendar.SECOND, second);
      calendar.set(Calendar.MILLISECOND, millisecond);
      return calendar;
   }

   @Override
   protected void setUp() throws Exception {
      helper = new DateHelper();
   }

}
