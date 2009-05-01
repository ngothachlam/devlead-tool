package com.jonas.jira;

import junit.framework.TestCase;

import org.jfree.data.time.Day;

public class JiraIssueTest extends TestCase{
   
   JiraIssue jiraissue = new JiraIssue("", "");
   
   public void testGetDay(){
      String date = "Mon, 2 Mar 2009 18:28:25 +0000 (GMT)";
      Day dateResult = jiraissue.getDay(date);
      System.out.println(dateResult);
   }

}
