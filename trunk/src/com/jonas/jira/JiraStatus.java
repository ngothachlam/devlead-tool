package com.jonas.jira;


public enum JiraStatus {
   Open("1", "Open"), InProgress("3", "In Progress"), ReOpened("4", "Reopened"), Resolved("5", "Resolved"), Closed("6", "Closed");

   // Use: http://jira.atlassian.com/secure/IssueNavigator.jspa?reset=true&status=7&tempMax=1 to try!!

   private final String title;
   private final String id;

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
      return null;
   }

   public String getTitle() {
      return title;
   }
   
   public String getId() {
      return id;
   }
}
