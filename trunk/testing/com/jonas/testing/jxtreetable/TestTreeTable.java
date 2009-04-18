package com.jonas.testing.jxtreetable;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import com.jonas.testHelpers.TryoutTester;
import com.jonas.testing.jxtreetable.column.BoardColumnMapperImpl;
import com.jonas.testing.jxtreetable.dao.DaoTreeBuilder;
import com.jonas.testing.jxtreetable.dao.TreeTableDaoImpl;
import com.jonas.testing.jxtreetable.userobject.DefaultUserObject;
import com.jonas.testing.jxtreetable.userobject.FixVersionUserObject;
import com.jonas.testing.jxtreetable.userobject.JiraUserObject;
import com.jonas.testing.jxtreetable.userobject.SprintUserObject;

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
      SprintUserObject sprint_12_1 = new SprintUserObject("12-1");
      SprintUserObject sprint_12_2 = new SprintUserObject("12-2");
      FixVersionUserObject fixVersion_11 = new FixVersionUserObject("LLU 11");
      FixVersionUserObject fixVersion_12 = new FixVersionUserObject("LLU 12");
      JiraUserObject jira_llu1 = new JiraUserObject("llu-123", "Description for llu-1");
      JiraUserObject jira_llu2 = new JiraUserObject("llu-23", "Description for llu-2");

      DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("root");
      DefaultMutableTreeTableNode sprintOne = addChildToParent(root, sprint_12_1);
      DefaultMutableTreeTableNode sprintTwo = addChildToParent(root, sprint_12_2);
      DefaultMutableTreeTableNode fixVersionOne = addChildToParent(sprintOne, fixVersion_11);
      DefaultMutableTreeTableNode fixVersionTwo = addChildToParent(sprintOne, fixVersion_12);;
      DefaultMutableTreeTableNode fixVersionTwoDupe = addChildToParent(sprintTwo, fixVersion_12);;
      addChildToParent(fixVersionOne, jira_llu1);
      addChildToParent(fixVersionTwo, jira_llu2);
      addChildToParent(fixVersionTwoDupe, jira_llu2);

      DefaultTreeTableModel treeTableModel = new JiraTreeTableModel(root, BoardColumnMapperImpl.boardColumnMapping);
      Component treeTable = getTreeTable(treeTableModel);
      TryoutTester.showInFrame(new JScrollPane(treeTable));

      TreeTableDaoImpl treeTableDao = new TreeTableDaoImpl();
      DaoTreeBuilder treeBuilder = new DaoTreeBuilder();
      treeTableDao.persist(new File("testTreeTableDao"), treeTableModel, treeBuilder);

      DefaultMutableTreeTableNode root2 = treeTableDao.read(new File("testTreeTableDao"), treeBuilder);
      JiraTreeTableModel model = new JiraTreeTableModel(root2, BoardColumnMapperImpl.boardColumnMapping);

      Component treeTable2 = getTreeTable(model);

      TryoutTester.showInFrame(new JScrollPane(treeTable2));
      /*
       * TODO versioning: Allow the loader to 1) See what version of the loaded xml 2) Apply (in sequence) all rollforwards to get the file into
       * the latest state!
       */

   }

   private Component getTreeTable(DefaultTreeTableModel treeTableModel) {
      JXTreeTable treeTable = new JXTreeTable(treeTableModel);
      treeTable.setFillsViewportHeight(true);
      treeTable.setColumnControlVisible(true);
      return treeTable;
   }

   private DefaultMutableTreeTableNode addChildToParent(DefaultMutableTreeTableNode parentNode, DefaultUserObject child) {
      DefaultMutableTreeTableNode childNode = new DefaultMutableTreeTableNode(child);
      parentNode.add(childNode);
      return childNode;
   }
}
