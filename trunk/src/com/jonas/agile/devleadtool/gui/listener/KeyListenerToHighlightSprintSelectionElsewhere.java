package com.jonas.agile.devleadtool.gui.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.tree.SprintTree;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.JiraNode;
import com.jonas.common.logging.MyLogger;

public final class KeyListenerToHighlightSprintSelectionElsewhere extends KeyAdapter {
   private SprintTree sprintTree;
   private final MyTable sourceTable;
   private final MyTable[] tablesToHighlight;
   private boolean pressed = false;
   private ListSelectionModel sourceTableSelectionModel;
   private final static Logger log = MyLogger.getLogger(KeyListenerToHighlightSprintSelectionElsewhere.class);

   public KeyListenerToHighlightSprintSelectionElsewhere(MyTable sourceTable, MyTable... tablesToHighlight) {
      this.sourceTable = sourceTable;
      this.tablesToHighlight = tablesToHighlight;
      sourceTableSelectionModel = sourceTable.getSelectionModel();
      
   }

   public KeyListenerToHighlightSprintSelectionElsewhere(SprintTree sprintTree, MyTable sourceTable, MyTable... tablesToHighlight) {
      this(sourceTable, tablesToHighlight);
      this.sprintTree = sprintTree;
   }

   @Override
   public void keyPressed(KeyEvent e) {
      // ctrl-f for finding in the other tables!
      if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_EQUALS && !pressed) {
         pressed = true;
         log.debug("KeyPressed Source: " + e.getSource());
         sourceTableSelectionModel.setValueIsAdjusting(true);
         for (MyTable tableToHighlight : tablesToHighlight) {
            tableToHighlight.getSelectionModel().setValueIsAdjusting(true);
            tableToHighlight.clearSelection();
         }
         if (e.getSource() instanceof SprintTree)
            setSelectionToOthersIfSourceisTree();
         else if (e.getSource() instanceof MyTable)
            setSelectionToOthersIfSourceisTable();
         for (MyTable tableToHighlight : tablesToHighlight) {
            tableToHighlight.scrollToSelection();
            tableToHighlight.getSelectionModel().setValueIsAdjusting(false);
         }
         sourceTableSelectionModel.setValueIsAdjusting(false);
      }
   }

   private void setSelectionToOthersIfSourceisTable() {
      clearSprintTreeSelection();
      if (!sourceTableSelectionModel.isSelectionEmpty()) {
         for (int i = sourceTableSelectionModel.getMinSelectionIndex(); i <= sourceTableSelectionModel.getMaxSelectionIndex(); i++) {
            if (sourceTableSelectionModel.isSelectedIndex(i)) {
               String jira = (String) sourceTable.getValueAt(Column.Jira, i);
               log.debug("Jira: " + jira);
               for (MyTable tableToHighlight : tablesToHighlight) {
                  tableToHighlight.addSelection(jira);
               }
               addSprintTreeSelection(jira);
            }
         }
         scrollSprintTreeSelection();
      }
   }

   private void scrollSprintTreeSelection() {
      if (sprintTree != null)
         sprintTree.scrollToSelection();
   }

   private void addSprintTreeSelection(String jira) {
      if (sprintTree != null)
         sprintTree.addSelection(jira);
   }

   private void clearSprintTreeSelection() {
      if (sprintTree != null)
         sprintTree.clearSelection();
   }

   private void setSelectionToOthersIfSourceisTree() {
      sourceTable.clearSelection();
      TreePath[] jiraTreePaths = sprintTree.getSelectionPaths();
      StringBuffer boardSb = new StringBuffer("");
      StringBuffer jiraSb = new StringBuffer("");
      for (int i = 0; i < jiraTreePaths.length; i++) {
         TreePath jiraTreePath = jiraTreePaths[i];
         Object lastPathComponent = jiraTreePath.getLastPathComponent();
         if (lastPathComponent instanceof JiraNode) {
            JiraNode jiraNode = (JiraNode) lastPathComponent;
            sprintTree.addSelection(jiraNode.getKey());
            if (!sourceTable.addSelection(jiraNode.getKey()))
               boardSb.append(jiraNode.getKey()).append(", ");
            for (MyTable tableToHighlight : tablesToHighlight) {
               if (!tableToHighlight.addSelection(jiraNode.getKey()))
                  jiraSb.append(jiraNode.getKey()).append(", ");
            }
            
         }
      }
      if (jiraTreePaths.length == 1)
         sprintTree.setLeadSelectionPath(jiraTreePaths[0]);
      sourceTable.scrollToSelection();
      scrollSprintTreeSelection();
      boardSb.append(" not found on 'Board' and ").append(jiraSb).append(" not found on 'Jira'");
      MyStatusBar.getInstance().setMessage(boardSb.toString(), true);
   }

   @Override
   public void keyReleased(KeyEvent e) {
      if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK || e.getKeyCode() == KeyEvent.VK_F) {
         pressed = false;
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }
}