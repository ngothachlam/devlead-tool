package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraClient {

	private Logger log = MyLogger.getLogger(JiraClient.class);
	private JiraHttpClient httpClient;
	private JiraSoapClient soapClient;

	public static final JiraClient JiraClientAolBB = new JiraClient(ClientConstants.JIRA_URL_AOLBB + "/rpc/soap/jirasoapservice-v2");
	public static final JiraClient JiraClientAtlassin = new JiraClient(ClientConstants.jIRA_URL_ATLASSIN + "/rpc/soap/jirasoapservice-v2");

	protected JiraClient(String address) {
		JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = setAddress(address);
		JiraSoapService jirasoapserviceV2;
		try {
			jirasoapserviceV2 = jiraSoapServiceServiceLocator.getJirasoapserviceV2();
			this.setHttpClient(new JiraHttpClient(ClientConstants.JIRA_URL_AOLBB));
			this.setSoapClient(new JiraSoapClient(jirasoapserviceV2));
		} catch (ServiceException e) {
			log.fatal(e);
			e.printStackTrace();
		}
	}

	public JiraClient(String address, String jiraUrl) {
		this(address);
		httpClient.setJiraUrl(jiraUrl);
	}

	private JiraSoapServiceServiceLocator setAddress(String address) {
		JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = new JiraSoapServiceServiceLocator();
		log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
		jiraSoapServiceServiceLocator.setJirasoapserviceV2EndpointAddress(address);
		log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
		return jiraSoapServiceServiceLocator;
	}

	private void setHttpClient(JiraHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	private void setSoapClient(JiraSoapClient soapClient) {
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

	public JiraVersion[] getFixVersionsFromProject(JiraProject jiraProject, boolean isArchived) {
		try {
			RemoteVersion[] fixVersions = soapClient.getFixVersions(jiraProject);
			buildJiraVersions(fixVersions, jiraProject);
			return jiraProject.getFixVersions(isArchived);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void buildJiraVersions(RemoteVersion[] fixVersions, JiraProject jiraProject) {
		jiraProject.clearFixVersions();
		for (int i = 0; i < fixVersions.length; i++) {
			jiraProject.addFixVersion(new JiraVersion(fixVersions[i], jiraProject));
		}
	}

}
