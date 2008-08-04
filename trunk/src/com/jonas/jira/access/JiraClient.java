package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.xml.rpc.ServiceException;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemoteException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraClient {

	private Logger log = MyLogger.getLogger(JiraClient.class);
	private JiraHttpClient httpClient;
	private JiraSoapClient soapClient;

	public static final JiraClient JiraClientAolBB = new JiraClient(ClientConstants.JIRA_URL_AOLBB + ClientConstants.WS_LOCATION,
			ClientConstants.JIRA_URL_AOLBB);
	public static final JiraClient JiraClientAtlassin = new JiraClient(ClientConstants.jIRA_URL_ATLASSIN
			+ ClientConstants.WS_LOCATION, ClientConstants.jIRA_URL_ATLASSIN);

	private JiraClient(String address) {
		JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = getLocator(address);
		JiraSoapService jirasoapserviceV2;
		try {
			jirasoapserviceV2 = jiraSoapServiceServiceLocator.getJirasoapserviceV2();
			this.setSoapClient(new JiraSoapClient(jirasoapserviceV2));
		} catch (ServiceException e) {
			log.fatal(e);
			e.printStackTrace();
		}
	}

	public JiraClient(String address, String jiraUrl) {
		this(address);
		this.setHttpClient(new JiraHttpClient(jiraUrl));
		// httpClient.setJiraUrl(jiraUrl);
	}

	private JiraSoapServiceServiceLocator getLocator(String address) {
		JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = new JiraSoapServiceServiceLocator();
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

	public JiraIssue[] getJirasFromFixVersion(JiraVersion version) throws HttpException, IOException, JDOMException {
		List<JiraIssue> jiras = httpClient.getJiras(version);
		return (JiraIssue[]) jiras.toArray(new JiraIssue[jiras.size()]);
	}

	public void login() throws HttpException, IOException {
		httpClient.loginToJira();
	}

	public JiraVersion[] getFixVersionsFromProject(JiraProject jiraProject, boolean isArchived) throws RemotePermissionException,
			RemoteAuthenticationException, RemoteException, java.rmi.RemoteException {
		RemoteVersion[] fixVersions = soapClient.getFixVersions(jiraProject);
		buildJiraVersions(fixVersions, jiraProject);
		return jiraProject.getFixVersions(isArchived);
	}

	private void buildJiraVersions(RemoteVersion[] fixVersions, JiraProject jiraProject) {
		jiraProject.clearFixVersions();
		for (int i = 0; i < fixVersions.length; i++) {
			jiraProject.addFixVersion(new JiraVersion(fixVersions[i], jiraProject));
		}
	}

	public JiraIssue getJira(String jira, JiraProject project) throws RemotePermissionException, RemoteAuthenticationException,
			RemoteException, java.rmi.RemoteException, JiraIssueNotFoundException {
		//TODO thread this!!
		RemoteIssue remoteJira = soapClient.getJira(jira.toUpperCase());
		JiraIssue jiraIssue = new JiraIssue(remoteJira, project);
		if(jiraIssue == null){
			throw new JiraIssueNotFoundException("Jira ["+jira+"] doesn't exist in " + project.getJiraKey());
		}
		return jiraIssue;
	}

}
