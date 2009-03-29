package com.jonas.agile.devleadtool.gui.component.tree.xml;

import java.util.ArrayList;
import java.util.List;

public class JiraDTO {

   public void setProject(String project) {
      this.project = project;
   }

   private List<String> fixVersions = new ArrayList<String>();
   private String id;
   private boolean isToSync = false;
   private String key;
   private int originalEstimate;
   private String resolution;
   private String sprint;
   private String status;
   private String summary;
   private String project = "";
   private int actual;

   public JiraDTO() {
   }

   public JiraDTO(String key, String id, String summary, List<String> list, String sprint, String status, String resolution, boolean isToSync, int originalEstimate, int actual, String project) {
      setKey(key);
      setId(id);
      setSummary(summary);
      setFixVersions(list);
      setSprint(sprint);
      setStatus(status);
      setResolution(resolution);
      setToSync(isToSync);
      setOriginalEstimate(originalEstimate);
      setActual(actual);
      setProject(project);
   }

   public void setActual(int actual) {
      this.actual = actual;
   }

   public void addFixVersion(String fixVersion) {
      this.fixVersions.add(fixVersion);
   }

   public List<String> getFixVersions() {
      return fixVersions;
   }

   public String getId() {
      return id;
   }

   public String getKey() {
      return key;
   }

   public int getOriginalEstimate() {
      return originalEstimate;
   }

   public String getResolution() {
      return resolution;
   }

   public String getSprint() {
      return sprint;
   }

   public String getStatus() {
      return status;
   }

   public String getSummary() {
      return summary;
   }

   public boolean getSyncable() {
      return isToSync;
   }

   public void setFixVersions(List<String> fixVersions) {
      for (String fixVersion : fixVersions) {
         addFixVersion(fixVersion);
      }
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public void setOriginalEstimate(int originalEstimate) {
      this.originalEstimate = originalEstimate;
   }

   public void setResolution(String resolution) {
      this.resolution = resolution;
   }

   public void setSprint(String sprint) {
      this.sprint = sprint;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }

   public void setToSync(Boolean isToSync) {
      this.isToSync = isToSync;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("[JiraDTO ");
      sb.append("Key: ");
      sb.append(key);
      sb.append(", [");
      for (String fixVersion : fixVersions) {
         sb.append("FixVersion: ");
         sb.append(fixVersion);
         sb.append(" ");
      }
      sb.append("], Sprint: ");
      sb.append(sprint);
      sb.append("]");
      return sb.toString();
   }

   public int getActual() {
      return actual;
   }

   public String getProject() {
     return project;
   }

}
