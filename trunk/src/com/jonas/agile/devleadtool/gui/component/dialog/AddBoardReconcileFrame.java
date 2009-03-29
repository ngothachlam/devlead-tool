package com.jonas.agile.devleadtool.gui.component.dialog;

import java.awt.Container;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import com.jonas.agile.devleadtool.gui.component.panel.AbstractAddPanel;
import com.jonas.agile.devleadtool.gui.component.panel.AddReconcilePanel;
import com.jonas.agile.devleadtool.gui.component.panel.ReconciliationTablePanel;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.listener.AddNewRowAction;
import com.jonas.agile.devleadtool.gui.listener.JiraToBeReconciledListener;
import com.jonas.common.swing.SwingUtil;

public class AddBoardReconcileFrame extends JFrame {

   private final MyTable boardTable;

   public AddBoardReconcileFrame(Frame parentFrame, MyTable boardTable) {
      super("Reconciliation with Board");
      this.boardTable = boardTable;
      this.setContentPane(getPanel(this));
      this.pack();
      this.setSize(650, 450);

      SwingUtil.centreWindowWithinWindow(this, parentFrame);
      setVisible(true);
   }

   private Container getPanel(Frame parentFrame) {
      AddReconcilePanel mainPanel = new AddReconcilePanel(parentFrame);
      ReconciliationTablePanel reconcilidationTablePanel = new ReconciliationTablePanel(boardTable, this);

      JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, reconcilidationTablePanel);
      splitPane.setDividerLocation(-1);

      mainPanel.setAddButtonAction(new AddToReconciliationTableAction(this, mainPanel, reconcilidationTablePanel));
      return splitPane;
   }

}


class AddToReconciliationTableAction extends AddNewRowAction implements JiraToBeReconciledListener {

   private final ReconciliationTablePanel reconcilidationTablePanel;

   public AddToReconciliationTableAction(Frame parentFrame, AbstractAddPanel mainPanel, ReconciliationTablePanel reconcilidationTablePanel) {
      super("Reconcile", "Add to the reconcile table", mainPanel.getJiraPrefixTextField(), mainPanel.getJiraCommasTextField(), mainPanel
            .getDefaultReleaseTextField(), mainPanel.getStatusComboBox(), parentFrame);
      this.reconcilidationTablePanel = reconcilidationTablePanel;
      addJiraToBeReconciledListener(this);
   }

   @Override
   public MyTable getTargetTable() {
      return reconcilidationTablePanel.getTable();
   }

   @Override
   public void jiraAdded(String jira, String devEst, String devAct, String release, String remainder, String qaEst, BoardStatusValue status) {
      reconcilidationTablePanel.getTable().addForReconciliation(jira, devEst, devAct, release, remainder, qaEst, status);
   }

}