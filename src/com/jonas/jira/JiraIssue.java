package com.jonas.jira;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class JiraIssue {

   public static Logger log = MyLogger.getLogger(JiraIssue.class);
   private String buildNo;
   private String estimate;
   private String spent;
   private List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
   private final String key;
   private int lluListPriority;
   private final String resolution;
   private final String status;
   private final String summary;
   private final String type;
   private String sprint;
   private String release = "";

   public JiraIssue(String key, String summary, String status, String resolution, String type) {
      this.key = key;
      this.summary = summary;
      this.status = status;
      this.resolution = resolution;
      this.type = type;
   }

   public JiraIssue(String key, String summary, String status, String resolution, String type, String buildNo, String estimate, int listPrio, String sprint) {
      this(key, summary, status, resolution, type);
      this.buildNo = buildNo;
      this.estimate = estimate;
      this.sprint = sprint;
      this.lluListPriority = listPrio;
   }

   public JiraIssue(String key, String release) {
      this(key, "", "", "", "");
      this.release = release;
      log.debug("Setting release to " + this.release + " = " + release);
   }

   public void addFixVersions(JiraVersion fixVersion) {
      if (fixVersion != null)
         fixVersions.add(fixVersion);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      JiraIssue other = (JiraIssue) obj;
      if (buildNo == null) {
         if (other.buildNo != null)
            return false;
      } else if (!buildNo.equals(other.buildNo))
         return false;
      if (estimate == null) {
         if (other.estimate != null)
            return false;
      } else if (!estimate.equals(other.estimate))
         return false;
      if (fixVersions == null) {
         if (other.fixVersions != null)
            return false;
      } else if (!fixVersions.equals(other.fixVersions))
         return false;
      if (key == null) {
         if (other.key != null)
            return false;
      } else if (!key.equals(other.key))
         return false;
      if (lluListPriority != other.lluListPriority)
         return false;
      if (resolution == null) {
         if (other.resolution != null)
            return false;
      } else if (!resolution.equals(other.resolution))
         return false;
      if (spent == null) {
         if (other.spent != null)
            return false;
      } else if (!spent.equals(other.spent))
         return false;
      if (status == null) {
         if (other.status != null)
            return false;
      } else if (!status.equals(other.status))
         return false;
      if (summary == null) {
         if (other.summary != null)
            return false;
      } else if (!summary.equals(other.summary))
         return false;
      if (type == null) {
         if (other.type != null)
            return false;
      } else if (!type.equals(other.type))
         return false;
      return true;
   }

   public String getBuildNo() {
      return buildNo;
   }

   public String getEstimate() {
      return estimate;
   }

   public String getSpent() {
      return spent;
   }

   public List<JiraVersion> getFixVersions() {
      return fixVersions;
   }

   public String getKey() {
      return key;
   }

   public int getLLUListPriority() {
      return lluListPriority;
   }

   public String getResolution() {
      return resolution;
   }

   public String getStatus() {
      return status;
   }

   public String getSummary() {
      return summary;
   }

   public String getType() {
      return type;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((buildNo == null) ? 0 : buildNo.hashCode());
      result = prime * result + ((estimate == null) ? 0 : estimate.hashCode());
      result = prime * result + ((fixVersions == null) ? 0 : fixVersions.hashCode());
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + lluListPriority;
      result = prime * result + ((resolution == null) ? 0 : resolution.hashCode());
      result = prime * result + ((spent == null) ? 0 : spent.hashCode());
      result = prime * result + ((status == null) ? 0 : status.hashCode());
      result = prime * result + ((summary == null) ? 0 : summary.hashCode());
      result = prime * result + ((type == null) ? 0 : type.hashCode());
      return result;
   }

   public void setBuildNo(String buildNo) {
      this.buildNo = buildNo;
   }

   public void setEstimate(String days) {
      this.estimate = days;
   }
   public void setSprint(String sprint) {
      this.sprint = sprint;
   }

   public void setLLUListPriority(int lluListPriority) {
      this.lluListPriority = lluListPriority;
   }

   public void setSpent(String days) {
      this.spent = days;
   }

   /**
    * @generated by CodeSugar http://sourceforge.net/projects/codesugar
    */

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[JiraIssue:");
      buffer.append(" log: ");
      buffer.append(log);
      buffer.append(" buildNo: ");
      buffer.append(buildNo);
      buffer.append(" fixVersions: ");
      buffer.append(fixVersions);
      buffer.append(" key: ");
      buffer.append(key);
      buffer.append(" resolution: ");
      buffer.append(resolution);
      buffer.append(" status: ");
      buffer.append(status);
      buffer.append(" summary: ");
      buffer.append(summary);
      buffer.append("]");
      return buffer.toString();
   }

   public String getSprint() {
      return sprint;
   }

   public String getRelease() {
      return release;
   }
}
