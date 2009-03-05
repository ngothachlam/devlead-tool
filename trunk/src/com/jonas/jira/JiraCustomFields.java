package com.jonas.jira;

public enum JiraCustomFields {

   LLUListPrio("customfield_10241"),
   LLUBuildNo("customfield_10160"),
   LLUSprint("customfield_10282"),
   LLUProject("customfield_10290"),
   LLUDeliveryDate("customfield_10188");

   private final String customFieldJiraId;

   private JiraCustomFields(String customFieldJiraId) {
      this.customFieldJiraId = customFieldJiraId;
   }
   
   public String toString(){
      return customFieldJiraId;
   }
}
