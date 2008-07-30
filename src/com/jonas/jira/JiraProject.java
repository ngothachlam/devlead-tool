package com.jonas.jira;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JiraProject {
	private static List<JiraProject> PROJECTS = new ArrayList<JiraProject>();

	public static final JiraProject LLU_SYSTEMS_PROVISIONING = new JiraProject("LLU Systems Provisioning", "LLU", "10070");
	public static final JiraProject LLU_DEV_SUPPORT = new JiraProject("LLU Dev Support", "LLUDEVSUP", "10192");

	private final String selectId;
	private final String name;

	private final String jiraKey;

	private JiraProject(String name, String jiraKey, String selectId) {
		this.name = name;
		this.jiraKey = jiraKey;
		this.selectId = selectId;
		PROJECTS.add(this);
	}

	public static JiraProject getProject(String name) {
		for (Iterator<JiraProject> iterator = PROJECTS.iterator(); iterator.hasNext();) {
			JiraProject jiraProject = iterator.next();
			if (jiraProject.getName().equals(name))
				return jiraProject;
		}
		return null;
	}

	public String getJiraKey() {
		return jiraKey;
	}

	public String getSelectId() {
		return selectId;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return jiraKey;
	}

}
