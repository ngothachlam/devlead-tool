package com.jonas.agile.devleadtool.component.tree.model;

import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import junit.framework.TestCase;
import com.jonas.agile.devleadtool.gui.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.gui.component.tree.xml.JiraDTO;
import com.jonas.agile.devleadtool.junitutils.JonasUnitTestHelper;

public class DnDTreeModelFunctionalUnitTest extends TestCase {

   private DefaultMutableTreeNode assertChild(DnDTreeModel model, DefaultMutableTreeNode parent, int childIndex, String expectedChildName,
         int expectedCountOfChildsChildren) {
      int childCount = model.getChildCount(parent);
      assertTrue("Cannot get childIdex " + childIndex + " from parent " + parent.getUserObject() + " as the number of children is " + childCount, childIndex < model.getChildCount(parent));
      DefaultMutableTreeNode child = (DefaultMutableTreeNode) model.getChild(parent, childIndex);
      assertEquals("ChildName is incorrect!", expectedChildName, child.getUserObject());
      assertEquals("Count of \"" + expectedChildName + "\" Children is incorrect!", expectedCountOfChildsChildren, model.getChildCount(child));
      return child;
   }

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldAddAndRemoveJiraOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 2");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      // add Jira
      model.addJira(jiraDto1);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);

      // remove Jira
      model.removeJira("Jira 1");

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 0);

      // add Jira again
      model.addJira(jiraDto1);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
   }

   public void testShouldAddDuplicateJiras() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
   }

   public void testShouldAddDuplicateJiraToDifferentFixVersions() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1", "FixVersion 2");
      model.addJira(jiraDto1);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 2);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriOne, 1, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 1", 0);
   }

   public void testShouldAddJirasOkWithNullFixVersion() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      
      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null);
      model.addJira(jiraDto1);
      
      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVersionNode = assertChild(model, sprintNode, 0, "<UnKnown FixVersion>", 1);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
      
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 2", null, null);
      model.addJira(jiraDto2);
      
      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      fixVersionNode = assertChild(model, sprintNode, 0, "<UnKnown FixVersion>", 2);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
      assertChild(model, fixVersionNode, 1, "Jira 2", 0);
      
      //Move jira 2 from unknown fix version in sprint 1 to the same fix in sprint 2
      jiraDto2.setSprint("Sprint 2");
      model.addJira(jiraDto2);
      
      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      fixVersionNode = assertChild(model, sprintNode, 0, "<UnKnown FixVersion>", 1);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 1);
      fixVersionNode = assertChild(model, sprintNode, 0, "<UnKnown FixVersion>", 1);
      assertChild(model, fixVersionNode, 0, "Jira 2", 0);
   }
   
   public void testShouldAddJirasOkWithNullSprint() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto(null, "Jira 1", null, null, "FixVersion 1");
      model.addJira(jiraDto1);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "<UnKnown Sprint>", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto(null, "Jira 2", null, null, "FixVersion 2");
      model.addJira(jiraDto2);
      
      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "<UnKnown Sprint>", 2);
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      fixVOne = assertChild(model, spriOne, 1, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 2", 0);
   }


   public void testShouldAddJirasOkWithNullSprintAndFixVersion() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto(null, "Jira 1", null, null);
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto(null, "Jira 2", null, null);
      JiraDTO jiraDto3 = JonasUnitTestHelper.getTestJiraDto(null, "Jira 3", null, null, "fixVersion 1");
      JiraDTO jiraDto4 = JonasUnitTestHelper.getTestJiraDto(null, "Jira 4", null, null, "fixVersion 2");
      JiraDTO jiraDto5 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 5", null, null);
      JiraDTO jiraDto6 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 6", null, null, "fixVersion 1");
      JiraDTO jiraDto7 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 7", null, null, "fixVersion 3");
      JiraDTO jiraDto8 = JonasUnitTestHelper.getTestJiraDto("Sprint 2", "Jira 8", null, null);
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);
      model.addJira(jiraDto3);
      model.addJira(jiraDto4);
      model.addJira(jiraDto5);
      model.addJira(jiraDto6);
      model.addJira(jiraDto7);
      model.addJira(jiraDto8);

      assertEquals(3, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "<UnKnown Sprint>", 3);
      DefaultMutableTreeNode spriTwo = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 1", 3);
      DefaultMutableTreeNode spriThr = assertChild(model, JonasUnitTestHelper.getRoot(model), 2, "Sprint 2", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "<UnKnown FixVersion>", 2);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriOne, 1, "fixVersion 1", 1);
      DefaultMutableTreeNode fixVThr = assertChild(model, spriOne, 2, "fixVersion 2", 1);
      DefaultMutableTreeNode fixVFor = assertChild(model, spriTwo, 0, "<UnKnown FixVersion>", 1);
      DefaultMutableTreeNode fixVFiv = assertChild(model, spriTwo, 1, "fixVersion 1", 1);
      DefaultMutableTreeNode fixVSix = assertChild(model, spriTwo, 2, "fixVersion 3", 1);
      DefaultMutableTreeNode fixVSev = assertChild(model, spriThr, 0, "<UnKnown FixVersion>", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVOne, 1, "Jira 2", 0);
      assertChild(model, fixVTwo, 0, "Jira 3", 0);
      assertChild(model, fixVThr, 0, "Jira 4", 0);
      assertChild(model, fixVFor, 0, "Jira 5", 0);
      assertChild(model, fixVFiv, 0, "Jira 6", 0);
      assertChild(model, fixVSix, 0, "Jira 7", 0);
      assertChild(model, fixVSev, 0, "Jira 8", 0);
   }

   public void testShouldAddJirasOkWithSameFixVersion() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 2", null, null, "FixVersion 1");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 2);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVOne, 1, "Jira 2", 0);
   }
   
   public void testShouldAddJirasOkWithSameFixVersionButDifferentSprint() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 2", "Jira 2", null, null, "FixVersion 1");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode spriTwo = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriTwo, 0, "FixVersion 1", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 2", 0);
   }
   
   public void testShouldAddJirasOkWithSameRoot() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 2", "Jira 2", null, null, "FixVersion 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode spriTwo = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriTwo, 0, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 2", 0);
   }

   public void testShouldAddJirasOkWithSameSprint() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 2", null, null, "FixVersion 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 2);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriOne, 1, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 2", 0);
   }
   
   public void testShouldAddJiraToAnotherSprintShouldMoveFixVersionsAsWell() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      JiraDTO jiraDto = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      // add Jira
      model.addJira(jiraDto);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);

      // add Jira again
      jiraDto = JonasUnitTestHelper.getTestJiraDto("Sprint 2", "Jira 1", null, null, "FixVersion 1");
      model.addJira(jiraDto);

      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode spriTwo = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 1);

      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 0);
      assertChild(model, spriTwo, 0, "FixVersion 1", 1);
   }

   public void testShouldAddJiraWithDifferentFixVersionsOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      jiraDto1.addFixVersion("FixVersion 2");

      model.addJira(jiraDto1);

      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 2);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriOne, 1, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 1", 0);

      jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 2", "Jira 1", null, null, "FixVersion 1");
      jiraDto1.addFixVersion("FixVersion 2");

      // adding same jira again with two fix versions to another sprint should delete the other sprint.
      model.addJira(jiraDto1);

      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      spriOne = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 2);
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 0);
      fixVTwo = assertChild(model, spriOne, 1, "FixVersion 2", 0);

      DefaultMutableTreeNode spriTwo = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 2);
      fixVOne = assertChild(model, spriTwo, 0, "FixVersion 1", 1);
      fixVTwo = assertChild(model, spriTwo, 1, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 1", 0);
   }

   public void testShouldAddSameJirasWithDifferentFixVersion() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      
      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      model.addJira(jiraDto1);
      
      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      DefaultMutableTreeNode sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 1", 1);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
      
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 2");
      model.addJira(jiraDto2);
      
      assertEquals(1, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 2);
      fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 1", 0);
      fixVersionNode = assertChild(model, sprintNode, 1, "FixVersion 2", 1);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
   }

   public void testShouldMoveJiraCorrectlyWhenAddingAtANullFixVersion() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      
      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      
      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 2", "Jira 2", null, null, "FixVersion 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);
      
      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      
      DefaultMutableTreeNode sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 1", 1);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
      
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 1);
      fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 2", 1);
      assertChild(model, fixVersionNode, 0, "Jira 2", 0);
      
      JiraDTO jiraDto3 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 2", null, null);
      model.addJira(jiraDto3);
      
      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 2);
      fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 1", 1);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
      fixVersionNode = assertChild(model, sprintNode, 1, "<UnKnown FixVersion>", 1);
      assertChild(model, fixVersionNode, 0, "Jira 2", 0);
      
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 1);
      fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 2", 0);
   }

   public void testShouldMoveJiraWhenAddingAtADifferentLocation() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(JonasUnitTestHelper.getRoot(model)));

      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 2", "Jira 2", null, null, "FixVersion 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      
      DefaultMutableTreeNode sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 1", 1);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
      
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 1);
      fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 2", 1);
      assertChild(model, fixVersionNode, 0, "Jira 2", 0);

      JiraDTO jiraDto3 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 2", null, null, "FixVersion 1");
      model.addJira(jiraDto3);

      assertEquals(2, model.getChildCount(JonasUnitTestHelper.getRoot(model)));
      
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 0, "Sprint 1", 1);
      fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 1", 2);
      assertChild(model, fixVersionNode, 0, "Jira 1", 0);
      assertChild(model, fixVersionNode, 1, "Jira 2", 0);
      
      sprintNode = assertChild(model, JonasUnitTestHelper.getRoot(model), 1, "Sprint 2", 1);
      fixVersionNode = assertChild(model, sprintNode, 0, "FixVersion 2", 0);
   }

   public void testShouldReturnJiraNodesOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      JiraDTO jiraDto1 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 1");
      JiraDTO jiraDto2 = JonasUnitTestHelper.getTestJiraDto("Sprint 1", "Jira 1", null, null, "FixVersion 2");

      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      List<JiraNode> jiraNodes = model.getJiraNodes("Jira 1");
      assertEquals(1, jiraNodes.size());
   }

}
