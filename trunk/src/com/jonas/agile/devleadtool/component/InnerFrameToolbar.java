package com.jonas.agile.devleadtool.component;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;
import com.jonas.agile.devleadtool.component.Action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.component.dialog.AddBoardReconcileDialog;
import com.jonas.agile.devleadtool.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.frame.BoardStatsFrame;
import com.jonas.agile.devleadtool.component.panel.BoardPanel;
import com.jonas.agile.devleadtool.component.panel.DnDTreePanel;
import com.jonas.agile.devleadtool.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.DateHelper;

public class InnerFrameToolbar extends JToolBar {

   public InnerFrameToolbar(final Frame parentFrame, BoardPanel boardPanel, JiraPanel jiraPanel, DnDTreePanel sprintPanel, MyTable boardTable,
         final MyTable jiraTable) {
      FreezeManipulationAction a = new FreezeManipulationAction(parentFrame);
      a.setPanels(boardPanel, jiraPanel, sprintPanel);
      this.add(new JCheckBox(a));

      this.addSeparator();
      final MyTable[] tables = { boardTable, jiraTable };

      this.add(new JButton(new ReconcileManuallyAction(parentFrame, jiraTable)));
      this.add(new JButton(new AddManuallyAction(parentFrame, tables)));
      this.add(new JButton(new AddFromJiraFilterAction(parentFrame, tables)));
      this.add(new JButton(new AddFromJiraVersionAction(parentFrame, tables)));
      this.addSeparator();
      this.add(new JButton(new CheckForDuplicatesAction("Duplicates?", "Higlight Duplicates in Board", parentFrame, boardTable)));
      this.addSeparator();
      this.add(new JButton(new BoardStatsAction("Board Stats", "Showing Board Statistics", parentFrame, boardTable)));
   }
}


final class FreezeManipulationAction extends BasicAbstractGUIAction {

   private BoardPanel boardPanel;
   private JiraPanel jiraPanel;
   private DnDTreePanel sprintPanel;

   FreezeManipulationAction(Frame parentFrame) {
      super("Freeze", "Freezes the manipulation of data", parentFrame);
   }

   public void setPanels(BoardPanel boardPanel, JiraPanel jiraPanel, DnDTreePanel sprintPanel) {
      this.boardPanel = boardPanel;
      this.jiraPanel = jiraPanel;
      this.sprintPanel = sprintPanel;
   }

   @Override
   public void doActionPerformed(ActionEvent evt) {
      JCheckBox cb = (JCheckBox) evt.getSource();

      boolean isSel = !cb.isSelected();
      boardPanel.setEditable(isSel);
      sprintPanel.setEditable(isSel);
      jiraPanel.setEditable(isSel);
   }
}


final class CheckForDuplicatesAction extends BasicAbstractGUIAction {
   private final MyTable sourceTable;

   CheckForDuplicatesAction(String name, String description, Frame parentFrame, MyTable sourceTable) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      Set<String> duplicateJiras = findAnyDuplicateJirasInTable(sourceTable);
      presentTheDuplicateJiras(duplicateJiras);
   }

   private Set<String> findAnyDuplicateJirasInTable(final MyTable boardTable) {
      int rows = boardTable.getRowCount();
      Set<String> duplicateJirasInTable = new HashSet<String>();
      for (int row = 0; row < rows; row++) {
         String jira = (String) boardTable.getValueAt(Column.Jira, row);
         if (!duplicateJirasInTable.contains(jira)) {
            for (int compareRow = row + 1; compareRow < rows; compareRow++) {
               String compareJira = (String) boardTable.getValueAt(Column.Jira, compareRow);
               if (jira.equalsIgnoreCase(compareJira)) {
                  duplicateJirasInTable.add(jira);
               }
            }
         }
      }
      return duplicateJirasInTable;
   }

   private void presentTheDuplicateJiras(Set<String> duplicateJiras) {
      StringBuffer sb = new StringBuffer();
      for (String string : duplicateJiras) {
         sb.append(string).append(" ");
      }

      AlertDialog.alertMessage(getParentFrame(), sb.toString());
   }
}


final class BoardStatsAction extends BasicAbstractGUIAction {
   private final MyTable sourceTable;

   BoardStatsAction(String name, String description, Frame parentFrame, MyTable sourceTable) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      BoardStatsFrame boardStatsFrame = new BoardStatsFrame(getParentFrame(), 600, 500, sourceTable, new DateHelper());
      boardStatsFrame.setVisible(true);
   }

}


final class ReconcileManuallyAction extends BasicAbstractGUIAction {
   private final MyTable jiraTable;

   public ReconcileManuallyAction(Frame parentFrame, MyTable jiraTable) {
      super("Reconcile", "Reconcile with the real visual board and the virtual Sprinter Board", parentFrame);
      this.jiraTable = jiraTable;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      new AddBoardReconcileDialog(getParentFrame(), jiraTable);
   }
}


abstract class BasicAbstractGUIActionWithTables extends BasicAbstractGUIAction {
   private final MyTable[] tables;

   public BasicAbstractGUIActionWithTables(String name, String desc, Frame parentFrame, MyTable... tables) {
      super(name, desc, parentFrame);
      this.tables = tables;
   }

   public MyTable[] getTables() {
      return tables;
   }

}


final class AddManuallyAction extends BasicAbstractGUIActionWithTables {

   public AddManuallyAction(Frame parentFrame, MyTable... tables) {
      super("Add", "Add jiras manually", parentFrame, tables);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      new AddManualDialog(getParentFrame(), getTables());
   }

}


final class AddFromJiraFilterAction extends BasicAbstractGUIActionWithTables {
   public AddFromJiraFilterAction(Frame parentFrame, MyTable... tables) {
      super("Filter", "Use predefined jira filters to add jiras to Sprinter", parentFrame, tables);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      new AddFilterDialog(getParentFrame(), getTables());
   }
}


final class AddFromJiraVersionAction extends BasicAbstractGUIActionWithTables {
   public AddFromJiraVersionAction(Frame parentFrame, MyTable[] tables) {
      super("Versions", "", parentFrame, tables);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      new AddVersionDialog(getParentFrame(), getTables());
   }
}