package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;

import com.jonas.jira.Jira;

public class JiraHttpClientTest extends TestCase {

	public void setUp() {
	}

	public void testApacheCommonsAttempt() throws IOException, HttpException, JDOMException {
		JiraHttpClient client = new JiraHttpClient();
		client.loginToJira();
		List<Jira> jiras = client.getJiras();
		
		assertTrue(jiras.size() > 1);
	}
}
