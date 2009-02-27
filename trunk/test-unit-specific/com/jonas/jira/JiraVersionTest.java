package com.jonas.jira;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class JiraVersionTest extends JonasTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		JiraVersion.clearVersions();
	}

	public void testCreatingJiraVersion(){
		assertVersion("id", "name", false, TestObjects.Project_TST1);
	}

	public void testCreatingJiraVersionThatHasAlreadyBeenCreatedWithSameId(){
		assertVersion("id", "name1", false, TestObjects.Project_TST1);
		//only overwriting named and archived!!
		assertVersion("id", "name2", true, TestObjects.Project_TST1);
	}
	
	private void assertVersion(String id, String name, boolean archived, JiraProject project) {
		assertVersion(id, name, archived, project, JiraVersion.getVersionById(id));
		assertVersion(id, name, archived, project, JiraVersion.getVersionByName(name));
		assertVersion(id, name, archived, project, JiraVersion.getVersionByProject(project)[0]);
		assertEquals( 1, JiraVersion.getVersionByProject(project).length);
	}

	private void assertVersion(String id, String name, boolean archived, JiraProject project, JiraVersion actualVersion) {
		assertEquals(id, actualVersion.getId());
		assertEquals(name, actualVersion.getName());
		assertEquals(project, actualVersion.getProject());
		assertEquals(archived, actualVersion.isArchived());
	}
}
