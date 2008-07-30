package com.jonas.jira;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class JiraVersion {

	private static List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
	public static JiraVersion Version10 = new JiraVersion("Version 10", "11382", JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion Version11 = new JiraVersion("Version 11", "11432", JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion Version11Next = new JiraVersion("Version 11 - Next Sprint (3)", "11449", JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion Backlog = new JiraVersion("Backlog", "11388", JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion PamsBacklog = new JiraVersion("Pam's Backlog", "11458", JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion Version9 = new JiraVersion("Version 9", "11264", JiraProject.LLU_SYSTEMS_PROVISIONING);


	private static String SELECT_NAME = "fixfor";
	private String name;
	private String jiraKey;
	private final JiraProject jiraProject;

	private JiraVersion(String name, String jiraKey, JiraProject jiraProject) {
		this.name = name;
		this.jiraKey = jiraKey;
		this.jiraProject = jiraProject;
		fixVersions.add(this);
	}

	public String getJiraKey() {
		return jiraKey;
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
		return jiraProject;
	}
	
	public String toString(){
		return name;
	}
}
