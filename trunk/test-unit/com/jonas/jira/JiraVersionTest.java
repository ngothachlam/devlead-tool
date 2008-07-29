package com.jonas.jira;

import junit.framework.TestCase;

public class JiraVersionTest extends TestCase {

	public void testJiraVersion(){
		JiraVersion version10 = JiraVersion.getVersion("Version 10");
		assertEquals("Version 10", version10.getName());
		assertEquals("Version 10", version10.getSelectId());
		assertEquals(JiraProject.LLU_SYSTEMS_PROVISIONING, version10.getProject());
	}
	
}
