package com.jonas.agile.devleadtool.sprint;

import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;

public class SprintTest extends TestCase {

   Sprint sprint = new Sprint();

   public void testShouldParseDateOk() {
      Calendar calendar = Calendar.getInstance();
      calendar.getTime();
      Sprint sprint = new Sprint();
      System.out.println(sprint.format(calendar.getTime()));
   }

   public void testShouldCalculateDayInSprintOk() {
      assertEquals(new Integer(0), getSprint(0, +1).calculateDayInSprint());
      assertEquals(new Integer(1), getSprint(-1, +1).calculateDayInSprint());
      assertEquals(new Integer(2), getSprint(-2, +1).calculateDayInSprint());
      assertEquals(new Integer(2), getSprint(-3, +1).calculateDayInSprint());
      assertEquals(new Integer(2), getSprint(-4, +1).calculateDayInSprint());
      assertEquals(new Integer(3), getSprint(-5, +1).calculateDayInSprint());
      assertEquals(new Integer(4), getSprint(-6, +1).calculateDayInSprint());
      assertEquals(new Integer(5), getSprint(-7, +1).calculateDayInSprint());
   }
   public void testA(){
   assertEquals(SprintTime.currentSprint, getSprint(-1, 0).calculateTime());
   }
   

   public void testShouldCalculateSprintTimesOk() {
      assertEquals(SprintTime.currentSprint, getSprint(-1, +1).calculateTime());
      assertEquals(SprintTime.currentSprint, getSprint(0, 1).calculateTime());
      assertEquals(SprintTime.currentSprint, getSprint(-1, 0).calculateTime());
      assertEquals(SprintTime.afterCurrentSprint, getSprint(1, 1).calculateTime());
      assertEquals(SprintTime.afterCurrentSprint, getSprint(1, 2).calculateTime());
      assertEquals(SprintTime.afterCurrentSprint, getSprint(2, 2).calculateTime());
      assertEquals(SprintTime.beforeCurrentSprint, getSprint(-1, -1).calculateTime());
      assertEquals(SprintTime.beforeCurrentSprint, getSprint(-2, -1).calculateTime());
      assertEquals(SprintTime.beforeCurrentSprint, getSprint(-2, -2).calculateTime());
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
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return calendar;
   }

   private Date getDateAgainstToday(int offset) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + offset);
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      Calendar calendar2 = Calendar.getInstance();
      assertEquals(calendar.getTime(), getDate(calendar2.get(Calendar.DAY_OF_MONTH) + offset, calendar2.get(Calendar.MONTH) + 1, calendar2
            .get(Calendar.YEAR)));
      return calendar.getTime();
   }
}
