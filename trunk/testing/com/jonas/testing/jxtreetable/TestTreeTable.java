package com.jonas.testing.jxtreetable;

import java.awt.Component;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.apache.tools.ant.types.resources.First;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import com.jonas.testHelpers.TryoutTester;
import com.jonas.testing.jxtreetable.column.BoardColumnMapperImpl;
import com.jonas.testing.jxtreetable.userobject.DefaultUserObject;
import com.jonas.testing.jxtreetable.userobject.FixVersionUserObject;
import com.jonas.testing.jxtreetable.userobject.RequirementUserObject;
import com.jonas.testing.jxtreetable.userobject.SprintUserObject;
import com.jonas.testing.jxtreetable.userobject.StoryUserObject;

public class TestTreeTable {

   public TestTreeTable() {
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
         e.printStackTrace();
      }
      TestTreeTable test = new TestTreeTable();
      try {
         test.create();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * creates the demo on the event dispatch thread.
    * 
    * @throws IOException
    * 
    */
   public void create() throws IOException {
      DefaultMutableTreeTableNode root = createTestRoot();

      DefaultTreeTableModel treeTableModel = new TestTreeTableModel(root, BoardColumnMapperImpl.boardColumnMapping);
      Component treeTable = getTreeTable(treeTableModel);
      TryoutTester.showInFrame(new JScrollPane(treeTable));

//      TreeTableDaoImpl treeTableDao = new TreeTableDaoImpl();
//      DaoTreeBuilder treeBuilder = new DaoTreeBuilder();
//      treeTableDao.persist(new File("testTreeTableDao"), treeTableModel, treeBuilder);
//
//      DefaultMutableTreeTableNode root2 = treeTableDao.read(new File("testTreeTableDao"), treeBuilder);
//      JiraTreeTableModel model = new JiraTreeTableModel(root2, BoardColumnMapperImpl.boardColumnMapping);
//
//      Component treeTable2 = getTreeTable(model);
//
//      TryoutTester.showInFrame(new JScrollPane(treeTable2));
      /*
       * TODO versioning: Allow the loader to 1) See what version of the loaded xml 2) Apply (in sequence) all rollforwards to get the file into
       * the latest state!
       */

   }

   private DefaultMutableTreeTableNode createTestRoot() {
      DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("root");
      
      FixVersionUserObject fixVersion_11 = new FixVersionUserObject("LLU 11", root);
      FixVersionUserObject fixVersion_12 = new FixVersionUserObject("LLU 12", root);
      
      SprintUserObject sprint_12_1 = new SprintUserObject("12-1", fixVersion_11);
      SprintUserObject sprint_12_2 = new SprintUserObject("12-2", fixVersion_12);
      
      RequirementUserObject req1 = new RequirementUserObject("Requirement 1", sprint_12_1);
      RequirementUserObject req2 = new RequirementUserObject("Requirement 2", sprint_12_2);
      
      StoryUserObject jira_llu1 = new StoryUserObject("llu-123", "Description for llu-1", req1);
      StoryUserObject jira_llu2 = new StoryUserObject("llu-23", "Description for llu-2", req2);

//      DefaultMutableTreeTableNode fixVersionOne = addChildToParent(root, fixVersion_11);
//      DefaultMutableTreeTableNode fixVersionTwo = addChildToParent(root, fixVersion_12);;
//      
//      DefaultMutableTreeTableNode sprintOne = addChildToParent(fixVersionOne, sprint_12_1);
//      DefaultMutableTreeTableNode sprintTwo = addChildToParent(fixVersionTwo, sprint_12_2);
//      DefaultMutableTreeTableNode sprintTwoDupe = addChildToParent(fixVersionTwo, sprint_12_1);
//
//      DefaultMutableTreeTableNode requirementOne = addChildToParent(sprintOne, req1);
//      DefaultMutableTreeTableNode requirementTwo = addChildToParent(sprintTwo, req2);
//      DefaultMutableTreeTableNode requirementTwoDupe = addChildToParent(sprintTwoDupe, req1);
//      
//      addChildToParent(requirementOne, jira_llu1);
//      addChildToParent(requirementTwo, jira_llu2);
//      addChildToParent(requirementTwoDupe, jira_llu1);
      
      return root;
   }

   private Component getTreeTable(DefaultTreeTableModel treeTableModel) {
      JXTreeTable treeTable = new JXTreeTable(treeTableModel);
      treeTable.setFillsViewportHeight(true);
      treeTable.setColumnControlVisible(true);
      treeTable.setSortable(true);
      
//      need to find a way to fireTableDataChanged when a userobject is being updated!
      
//      treeTable.getTreeTableModel()
      
      return treeTable;
   }

//   private DefaultMutableTreeTableNode addChildToParent(DefaultMutableTreeTableNode parentNode, DefaultUserObject child) {
//      DefaultMutableTreeTableNode childNode = new DefaultMutableTreeTableNode(child);
//      parentNode.add(childNode);
//      return childNode;
//   }
}
