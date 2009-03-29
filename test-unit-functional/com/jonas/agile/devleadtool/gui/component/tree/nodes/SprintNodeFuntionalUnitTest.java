package com.jonas.agile.devleadtool.gui.component.tree.nodes;

import com.jonas.agile.devleadtool.gui.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.Status;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.SprintNode.SprintAnalyser;
import com.jonas.agile.devleadtool.gui.component.tree.xml.JiraDTO;
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
      SprintAnalyser analysis = sprint.analyseData();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.UnKnown, analysis.getLowestStatus());
      assertEquals(0, analysis.getCount(Status.Open));
      assertEquals("0%", analysis.getPercentage(Status.Open));
      
      jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", 28800, "Open", "FixVersion 1");
      model.addJira(jiraDto1);
      sprint.analyseData();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.Open, analysis.getLowestStatus());
      assertEquals(1, analysis.getCount(Status.Open));
      assertEquals("100%", analysis.getPercentage(Status.Open));
      
      jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 2", 28800, "Resolved", "FixVersion 1");
      model.addJira(jiraDto1);
      
      jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 3", 28800, "Reopened", "FixVersion 2");
      model.addJira(jiraDto1);
      sprint.analyseData();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(2, sprint.getFixVersionNode("FixVersion 1").getChildCount());
      assertEquals(1, sprint.getFixVersionNode("FixVersion 2").getChildCount());
      assertEquals(Status.Open, analysis.getLowestStatus());
      assertEquals(1, analysis.getCount(Status.Open));
      assertEquals(1, analysis.getCount(Status.Reopened));
      assertEquals(1, analysis.getCount(Status.Resolved));
      assertEquals("33%", analysis.getPercentage(Status.Open));
      assertEquals("33%", analysis.getPercentage(Status.Reopened));
      assertEquals("33%", analysis.getPercentage(Status.Resolved));
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
      SprintAnalyser analysis = sprint1.analyseData();
      
      assertEquals("Sprint 1", sprint1.getSprintName());
      assertEquals(4, sprint1.getFixVersionNode("FixVersion 1").getChildCount());
      assertEquals(2, sprint1.getFixVersionNode("FixVersion 2").getChildCount());
      assertEquals(Status.Open, analysis.getLowestStatus());
      assertEquals(1, analysis.getCount(Status.UnKnown));
      assertEquals(1, analysis.getCount(Status.Open));
      assertEquals(2, analysis.getCount(Status.Reopened));
      assertEquals(1, analysis.getCount(Status.Resolved));
      assertEquals(1, analysis.getCount(Status.Closed));
      assertEquals("17%", analysis.getPercentage(Status.UnKnown));
      assertEquals("17%", analysis.getPercentage(Status.Open));
      assertEquals("33%", analysis.getPercentage(Status.Reopened));
      assertEquals("17%", analysis.getPercentage(Status.Resolved));
      assertEquals("17%", analysis.getPercentage(Status.Closed));
      
      assertEquals(72000, analysis.getEstimateTotal());
      
      analysis = sprint2.analyseData();
      assertEquals(3600*7, analysis.getEstimateTotal());
   }

   public void testShouldLowestResolutionOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      model.addJira(jiraDto1);

      SprintNode sprint = JonasUnitTestHelper.getSprintFromModel(model, 0);
      SprintAnalyser analysis = sprint.analyseData();
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.UnKnown, analysis.getLowestStatus());
      
      jiraDto1.setKey("Jira 2");
      jiraDto1.setStatus("Resolved");
      model.addJira(jiraDto1);
      sprint.analyseData();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.Resolved, analysis.getLowestStatus());
      
      jiraDto1.setKey("Jira 3");
      jiraDto1.setStatus("Closed");
      model.addJira(jiraDto1);
      sprint.analyseData();
      
      assertEquals("Sprint 1", sprint.getSprintName());
      assertEquals(Status.Resolved, analysis .getLowestStatus());
   }
}
