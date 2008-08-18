package com.jonas.jira.access;

import junit.framework.TestCase;

import org.apache.axis.client.Stub;
import org.apache.axis.description.TypeDesc;
import org.jdom.Element;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteCustomFieldValue;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.jira.JiraProject;

public class JiraSoapClientTest extends TestCase {

	JiraSoapClient client = null;
	private JiraSoapService jiraSoapService;

	protected void setUp() throws Exception {
		super.setUp();
		JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = new JiraSoapServiceServiceLocator();
		jiraSoapServiceServiceLocator.setJirasoapserviceV2EndpointAddress(ClientConstants.JIRA_URL_AOLBB + ClientConstants.WS_LOCATION);
		jiraSoapService = (jiraSoapServiceServiceLocator).getJirasoapserviceV2();
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

	public void testGetJiraOk() throws RemotePermissionException, RemoteAuthenticationException, RemoteException, java.rmi.RemoteException,
			JiraIssueNotFoundException, InterruptedException {
		RemoteIssue jira = client.getJira("LLU-1");
		assertEquals("LLU-1", jira.getKey());
		RemoteCustomFieldValue[] customFieldValues = jira.getCustomFieldValues();
		for (int i = 0; i < customFieldValues.length; i++) {
			RemoteCustomFieldValue rcfv = customFieldValues[i];
			System.out.println(rcfv.getCustomfieldId() + " - " + rcfv.getKey() );
			if ("customfield_10160".equals(rcfv.getCustomfieldId())) {
				String[] values = rcfv.getValues();
				System.out.println("*");
				for (int j = 0; j < values.length; j++) {
					System.out.println(i + ": " + values[i]);
				}
			}
		}
		assertTrue(false);
	}
	


	public void testGetJiraThatDoesNotExist() throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
			java.rmi.RemoteException, JiraIssueNotFoundException, InterruptedException {
		assertEquals("LLU-1", client.getJira("LLU-1").getKey());
		((Stub) jiraSoapService).setTimeout(500);
		try {
			assertEquals(null, client.getJira("LLU-2"));
			assertTrue(false);
		} catch (JiraIssueNotFoundException e) {
		}
	}

	public void testGetJiraThatTimesOut() throws RemotePermissionException, RemoteAuthenticationException, RemoteException,
			java.rmi.RemoteException, JiraIssueNotFoundException, InterruptedException {
		assertEquals("LLU-1", client.getJira("LLU-1").getKey());
		((Stub) jiraSoapService).setTimeout(500);
		try {
			assertEquals(null, client.getJira("LLU-2"));
			assertTrue(false);
		} catch (JiraIssueNotFoundException e) {
		}
		Thread.currentThread().sleep(700);
		assertTrue("Get Timeout to work!", false);
		assertEquals("LLU-3", client.getJira("LLU-3").getKey());
	}

}
