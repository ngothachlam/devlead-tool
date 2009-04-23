package com.jonas.agile.devleadtool.gui.action;

import java.util.Calendar;
import java.util.Date;
import com.jonas.agile.devleadtool.sprint.SprintCreationSource;
import junit.framework.TestCase;

public class CalculateSprintLengthActionTest extends TestCase {
   @Override
   protected void setUp() throws Exception {
      action = new CalculateSprintLengthAction(null);
   }

   CalculateSprintLengthAction action;

   
   
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

   public void testFullWorkingWeeks() {
      Calendar startCalendar = getCalendar(5, 4, 2009); // sunday
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
   }

   public void testWorkingDays() {
      //thursday...
      assertEquals(1, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 23, 4, 2009)));
      assertEquals(2, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 24, 4, 2009)));
      assertEquals(2, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 25, 4, 2009)));
      assertEquals(2, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 26, 4, 2009)));
      assertEquals(3, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 27, 4, 2009)));
      assertEquals(4, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 28, 4, 2009)));
      assertEquals(5, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 29, 4, 2009)));
      assertEquals(6, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 30, 4, 2009)));
      assertEquals(7, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 1, 5, 2009)));
      assertEquals(7, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 2, 5, 2009)));
      assertEquals(7, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 3, 5, 2009)));
      assertEquals(8, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 4, 5, 2009)));
      assertEquals(9, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 5, 5, 2009)));
      assertEquals(10, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 6, 5, 2009)));
      assertEquals(11, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 7, 5, 2009)));
      assertEquals(12, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 8, 5, 2009)));
      assertEquals(12, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 9, 5, 2009)));
      assertEquals(12, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 10, 5, 2009)));
      assertEquals(13, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(23, 4, 2009, 11, 5, 2009)));
      
      //friday...
      assertEquals(-1, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(24, 4, 2009, 22, 4, 2009)));
      assertEquals(0, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(24, 4, 2009, 23, 4, 2009)));
      assertEquals(1, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(24, 4, 2009, 24, 4, 2009)));
      assertEquals(1, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(24, 4, 2009, 25, 4, 2009)));
      assertEquals(1, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(24, 4, 2009, 26, 4, 2009)));
      assertEquals(2, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(24, 4, 2009, 27, 4, 2009)));
      
      //Saturday
      assertEquals(0, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(25, 4, 2009, 24, 4, 2009)));
      assertEquals(0, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(25, 4, 2009, 25, 4, 2009)));
      assertEquals(0, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(25, 4, 2009, 26, 4, 2009)));
      assertEquals(1, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(25, 4, 2009, 27, 4, 2009)));
      
      //Sunday
      assertEquals(0, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(26, 4, 2009, 25, 4, 2009)));
      assertEquals(0, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(26, 4, 2009, 26, 4, 2009)));
      assertEquals(1, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(26, 4, 2009, 27, 4, 2009)));
      assertEquals(2, action.getWorkingDaysBetweenStartAndEnd(setUpResponse(26, 4, 2009, 28, 4, 2009)));
      
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
