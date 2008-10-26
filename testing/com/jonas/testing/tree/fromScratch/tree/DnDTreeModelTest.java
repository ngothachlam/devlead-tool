package com.jonas.testing.tree.fromScratch.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import junit.framework.TestCase;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;

public class DnDTreeModelTest extends TestCase {

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
      model.removeJira("Jira 1");

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
   
   public void testShouldAddJirasOkFromDTOs() {
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
      DefaultMutableTreeNode fixVTwo = assertChild(model, spriTwo, 1, "FixVersion 1", 1);
      DefaultMutableTreeNode jiraOne = assertChild(model, fixVOne, 0, "Jira 1", 0);
      DefaultMutableTreeNode jiraTwo = assertChild(model, fixVTwo, 0, "Jira 2", 0);
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
      jiraDto.setFixVersion(fixVersion);
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
