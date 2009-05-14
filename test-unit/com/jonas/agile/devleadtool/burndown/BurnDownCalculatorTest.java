package com.jonas.agile.devleadtool.burndown;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BurnDownCalculatorTest {

   private BurnDownCalculator burnDownCalculator;

   @Before
   public void setUp() throws Exception {
      burnDownCalculator = new BurnDownCalculator();
   }

   @Test
   public void testShouldCalculateBurndownsCorrectly() {
      Double dEst = 1d;
      Double qEst = 2d;
      Double dRem = 3d;
      Double qRem = 4d;
      assertEquals(new Double(7d), burnDownCalculator.calculateRemainingPointsForJira(true, dEst, qEst, dRem, qRem));
      assertEquals(new Double(3d), burnDownCalculator.calculateRemainingPointsForJira(false, dEst, qEst, dRem, qRem));
      
   }

}
