package com.jonas.testing.jxtreetable;

import java.awt.Component;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import com.jonas.testHelpers.TryoutTester;
import com.jonas.testing.jxtreetable.column.BoardColumnMapperImpl;
import com.jonas.testing.jxtreetable.userobject.VersionUserObject;
import com.jonas.testing.jxtreetable.userobject.SprintUserObject;
import com.jonas.testing.jxtreetable.userobject.StoryUserObject;
import com.jonas.testing.jxtreetable.userobject.TaskUserObject;

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

      // TreeTableDaoImpl treeTableDao = new TreeTableDaoImpl();
      // DaoTreeBuilder treeBuilder = new DaoTreeBuilder();
      // treeTableDao.persist(new File("testTreeTableDao"), treeTableModel, treeBuilder);
      //
      // DefaultMutableTreeTableNode root2 = treeTableDao.read(new File("testTreeTableDao"), treeBuilder);
      // JiraTreeTableModel model = new JiraTreeTableModel(root2, BoardColumnMapperImpl.boardColumnMapping);
      //
      // Component treeTable2 = getTreeTable(model);
      //
      // TryoutTester.showInFrame(new JScrollPane(treeTable2));
      /*
       * TODO versioning: Allow the loader to 1) See what version of the loaded xml 2) Apply (in sequence) all rollforwards to get the file into the latest
       * state!
       */
   }

   private DefaultMutableTreeTableNode createTestRoot() {
      DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("root");

      VersionUserObject fixVersion_11 = new VersionUserObject("LLU 11", root);
      VersionUserObject fixVersion_12 = new VersionUserObject("LLU 12", root);

      SprintUserObject sprint_12_1 = new SprintUserObject("12-1", fixVersion_11);
      SprintUserObject sprint_12_2 = new SprintUserObject("12-2", fixVersion_12);

      StoryUserObject req1 = new StoryUserObject("llu-1", "Add a database table for blah blah", sprint_12_1);
      StoryUserObject req2 = new StoryUserObject("llu-2", "New report for blah blah", sprint_12_2);
      StoryUserObject req3 = new StoryUserObject("llu-3", "Spike test of Terracotta blah blah", sprint_12_2);

      TaskUserObject jira_llu1 = new TaskUserObject("Dev", "Development for llu-1", req1);
      TaskUserObject jira_llu2 = new TaskUserObject("QA", "QA for llu-2", req2);

      return root;
   }

   private Component getTreeTable(DefaultTreeTableModel treeTableModel) {
      JXTreeTable treeTable = new JXTreeTable(treeTableModel);
      treeTable.setFillsViewportHeight(true);
      treeTable.setColumnControlVisible(true);
      treeTable.setSortable(true);

      // need to find a way to fireTableDataChanged when a userobject is being updated!

      // treeTable.getTreeTableModel()

      return treeTable;
   }

   // private DefaultMutableTreeTableNode addChildToParent(DefaultMutableTreeTableNode parentNode, DefaultUserObject child) {
   // DefaultMutableTreeTableNode childNode = new DefaultMutableTreeTableNode(child);
   // parentNode.add(childNode);
   // return childNode;
   // }
}
