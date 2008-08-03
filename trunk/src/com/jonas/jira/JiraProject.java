package com.jonas.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

import com.jonas.common.logging.MyLogger;
import org.apache.log4j.Logger;

import com.jonas.jira.access.JiraClient;

public class JiraProject {
	private static List<JiraProject> PROJECTS = new ArrayList<JiraProject>();
	private static Logger log = MyLogger.getLogger(JiraProject.class);
	
	public static final JiraProject LLU_SYSTEMS_PROVISIONING = new JiraProject(JiraClient.JiraClientAolBB, "LLU Systems Provisioning",
			"LLU", "10070");
	public static final JiraProject LLU_DEV_SUPPORT = new JiraProject(JiraClient.JiraClientAolBB, "LLU Dev Support", "LLUDEVSUP", "10192");
	public static final JiraProject ATLASSIN_TST = new JiraProject(JiraClient.JiraClientAtlassin, "Atlassin - TST", "TST", "10420");

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
	}

	public void addFixVersion(JiraVersion jiraVersion) {
		log.debug("Adding fixVersion " + jiraVersion.getName());
		fixVersions.put(jiraVersion.getId(), jiraVersion);
	}

	public JiraVersion[] getFixVersions(boolean isArchived) {
		List<JiraVersion> tempFixVersions = new ArrayList<JiraVersion>();
		for (Iterator iterator = fixVersions.values().iterator(); iterator.hasNext();) {
			JiraVersion version = (JiraVersion) iterator.next();
			if (version.isArchived() == isArchived) {
				tempFixVersions.add(version);
			}
		}
		return (JiraVersion[]) tempFixVersions.toArray(new JiraVersion[tempFixVersions.size()]);
	}

	public JiraClient getJiraClient() {
		return client;
	}

   public static List<JiraProject> getProjects() {
      return PROJECTS;
   }

   public static JiraProject getProjectByKey(String key) {
      //TODO optimise by changing PROJECTS to become a map with "key" as the keyobject
      for (Iterator<JiraProject> iterator = PROJECTS.iterator(); iterator.hasNext();) {
         JiraProject jiraProject = iterator.next();
         if (jiraProject.getJiraKey().equalsIgnoreCase(key))
            return jiraProject;
      }
      return null;
   }
}
