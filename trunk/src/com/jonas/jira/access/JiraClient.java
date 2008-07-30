package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class JiraClient {

	private JiraHttpClient httpClient;
	private JiraSoapClient soapClient;

	JiraClient(String jiraUrl) {
		this(new JiraHttpClient(jiraUrl), new JiraSoapClient());
	}

	JiraClient(JiraHttpClient httpClient, JiraSoapClient soapClient) {
		this.httpClient = httpClient;
		this.soapClient = soapClient;
	}

	public JiraIssue[] getJirasFromFixVersion(JiraVersion version) {
		List<JiraIssue> jiras = httpClient.getJiras(version);
		return (JiraIssue[]) jiras.toArray(new JiraIssue[jiras.size()]);
	}

	public void login() {
		try {
			httpClient.loginToJira();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
