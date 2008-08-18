package com.jonas.jira;

import junit.framework.TestCase;

public class JiraVersionTest extends TestCase {

	public void testJiraVersion() {
		assertVersion("Backlog", "11388", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertVersion("Version 9", "11264", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertVersion("Version 10", "11382", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertVersion("Version 11", "11432", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertVersionByProject(JiraProject.LLU_SYSTEMS_PROVISIONING);
	}

	private void assertVersion(String string, String string2, boolean b, JiraProject lluSystemsProvisioning) {
		assertVersionById("Version 11", "11432", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertVersionByName("Version 11", "11432", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
	}

	public void testAddingJiraVersion() {
		assertTrue(JiraVersion.getVersionById("1") == null);
		new JiraVersion("1", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 1", false);
		assertVersion("Version 1", "1", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
	}

	public void testAddingJiraVersionShouldOverwriteAllValues() {
		String id = "1";

		JiraVersion.removeVersion(id);
		assertTrue(JiraVersion.getVersionById(id) == null);

		String name = "Version 1";
		boolean isArchived = false;
		new JiraVersion(id, JiraProject.LLU_SYSTEMS_PROVISIONING, name, isArchived);
		assertVersion(name, id, isArchived, JiraProject.LLU_SYSTEMS_PROVISIONING);

		String newName = name + " new";
		new JiraVersion(id, JiraProject.LLU_SYSTEMS_PROVISIONING, newName, !isArchived);
		assertVersion(newName, id, !isArchived, JiraProject.LLU_SYSTEMS_PROVISIONING);
	}

	private void assertVersionById(String name, String id, boolean isArchived, JiraProject lluSystemsProvisioning) {
		JiraVersion version10 = JiraVersion.getVersionById(id);
		assertEquals(name, version10.getName());
		assertEquals(id, version10.getId());
		assertEquals(isArchived, version10.isArchived());
		assertEquals(lluSystemsProvisioning, version10.getProject());
	}

	private void assertVersionByName(String name, String id, boolean isArchived, JiraProject lluSystemsProvisioning) {
		JiraVersion version10 = JiraVersion.getVersionByName(name);
		assertEquals(name, version10.getName());
		assertEquals(id, version10.getId());
		assertEquals(isArchived, version10.isArchived());
		assertEquals(lluSystemsProvisioning, version10.getProject());
	}

	private void assertVersionByProject(JiraProject lluSystemsProvisioning) {
		JiraVersion[] versions = JiraVersion.getVersionByProject(lluSystemsProvisioning);
		assertEquals(6, versions.length);
		// TODO solve below:
		// versions.contains(JiraVersion.Version10);
		// versions.contains(JiraVersion.Version11);
		// versions.contains(JiraVersion.Version11Next);
		// versions.contains(JiraVersion.Backlog);
		// versions.contains(JiraVersion.PamsBacklog);
		// versions.contains(JiraVersion.Version9);
	}

}
