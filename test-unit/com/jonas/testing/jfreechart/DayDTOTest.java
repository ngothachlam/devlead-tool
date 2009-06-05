package com.jonas.testing.jfreechart;

import junit.framework.TestCase;

import org.jfree.data.time.Day;
import org.junit.Test;

import com.jonas.stats.charts.common.LowestCommonDenominatorRegularTime;

public class DayDTOTest extends TestCase{

   @Test
   public void shouldParseDateOk(){
      assertDTODates(new Day(10,5,2009), new Day(10,5,2009), false);
      assertDTODates(new Day(11,5,2009), new Day(11,5,2009), false);
      assertDTODates(new Day(12,5,2009), new Day(12,5,2009), false);
      assertDTODates(new Day(13,5,2009), new Day(13,5,2009), false);
      assertDTODates(new Day(14,5,2009), new Day(14,5,2009), false);
      assertDTODates(new Day(15,5,2009), new Day(15,5,2009), false);
      assertDTODates(new Day(16,5,2009), new Day(16,5,2009), false);
      assertDTODates(new Day(17,5,2009), new Day(17,5,2009), false);
      assertDTODates(new Day(18,5,2009), new Day(18,5,2009), false);
   }
   
   @Test
   public void shouldParseDateOkAsWeek(){
      assertDTODates(new Day(4,5,2009), new Day(10,5,2009), true);
      assertDTODates(new Day(11,5,2009), new Day(11,5,2009), true);
      assertDTODates(new Day(11,5,2009), new Day(12,5,2009), true);
      assertDTODates(new Day(11,5,2009), new Day(13,5,2009), true);
      assertDTODates(new Day(11,5,2009), new Day(14,5,2009), true);
      assertDTODates(new Day(11,5,2009), new Day(15,5,2009), true);
      assertDTODates(new Day(11,5,2009), new Day(16,5,2009), true);
      assertDTODates(new Day(11,5,2009), new Day(17,5,2009), true);
      assertDTODates(new Day(18,5,2009), new Day(18,5,2009), true);
   }

   private void assertDTODates(Day expectedCreationDto, Day dtoCreationDto, boolean isWeek) {
      LowestCommonDenominatorRegularTime dto = new LowestCommonDenominatorRegularTime(dtoCreationDto, isWeek);
      assertEquals(expectedCreationDto, dto.getDate());
   }
   
}
