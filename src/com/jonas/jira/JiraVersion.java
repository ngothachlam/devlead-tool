package com.jonas.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.logging.MyLogger;

public class JiraVersion {

	private static Map<String, JiraVersion> VERSIONS = new HashMap<String, JiraVersion>();
	private static Logger log = MyLogger.getLogger(JiraVersion.class);

	private boolean archived;
	private String name;
	private String id;
	private JiraProject jiraProject;

	public JiraVersion(String id, JiraProject jiraProject, String name, boolean archived) {
		this.name = name;
		this.id = id;
		this.archived = archived;
		this.jiraProject = jiraProject;
		addVersion(this);
	}

	public JiraVersion(RemoteVersion remoteVersion, JiraProject jiraProject) {
		this(remoteVersion.getId(), jiraProject, remoteVersion.getName(), remoteVersion.isArchived());
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static void addVersion(JiraVersion version) {
		JiraVersion jiraVersion = VERSIONS.get(version.getId());
		if (jiraVersion != null) {
			log.warn("version " + version + "(with id=" + version.getId() + ") already exists - not adding it, but overwriting values!");
			jiraVersion.setArchived(version.isArchived());
			jiraVersion.setName(version.getName());
		} else
			VERSIONS.put(version.getId(), version);
	}

	private void setName(String name) {
		this.name = name;

	}

	public static JiraVersion getVersionById(String id) {
		return VERSIONS.get(id);
	}

	public static JiraVersion getVersionByName(String name) {
		for (Iterator iterator = VERSIONS.values().iterator(); iterator.hasNext();) {
			JiraVersion version = (JiraVersion) iterator.next();
			if (version.getName().equals(name)) {
				return version;
			}
		}
		log.error("Version: \"" + name + "\" doesn't exist!");
		return null;
	}

	public static JiraVersion[] getVersionByProject(JiraProject lluSystemsProvisioning) {
		List<JiraVersion> versions = new ArrayList<JiraVersion>();
		for (Iterator<JiraVersion> iterator = VERSIONS.values().iterator(); iterator.hasNext();) {
			JiraVersion version = (JiraVersion) iterator.next();
			if (version.getProject().equals(lluSystemsProvisioning)) {
				versions.add(version);
			}
		}
		return versions.toArray(new JiraVersion[versions.size()]);
	}

	public JiraProject getProject() {
		return jiraProject;
	}

	public String toString() {
		return name;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public static void removeVersion(String id) {
		VERSIONS.remove(id);
	}

}
