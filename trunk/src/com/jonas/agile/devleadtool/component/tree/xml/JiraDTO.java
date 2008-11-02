package com.jonas.agile.devleadtool.component.tree.xml;

import java.util.ArrayList;
import java.util.List;

public class JiraDTO {

   private String summary;
   private List<String> fixVersions = new ArrayList<String>();
   private String key;
   private String resolution;
   private String sprint;
   private String status;
   private boolean isToSync = false;
   private String id;

   public JiraDTO() {
   }

   public JiraDTO(String key, String id, String summary, List<String> list, String sprint, String status, String resolution, boolean isToSync) {
      setKey(key);
      setId(id);
      setSummary(summary);
      setFixVersions(list);
      setSprint(sprint);
      setStatus(status);
      setResolution(resolution);
      setToSync(isToSync);

      // this.resolution = resolution;
      // this.sprint = sprint;
      // this.status = status;
      // this.summary = summary;
      // this.id = id;
      // this.isToSync = isToSync;
      // for (String fixVersion : fixVersions) {
      // addFixVersion(fixVersion);
      // }
   }

   public String getSummary() {
      return summary;
   }

   public List<String> getFixVersions() {
      return fixVersions;
   }

   public String getKey() {
      return key;
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

   public void setSummary(String summary) {
      this.summary = cutString(summary, 70);
   }

   String cutString(String string, int i) {
      return string.length() > i ? string.substring(0, i) + "..." : string;
   }

   public void addFixVersion(String fixVersion) {
      this.fixVersions.add(fixVersion);
   }

   public void setKey(String key) {
      this.key = key;
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

   public void setToSync(Boolean isToSync) {
      this.isToSync = isToSync;
   }

   public boolean getSyncable() {
      return isToSync;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setFixVersions(List<String> fixVersions) {
      for (String fixVersion : fixVersions) {
         addFixVersion(fixVersion);
      }
   }

}
