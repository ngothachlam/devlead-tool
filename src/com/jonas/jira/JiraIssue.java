package com.jonas.jira;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.logging.MyLogger;

public class JiraIssue {

	public static Logger log = MyLogger.getLogger(JiraIssue.class);
	private String key;
	private List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
	private String status;
	private String resolution;
	private String summary;
	private String buildNo;

	public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}

	public JiraIssue(String key, String summary, String status, String resolution) {
		this.key = key;
		this.summary = summary;
		this.status = status;
		this.resolution = resolution;
		// this.buildNo = buildNo;
		// TODO: add build no
	}

	public JiraIssue(RemoteIssue jira, JiraProject project) {
		this(jira.getKey(), jira.getSummary(), jira.getStatus(), JiraResolution.getResolution(jira.getResolution()).getName());
		RemoteVersion[] tempFixVersions = jira.getFixVersions();
		for (int i = 0; i < tempFixVersions.length; i++) {
			RemoteVersion remoteVersion = tempFixVersions[i];
			JiraVersion fixVers = JiraVersion.getVersionById(remoteVersion.getId());
			if (fixVers == null) {
				fixVers = new JiraVersion(remoteVersion, project);
			}
			addFixVersions(fixVers);
			if (i > 1) {
				log.error("Cannot handle more than one fix version at the moment for " + getKey());
			}
		}
	}

	public String getKey() {
		return key;
	}

	private void clearFixVersions() {
		fixVersions.clear();
	}

	public void addFixVersions(JiraVersion fixVersion) {
		if (fixVersion == null) {
			throw new NullPointerException("fixVersion is null!");
		}
		fixVersions.add(fixVersion);
	}

	public List<JiraVersion> getFixVersions() {
		return fixVersions;
	}

	public String getStatus() {
		return status;
	}

	public String getResolution() {
		return resolution;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Jira: (");
		sb.append("Name: \"").append(key).append("\"");
		sb.append(", FixVersion: \"").append(fixVersions).append("\"");
		sb.append(", Status: \"").append(status).append("\"");
		sb.append(", Resolution: \"").append(resolution).append("\"");
		sb.append(")");
		return sb.toString();
	}

	public String getSummary() {
		return summary;
	}

	public String getBuildNo() {
		return buildNo;
	}
}
