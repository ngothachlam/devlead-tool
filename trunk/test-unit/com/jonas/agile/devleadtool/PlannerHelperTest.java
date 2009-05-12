package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class PlannerHelperTest extends JonasTestCase {

	public void testShouldExtractProjectnameFromJiraNameok() {
		assertEquals("LLU", PlannerHelper.getProjectKey("LLU"));
		assertEquals("LLU", PlannerHelper.getProjectKey("LLU-"));
		assertEquals("LLUDEVSUP", PlannerHelper.getProjectKey("LLUDEVSUP-1234"));
		assertEquals("1234", PlannerHelper.getProjectKey("1234"));
	}
	
	public void testShouldCheckJiraNameOk() {
		PlannerHelper helper = new PlannerHelper(null, "test");
		assertEquals(true, PlannerHelper.isJiraString("A-1"));
		assertEquals(true, PlannerHelper.isJiraString("LLU-1234"));
		assertEquals(false, PlannerHelper.isJiraString("A1"));
		assertEquals(false, PlannerHelper.isJiraString("A"));
		assertEquals(false, PlannerHelper.isJiraString("1"));
		assertEquals(false, PlannerHelper.isJiraString("LLU-112A"));
		assertEquals(false, PlannerHelper.isJiraString("LLU-11A2"));
		assertEquals(false, PlannerHelper.isJiraString("L1LU-112"));
		assertEquals(false, PlannerHelper.isJiraString("1LLU-112"));
		assertEquals(true, PlannerHelper.isJiraString("LLUDEVSUP-497"));
	}
}
