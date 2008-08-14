package com.jonas.jira;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.atlassian.jira.rpc.soap.beans.RemoteIssue;
import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.logging.MyLogger;

public class JiraIssue {

	private static Logger log = MyLogger.getLogger(JiraIssue.class);
	private String key;
	private List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
	private String status;
	private String resolution;
	private String summary;

	private JiraIssue(Element e) {
		this(get(e, "key"), get(e, "summary"), get(e, "status"), get(e, "resolution"));
	}

	public JiraIssue(String key, String summary, String status, String resolution) {
		this.key = key;
		this.summary = summary;
		this.status = status;
		this.resolution = resolution;
	}

	public JiraIssue(Element e, List<JiraVersion> fixVersions) {
		this(e);
		this.fixVersions = fixVersions;
		if (fixVersions.size() > 1) {
			log.error("Cannot handle more than one fix version at the moment for " + getKey());
		}
	}

	public JiraIssue(RemoteIssue jira, JiraProject project) {
		this(jira.getKey(), jira.getSummary(), jira.getStatus(), jira.getResolution());
		RemoteVersion[] tempFixVersions = jira.getFixVersions();
		// if (tempFixVersions.length > 1) {
		// throw new RuntimeException(getName() + " - has more than one fixversion!. Cannot handle this at the moment!!");
		// }
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

	private void addFixVersions(JiraVersion fixVersion) {
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

	public static List<JiraIssue> buildJiras(List<Element> list) {
		List<JiraIssue> jiras = new ArrayList<JiraIssue>();
		for (Iterator<Element> iterator = list.iterator(); iterator.hasNext();) {
			Element e = iterator.next();
			List<Element> fixVersionStrings = e.getChildren("fixVersion");
			List<JiraVersion> versions = new ArrayList<JiraVersion>();
			for (Iterator<Element> iterator2 = fixVersionStrings.iterator(); iterator2.hasNext();) {
				Element element = iterator2.next();
				JiraVersion versionByName = JiraVersion.getVersionByName(element.getText());
				log.debug("Getting version byName: \"" + element.getText() + "\" is \"" + versionByName + "\"");
				versions.add(versionByName);
			}
			jiras.add(new JiraIssue(e, versions));

		}
		return jiras;
	}

	private static String get(Element element, String string) {
		return element.getChildText(string);
	}

	@Override
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
}
