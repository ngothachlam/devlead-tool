package com.jonas.common;

import java.util.Calendar;
import junit.framework.TestCase;

public class DateHelperTest extends TestCase {

   DateHelper helper;

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

   public void testShouldTestGettinNiceDateAsStringOk() {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_MONTH, 21);
      calendar.set(Calendar.MONTH, 2);
      calendar.set(Calendar.YEAR, 2009);

      assertEquals("21/03-2009", helper.getDateAsString(calendar.getTime()));
   }

   private Calendar getCalendar(int dayOfMonth, int month, int year) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
      calendar.set(Calendar.MONTH, month - 1);
      calendar.set(Calendar.YEAR, year);
      return calendar;
   }

   @Override
   protected void setUp() throws Exception {
      helper = new DateHelper();
   }

}
