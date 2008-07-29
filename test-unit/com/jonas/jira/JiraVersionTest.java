package com.jonas.jira;

import junit.framework.TestCase;

public class JiraVersionTest extends TestCase {

	public void testJiraVersion(){
		assertVersion("Backlog", "11388", JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertVersion("Version 9", "11264", JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertVersion("Version 10", "11382", JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertVersion("Version 11", "11432", JiraProject.LLU_SYSTEMS_PROVISIONING);
	}

	private void assertVersion(String string, String string2, JiraProject lluSystemsProvisioning) {
		JiraVersion version10 = JiraVersion.getVersion(string);
		assertEquals(string, version10.getName());
		assertEquals(string2, version10.getJiraKey());
		assertEquals(lluSystemsProvisioning, version10.getProject());
	}
	
}
