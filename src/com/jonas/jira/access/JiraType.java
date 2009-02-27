package com.jonas.jira.access;

import java.util.HashMap;
import java.util.Map;
import com.atlassian.jira.rpc.soap.beans.RemoteIssueType;

public class JiraType {

   private final static Map<String, JiraType> map = new HashMap<String, JiraType>();
   
   //Use: http://jira.atlassian.com/secure/IssueNavigator.jspa?reset=true&resolution=-1&tempMax=1 to try!!
   //TODO: Add preset resolutions!!
   
   private final String name;

   private JiraType(String name) {
      this.name = name;
   }

   public static int getAmount() {
      return map.size();
   }
   
   public static JiraType getResolution(String id) {
      return map.get(id);
   }

   public static void setTypes(RemoteIssueType[] remoteTypes) {
      for (RemoteIssueType remoteResolution : remoteTypes) {
         map.put(remoteResolution.getId(), new JiraType(remoteResolution.getName()));
      }
   }

   public String getName() {
      return name;
   }
   
}
