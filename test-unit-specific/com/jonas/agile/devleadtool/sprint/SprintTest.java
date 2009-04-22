package com.jonas.agile.devleadtool.sprint;

import java.util.Calendar;
import junit.framework.TestCase;

public class SprintTest extends TestCase {

   public void testShouldParseDateOk(){
      Calendar calendar = Calendar.getInstance();
      calendar.getTime();
      Sprint sprint = new Sprint();
      System.out.println(sprint.format(calendar.getTime()));
   }
   
}
