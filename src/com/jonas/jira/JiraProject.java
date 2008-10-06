package com.jonas.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.JiraClient;

public class JiraProject {
   private static Vector<JiraProject> projects = new Vector<JiraProject>();
   
   public static final JiraProject LLU_SYSTEMS_PROVISIONING = new JiraProject(JiraClient.JiraClientAolBB, "LLU", "LLU", "10070");
   public static final JiraProject LLU_DEV_SUPPORT = new JiraProject(JiraClient.JiraClientAolBB, "LLUDEVSUP", "LLUDEVSUP", "10192");
   public static final JiraProject ATLASSIN_TST = new JiraProject(JiraClient.JiraClientAtlassin, "Atlassin", "TST", "10420");
   
   private static Logger log = MyLogger.getLogger(JiraProject.class);

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

   public static Vector<JiraProject> getProjects() {
      return projects;
   }

   public void clearFixVersions() {
      fixVersions.clear();
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      JiraProject other = (JiraProject) obj;
      if (client == null) {
         if (other.client != null)
            return false;
      } else if (!client.equals(other.client))
         return false;
      if (fixVersions == null) {
         if (other.fixVersions != null)
            return false;
      } else if (!fixVersions.equals(other.fixVersions))
         return false;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (jiraKey == null) {
         if (other.jiraKey != null)
            return false;
      } else if (!jiraKey.equals(other.jiraKey))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
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

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((client == null) ? 0 : client.hashCode());
      result = prime * result + ((fixVersions == null) ? 0 : fixVersions.hashCode());
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((jiraKey == null) ? 0 : jiraKey.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }

   public String toString() {
      return jiraKey;
   }
}
