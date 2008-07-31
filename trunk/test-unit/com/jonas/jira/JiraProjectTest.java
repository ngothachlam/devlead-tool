package com.jonas.jira;

import com.jonas.jira.access.JiraClient;

import junit.framework.TestCase;

public class JiraProjectTest extends TestCase {
	
	public void testJiraProject(){
		assertJiraProject("LLU Systems Provisioning", "10070");
		assertJiraProject("LLU Dev Support", "10192");
	}

	private void assertJiraProject(String string, String key) {
		assertEquals( string, JiraProject.getProject(string).getName() );
		assertEquals( key, JiraProject.getProject(string).getId() );
	}

	public void testJiraProjectAndFixVersion(){
		JiraProject jiraProject = new JiraProject(JiraClient.JiraClientAolBB, "test", "1234", "4321");
		assertEquals(0, jiraProject.getFixVersions(false).length);
		assertEquals(0, jiraProject.getFixVersions(true).length);
		jiraProject.addFixVersion(new JiraVersion("test", "1234", false, JiraProject.LLU_SYSTEMS_PROVISIONING));
		assertEquals(1, jiraProject.getFixVersions(false).length);
		assertEquals(0, jiraProject.getFixVersions(true).length);
		jiraProject.addFixVersion(new JiraVersion("test", "1234", true, JiraProject.LLU_SYSTEMS_PROVISIONING));
		assertEquals(0, jiraProject.getFixVersions(false).length);
		assertEquals(1, jiraProject.getFixVersions(true).length);
	}
}
