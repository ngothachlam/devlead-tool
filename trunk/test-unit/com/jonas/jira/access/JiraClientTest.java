package com.jonas.jira.access;

import java.io.IOException;
import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.TestObjects;
import com.jonas.jira.JiraVersion;

public class JiraClientTest extends TestCase {

	JiraClient jiraClient = null;

	public void setUp() {
		jiraClient = JiraClient.JiraClientAtlassin;
	}

	public void testShouldGetJirasForFixVersionOk() throws HttpException, IOException, JDOMException, JiraException {
		jiraClient.login();
		JiraIssue[] jiras = jiraClient.getJirasFromFixVersion(TestObjects.Atlassain_TST);
		assertTrue(jiras.length > 0);
	}

	public void testShouldGetFixVersionsOk() throws HttpException, IOException, JiraException {
		jiraClient.login();
		JiraVersion[] fixVersions = jiraClient.getFixVersionsFromProject(JiraProject.ATLASSIN_TST, false);
		assertEquals(5, fixVersions.length);
	}

	public void testShouldGetFixVersionsOkIfThereAreNoJiras() throws HttpException, IOException, JDOMException, JiraException {
		jiraClient.login();
		JiraIssue[] jiras = null;
		try {
			jiras = jiraClient.getJirasFromFixVersion(new JiraVersion("1", JiraProject.ATLASSIN_TST, "empty", false));
			assertTrue(false);
		} catch (JiraException e) {
		}
		assertEquals(0, jiras.length);
	}

}
