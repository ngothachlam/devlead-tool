package com.jonas.testing.jxtreetable;

import java.io.File;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import com.jonas.testHelpers.TryoutTester;
import com.jonas.testing.jxtreetable.dao.TreeTableDao;
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
      try {
         test.create();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * creates the demo on the event dispatch thread.
    * @throws IOException 
    * 
    */
   public void create() throws IOException {
      DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("root");
      DefaultMutableTreeTableNode sprintOne = new DefaultMutableTreeTableNode(new Sprint("12-1"));
      DefaultMutableTreeTableNode sprintTwo = new DefaultMutableTreeTableNode(new Sprint("12-2"));
      DefaultMutableTreeTableNode fixVersionOne = new DefaultMutableTreeTableNode(new FixVersion("LLU 11"));
      DefaultMutableTreeTableNode fixVersionTwo = new DefaultMutableTreeTableNode(new FixVersion("LLU 12"));
      DefaultMutableTreeTableNode fixVersionTwoDupe = new DefaultMutableTreeTableNode(new FixVersion("LLU 12"));
      DefaultMutableTreeTableNode jiraOne = new DefaultMutableTreeTableNode(new Jira("llu-1", "Description for llu-1"));
      DefaultMutableTreeTableNode jiraTwo = new DefaultMutableTreeTableNode(new Jira("llu-2", "Description for llu-2"));
      DefaultMutableTreeTableNode jiraTwoDupe = new DefaultMutableTreeTableNode(new Jira("llu-2", "Description for llu-2"));

      addChildrenToParent(root, sprintOne);
      addChildrenToParent(root, sprintTwo);
      addChildrenToParent(sprintOne, fixVersionOne);
      addChildrenToParent(sprintOne, fixVersionTwo);
      addChildrenToParent(sprintTwo, fixVersionTwoDupe);
      addChildrenToParent(fixVersionOne, jiraOne);
      addChildrenToParent(fixVersionTwo, jiraTwo);
      addChildrenToParent(fixVersionTwoDupe, jiraTwoDupe);

      DefaultTreeTableModel treeTableModel = new JiraTreeTableModel(root, BoardColumnMapper.boardColumnMapping);
      treeTable = new JXTreeTable(treeTableModel);
      treeTable.setFillsViewportHeight(true);
      treeTable.setColumnControlVisible(true);

      TryoutTester.showInFrame(new JScrollPane(treeTable));
      
      TreeTableDao treeTableDao = new TreeTableDao();
      treeTableDao.persist(new File("testTreeTableDao"), treeTableModel);
      
   }

   private void addChildrenToParent(DefaultMutableTreeTableNode parent, DefaultMutableTreeTableNode... children) {
      for (DefaultMutableTreeTableNode child : children) {
         parent.add(child);
      }
   }
}

