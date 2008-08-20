package com.jonas.jira;

import java.util.HashMap;
import java.util.Map;

public class JiraStatus {
   private static Map<String, JiraStatus> statuses = new HashMap<String, JiraStatus>();

   // Use: http://jira.atlassian.com/secure/IssueNavigator.jspa?reset=true&status=7&tempMax=1 to try!!
   public final static JiraStatus OPEN = new JiraStatus("1", "Open");
   public final static JiraStatus INPROGRESS = new JiraStatus("3", "In Progress");
   public final static JiraStatus REOPENED = new JiraStatus("4", "Reopened");
   public final static JiraStatus RESOLVED = new JiraStatus("5", "Resolved");
   public final static JiraStatus CLOSED = new JiraStatus("6", "Closed");

   private final String name;
   private final String id;

   private JiraStatus(String id, String name) {
      this.id = id;
      this.name = name;
      statuses.put(id, this);
   }

   public static JiraStatus getJiraStatusById(String id) {
      return statuses.get(id);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      JiraStatus other = (JiraStatus) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

   public String getName() {
      return name;
   }
}
