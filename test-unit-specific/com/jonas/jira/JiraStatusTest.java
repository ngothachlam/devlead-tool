package com.jonas.jira;

import junit.framework.TestCase;

public class JiraStatusTest extends TestCase {

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }
   
   public void testAllStatusesExist(){
      assertEquals(JiraStatus.OPEN, JiraStatus.getJiraStatusById("1"));
      assertEquals(JiraStatus.INPROGRESS, JiraStatus.getJiraStatusById("3"));
      assertEquals(JiraStatus.REOPENED, JiraStatus.getJiraStatusById("4"));
      assertEquals(JiraStatus.RESOLVED, JiraStatus.getJiraStatusById("5"));
      assertEquals(JiraStatus.CLOSED, JiraStatus.getJiraStatusById("6"));
   }

}
