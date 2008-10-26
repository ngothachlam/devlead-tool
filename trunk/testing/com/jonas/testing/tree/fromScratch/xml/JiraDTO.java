package com.jonas.testing.tree.fromScratch.xml;

import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class JiraDTO {

   private Logger log = MyLogger.getLogger(JiraDTO.class);

   private String fixVersion;
   private String key;
   private String sprint;

   public String getFixVersion() {
      return fixVersion;
   }

   public String getKey() {
      return key;
   }

   public String getSprint() {
      return sprint;
   }

   public void setFixVersion(String fixVersion) {
      log.debug("setFixVersion: " + fixVersion);
      this.fixVersion = fixVersion;
   }

   public void setKey(String key) {
      log.debug("setKey: " + key);
      this.key = key;
   }

   public void setSprint(String sprint) {
      log.debug("setSprint: " + sprint);
      this.sprint = sprint;
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
