package com.jonas.jira;

import junit.framework.TestCase;

public class JiraStatusTest extends TestCase {

   @Override
   protected void setUp() throws Exception {
      super.setUp();
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }
   
   public void testAllStatusesExist(){
      assertEquals(JiraStatus.Open, JiraStatus.getJiraStatusById("1"));
      assertEquals(JiraStatus.InProgress, JiraStatus.getJiraStatusById("3"));
      assertEquals(JiraStatus.ReOpened, JiraStatus.getJiraStatusById("4"));
      assertEquals(JiraStatus.Resolved, JiraStatus.getJiraStatusById("5"));
      assertEquals(JiraStatus.Closed, JiraStatus.getJiraStatusById("6"));
   }

}
