package com.jonas.agile.devleadtool.controller.listener;

import com.jonas.jira.JiraIssue;

public interface SyncWithJiraActionListenerListener {

   public void jiraSynced(JiraIssue jira, int tableRowSynced);
   public void jiraAdded(JiraIssue jiraIssue);

}
