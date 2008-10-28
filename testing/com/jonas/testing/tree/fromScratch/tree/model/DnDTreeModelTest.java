package com.jonas.testing.tree.fromScratch.tree.model;

import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import junit.framework.TestCase;
import com.jonas.testing.tree.fromScratch.tree.nodes.JiraNode;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;

public class DnDTreeModelTest extends TestCase {

   public void testShouldReturnJiraNodesOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto("Sprint 1", "FixVersion 2", "Jira 1");
      
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);
      
      List<JiraNode> jiraNodes = model.getJiraNodes("Jira 1");
      assertEquals(2, jiraNodes.size());
   }
   
   public void testShouldAddAndRemoveJiraOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      JiraDTO jiraDto = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");

      assertEquals(0, model.getChildCount(model.getRoot()));
      // add Jira
      model.addJira(jiraDto);

      assertEquals(1, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);

      // remove Jira
      model.removeJira("Jira 1", "FixVersion 1");

      assertEquals(1, model.getChildCount(model.getRoot()));
      spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 0);

      // add Jira again
      model.addJira(jiraDto);

      assertEquals(1, model.getChildCount(model.getRoot()));
      spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
   }
   
   public void testShouldAddJiraToAnotherSprintShouldMoveFixVersionsAsWell() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      JiraDTO jiraDto = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      
      assertEquals(0, model.getChildCount(model.getRoot()));
      // add Jira
      model.addJira(jiraDto);
      
      assertEquals(1, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
      
      // add Jira again
      jiraDto = getTestJiraDto("Sprint 2", "FixVersion 1", "Jira 1");
      model.addJira(jiraDto);
      
      assertEquals(2, model.getChildCount(model.getRoot()));
      spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      DefaultMutableTreeNode spriTwo = assertChild(model, model.getRoot(), 1, "Sprint 2", 1);
      
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 0);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriTwo, 0, "FixVersion 1", 1);
   }

   public void testShouldAddJiraWithDifferentFixVersionsOk() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      
      assertEquals(0, model.getChildCount(model.getRoot()));
      
      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      jiraDto1.addFixVersion("FixVersion 2");
      
      model.addJira(jiraDto1);
      
      assertEquals(1, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 2);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriOne, 1, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 1", 0);
      
      jiraDto1 = getTestJiraDto("Sprint 2", "FixVersion 1", "Jira 1");
      jiraDto1.addFixVersion("FixVersion 2");
      
      //adding same jira again with two fix versions to another sprint should delete the other sprint. 
      model.addJira(jiraDto1);
      
      assertEquals(2, model.getChildCount(model.getRoot()));
      spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 2);
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 0);
      fixVTwo = assertChild(model, spriOne, 1, "FixVersion 2", 0);
      
      DefaultMutableTreeNode spriTwo = assertChild(model, model.getRoot(), 1, "Sprint 2", 2);
      fixVOne = assertChild(model, spriTwo, 0, "FixVersion 1", 1);
      fixVTwo = assertChild(model, spriTwo, 1, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 1", 0);
   }
   
   public void testShouldAddDuplicateJiraToDifferentFixVersions() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      
      assertEquals(0, model.getChildCount(model.getRoot()));
      
      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto("Sprint 1", "FixVersion 2", "Jira 1");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);
      
      assertEquals(1, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 2);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriOne, 1, "FixVersion 2", 1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVTwo, 0, "Jira 1", 0);
   }
   
   public void testShouldAddDuplicateJiras() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(model.getRoot()));

      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(1, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
   }

   public void testShouldAddJirasOkWithSameFixVersion() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(model.getRoot()));

      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(1, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 2);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
      DefaultMutableTreeNode jiraTwo = assertChild(model, fixVOne, 1, "Jira 2", 0);
   }

   public void testShouldAddJirasOkWithSameSprint() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(model.getRoot()));

      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto("Sprint 1", "FixVersion 2", "Jira 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(1, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 2);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriOne, 1, "FixVersion 2", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
      DefaultMutableTreeNode jiraTwo = assertChild(model, fixVTwo, 0, "Jira 2", 0);
   }

   public void testShouldAddJirasOkWithSameRoot() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(model.getRoot()));

      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto("Sprint 2", "FixVersion 2", "Jira 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(2, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      DefaultMutableTreeNode spriTwo = assertChild(model, model.getRoot(), 1, "Sprint 2", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriTwo, 0, "FixVersion 2", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
      DefaultMutableTreeNode jiraTwo = assertChild(model, fixVTwo, 0, "Jira 2", 0);
   }
   
   public void testShouldMoveJiraWhenAddingAtADifferentLocation() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(model.getRoot()));

      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto("Sprint 2", "FixVersion 2", "Jira 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(2, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      DefaultMutableTreeNode spriTwo = assertChild(model, model.getRoot(), 1, "Sprint 2", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriTwo, 0, "FixVersion 2", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
      DefaultMutableTreeNode jiraTwo = assertChild(model, fixVTwo, 0, "Jira 2", 0);
      
      JiraDTO jiraDto3 = getTestJiraDto("Sprint 2", null, "Jira 2");
      model.addJira(jiraDto3);
      
      assertEquals(2, model.getChildCount(model.getRoot()));
      spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      spriTwo = assertChild(model, model.getRoot(), 1, "Sprint 2", 2);
      fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      fixVTwo = assertChild(model, spriTwo, 0, "FixVersion 2", 1);
      DefaultMutableTreeNode fixVTrh = assertChild(model, spriTwo, 1, "<UnKnown FixVersion>", 1);
      jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
      jiraTwo = assertChild(model, fixVTwo, 0, "Jira 2", 0);
      jiraTwo = assertChild(model, fixVTrh, 0, "Jira 2", 0);
   }

   public void testShouldAddJirasOkWithSameFixVersionButDifferentSprint() {
      DnDTreeModel model = new DnDTreeModel("LLU");

      assertEquals(0, model.getChildCount(model.getRoot()));

      JiraDTO jiraDto1 = getTestJiraDto("Sprint 1", "FixVersion 1", "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto("Sprint 2", "FixVersion 1", "Jira 2");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);

      assertEquals(2, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "Sprint 1", 1);
      DefaultMutableTreeNode spriTwo = assertChild(model, model.getRoot(), 1, "Sprint 2", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriTwo, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
      DefaultMutableTreeNode jiraTwo = assertChild(model, fixVTwo, 0, "Jira 2", 0);
   }
   
   public void testShouldAddJirasOkWithNullSprint() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      
      assertEquals(0, model.getChildCount(model.getRoot()));
      
      JiraDTO jiraDto1 = getTestJiraDto(null, "FixVersion 1", "Jira 1");
      model.addJira(jiraDto1);
      
      assertEquals(1, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "<UnKnown Sprint>", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "FixVersion 1", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
   }
   
   public void testShouldAddJirasOkWithNullSprintAndFixVersion() {
      DnDTreeModel model = new DnDTreeModel("LLU");
      
      assertEquals(0, model.getChildCount(model.getRoot()));
      
      JiraDTO jiraDto1 = getTestJiraDto(null, null, "Jira 1");
      JiraDTO jiraDto2 = getTestJiraDto(null, null, "Jira 2");
      JiraDTO jiraDto3 = getTestJiraDto(null, "fixVersion 1", "Jira 3");
      JiraDTO jiraDto4 = getTestJiraDto(null, "fixVersion 2", "Jira 4");
      JiraDTO jiraDto5 = getTestJiraDto("Sprint 1", null, "Jira 5");
      JiraDTO jiraDto6 = getTestJiraDto("Sprint 1", "fixVersion 1", "Jira 6");
      JiraDTO jiraDto7 = getTestJiraDto("Sprint 1", "fixVersion 3", "Jira 7");
      JiraDTO jiraDto8 = getTestJiraDto("Sprint 2", null, "Jira 8");
      model.addJira(jiraDto1);
      model.addJira(jiraDto2);
      model.addJira(jiraDto3);
      model.addJira(jiraDto4);
      model.addJira(jiraDto5);
      model.addJira(jiraDto6);
      model.addJira(jiraDto7);
      model.addJira(jiraDto8);
      
      assertEquals(3, model.getChildCount(model.getRoot()));
      DefaultMutableTreeNode spriOne = assertChild(model, model.getRoot(), 0, "<UnKnown Sprint>", 3);
      DefaultMutableTreeNode spriTwo = assertChild(model, model.getRoot(), 1, "Sprint 1", 3);
      DefaultMutableTreeNode spriThr = assertChild(model, model.getRoot(), 2, "Sprint 2", 1);
      DefaultMutableTreeNode fixVOne = assertChild(model, spriOne, 0, "<UnKnown FixVersion>", 2);
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriOne, 1, "fixVersion 1", 1);
      DefaultMutableTreeNode fixVThr = assertChild(model, spriOne, 2, "fixVersion 2", 1);
      DefaultMutableTreeNode fixVFor = assertChild(model, spriTwo, 0, "<UnKnown FixVersion>", 1);
      DefaultMutableTreeNode fixVFiv = assertChild(model, spriTwo, 1, "fixVersion 1", 1);
      DefaultMutableTreeNode fixVSix = assertChild(model, spriTwo, 2, "fixVersion 3", 1);
      DefaultMutableTreeNode fixVSev = assertChild(model, spriThr, 0, "<UnKnown FixVersion>",1);
      assertChild(model, fixVOne, 0, "Jira 1", 0);
      assertChild(model, fixVOne, 1, "Jira 2", 0);
      assertChild(model, fixVTwo, 0, "Jira 3", 0);
      assertChild(model, fixVThr, 0, "Jira 4", 0);
      assertChild(model, fixVFor, 0, "Jira 5", 0);
      assertChild(model, fixVFiv, 0, "Jira 6", 0);
      assertChild(model, fixVSix, 0, "Jira 7", 0);
      assertChild(model, fixVSev, 0, "Jira 8", 0);
   }

   private DefaultMutableTreeNode assertChild(DnDTreeModel model, Object parent, int childIndex, String expectedChildName, int expectedCountOfChildsChildren) {
      DefaultMutableTreeNode child;
      child = (DefaultMutableTreeNode) model.getChild(parent, childIndex);

      assertEquals("ChildName is incorrect!", expectedChildName, child.getUserObject());
      assertEquals("Count of \"" + expectedChildName + "\" Children is incorrect!", expectedCountOfChildsChildren, model.getChildCount(child));
      return child;
   }

   private JiraDTO getTestJiraDto(String sprint, String fixVersion, String jira) {
      JiraDTO jiraDto = new JiraDTO();
      jiraDto.setSprint(sprint);
      jiraDto.addFixVersion(fixVersion);
      jiraDto.setKey(jira);
      return jiraDto;
   }

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

}
