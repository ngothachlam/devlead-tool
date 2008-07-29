package com.jonas.jira;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class JiraVersion {

	public static JiraVersion Version10 = new JiraVersion("Version 10", "11382");
	public static JiraVersion Version11 = new JiraVersion("Version 11", "11432");
	public static JiraVersion Version11Next = new JiraVersion("Version 11 - Next Sprint (3)", "11449");
	public static JiraVersion Backlog = new JiraVersion("Backlog", "11388");
	public static JiraVersion PamsBacklog = new JiraVersion("Pam's Backlog", "11458");
	public static JiraVersion Version9 = new JiraVersion("Version 9", "11264");

	private static List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();

	private static String SELECT_NAME = "fixfor";
	private String name;
	private String selectId;

	private JiraVersion(String name, String selectId) {
		this.name = name;
		this.selectId = selectId;
	}

	public String getSelectId() {
		return selectId;
	}

	public String getName() {
		return name;
	}

	public static JiraVersion getVersion(String name) {
		
		for (Iterator iterator = fixVersions.iterator(); iterator.hasNext();) {
			JiraVersion fixVersion = (JiraVersion) iterator.next();
			if (fixVersion.getName().equals(name))
				return fixVersion;
		}
		return null;
	}

	public JiraProject getProject() {
		return null;
	}
}
