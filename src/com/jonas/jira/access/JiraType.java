package com.jonas.jira.access;

import java.util.HashMap;
import java.util.Map;
import com.atlassian.jira.rpc.soap.beans.RemoteIssueType;
import com.atlassian.jira.rpc.soap.beans.RemoteResolution;
import com.jonas.jira.JiraResolution;

public class JiraType {

   private final static Map<String, JiraType> map = new HashMap<String, JiraType>();
   
   //Use: http://jira.atlassian.com/secure/IssueNavigator.jspa?reset=true&resolution=-1&tempMax=1 to try!!
   //TODO: Add preset resolutions!!
   
   private final String id;
   private final String name;
   private final String description;

   private JiraType(String id, String name, String description) {
      this.id = id;
      this.name = name;
      this.description = description;
   }

   public static int getAmount() {
      return map.size();
   }
   
   public static JiraType getResolution(String id) {
      return map.get(id);
   }

   public static void setTypes(RemoteIssueType[] remoteTypes) {
      for (RemoteIssueType remoteResolution : remoteTypes) {
         map.put(remoteResolution.getId(), new JiraType(remoteResolution.getId(), remoteResolution.getName(),
               remoteResolution.getDescription()));
      }
   }

   public String getName() {
      return name;
   }
   
}
