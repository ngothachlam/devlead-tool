package com.jonas.agile.devleadtool;

import junit.framework.TestCase;

public class PlannerHelperTest extends TestCase {

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldExtractProjectnameFromJiraNameok(){
      PlannerHelper helper = new PlannerHelper(null, "test");
      assertEquals("LLU", helper.getProjectKey("LLU"));
      assertEquals("LLU", helper.getProjectKey("LLU-"));
      assertEquals("LLUDEVSUP", helper.getProjectKey("LLUDEVSUP-1234"));
   }
}
