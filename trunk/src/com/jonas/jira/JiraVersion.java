package com.jonas.jira;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.atlassian.jira.rpc.soap.beans.RemoteVersion;
import com.jonas.common.logging.MyLogger;

public class JiraVersion {

	private static Map<String, JiraVersion> fixVersions = new HashMap<String, JiraVersion>();
	private static Logger log = MyLogger.getLogger(JiraVersion.class);

	public static JiraVersion Version10 = new JiraVersion("11382", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 10", false);
	public static JiraVersion Version11 = new JiraVersion("11432", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 11", false);
	public static JiraVersion Version11Next = new JiraVersion("11449", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 11 - Next Sprint (3)", false);
	public static JiraVersion Backlog = new JiraVersion("11388", JiraProject.LLU_SYSTEMS_PROVISIONING, "Backlog", false);
	public static JiraVersion PamsBacklog = new JiraVersion("11458", JiraProject.LLU_SYSTEMS_PROVISIONING, "Pam's Backlog", false);
	public static JiraVersion Version9 = new JiraVersion("11264", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 9", false);

	private static String SELECT_NAME = "fixfor";
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
		JiraVersion jiraVersion = fixVersions.get(version.getId());
		if (jiraVersion != null){
			log.warn("version " + version + "(with id=" + version.getId() + ") already exists - not adding it, but overwriting values!");
			jiraVersion.setArchived(version.isArchived());
			jiraVersion.setName(version.getName());
		}
		else
			fixVersions.put(version.getId(), version);
	}

	private void setName(String name) {
		this.name = name;
		
	}

	public static JiraVersion getVersionById(String id) {
		return fixVersions.get(id);
	}

	public static JiraVersion getVersionByName(String name) {
		for (Iterator iterator = fixVersions.values().iterator(); iterator.hasNext();) {
			JiraVersion version = (JiraVersion) iterator.next();
			if(version.getName().equals(name)){
				return version;
			}
		}
		log.error("Version: \"" + name + "\" doesn't exist!");
		return null;
	}
	
	public JiraProject getProject() {
		return jiraProject;
	}

	public String toString() {
		return name;
	}

	class VersionAlreadysExistsException extends Exception {

		public VersionAlreadysExistsException(String string) {
			super(string);
		}
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public static void removeVersion(String id) {
		fixVersions.remove(id);
	}


}
