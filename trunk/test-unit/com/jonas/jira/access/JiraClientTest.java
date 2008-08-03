package com.jonas.jira.access;

import java.io.IOException;

import javax.xml.rpc.ServiceException;

import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;

import junit.framework.TestCase;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraClientTest extends TestCase {

	JiraClient jiraClient = null;

	public void setUp() {
		jiraClient = JiraClient.JiraClientAolBB;
	}

	public void testShouldGetJirasForFixVersionOk() throws HttpException, IOException, JDOMException {
		jiraClient.login();
		JiraIssue[] jiras = jiraClient.getJirasFromFixVersion(JiraVersion.Version10);
		assertTrue(jiras.length > 0);
	}

	public void testShouldGetFixVersionsOk() throws HttpException, IOException {
		jiraClient.login();
		JiraVersion[] fixVersions = jiraClient.getFixVersionsFromProject(JiraProject.LLU_SYSTEMS_PROVISIONING, false);
		assertEquals(5, fixVersions.length);
	}

}
