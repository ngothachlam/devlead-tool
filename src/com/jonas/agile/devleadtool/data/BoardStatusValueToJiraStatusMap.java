package com.jonas.agile.devleadtool.data;

import java.util.Set;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.jira.JiraStatus;

public class BoardStatusValueToJiraStatusMap {

   public static boolean isMappedOk(BoardStatusValue boardStatus, String jiraStatus) {
      Set<JiraStatus> jiraStatuses = boardStatus.getJiraStatuses();
      for (JiraStatus string : jiraStatuses) {
         if (jiraStatus.length() >= string.getTitle().length()) {
            String preJiraStatus = jiraStatus.substring(0, string.getTitle().length());
            if (preJiraStatus.equals(string.getTitle()))
               return true;
         }
      }
      return false;
   }

}
