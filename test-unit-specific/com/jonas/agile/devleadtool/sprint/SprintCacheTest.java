package com.jonas.agile.devleadtool.sprint;

import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;

public class SprintCacheTest extends TestCase {

   @Override
   protected void setUp() throws Exception {
      sprintCache = new SprintCache();
   }

   private SprintCache sprintCache;

   public void testShouldCalculateSprintTimesOk() {
      assertEquals(SprintTime.currentSprint, sprintCache.getSprintTime(getSprint(-1, +1)));
      assertEquals(SprintTime.currentSprint, sprintCache.getSprintTime(getSprint(0, 1)));
      assertEquals(SprintTime.currentSprint, sprintCache.getSprintTime(getSprint(-1, 0)));
      assertEquals(SprintTime.afterCurrentSprint, sprintCache.getSprintTime(getSprint(2, 2)));
      assertEquals(SprintTime.afterCurrentSprint, sprintCache.getSprintTime(getSprint(2, 3)));
      assertEquals(SprintTime.beforeCurrentSprint, sprintCache.getSprintTime(getSprint(-2, -2)));
      assertEquals(SprintTime.beforeCurrentSprint, sprintCache.getSprintTime(getSprint(-2, -1)));
   }

   private Sprint getSprint(int startOffsetFromToday, int endOffsetFromToday) {
      Sprint sprint = new Sprint();
      sprint.setStartDate(getDateAgainstToday(startOffsetFromToday));
      sprint.setEndDate(getDateAgainstToday(endOffsetFromToday));
      return sprint;
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

   private Date getDateAgainstToday(int offset) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + offset);
      Calendar calendar2 = Calendar.getInstance();
      assertEquals(calendar.getTime(), getDate(calendar2.get(Calendar.DAY_OF_MONTH) + offset, calendar2.get(Calendar.MONTH) + 1, calendar2
            .get(Calendar.YEAR)));
      return calendar.getTime();
   }

   public void testShouldParseDateOk() {
      String testDateAsString = "Tue Apr 08 00:00:00 BST 2009";
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(sprintCache.getDate(testDateAsString));
      assertEquals(8, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(4, calendar.get(Calendar.DAY_OF_WEEK));
      assertEquals(3, calendar.get(Calendar.MONTH));
      assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(0, calendar.get(Calendar.MINUTE));
      assertEquals(0, calendar.get(Calendar.SECOND));
      assertEquals(2009, calendar.get(Calendar.YEAR));

   }

}
