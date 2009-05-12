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
