package com.jonas.jira;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.access.JiraClient;

public class JiraProjectTest extends JonasTestCase{
	
	public void testJiraProject(){
		assertJiraProject("LLU", "LLU", "10070");
		assertJiraProject("LLUDEVSUP", "LLUDEVSUP", "10192");
		assertJiraProject("Atlassin", "TST", "10420");
		assertJiraProject("Atlassin", "tst", "10420");
	}

	private void assertJiraProject(String name, String key, String id) {
		assertJiraProject(name, key, id, JiraProject.getProjectByKey(key));
		assertJiraProject(name, key, id, JiraProject.getProjectByName(name));
	}

	private void assertJiraProject(String name, String key, String id, JiraProject project) {
		assertEquals( key.toUpperCase(), project.getJiraKey() );
		assertEquals( name, project.getName() );
		assertEquals( id, project.getId() );
	}

	public void testJiraProjectAndFixVersion(){
		JiraProject jiraProject = new JiraProject(JiraClient.JiraClientAolBB, "test", "key", "4321");
		assertEquals(0, jiraProject.getFixVersions(false).length);
		assertEquals(0, jiraProject.getFixVersions(true).length);
		new JiraVersion("1234", jiraProject, "test", false);
		assertEquals(1, jiraProject.getFixVersions(false).length);
		assertEquals(0, jiraProject.getFixVersions(true).length);
		new JiraVersion("1234", jiraProject, "test", true);
		assertEquals(0, jiraProject.getFixVersions(false).length);
		assertEquals(1, jiraProject.getFixVersions(true).length);
		new JiraVersion("12345", jiraProject, "test", false);
		assertEquals(1, jiraProject.getFixVersions(false).length);
		assertEquals(1, jiraProject.getFixVersions(true).length);
	}
	
	  public void testShouldExtractProjectFromJiraNameok() {
	     assertEquals(JiraProject.LLU_SYSTEMS_PROVISIONING, JiraProject.getProjectByKey("LLU"));
	      assertEquals(JiraProject.LLU_DEV_SUPPORT, JiraProject.getProjectByKey("LLUDEVSUP"));
	   }
}
