package com.jonas.jira;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.JiraHttpClient;


public enum JiraStatus {
   Open("1", "Open"), InProgress("3", "In Progress"), ReOpened("4", "Reopened"), Resolved("5", "Resolved"), Closed("6", "Closed"), Closable("10016", "Closable");

   // Use: http://jira.atlassian.com/secure/IssueNavigator.jspa?reset=true&status=7&tempMax=1 to try!!

   private final String title;
   private final String id;
   
   private static final Logger log = MyLogger.getLogger(JiraStatus.class);

   private JiraStatus(String id, String title) {
      this.id = id;
      this.title = title;
   }

   public static JiraStatus getJiraStatusById(String id) {
      for (JiraStatus jiraStatus : values()) {
         if(jiraStatus.getId().equals(id)){
            return jiraStatus;
         }
      }
      return null;
   }
   
   public static JiraStatus getJiraStatusByName(String name) {
      for (JiraStatus jiraStatus : values()) {
         if(jiraStatus.getTitle().equals(name)){
            return jiraStatus;
         }
      }
      for (JiraStatus jiraStatus : values()) {
         if(jiraStatus.toString().equals(name)){
            return jiraStatus;
         }
      }
      log.error("could not find status " + name);
      return null;
   }

   public String getTitle() {
      return title;
   }
   
   public String getId() {
      return id;
   }
}
