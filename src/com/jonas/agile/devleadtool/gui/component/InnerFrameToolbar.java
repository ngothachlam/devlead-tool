package com.jonas.agile.devleadtool.gui.component;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.burndown.BurnDownCalculator;
import com.jonas.agile.devleadtool.burndown.BurnDownCriticalPathCalculatorImpl;
import com.jonas.agile.devleadtool.burndown.BurnData;
import com.jonas.agile.devleadtool.burndown.BurnDownDataRetriever;
import com.jonas.agile.devleadtool.burndown.BurnDownProgressionCalculatorImpl;
import com.jonas.agile.devleadtool.burndown.JiraStatsDataDTO;
import com.jonas.agile.devleadtool.burndown.ManualBurnDownFrame;
import com.jonas.agile.devleadtool.burndown.ManualBurnUpFrame;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.action.HighlightIssuesAction;
import com.jonas.agile.devleadtool.gui.action.SprintManagerGuiAction;
import com.jonas.agile.devleadtool.gui.component.dialog.AddBoardReconcileFrame;
import com.jonas.agile.devleadtool.gui.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.gui.component.frame.BoardStatsFrame;
import com.jonas.agile.devleadtool.gui.component.panel.DnDTreePanel;
import com.jonas.agile.devleadtool.gui.component.panel.JiraPanel;
import com.jonas.agile.devleadtool.gui.component.panel.MyDataPanel;
import com.jonas.agile.devleadtool.gui.component.table.EnabledQuery;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.DateHelper;

public class InnerFrameToolbar extends JToolBar {

   public InnerFrameToolbar(final Frame parentFrame, MyDataPanel boardPanel, JiraPanel jiraPanel, DnDTreePanel sprintPanel, MyTable boardTable, final MyTable jiraTable, PlannerHelper helper, ExcelSprintDao sprintDao) {

      final MyTable[] tables = { boardTable, jiraTable };

      BasicAbstractGUIAction highlightAction = new HighlightIssuesAction("Higlight Discrepancies", helper.getParentFrame(), jiraPanel.getTable(), boardPanel.getTable());
      BasicAbstractGUIAction freezeAction = new FreezeManipulationAction(parentFrame, boardPanel, jiraPanel, sprintPanel);
      BasicAbstractGUIAction dupeAction = new CheckForDuplicatesAction("Highlight Duplicates", "Higlight Duplicates in Board", parentFrame, boardTable);
      BasicAbstractGUIAction reconcileAction = new ReconcileManuallyAction(parentFrame, boardTable);
      BasicAbstractGUIAction addManualAction = new AddManuallyAction(parentFrame, tables);
      BasicAbstractGUIAction addFilterAction = new AddFromJiraFilterAction(parentFrame, tables);
      BasicAbstractGUIAction addVersionAction = new AddFromJiraVersionAction(parentFrame, tables);

      BasicAbstractGUIAction sprintManager = new SprintManagerGuiAction(parentFrame, helper, sprintDao);
      BasicAbstractGUIAction boardStats = new BurndownAction("Calculate Burndown", "Showing Board Statistics", parentFrame, boardTable, helper);
      BasicAbstractGUIAction newBurndown = new NewBurnDownAction("Calculate Burndown (new)", "Showing Board Statistics", parentFrame, boardTable, helper);
      BasicAbstractGUIAction newBurnup = new NewBurnUpAction("Calculate BurnUp", "BurnUp Statistics", parentFrame, boardTable, helper);

      JMenuBar comp = new JMenuBar();
      comp.add(getDataModificationMenu("Data Management", reconcileAction, null, addManualAction, addVersionAction, addFilterAction, null, highlightAction, dupeAction, freezeAction));
      comp.add(getDataModificationMenu("Sprint", sprintManager, boardStats, newBurndown));
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

final class CheckForDuplicatesAction extends BasicAbstractGUIAction implements EnabledQuery {
   @Override
   public boolean isCheckBoxAction() {
      return true;
   }

   CheckForDuplicatesAction(String name, String description, Frame parentFrame, MyTable... sourceTables) {
      super(name, description, parentFrame);
      for (MyTable myTable : sourceTables) {
         myTable.setDupelicateHighlighterEnableQuery(this);
      }
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
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


final class NewBurnUpAction extends BasicAbstractGUIAction implements BurnDownDataRetriever {
   private final MyTable sourceTable;
   private BurnData data;
   private final PlannerHelper helper;
   
   NewBurnUpAction(String name, String description, Frame parentFrame, MyTable sourceTable, PlannerHelper helper) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
      this.helper = helper;
   }
   
   @Override
   public void doActionPerformed(ActionEvent e) {
      ManualBurnUpFrame boardStatsFrame = new ManualBurnUpFrame(getParentFrame(), new DateHelper(), this);
      boardStatsFrame.setVisible(true);
   }
   
   @Override
   public BurnData getBurnDownData() {
      return data;
   }
   
   @Override
   public void calculateBurndownData() {
      JiraStatsDataDTO jiraStatsDataDTO = new JiraStatsDataDTO(sourceTable);
      jiraStatsDataDTO.calculateJiraStats();
      
      BurnDownCalculator progressionCalculator = new BurnDownProgressionCalculatorImpl(jiraStatsDataDTO.getJiras());
      progressionCalculator.calculateBurndownData();
      
      BurnDownCalculator criticalPathCalculator = new BurnDownCriticalPathCalculatorImpl(sourceTable);
      criticalPathCalculator.calculateBurndownData();
      
      SprintCache sprintCache = helper.getSprintCache();
      Sprint currentSprint = sprintCache.getCurrentSprint();
      
      data = new BurnData();
      data.add("Open", 0d, progressionCalculator.getTotalEstimates());
      data.add("Open", currentSprint.calculateDayInSprint(), progressionCalculator.getRemainingEstimates());
   }
}
final class NewBurnDownAction extends BasicAbstractGUIAction implements BurnDownDataRetriever {
   private final MyTable sourceTable;
   private BurnData data;
   private final PlannerHelper helper;

   NewBurnDownAction(String name, String description, Frame parentFrame, MyTable sourceTable, PlannerHelper helper) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
      this.helper = helper;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      ManualBurnDownFrame boardStatsFrame = new ManualBurnDownFrame(getParentFrame(), new DateHelper(), this);
      boardStatsFrame.setVisible(true);
   }

   @Override
   public BurnData getBurnDownData() {
      return data;
   }

   @Override
   public void calculateBurndownData() {
      JiraStatsDataDTO jiraStatsDataDTO = new JiraStatsDataDTO(sourceTable);
      jiraStatsDataDTO.calculateJiraStats();

      BurnDownCalculator progressionCalculator = new BurnDownProgressionCalculatorImpl(jiraStatsDataDTO.getJiras());
      progressionCalculator.calculateBurndownData();
      
      BurnDownCalculator criticalPathCalculator = new BurnDownCriticalPathCalculatorImpl(sourceTable);
      criticalPathCalculator.calculateBurndownData();

      SprintCache sprintCache = helper.getSprintCache();
      Sprint currentSprint = sprintCache.getCurrentSprint();

      data = new BurnData();
      data.add("Progression", 0d, progressionCalculator.getTotalEstimates());
      data.add("Progression", currentSprint.calculateDayInSprint(), progressionCalculator.getRemainingEstimates());
      data.add("Ideal Progression", 0d, progressionCalculator.getTotalEstimates());
      data.add("Ideal Progression", currentSprint.getLength(), 0d);
      data.add("Critical Path", 0d, criticalPathCalculator.getTotalEstimates());
      data.add("Critical Path", currentSprint.calculateDayInSprint(), criticalPathCalculator.getRemainingEstimates());
      data.add("Ideal Critical Path", 0d, criticalPathCalculator.getTotalEstimates());
      data.add("Ideal Critical Path", currentSprint.getLength(), 0d);
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