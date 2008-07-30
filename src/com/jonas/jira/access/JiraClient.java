package com.jonas.jira.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.httpclient.HttpException;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraClient {

	private JiraHttpClient httpClient;
	private JiraSoapClient soapClient;

	public JiraClient(JiraSoapService jirasoapserviceV2)  {
		this(new JiraHttpClient(ClientConstants.JIRA_URL_AOLBB), new JiraSoapClient(jirasoapserviceV2));
	}

	public JiraClient(JiraSoapService jirasoapserviceV2, String jiraUrl) {
		this(new JiraHttpClient(jiraUrl), new JiraSoapClient(jirasoapserviceV2));
	}

	public JiraClient(JiraHttpClient httpClient, JiraSoapClient soapClient) {
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

	public JiraVersion[] getFixVersionsFromProject(JiraProject jiraProject) {
		try {
			RemoteVersion[] fixVersions = soapClient.getFixVersions(jiraProject);
			return buildJiraVersions(fixVersions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private JiraVersion[] buildJiraVersions(RemoteVersion[] fixVersions) {
		JiraVersion[] fixVersionArray = new JiraVersion[fixVersions.length];
		for (int i = 0; i < fixVersions.length; i++) {
			fixVersionArray[i] = JiraVersion.getVersion(fixVersions[i].getName());
		}
		return fixVersionArray;
	}

}
