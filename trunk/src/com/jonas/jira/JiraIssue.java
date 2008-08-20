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
	}

	public String getKey() {
		return key;
	}

	private void clearFixVersions() {
		fixVersions.clear();
	}

	public void addFixVersions(JiraVersion fixVersion) {
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
