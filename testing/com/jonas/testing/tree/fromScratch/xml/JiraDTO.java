package com.jonas.testing.tree.fromScratch.xml;


public class JiraDTO {

   private String summary;
   private String fixVersion;
   private String key;
   private String resolution;
   private String sprint;
   private String status;

   public String getSummary() {
      return summary;
   }

   public String getFixVersion() {
      return fixVersion;
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
      this.summary = cutString( summary, 70);
   }

   String cutString(String string, int i) {
      return string.length() > i ? string.substring(0, i) + "..." : string;
   }

   public void setFixVersion(String fixVersion) {
      this.fixVersion = fixVersion;
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
      sb.append(", FixVersion: ");
      sb.append(fixVersion);
      sb.append(", Sprint: ");
      sb.append(sprint);
      sb.append("]");
      return sb.toString();
   }

}
