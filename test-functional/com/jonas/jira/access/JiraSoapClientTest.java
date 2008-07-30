package com.jonas.jira.access;

import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.jira.JiraProject;

import junit.framework.TestCase;

public class JiraSoapClientTest extends TestCase {

	JiraSoapClient client = new JiraSoapClient();

	public void testShouldGetFixVersionsOk() throws Exception {
		RemoteVersion[] fixVersions = client.getFixVersions(JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertTrue(fixVersions.length > 0);
	}

	public void testShouldGetSingleFixVersionOk() throws Exception {
		// TODO optimise this call = re-do in httpClient if XML can be found?
		RemoteVersion fixVersion = client.getFixVersion("Version 10", JiraProject.LLU_SYSTEMS_PROVISIONING);
		assertTrue(fixVersion != null);
	}
	
	public void testGetJira() throws Exception{
		assertEquals("LLU-1", client.getJira("LLU-1").getKey());
		assertEquals("LLU-2", client.getJira("LLU-2").getKey());
	}

}
