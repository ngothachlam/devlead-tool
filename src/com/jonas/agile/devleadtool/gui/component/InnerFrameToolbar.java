package com.jonas.agile.devleadtool.gui.component;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.action.HighlightIssuesAction;
import com.jonas.agile.devleadtool.gui.action.SprintManagerGuiAction;
import com.jonas.agile.devleadtool.gui.component.dialog.AddBoardReconcileFrame;
import com.jonas.agile.devleadtool.gui.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.frame.BoardStatsFrame;
import com.jonas.agile.devleadtool.gui.component.panel.DnDTreePanel;
import com.jonas.agile.devleadtool.gui.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.gui.component.panel.MyDataPanel;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.common.DateHelper;

public class InnerFrameToolbar extends JToolBar {

   public InnerFrameToolbar(final Frame parentFrame, MyDataPanel boardPanel, JiraPanel jiraPanel, DnDTreePanel sprintPanel, MyTable boardTable,
         final MyTable jiraTable, PlannerHelper helper, ExcelSprintDao sprintDao) {

      final MyTable[] tables = { boardTable, jiraTable };

      BasicAbstractGUIAction highlightAction = new HighlightIssuesAction("Higlight Issues", helper.getParentFrame(), jiraPanel.getTable(), boardPanel.getTable());
      BasicAbstractGUIAction freezeAction = new FreezeManipulationAction(parentFrame, boardPanel, jiraPanel, sprintPanel);
      BasicAbstractGUIAction dupeAction = new CheckForDuplicatesAction("Identify Duplicates", "Higlight Duplicates in Board", parentFrame,
            boardTable);
      BasicAbstractGUIAction reconcileAction = new ReconcileManuallyAction(parentFrame, boardTable);
      BasicAbstractGUIAction addManualAction = new AddManuallyAction(parentFrame, tables);
      BasicAbstractGUIAction addFilterAction = new AddFromJiraFilterAction(parentFrame, tables);
      BasicAbstractGUIAction addVersionAction = new AddFromJiraVersionAction(parentFrame, tables);
      
      BasicAbstractGUIAction sprintManager = new SprintManagerGuiAction(parentFrame, helper, sprintDao);
      BasicAbstractGUIAction boardStats = new BurndownAction("Calculate Burndown", "Showing Board Statistics", parentFrame, boardTable, helper);

      JMenuBar comp = new JMenuBar();
      comp.add(getDataModificationMenu("Data Management", reconcileAction, null, addManualAction, addVersionAction, addFilterAction, null, highlightAction, dupeAction, freezeAction));
      comp.add(getDataModificationMenu("Sprint", sprintManager, boardStats));
      this.add(comp);

   }

   private JMenu getDataModificationMenu(String menuTitle, BasicAbstractGUIAction... actions) {
      JMenu menu = new JMenu(menuTitle);
      for (BasicAbstractGUIAction action : actions) {
         if (action == null) {
            menu.addSeparator();
         } else if (action.isCheckBoxAction()) {
            menu.add(new JCheckBoxMenuItem(action));
         } else {
            menu.add(new JMenuItem(action));
         } 
      }
      return menu;
   }
}


final class FreezeManipulationAction extends BasicAbstractGUIAction {

   private MyDataPanel boardPanel;
   private JiraPanel jiraPanel;
   private DnDTreePanel sprintPanel;

   FreezeManipulationAction(Frame parentFrame, MyDataPanel boardPanel, JiraPanel jiraPanel, DnDTreePanel sprintPanel) {
      super("Freeze", "Does not allow manipulation of data whilst checked", parentFrame);
      this.boardPanel = boardPanel;
      this.jiraPanel = jiraPanel;
      this.sprintPanel = sprintPanel;
   }

   @Override
   public void doActionPerformed(ActionEvent evt) {
      JCheckBoxMenuItem cb = (JCheckBoxMenuItem) evt.getSource();

      boolean isSel = !cb.isSelected();
      boardPanel.setEditable(isSel);
      sprintPanel.setEditable(isSel);
      jiraPanel.setEditable(isSel);
   }

   @Override
   public boolean isCheckBoxAction() {
      return true;
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


final class BurndownAction extends BasicAbstractGUIAction {
   private final MyTable sourceTable;
   private PlannerHelper helper;

   BurndownAction(String name, String description, Frame parentFrame, MyTable sourceTable, PlannerHelper helper) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
      this.helper = helper;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      BoardStatsFrame boardStatsFrame = new BoardStatsFrame(getParentFrame(), 600, 500, sourceTable, new DateHelper(), helper.getSprintCache());
      boardStatsFrame.setVisible(true);
   }

}


final class ReconcileManuallyAction extends BasicAbstractGUIAction {
   private final MyTable boardTable;

   public ReconcileManuallyAction(Frame parentFrame, MyTable boardTable) {
      super("Commence Reconciliation", "Reconcile with the real visual board and the virtual Sprinter Board", parentFrame);
      this.boardTable = boardTable;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      new AddBoardReconcileFrame(getParentFrame(), boardTable);
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
      super("Add Manually", "Add jiras manually", parentFrame, tables);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      new AddManualDialog(getParentFrame(), getTables());
   }

}


final class AddFromJiraFilterAction extends BasicAbstractGUIActionWithTables {
   public AddFromJiraFilterAction(Frame parentFrame, MyTable... tables) {
      super("Add Filters", "Use predefined jira filters to add jiras to Sprinter", parentFrame, tables);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      new AddFilterDialog(getParentFrame(), getTables());
   }
}


final class AddFromJiraVersionAction extends BasicAbstractGUIActionWithTables {
   public AddFromJiraVersionAction(Frame parentFrame, MyTable[] tables) {
      super("Add Versions", "", parentFrame, tables);
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      new AddVersionDialog(getParentFrame(), getTables());
   }
}