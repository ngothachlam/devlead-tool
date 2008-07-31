package com.jonas.jira;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.jonas.common.logging.MyLogger;

public class JiraIssue {

	private static Logger log = MyLogger.getLogger(JiraIssue.class);
	private final String name;
	private final JiraVersion fixVersion;
	private final String status;
	private final String resolution;

	private JiraIssue(Element e, JiraVersion fixVersion) {
		this(get(e, "key"), get(e, "status"), get(e, "resolution"), fixVersion);
	}

	public String getName() {
		return name;
	}

	public JiraVersion getFixVersion() {
		return fixVersion;
	}

	public String getStatus() {
		return status;
	}

	public String getResolution() {
		return resolution;
	}

	public JiraIssue(String name, String status, String resolution, JiraVersion fixVersion) {
		this.name = name;
		this.fixVersion = fixVersion;
		this.status = status;
		this.resolution = resolution;
	}

	public static List<JiraIssue> buildJiras(List<Element> list, JiraVersion fixVersion2) {
		List<JiraIssue> jiras = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			String fixVersionName = e.getChildText("fixVersion");
			log.debug("fixVersionName: " + fixVersionName);
			JiraVersion versionByName = JiraVersion.getVersionByName(fixVersionName);
			log.debug("versionByName: " + versionByName);
			jiras.add(new JiraIssue(e, versionByName));
		}
		return jiras;
	}

	private static String get(Element element, String string) {
		return element.getChildText(string);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[Jira: (");
		sb.append("Name: \"").append(name).append("\"");
		sb.append(", FixVersion: \"").append(fixVersion).append("\"");
		sb.append(", Status: \"").append(status).append("\"");
		sb.append(", Resolution: \"").append(resolution).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
