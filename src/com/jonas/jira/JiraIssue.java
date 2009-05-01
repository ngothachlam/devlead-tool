package com.jonas.jira;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.jfree.data.time.Day;

import com.jonas.common.DateHelper;
import com.jonas.common.logging.MyLogger;

public class JiraIssue {

   public void setOwner(String owner) {
      this.owner = owner;
   }

   public void setEnvironment(String environment) {
      this.environment = environment;
   }

   public static Logger log = MyLogger.getLogger(JiraIssue.class);

   private String buildNo;
   private String deliveryDate;
   private String estimate;
   private List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
   private StringBuffer fixVersionsAsStrings = new StringBuffer("");
   private int lluListPriority;
   private String projectAsString;
   private String release = "";
   private String spent;
   private String sprint;
   private String owner;
   private String environment;

   private final String key;
   private final String summary;
   private final String status;
   private final String resolution;
   private final String type;

   private String creationDate;

   public JiraIssue(String key, String release) {
      this(key, "", "", "", "", "");
      this.release = release;
      log.debug("Setting release to " + this.release + " = " + release);
   }

   public JiraIssue(String key, String summary, String status, String resolution, String type, String creationDate) {
      this.creationDate = creationDate;
      this.key = key.toUpperCase();
      this.summary = summary;
      this.status = status;
      this.resolution = resolution;
      this.type = type;
   }

   public JiraIssue(String key, String summary, String status, String resolution, String type, String buildNo, String estimate, int listPrio, String sprint, String project, String environment, String owner, String creationDate) {
      this(key, summary, status, resolution, type, creationDate);
      setBuildNo(buildNo);
      setEstimate(estimate);
      setSprint(sprint);
      setLLUListPriority(listPrio);
      setProject(project);
      setEnvironment(environment);
      setOwner(owner);
   }

   public void addFixVersions(JiraVersion fixVersion) {
      if (fixVersion != null) {
         fixVersions.add(fixVersion);
         if (fixVersions.size() > 1) {
            fixVersionsAsStrings.append(", ");
         }
         fixVersionsAsStrings.append(fixVersion.getName());
      }
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

   public String getDeliveryDate() {
      return deliveryDate;
   }

   public String getEstimate() {
      return estimate;
   }

   public List<JiraVersion> getFixVersions() {
      return fixVersions;
   }

   public String getFixVersionsAsStrings() {
      return fixVersionsAsStrings.toString();
   }

   public String getKey() {
      return key;
   }

   public int getLLUListPriority() {
      return lluListPriority;
   }

   public String getProjectAsString() {
      return projectAsString;
   }

   public String getRelease() {
      return release;
   }

   public String getResolution() {
      return resolution;
   }

   public String getSpent() {
      return spent;
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

   public void setDeliveryDate(String deliveryDate) {
      this.deliveryDate = deliveryDate;
   }

   public void setEstimate(String days) {
      this.estimate = days;
   }

   public void setLLUListPriority(int lluListPriority) {
      this.lluListPriority = lluListPriority;
   }

   public void setProject(String project) {
      this.projectAsString = project;
   }

   public void setSpent(String days) {
      this.spent = days;
   }

   public void setSprint(String sprint) {
      this.sprint = sprint;
   }

   /**
    * @generated by CodeSugar http://sourceforge.net/projects/codesugar
    */

   @Override
   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[JiraIssue:");
      buffer.append(" key: ");
      buffer.append(key);
      buffer.append(" fixVersions: ");
      buffer.append(fixVersions);
      buffer.append(" status: ");
      buffer.append(status);
      buffer.append(" summary: ");
      buffer.append(summary);
      buffer.append(" resolution: ");
      buffer.append(resolution);
      buffer.append(" buildNo: ");
      buffer.append(buildNo);
      buffer.append("]");
      return buffer.toString();
   }

   public String getEnvironment() {
      return environment;
   }

   public String getOwner() {
      return owner;
   }

   private SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

   protected Day getDay(String date){
      Calendar calendar = DateHelper.getDate(format, date);
      Day chartDay = new Day(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
      return chartDay;
   }
   
   public Day getCreationDay() {
      return getDay(creationDate);
   }
}
