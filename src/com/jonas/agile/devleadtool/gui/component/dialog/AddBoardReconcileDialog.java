package com.jonas.agile.devleadtool.gui.component.dialog;

import java.awt.Container;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import com.jonas.agile.devleadtool.component.panel.AddReconcilePanel;
import com.jonas.agile.devleadtool.component.panel.ReconciliationTablePanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.swing.SwingUtil;

public class AddBoardReconcileDialog extends JFrame {

   public AddBoardReconcileDialog(Frame parentFrame, MyTable... tables) {
      this.setContentPane(getPanel(this));
      this.pack();
      this.setSize(450, 450);

      SwingUtil.centreWindowWithinWindow(this, parentFrame);
      setVisible(true);
   }

   private Container getPanel(Frame parentFrame, MyTable... tables) {
      AddReconcilePanel mainPanel = new AddReconcilePanel(parentFrame);
      JPanel reconcilidationTablePanel = new ReconciliationTablePanel(tables);

      JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, reconcilidationTablePanel);
      splitPane.setDividerLocation(-1);

      
//      mainPanel.setAddButtonAction(new AddButtonAction());
      //FIXME!!
      
      return splitPane;
   }

}

