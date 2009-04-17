package com.jonas.testing.jxtreetable;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import com.jonas.testHelpers.TryoutTester;
import com.jonas.testing.jxtreetable.userobject.FixVersion;
import com.jonas.testing.jxtreetable.userobject.Jira;
import com.jonas.testing.jxtreetable.userobject.Sprint;

public class TestTreeTable {
   private JXTreeTable treeTable;

   public TestTreeTable() {
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
         e.printStackTrace();
      }
      TestTreeTable test = new TestTreeTable();
      test.create();
   }

   /**
    * creates the demo on the event dispatch thread.
    * 
    */
   public void create() {
      DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("root");
      DefaultMutableTreeTableNode sprintOne = new DefaultMutableTreeTableNode(new Sprint("12-1"));
      DefaultMutableTreeTableNode sprintTwo = new DefaultMutableTreeTableNode(new Sprint("12-2"));
      DefaultMutableTreeTableNode fixVersionOne = new DefaultMutableTreeTableNode(new FixVersion("LLU 11"));
      DefaultMutableTreeTableNode fixVersionTwo = new DefaultMutableTreeTableNode(new FixVersion("LLU 12"));
      DefaultMutableTreeTableNode jiraOne = new DefaultMutableTreeTableNode(new Jira("llu-1", "Description for llu-1"));
      DefaultMutableTreeTableNode jiraTwo = new DefaultMutableTreeTableNode(new Jira("llu-2", "Description for llu-2"));

      addChildrenToParent(root, sprintOne);
      addChildrenToParent(root, sprintTwo);
      addChildrenToParent(sprintOne, fixVersionOne, fixVersionTwo);
      addChildrenToParent(fixVersionOne, jiraOne, jiraTwo);

      DefaultTreeTableModel treeTableModel = new JiraTreeTableModel(root, BoardColumnMapper.boardColumnMapping);
      treeTable = new JXTreeTable(treeTableModel);
      treeTable.setFillsViewportHeight(true);
      treeTable.setColumnControlVisible(true);

      TryoutTester.showInFrame(new JScrollPane(treeTable));
   }

   private void addChildrenToParent(DefaultMutableTreeTableNode parent, DefaultMutableTreeTableNode... children) {
      for (DefaultMutableTreeTableNode child : children) {
         parent.add(child);
      }
   }
}

