package com.jonas.agile.devleadtool;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraProject;

public class PlannerHelperTest extends JonasTestCase {

	public void testShouldExtractProjectnameFromJiraNameok() {
		PlannerHelper helper = new PlannerHelper(null, "test");
		assertEquals("LLU", helper.getProjectKey("LLU"));
		assertEquals("LLU", helper.getProjectKey("LLU-"));
		assertEquals("LLUDEVSUP", helper.getProjectKey("LLUDEVSUP-1234"));
		assertEquals("1234", PlannerHelper.getProjectKey("1234"));
	}
	
	public void testShouldCheckJiraNameOk() {
		PlannerHelper helper = new PlannerHelper(null, "test");
		assertEquals(true, helper.isJiraString("A-1"));
		assertEquals(true, helper.isJiraString("LLU-1234"));
		assertEquals(false, helper.isJiraString("A1"));
		assertEquals(false, helper.isJiraString("A"));
		assertEquals(false, helper.isJiraString("1"));
		assertEquals(false, helper.isJiraString("LLU-112A"));
		assertEquals(false, helper.isJiraString("LLU-11A2"));
		assertEquals(false, helper.isJiraString("L1LU-112"));
		assertEquals(false, helper.isJiraString("1LLU-112"));
		assertEquals(true, helper.isJiraString("LLUDEVSUP-497"));
	}
}
