package com.jonas.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.JiraClient;

public class JiraProject {
	private static Logger log = MyLogger.getLogger(JiraProject.class);
	private static List<JiraProject> projects = new ArrayList<JiraProject>();
	public static final JiraProject ATLASSIN_TST = new JiraProject(JiraClient.JiraClientAtlassin, "Atlassin", "TST", "10420");
	public static final JiraProject LLU_DEV_SUPPORT = new JiraProject(JiraClient.JiraClientAolBB, "LLUDEVSUP", "LLUDEVSUP", "10192");
	public static final JiraProject LLU_SYSTEMS_PROVISIONING = new JiraProject(JiraClient.JiraClientAolBB, "LLU", "LLU", "10070");

	private JiraClient client;

	private final Map<String, JiraVersion> fixVersions = new HashMap<String, JiraVersion>();
	private final String id;

	private final String jiraKey;

	private final String name;

	protected JiraProject(JiraClient client, String name, String jiraKey, String id) {
		this.client = client;
		this.name = name;
		this.jiraKey = jiraKey;
		this.id = id;
		projects.add(this);
	}

	public static JiraProject getProjectByKey(String key) {
		for (Iterator<JiraProject> iterator = projects.iterator(); iterator.hasNext();) {
			JiraProject jiraProject = iterator.next();
			if (jiraProject.getJiraKey().equalsIgnoreCase(key))
				return jiraProject;
		}
		return null;
	}

	public static JiraProject getProjectByName(String name) {
		for (Iterator<JiraProject> iterator = projects.iterator(); iterator.hasNext();) {
			JiraProject jiraProject = iterator.next();
			if (jiraProject.getName().equals(name))
				return jiraProject;
		}
		return null;
	}

	public static List<JiraProject> getProjects() {
		return projects;
	}

	public void clearFixVersions() {
		fixVersions.clear();
	}

	public JiraVersion[] getFixVersions(boolean getArchivedVersions) {
		JiraVersion[] versionByProject = JiraVersion.getVersionByProject(this);
		List<JiraVersion> versionsToReturn = new ArrayList<JiraVersion>();
		for (int i = 0; i < versionByProject.length; i++) {
			JiraVersion jiraVersion = versionByProject[i];
			log.debug("checking " + jiraVersion.getId());
			if (jiraVersion.isArchived() == getArchivedVersions) {
				log.debug("removing " + jiraVersion.getId());
				versionsToReturn.add(jiraVersion);
			}
		}
		return versionsToReturn.toArray(new JiraVersion[versionsToReturn.size()]);
	}

	public String getId() {
		return id;
	}

	public JiraClient getJiraClient() {
		return client;
	}

	public String getJiraKey() {
		return jiraKey;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return jiraKey;
	}
}
