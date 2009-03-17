package com.jonas.agile.devleadtool.component.dialog;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import com.jonas.agile.devleadtool.component.panel.AddManualPanel;
import com.jonas.agile.devleadtool.component.panel.ReconciliationTablePanel;
import com.jonas.common.SwingUtil;

public class AddBoardReconcileDialog extends JFrame {

   public AddBoardReconcileDialog(JFrame parentFrame) {
      this.setContentPane(getPanel(this));
      this.pack();
      this.setSize(450, 450);

      SwingUtil.centreWindowWithinWindow(this, parentFrame);
      setVisible(true);
   }

   private Container getPanel(JFrame parentFrame) {
      JPanel mainPanel = new AddManualPanel(parentFrame);
      JPanel reconcilidationTablePanel = new ReconciliationTablePanel();

      JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, reconcilidationTablePanel);
      splitPane.setDividerLocation(-1);

      return splitPane;
   }

}
