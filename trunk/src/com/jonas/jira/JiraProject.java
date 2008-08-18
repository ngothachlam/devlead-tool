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
	private static List<JiraProject> PROJECTS = new ArrayList<JiraProject>();
	private static Logger log = MyLogger.getLogger(JiraProject.class);

	public static final JiraProject LLU_SYSTEMS_PROVISIONING = new JiraProject(JiraClient.JiraClientAolBB, "LLU", "LLU", "10070");
	public static final JiraProject LLU_DEV_SUPPORT = new JiraProject(JiraClient.JiraClientAolBB, "LLUDEVSUP", "LLUDEVSUP", "10192");
	public static final JiraProject ATLASSIN_TST = new JiraProject(JiraClient.JiraClientAtlassin, "Atlassin", "TST", "10420");

	private final Map<String, JiraVersion> fixVersions = new HashMap<String, JiraVersion>();

	private final String id;
	private final String name;

	private final String jiraKey;

	private JiraClient client;

	protected JiraProject(JiraClient client, String name, String jiraKey, String id) {
		this.client = client;
		this.name = name;
		this.jiraKey = jiraKey;
		this.id = id;
		PROJECTS.add(this);
	}

	public static JiraProject getProjectByName(String name) {
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

	public String toString() {
		return jiraKey;
	}

	public void clearFixVersions() {
		fixVersions.clear();
		// TODO Add fix version listener
	}

	// public void addFixVersion(JiraVersion jiraVersion) {
	// log.debug("Adding fixVersion " + jiraVersion.getName());
	// fixVersions.put(jiraVersion.getId(), jiraVersion);
	// // TODO Add fix version listener
	// }

	public JiraVersion[] getFixVersions(boolean getArchivedVersions) {
		// TODO make this change to use JiraVersion static classes instead of its own instance of fixversions!!
		// List<JiraVersion> tempFixVersions = new ArrayList<JiraVersion>();
		// for (Iterator iterator = fixVersions.values().iterator(); iterator.hasNext();) {
		// JiraVersion version = (JiraVersion) iterator.next();
		// if (version.isArchived() == getArchivedVersions) {
		// tempFixVersions.add(version);
		// }
		// }
		// return tempFixVersions;
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

	public JiraClient getJiraClient() {
		return client;
	}

	public static List<JiraProject> getProjects() {
		return PROJECTS;
	}

	public static JiraProject getProjectByKey(String key) {
		// TODO optimise by changing PROJECTS to become a map with "key" as the keyobject
		for (Iterator<JiraProject> iterator = PROJECTS.iterator(); iterator.hasNext();) {
			JiraProject jiraProject = iterator.next();
			if (jiraProject.getJiraKey().equalsIgnoreCase(key))
				return jiraProject;
		}
		return null;
	}
}
