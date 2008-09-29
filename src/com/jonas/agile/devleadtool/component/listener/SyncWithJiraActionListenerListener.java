package com.jonas.agile.devleadtool.component.listener;

import com.jonas.jira.JiraIssue;

public interface SyncWithJiraActionListenerListener {

   public void jiraSynced(JiraIssue jira, int tableRowSynced);
   public void jiraAdded(JiraIssue jiraIssue);

}
