package com.jonas.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JiraProject {
	private static List<JiraProject> PROJECTS = new ArrayList<JiraProject>();

	public static final JiraProject LLU_SYSTEMS_PROVISIONING = new JiraProject("LLU Systems Provisioning", "LLU", "10070");
	public static final JiraProject LLU_DEV_SUPPORT = new JiraProject("LLU Dev Support", "LLUDEVSUP", "10192");
	public static final JiraProject ATLASSIN_TST = new JiraProject("Atlassin - TST", "TST", "10420");

	private final Map<String, JiraVersion> fixVersions = new HashMap<String, JiraVersion>();
	
	private final String id;
	private final String name;

	private final String jiraKey;

	protected JiraProject(String name, String jiraKey, String id) {
		this.name = name;
		this.jiraKey = jiraKey;
		this.id = id;
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

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return jiraKey;
	}

	public void clearFixVersions() {
		fixVersions.clear();
	}

	public void addFixVersion(JiraVersion jiraVersion) {
		fixVersions.put(jiraVersion.getId(), jiraVersion);
	}

	public JiraVersion[] getFixVersions(boolean isArchived) {
		List<JiraVersion> tempFixVersions = new ArrayList<JiraVersion>();
		for (Iterator iterator = fixVersions.values().iterator(); iterator.hasNext();) {
			JiraVersion version = (JiraVersion) iterator.next();
			if(version.isArchived() == isArchived){
				tempFixVersions.add(version);
			}
		}
		return (JiraVersion[]) tempFixVersions.toArray(new JiraVersion[tempFixVersions.size()]);
	}

}
