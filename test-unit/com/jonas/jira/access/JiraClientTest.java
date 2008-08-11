package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraClientTest extends TestCase {

	JiraClient jiraClient = null;

	public void setUp() {
		jiraClient = JiraClient.JiraClientAtlassin;
	}

	public void testShouldGetJirasForFixVersionOk() throws HttpException, IOException, JDOMException {
		jiraClient.login();
		JiraIssue[] jiras = jiraClient.getJirasFromFixVersion(JiraVersion.Atlassain_TST);
		assertTrue(jiras.length > 0);
	}

	public void testShouldGetFixVersionsOk() throws HttpException, IOException {
		jiraClient.login();
		JiraVersion[] fixVersions = jiraClient.getFixVersionsFromProject(JiraProject.ATLASSIN_TST, false);
		assertEquals(5, fixVersions.length);
	}

}
