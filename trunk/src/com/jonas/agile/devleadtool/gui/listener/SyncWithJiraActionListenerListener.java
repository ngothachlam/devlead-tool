package com.jonas.agile.devleadtool.gui.listener;

import com.jonas.jira.JiraIssue;

public interface SyncWithJiraActionListenerListener {

   public void jiraSynced(JiraIssue jira);
   public void jiraAdded(JiraIssue jira);

}
