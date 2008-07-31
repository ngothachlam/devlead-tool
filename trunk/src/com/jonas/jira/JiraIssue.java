package com.jonas.jira;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import com.jonas.common.logging.MyLogger;

public class JiraIssue {

   private static Logger log = MyLogger.getLogger(JiraIssue.class);
   private String name;
   private List<JiraVersion> fixVersions = new ArrayList<JiraVersion>();
   private String status;
   private String resolution;

   private JiraIssue(Element e, JiraVersion fixVersion) {
      this(e);
      this.addFixVersion(fixVersion);
   }

   private JiraIssue(Element e) {
      this(get(e, "key"), get(e, "status"), get(e, "resolution"));
   }

   public JiraIssue(String name, String status, String resolution) {
      this.name = name;
      this.status = status;
      this.resolution = resolution;
   }

   public JiraIssue(Element e, List<JiraVersion> fixVersions) {
      this(e);
      this.fixVersions = fixVersions;
   }

   public String getName() {
      return name;
   }

   private void clearFixVersion() {
      fixVersions.clear();
   }

   private void addFixVersion(JiraVersion fixVersion2) {
      fixVersions.add(fixVersion2);
   }

   public List<JiraVersion> getFixVersions() {
      return fixVersions;
   }

   public String getStatus() {
      return status;
   }

   public String getResolution() {
      return resolution;
   }

   public static List<JiraIssue> buildJiras(List<Element> list, JiraVersion fixVersion2) {
      List<JiraIssue> jiras = new ArrayList();
      for (Iterator iterator = list.iterator(); iterator.hasNext();) {
         Element e = (Element) iterator.next();
         if (false) {
            String fixVersionName = e.getChildText("fixVersion");
            log.debug("fixVersionName: " + fixVersionName);
            JiraVersion versionByName = JiraVersion.getVersionByName(fixVersionName);
            log.debug("versionByName: " + versionByName);
            jiras.add(new JiraIssue(e, versionByName));
         } else {
            List<Element> fixVersionStrings = e.getChildren("fixVersion");
            List<JiraVersion> versions = new ArrayList<JiraVersion>();
            for (Iterator iterator2 = fixVersionStrings.iterator(); iterator2.hasNext();) {
               Element element = (Element) iterator2.next();
               JiraVersion versionByName = JiraVersion.getVersionByName(element.getText());
               versions.add(versionByName);
            }
            jiras.add(new JiraIssue(e, versions));

         }
      }
      return jiras;
   }

   private static String get(Element element, String string) {
      return element.getChildText(string);
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer("[Jira: (");
      sb.append("Name: \"").append(name).append("\"");
      sb.append(", FixVersion: \"").append(fixVersions).append("\"");
      sb.append(", Status: \"").append(status).append("\"");
      sb.append(", Resolution: \"").append(resolution).append("\"");
      sb.append(")");
      return sb.toString();
   }

}
