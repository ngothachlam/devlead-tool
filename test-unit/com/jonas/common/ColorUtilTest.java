package com.jonas.common;

import junit.framework.TestCase;

public class ColorUtilTest extends TestCase {

   public void testShouldLimitColorTo255(){
      assertEquals(255,ColorUtil.getDivision(255, 255));
      assertEquals(255,ColorUtil.getDivision(256, 255));
      assertEquals(1,ColorUtil.getDivision(1, 256));
      assertEquals(0,ColorUtil.getDivision(0, 256));
      assertEquals(0,ColorUtil.getDivision(-1, 256));
      assertEquals(0,ColorUtil.getDivision(-10, 256));
   }

}
