package com.jonas.common;

import junit.framework.TestCase;

public class CalculatorHelperTest extends TestCase {

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testGetSecondsAsDays() {
      assertEquals(1f, CalculatorHelper.getSecondsAsDays(60 * 60 * 8));
      assertEquals(0.5f, CalculatorHelper.getSecondsAsDays(60 * 60 * 4));
      assertEquals(0.25f, CalculatorHelper.getSecondsAsDays(60 * 60 * 2));
      assertEquals(0.125f, CalculatorHelper.getSecondsAsDays(60 * 60 * 1));

      assertEquals("0.125", CalculatorHelper.getSecondsAsDays("3600"));
   }

   public void testShouldCutStringOk() {
      assertEquals("1234", CalculatorHelper.cutString("1234", 6, "..."));
      assertEquals("1234", CalculatorHelper.cutString("1234", 5, "..."));
      assertEquals("1234", CalculatorHelper.cutString("1234", 4, "..."));
      assertEquals("123...", CalculatorHelper.cutString("1234", 3, "..."));
      assertEquals("12...", CalculatorHelper.cutString("1234", 2, "..."));
      assertEquals("1...", CalculatorHelper.cutString("1234", 1, "..."));
      assertEquals("...", CalculatorHelper.cutString("1234", 0, "..."));
      try {
         assertEquals("", CalculatorHelper.cutString("1234", -1, "..."));
         assertTrue(false);
      } catch (Exception e) {
      }
   }
}
