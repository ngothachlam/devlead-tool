package com.jonas.testing.jfreechart;

import org.jfree.data.time.Day;

import junit.framework.TestCase;

public class DayDTOTest extends TestCase {

   public void testShouldParseDateOk(){
      DayDTO dto = new DayDTO(new Day(1,5,2009), true);
      System.out.println(dto.getDate());
      
      dto = new DayDTO(new Day(2,5,2009), true);
      System.out.println(dto.getDate());
      
      dto = new DayDTO(new Day(3,5,2009), true);
      System.out.println(dto.getDate());
      
      dto = new DayDTO(new Day(4,5,2009), true);
      System.out.println(dto.getDate());
      
   }
   
}
