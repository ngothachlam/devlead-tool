package com.jonas.jira.jirastat;

import org.jfree.data.time.Day;

import com.jonas.jira.JiraIssue;

public interface DateRetriever {

   public Day retrieveTimeLineDateFromJira(JiraIssue jiraIssue);

}