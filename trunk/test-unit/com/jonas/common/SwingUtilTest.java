package com.jonas.common;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class SwingUtilTest extends JonasTestCase {
   
   public void testShouldReturnCorrectXandYconsideringStartMenu(){
      assertEquals(50, SwingUtil.getRelativeXLocationConsideringStartMenu(1000, 900));
      assertEquals(45, SwingUtil.getRelativeYLocationConsideringStartMenu(1000, 900, 10));
      
      assertEquals(10, SwingUtil.getRelativeXLocationConsideringStartMenu(1000, 980));
      assertEquals(20, SwingUtil.getRelativeYLocationConsideringStartMenu(1000, 950, 10));
   }

}
