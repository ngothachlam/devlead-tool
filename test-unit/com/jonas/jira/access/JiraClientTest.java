package com.jonas.jira.access;

import junit.framework.TestCase;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class JiraClientTest extends TestCase {

	JiraClient jiraClient = new JiraClient(ClientConstants.JIRA_URL_AOLBB);

	public void testShouldGetJirasForFixVersionOk() {
		jiraClient.login();
		JiraIssue[] jiras = jiraClient.getJirasFromFixVersion(JiraVersion.Version10);
		assertTrue(jiras.length > 0);
	}

}
