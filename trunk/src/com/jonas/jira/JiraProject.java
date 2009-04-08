package com.jonas.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.jira.access.JiraClient;

public class JiraProject {
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((client == null) ? 0 : client.hashCode());
      result = prime * result + ((closeAction == null) ? 0 : closeAction.hashCode());
      result = prime * result + ((fixVersions == null) ? 0 : fixVersions.hashCode());
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((jiraKey == null) ? 0 : jiraKey.hashCode());
      result = prime * result + ((reOpenAction == null) ? 0 : reOpenAction.hashCode());
      return result;
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
      if (closeAction == null) {
         if (other.closeAction != null)
            return false;
      } else if (!closeAction.equals(other.closeAction))
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
      if (reOpenAction == null) {
         if (other.reOpenAction != null)
            return false;
      } else if (!reOpenAction.equals(other.reOpenAction))
         return false;
      return true;
   }

   private static Vector<JiraProject> projects = new Vector<JiraProject>();

   public static final JiraProject LLU = new JiraProject(JiraClient.JiraClientAolBB, "LLU", "LLU", "10070", "2", "3");
   public static final JiraProject TALK = new JiraProject(JiraClient.JiraClientAolBB, "CPS", "10021", "701");
   public static final JiraProject LLUDEVSUP = new JiraProject(JiraClient.JiraClientAolBB, "LLUDEVSUP", "10192", "2");
   public static final JiraProject BBMS = new JiraProject(JiraClient.JiraClientAolBB, "BBMS", "10000", null);
   public static final JiraProject ATLASSIN_TST = new JiraProject(JiraClient.JiraClientAtlassin, "TST", "10420", null);

   private JiraClient client;
   private final Map<String, JiraVersion> fixVersions = new HashMap<String, JiraVersion>();
   private final String id;
   private final String jiraKey;
   private String closeAction = null;
   private String reOpenAction = null;

   JiraProject(JiraClient client, String jiraKey, String id, String closeAction) {
      this.client = client;
      this.jiraKey = jiraKey;
      this.id = id;
      this.closeAction = closeAction;
      projects.add(this);
   }

   JiraProject(JiraClient client, String name, String jiraKey, String id, String closeAction, String reOpenAction) {
      this(client, jiraKey, id, closeAction);
      this.reOpenAction = reOpenAction;
   }

   public static JiraProject getProjectByKey(String key) {
      for (Iterator<JiraProject> iterator = projects.iterator(); iterator.hasNext();) {
         JiraProject jiraProject = iterator.next();
         if (jiraProject.getJiraKey().equalsIgnoreCase(key))
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

   public JiraVersion[] getFixVersions(boolean includeArchived) {
      return getFixVersions(includeArchived, true);
   }
   public JiraVersion[] getFixVersions(boolean includeArchived, boolean includeNonArchived) {
      JiraVersion[] versionByProject = JiraVersion.getVersionByProject(this);
      List<JiraVersion> versionsToReturn = new ArrayList<JiraVersion>();
      for (int i = 0; i < versionByProject.length; i++) {
         JiraVersion jiraVersion = versionByProject[i];
         if (isToIncludeArchived(includeArchived, jiraVersion) || isToIncludeNonArchived(includeNonArchived, jiraVersion)) {
            versionsToReturn.add(jiraVersion);
         }
      }
      return versionsToReturn.toArray(new JiraVersion[versionsToReturn.size()]);
   }

   private boolean isToIncludeNonArchived(boolean include, JiraVersion jiraVersion) {
      return include && !jiraVersion.isArchived();
   }

   private boolean isToIncludeArchived(boolean include, JiraVersion jiraVersion) {
      return include && jiraVersion.isArchived();
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

   public String toString() {
      return jiraKey;
   }

   public static JiraProject getProjectByJira(String string) {
      return getProjectByKey(PlannerHelper.getProjectKey(string));
   }

   public String getCloseAction() {
      return closeAction;
   }

   public String getReOpenAction() {
      return reOpenAction;
   }

}
