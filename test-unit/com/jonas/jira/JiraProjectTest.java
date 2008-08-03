package com.jonas.jira;

import com.jonas.jira.access.JiraClient;

import junit.framework.TestCase;

public class JiraProjectTest extends TestCase {
	
	public void testJiraProject(){
		assertJiraProject("LLU Systems Provisioning", "LLU", "10070");
		assertJiraProject("LLU Dev Support", "LLUDEVSUP", "10192");
		assertJiraProject("Atlassin - TST", "TST", "10420");
		assertJiraProject("Atlassin - TST", "tst", "10420");
	}

	private void assertJiraProject(String name, String key, String id) {
		assertEquals( key.toUpperCase(), JiraProject.getProjectByKey(key).getJiraKey() );
		assertEquals( name, JiraProject.getProjectByKey(key).getName() );
		assertEquals( id, JiraProject.getProjectByKey(key).getId() );
		assertEquals( key.toUpperCase(), JiraProject.getProjectByName(name).getJiraKey() );
		assertEquals( name, JiraProject.getProjectByName(name).getName() );
		assertEquals( id, JiraProject.getProjectByName(name).getId() );
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
