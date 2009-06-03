package com.jonas.agile.devleadtool.gui.component;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.burndown.BurnCalculator;
import com.jonas.agile.devleadtool.burndown.BurnData;
import com.jonas.agile.devleadtool.burndown.BurnDataRetriever;
import com.jonas.agile.devleadtool.burndown.BurnDownCriticalPathCalculatorImpl;
import com.jonas.agile.devleadtool.burndown.BurnDownProgressionCalculatorImpl;
import com.jonas.agile.devleadtool.burndown.BurnType;
import com.jonas.agile.devleadtool.burndown.BurnUpCalculator;
import com.jonas.agile.devleadtool.burndown.Category;
import com.jonas.agile.devleadtool.burndown.HistoricalBoardDao;
import com.jonas.agile.devleadtool.burndown.HistoricalData;
import com.jonas.agile.devleadtool.burndown.HistoricalDataCriteria;
import com.jonas.agile.devleadtool.burndown.JiraStatsDataDTO;
import com.jonas.agile.devleadtool.burndown.ManualBurnFrame;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.action.HighlightIssuesAction;
import com.jonas.agile.devleadtool.gui.action.SaveDataAction;
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
import com.jonas.agile.devleadtool.gui.component.table.EnabledQuery;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.DateHelper;
import com.jonas.common.swing.SwingUtil;

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
      // BasicAbstractGUIAction boardStats = new BurndownAction("Calculate Burndown (old)", "Showing Board Statistics", parentFrame, boardTable, helper);
      BasicAbstractGUIAction newBurndown = new NewBurnDownAction("Graph Burn-down", "Showing Board Statistics", parentFrame, boardTable, helper);
      BasicAbstractGUIAction newBurnup = new NewBurnUpAction("Graph Burn-Up", "BurnUp Statistics", parentFrame, helper);

      JMenuBar comp = new JMenuBar();
      comp.add(getDataModificationMenu("Data Management", reconcileAction, null, addManualAction, addVersionAction, addFilterAction, null, highlightAction, dupeAction, freezeAction));
      comp.add(getDataModificationMenu("Sprint", sprintManager, newBurndown, newBurnup));
      HistoricalBoardDao dao = new HistoricalBoardDao(new DateHelper());
      comp.add(new JButton(new SaveDataAction("Save", "Save historical view for the future (BurnDowns, BurnUps, etc)", parentFrame, boardTable.getMyModel(), dao, helper)));
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

final class NewBurnUpAction extends BasicAbstractGUIAction implements BurnDataRetriever {
   private BurnData data;
   private final PlannerHelper helper;
   private ManualBurnFrame boardStatsFrame;

   NewBurnUpAction(String name, String description, Frame parentFrame, PlannerHelper helper) {
      super(name, description, parentFrame);
      this.helper = helper;
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      boardStatsFrame = new ManualBurnFrame(getParentFrame(), new DateHelper(), this);
      boardStatsFrame.setVisible(true);
   }

   @Override
   public BurnData getBurnData() {
      return data;
   }

   @Override
   public void calculateBurndownData() {
      try {
         SprintCache sprintCache = helper.getSprintCache();
         BurnUpCalculator burnUpCalculator = new BurnUpCalculator();
         Sprint currentSprint = sprintCache.getCurrentSprint();

         if (currentSprint == null) {
            AlertDialog.alertMessage(helper.getParentFrame(), "Could not find a current sprint!");
            return;
         }
         String currentSprintName = currentSprint.getName();
         String currentSprintStart = DateHelper.getDateAsString(currentSprint.getStartDate());
         String currentSprintEnd = DateHelper.getDateAsString(currentSprint.getEndDate());
         boardStatsFrame.setChartText(currentSprintName + " (" + currentSprintStart + " to " + currentSprintEnd + ")");

         HistoricalBoardDao dao = new HistoricalBoardDao(new DateHelper());

         File historicalDataFile = dao.getFileForHistoricalSave(helper.getSaveDirectory(), helper.getExcelFile());
         HistoricalData historicalData = dao.load(historicalDataFile);

         HistoricalDataCriteria criteria = new HistoricalDataCriteria("Sprint", currentSprint.toString());
         data = burnUpCalculator.getSortedDataUsingCriteria(historicalData, criteria, sprintCache.getCurrentSprint());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}

final class NewBurnDownAction extends BasicAbstractGUIAction implements BurnDataRetriever {
   private final MyTable sourceTable;
   private BurnData data;
   private final PlannerHelper helper;
   private ManualBurnFrame boardStatsFrame;
   private DateHelper dateHelper;

   NewBurnDownAction(String name, String description, Frame parentFrame, MyTable sourceTable, PlannerHelper helper) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
      this.helper = helper;
      this.dateHelper = new DateHelper();
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      boardStatsFrame = new ManualBurnFrame(getParentFrame(), new DateHelper(), this);
      boardStatsFrame.setVisible(true);
   }

   @Override
   public BurnData getBurnData() {
      return data;
   }

   @Override
   public void calculateBurndownData() {

      JiraStatsDataDTO jiraStatsDataDTO = new JiraStatsDataDTO(sourceTable);
      jiraStatsDataDTO.calculateJiraStats();

      BurnCalculator progressionCalculator = new BurnDownProgressionCalculatorImpl(jiraStatsDataDTO.getJiras());
      progressionCalculator.calculateBurnData();

      BurnCalculator criticalPathCalculator = new BurnDownCriticalPathCalculatorImpl(sourceTable);
      criticalPathCalculator.calculateBurnData();

      SprintCache sprintCache = helper.getSprintCache();
      Sprint currentSprint = sprintCache.getCurrentSprint();
      if (currentSprint == null) {
         AlertDialog.alertMessage(helper.getParentFrame(), "Could not find a current sprint!");
         return;
      }
      String currentSprintName = currentSprint.getName();
      String currentSprintStart = DateHelper.getDateAsString(currentSprint.getStartDate());
      String currentSprintEnd = DateHelper.getDateAsString(currentSprint.getEndDate());
      boardStatsFrame.setChartText(currentSprintName + " (" + currentSprintStart + " to " + currentSprintEnd + ")");

      data = new BurnData(BurnType.BurnDown, "Remaining Estimated Points");
      Category category = new Category("Progression", SwingUtil.cellGreen, 0);
      data.add(category, 0d, progressionCalculator.getTotalEstimates());
      data.add(category, currentSprint.calculateDayInSprint(), progressionCalculator.getRemainingEstimates());
      Category category2 = new Category("Ideal Progression", SwingUtil.cellLightGreen, 1);
      data.add(category2, 0d, progressionCalculator.getTotalEstimates());
      data.add(category2, currentSprint.getLength(), 0d);
      Category category3 = new Category("Critical Path", SwingUtil.cellRed, 2);
      data.add(category3, 0d, criticalPathCalculator.getTotalEstimates());
      data.add(category3, currentSprint.calculateDayInSprint(), criticalPathCalculator.getRemainingEstimates());
      Category category4 = new Category("Ideal Critical Path", SwingUtil.cellLightRed, 3);
      data.add(category4, 0d, criticalPathCalculator.getTotalEstimates());
      data.add(category4, currentSprint.getLength(), 0d);
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