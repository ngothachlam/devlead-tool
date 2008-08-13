package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;

import com.jonas.jira.JiraVersion;
import com.jonas.jira.JiraIssue;

public class JiraHttpClientTest extends TestCase {

	public void setUp() {
	}

	public void testApacheCommonsAttempt() throws IOException, HttpException, JDOMException, JiraException {
		JiraHttpClient client = new JiraHttpClient(ClientConstants.JIRA_URL_ATLASSIN);
		client.loginToJira();
		asserFixVersionExistsAndHasJirasAgainstIt(client, JiraVersion.Atlassain_TST);
	}

	private void asserFixVersionExistsAndHasJirasAgainstIt(JiraHttpClient client, JiraVersion version) throws HttpException, IOException, JDOMException, JiraException {
		List<JiraIssue> jiras = client.getJiras(version);
		assertTrue(jiras.size() > 1);
		for (int i = 0; i < jiras.size(); i++) {
			System.out.println(jiras.get(i));
		}
		JiraVersion fixVersion = jiras.get(0).getFixVersions().get(0);
		assertEquals(version, JiraVersion.getVersionById(fixVersion.getId()));
	}
}
