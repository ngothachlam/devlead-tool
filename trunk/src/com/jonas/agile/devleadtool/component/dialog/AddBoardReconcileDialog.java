package com.jonas.agile.devleadtool.component.dialog;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import com.jonas.agile.devleadtool.component.panel.AddReconcilePanel;
import com.jonas.agile.devleadtool.component.panel.ReconciliationTablePanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.SwingUtil;

public class AddBoardReconcileDialog extends JFrame {

   public AddBoardReconcileDialog(JFrame parentFrame, MyTable jiraTable) {
      this.setContentPane(getPanel(this, jiraTable));
      this.pack();
      this.setSize(450, 450);

      SwingUtil.centreWindowWithinWindow(this, parentFrame);
      setVisible(true);
   }

   private Container getPanel(JFrame parentFrame, MyTable jiraTable) {
      AddReconcilePanel mainPanel = new AddReconcilePanel(parentFrame);
      JPanel reconcilidationTablePanel = new ReconciliationTablePanel(jiraTable);

      JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, reconcilidationTablePanel);
      splitPane.setDividerLocation(-1);

      
      mainPanel.setAddButtonAction(new AddButtonAction());
      
      return splitPane;
   }

}

class AddButtonAction implements Action{

}