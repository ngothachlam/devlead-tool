package com.jonas.agile.devleadtool.component.tree.nodes;

import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;
import com.jonas.agile.devleadtool.junitutils.JonasUnitTestHelper;
import junit.framework.TestCase;

public class SprintNodeFuntionalUnitTest extends TestCase {

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   
   public void testShouldGetJiraCountsFromAnalysisOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      
      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      model.addJira(jiraDto1);
      
      SprintNode sprint = JonasUnitTestHelper.getSprintFromModel(model, 0);
      sprint.analyseToolTip();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.UnKnown, sprint.getLowestStatus());
      assertEquals(0, sprint.getCount(Status.Open));
      assertEquals("0%", sprint.getPercentage(Status.Open));
      
      jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", 28800, "Open", "FixVersion 1");
      model.addJira(jiraDto1);
      sprint.analyseToolTip();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.Open, sprint.getLowestStatus());
      assertEquals(1, sprint.getCount(Status.Open));
      assertEquals("100%", sprint.getPercentage(Status.Open));
      
      jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 2", 28800, "Resolved", "FixVersion 1");
      model.addJira(jiraDto1);
      
      jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 3", 28800, "Reopened", "FixVersion 2");
      model.addJira(jiraDto1);
      sprint.analyseToolTip();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(2, sprint.getFixVersionNode("FixVersion 1").getChildCount());
      assertEquals(1, sprint.getFixVersionNode("FixVersion 2").getChildCount());
      assertEquals(Status.Open, sprint.getLowestStatus());
      assertEquals(1, sprint.getCount(Status.Open));
      assertEquals(1, sprint.getCount(Status.Reopened));
      assertEquals(1, sprint.getCount(Status.Resolved));
      assertEquals("33%", sprint.getPercentage(Status.Open));
      assertEquals("33%", sprint.getPercentage(Status.Reopened));
      assertEquals("33%", sprint.getPercentage(Status.Resolved));
   }
   
   public void testShouldGetJiraEstimatesFromAnalysisOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      
      model.addJira(JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", 3600/2, null, "FixVersion 1"));
      model.addJira(JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", 3600*2, "Open", "FixVersion 1"));
      model.addJira(JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 2", 3600*3, "Reopened", "FixVersion 1"));
      model.addJira(JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 3", 3600*4, "Resolved", "FixVersion 1"));
      model.addJira(JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 4", 3600*5, "Closed", "FixVersion 1"));
      model.addJira(JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 5", null, null, "FixVersion 2"));
      model.addJira(JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 6", 3600*6, "Reopened", "FixVersion 2"));
      model.addJira(JonasUnitTestHelper.getTestJiraDto("Sprint 2", "Jira 7", 3600*7, "Reopened", "FixVersion 1"));
      
      SprintNode sprint1 = JonasUnitTestHelper.getSprintFromModel(model, 0);
      SprintNode sprint2 = JonasUnitTestHelper.getSprintFromModel(model, 1);
      sprint1.analyseToolTip();
      
      assertEquals("Sprint 1", sprint1.getSprintName());
      assertEquals(4, sprint1.getFixVersionNode("FixVersion 1").getChildCount());
      assertEquals(2, sprint1.getFixVersionNode("FixVersion 2").getChildCount());
      assertEquals(Status.Open, sprint1.getLowestStatus());
      assertEquals(1, sprint1.getCount(Status.UnKnown));
      assertEquals(1, sprint1.getCount(Status.Open));
      assertEquals(2, sprint1.getCount(Status.Reopened));
      assertEquals(1, sprint1.getCount(Status.Resolved));
      assertEquals(1, sprint1.getCount(Status.Closed));
      assertEquals("17%", sprint1.getPercentage(Status.UnKnown));
      assertEquals("17%", sprint1.getPercentage(Status.Open));
      assertEquals("33%", sprint1.getPercentage(Status.Reopened));
      assertEquals("17%", sprint1.getPercentage(Status.Resolved));
      assertEquals("17%", sprint1.getPercentage(Status.Closed));
      
      assertEquals(72000, sprint1.getEstimateTotal());
      
      sprint2.analyseToolTip();
      assertEquals(3600*7, sprint2.getEstimateTotal());
   }

   public void testShouldLowestResolutionOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      model.addJira(jiraDto1);

      SprintNode sprint = JonasUnitTestHelper.getSprintFromModel(model, 0);
      sprint.analyseToolTip();
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.UnKnown, sprint.getLowestStatus());
      
      jiraDto1.setKey("Jira 2");
      jiraDto1.setStatus("Resolved");
      model.addJira(jiraDto1);
      sprint.analyseToolTip();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.Resolved, sprint.getLowestStatus());
      
      jiraDto1.setKey("Jira 3");
      jiraDto1.setStatus("Closed");
      model.addJira(jiraDto1);
      sprint.analyseToolTip();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.Resolved, sprint.getLowestStatus());
   }
}
