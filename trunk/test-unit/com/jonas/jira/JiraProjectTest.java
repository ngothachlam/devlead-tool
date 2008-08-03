package com.jonas.jira;

import com.jonas.jira.access.JiraClient;

import junit.framework.TestCase;

public class JiraProjectTest extends TestCase {
	
	public void testJiraProject(){
		assertJiraProject("LLU Systems Provisioning", "LLU", "10070");
		assertJiraProject("LLU Dev Support", "LLUDEVSUP", "10192");
	}

	private void assertJiraProject(String name, String key, String id) {
		assertEquals( name, JiraProject.getProjectByName(name).getName() );
		assertEquals( key, JiraProject.getProjectByName(name).getJiraKey() );
		assertEquals( id, JiraProject.getProjectByName(name).getId() );
		assertEquals( name, JiraProject.getProjectByKey(key).getName() );
		assertEquals( key, JiraProject.getProjectByKey(key).getJiraKey() );
		assertEquals( id, JiraProject.getProjectByKey(key).getId() );
	}

	public void testJiraProjectAndFixVersion(){
		JiraProject jiraProject = new JiraProject(JiraClient.JiraClientAolBB, "test", "1234", "4321");
		assertEquals(0, jiraProject.getFixVersions(false).length);
		assertEquals(0, jiraProject.getFixVersions(true).length);
		jiraProject.addFixVersion(new JiraVersion("1234", JiraProject.LLU_SYSTEMS_PROVISIONING, "test", false));
		assertEquals(1, jiraProject.getFixVersions(false).length);
		assertEquals(0, jiraProject.getFixVersions(true).length);
		jiraProject.addFixVersion(new JiraVersion("1234", JiraProject.LLU_SYSTEMS_PROVISIONING, "test", true));
		assertEquals(0, jiraProject.getFixVersions(false).length);
		assertEquals(1, jiraProject.getFixVersions(true).length);
	}
}
