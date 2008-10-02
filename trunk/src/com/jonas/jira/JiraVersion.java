package com.jonas.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class JiraVersion {

   private static Logger log = MyLogger.getLogger(JiraVersion.class);
   private static Map<String, JiraVersion> versions = new HashMap<String, JiraVersion>();

   private boolean archived;
   private String id;
   private JiraProject jiraProject;
   private String name;

   public JiraVersion(String id, JiraProject jiraProject, String name, boolean archived) {
      this.name = name;
      this.id = id;
      this.archived = archived;
      this.jiraProject = jiraProject;
      addVersion(this);
   }

   public static void addVersion(JiraVersion version) {
      JiraVersion jiraVersion = versions.get(version.getId());
      if (jiraVersion != null) {
         log.warn("version " + version + "(with id=" + version.getId() + ") already exists - not adding it, but overwriting values!");
         jiraVersion.setArchived(version.isArchived());
         jiraVersion.setName(version.getName());
      } else
         versions.put(version.getId(), version);
   }

   public static void clearVersions() {
      versions.clear();
   }

   public static JiraVersion getVersionById(String id) {
      return versions.get(id);
   }

   public static JiraVersion getVersionByName(String name) {
      for (JiraVersion version : versions.values()) {
         if (version.getName().equals(name)) {
            return version;
         }
      }
      log.error("Version: \"" + name + "\" doesn't exist!");
      return null;
   }

   public static JiraVersion[] getVersionByProject(JiraProject lluSystemsProvisioning) {
      List<JiraVersion> tempVersions = new ArrayList<JiraVersion>();
      for (Iterator<JiraVersion> iterator = versions.values().iterator(); iterator.hasNext();) {
         JiraVersion version = (JiraVersion) iterator.next();
         if (version.getProject().equals(lluSystemsProvisioning)) {
            tempVersions.add(version);
         }
      }
      return tempVersions.toArray(new JiraVersion[tempVersions.size()]);
   }

   public static void removeVersion(String id) {
      versions.remove(id);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      JiraVersion other = (JiraVersion) obj;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public JiraProject getProject() {
      return jiraProject;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }

   public boolean isArchived() {
      return archived;
   }

   public void setArchived(boolean archived) {
      this.archived = archived;
   }

   void setJiraProject(JiraProject jiraProject) {
      this.jiraProject = jiraProject;
   }

   void setName(String name) {
      this.name = name;

   }

   public String toString() {
      return name;
   }

}
