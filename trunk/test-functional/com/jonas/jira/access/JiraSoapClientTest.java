package com.jonas.jira.access;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.jira.JiraProject;

import junit.framework.TestCase;

public class JiraSoapClientTest extends TestCase {

	JiraSoapClient client = null;

	protected void setUp() throws Exception {
		super.setUp();
		JiraSoapService jiraSoapService = (new JiraSoapServiceServiceLocator()).getJirasoapserviceV2();
		client = new JiraSoapClient(jiraSoapService);
	}

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
