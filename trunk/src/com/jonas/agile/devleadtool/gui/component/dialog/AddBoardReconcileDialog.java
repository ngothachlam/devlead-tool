package com.jonas.agile.devleadtool.gui.component.dialog;

import java.awt.Container;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import com.jonas.agile.devleadtool.gui.component.panel.AbstractAddPanel;
import com.jonas.agile.devleadtool.gui.component.panel.AddReconcilePanel;
import com.jonas.agile.devleadtool.gui.component.panel.ReconciliationTablePanel;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.listener.AddNewRowAction;
import com.jonas.agile.devleadtool.gui.listener.JiraToBeReconciledListener;
import com.jonas.common.swing.SwingUtil;

public class AddBoardReconcileDialog extends JFrame {

   private final MyTable boardTable;

   public AddBoardReconcileDialog(Frame parentFrame, MyTable boardTable) {
      this.boardTable = boardTable;
      this.setContentPane(getPanel(this));
      this.pack();
      this.setSize(450, 450);

      SwingUtil.centreWindowWithinWindow(this, parentFrame);
      setVisible(true);
   }

   private Container getPanel(Frame parentFrame) {
      AddReconcilePanel mainPanel = new AddReconcilePanel(parentFrame);
      ReconciliationTablePanel reconcilidationTablePanel = new ReconciliationTablePanel(boardTable);

      JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, reconcilidationTablePanel);
      splitPane.setDividerLocation(-1);

      mainPanel.setAddButtonAction(new AddButtonAction(this, mainPanel, reconcilidationTablePanel));

      return splitPane;
   }

}


class AddButtonAction extends AddNewRowAction implements JiraToBeReconciledListener {

   private final AbstractAddPanel mainPanel;
   private final ReconciliationTablePanel reconcilidationTablePanel;

   public AddButtonAction(Frame parentFrame, AbstractAddPanel mainPanel, ReconciliationTablePanel reconcilidationTablePanel) {
      super("Reconcile", "Add to the reconcile table", mainPanel.getJiraPrefixTextField(), mainPanel.getJiraCommasTextField(), mainPanel
            .getDefaultReleaseTextField(), parentFrame);
      this.mainPanel = mainPanel;
      this.reconcilidationTablePanel = reconcilidationTablePanel;
      addJiraToBeReconciledListener(this);
   }

   @Override
   public MyTable getTargetTable() {
      return reconcilidationTablePanel.getTable();
   }

   @Override
   public void jiraAdded(String jira, String devEst, String devAct, String release, String remainder, String qaEst) {
      System.out.println("adding: " + jira);
   }

}