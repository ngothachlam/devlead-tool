package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class PlannerHelperTest extends JonasTestCase {

   public void testShouldExtractProjectnameFromJiraNameok(){
      PlannerHelper helper = new PlannerHelper(null, "test");
      assertEquals("LLU", helper.getProjectKey("LLU"));
      assertEquals("LLU", helper.getProjectKey("LLU-"));
      assertEquals("LLUDEVSUP", helper.getProjectKey("LLUDEVSUP-1234"));
   }
}
