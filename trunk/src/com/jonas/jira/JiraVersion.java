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

	public static JiraVersion Version10 = new JiraVersion("Version 10", "11382", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion Version11 = new JiraVersion("Version 11", "11432", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion Version11Next = new JiraVersion("Version 11 - Next Sprint (3)", "11449", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion Backlog = new JiraVersion("Backlog", "11388", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion PamsBacklog = new JiraVersion("Pam's Backlog", "11458", false, JiraProject.LLU_SYSTEMS_PROVISIONING);
	public static JiraVersion Version9 = new JiraVersion("Version 9", "11264", false, JiraProject.LLU_SYSTEMS_PROVISIONING);

	private static String SELECT_NAME = "fixfor";
	private boolean archived;
	private String name;
	private String id;
	private JiraProject jiraProject;

	public JiraVersion(String name, String id, boolean archived, JiraProject jiraProject) {
		this.name = name;
		this.id = id;
		this.archived = archived;
		this.jiraProject = jiraProject;
		addVersion(this);
	}

	public JiraVersion(RemoteVersion remoteVersion, JiraProject jiraProject) {
		this(remoteVersion.getName(), remoteVersion.getId(), remoteVersion.isArchived(), jiraProject);
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
		}
		else
			fixVersions.put(version.getId(), version);
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

}
