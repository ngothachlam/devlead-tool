package com.jonas.jira.access;

import java.util.List;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class JiraClient {

	private JiraHttpClient httpClient;
	private JiraSoapClient soapClient;

	JiraClient() {
		this(new JiraHttpClient(), new JiraSoapClient());
	}

	JiraClient(JiraHttpClient httpClient, JiraSoapClient soapClient) {
		this.httpClient = httpClient;
		this.soapClient = soapClient;
	}

	public JiraIssue[] getJirasFromFixVersion(JiraVersion version) {
		List<JiraIssue> jiras = httpClient.getJiras(version);
		return (JiraIssue[]) jiras.toArray(new JiraIssue[jiras.size()]);
	}

}
