package com.jonas.jira;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class JiraIssue {

	public static Logger log = MyLogger.getLogger(JiraIssue.class);
	private String buildNo;
	private String estimate;
	private List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
	private final String key;
	private String lluListPriority;
	private final String resolution;
   private final String status;
   private final String summary;
   private final String type;

   public JiraIssue(String key, String summary, String status, String resolution, String type) {
      this.key = key;
      this.summary = summary;
      this.status = status;
      this.resolution = resolution;
      this.type = type;
   }
   
	public JiraIssue(String key, String summary, String status, String resolution, String type, String buildNo, String estimate, String listPrio) {
	   this(key, summary, status, resolution, type);
      this.buildNo = buildNo;
      this.estimate = estimate;
      this.lluListPriority = listPrio;
	}

	public void addFixVersions(JiraVersion fixVersion) {
		fixVersions.add(fixVersion);
	}

   /**
    * Returns <code>true</code> if this <code>JiraIssue</code> is the same as the o argument.
    *
    * @return <code>true</code> if this <code>JiraIssue</code> is the same as the o argument.
    */
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null) {
         return false;
      }
      if (o.getClass() != getClass()) {
         return false;
      }
      JiraIssue castedObj = (JiraIssue) o;
      return ((this.buildNo == null ? castedObj.buildNo == null : this.buildNo.equals(castedObj.buildNo))
         && (this.fixVersions == null ? castedObj.fixVersions == null : this.fixVersions.equals(castedObj.fixVersions))
         && (this.key == null ? castedObj.key == null : this.key.equals(castedObj.key))
         && (this.resolution == null ? castedObj.resolution == null : this.resolution.equals(castedObj.resolution))
         && (this.status == null ? castedObj.status == null : this.status.equals(castedObj.status)) && (this.summary == null
         ? castedObj.summary == null
         : this.summary.equals(castedObj.summary)));
   }

	public String getBuildNo() {
		return buildNo;
	}

	public String getEstimate() {
      return estimate;
   }

	public List<JiraVersion> getFixVersions() {
		return fixVersions;
	}

	public String getKey() {
		return key;
	}

	public String getLLUListPriority() {
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

   /**
    * Override hashCode.
    *
    * @return the Objects hashcode.
    */
   public int hashCode() {
      int hashCode = 1;
      hashCode = 31 * hashCode + (log == null ? 0 : log.hashCode());
      hashCode = 31 * hashCode + (buildNo == null ? 0 : buildNo.hashCode());
      hashCode = 31 * hashCode + (fixVersions == null ? 0 : fixVersions.hashCode());
      hashCode = 31 * hashCode + (key == null ? 0 : key.hashCode());
      hashCode = 31 * hashCode + (resolution == null ? 0 : resolution.hashCode());
      hashCode = 31 * hashCode + (status == null ? 0 : status.hashCode());
      hashCode = 31 * hashCode + (summary == null ? 0 : summary.hashCode());
      return hashCode;
   }

   public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}

   public void setEstimate(String xpathResult) {
      this.estimate = xpathResult;
   }
   public void setLLUListPriority(String lluListPriority) {
      this.lluListPriority = lluListPriority;
   }

   /**
    * @generated by CodeSugar http://sourceforge.net/projects/codesugar */
   
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

   private void clearFixVersions() {
		fixVersions.clear();
	}
}
