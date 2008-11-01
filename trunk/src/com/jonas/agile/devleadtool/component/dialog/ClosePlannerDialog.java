package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.data.SystemProperties;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;

public class ClosePlannerDialog extends JDialog {

   public ClosePlannerDialog(final JFrame parent, final PlannerHelper plannerHelper) {
      super(parent, "Closing...", true);
      setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

      MyPanel panel = new MyPanel(new BorderLayout()).bordered(15, 15, 15, 15);
      panel.addCenter(new JLabel("Closing all connections... ", JLabel.CENTER));
      setContentPane(panel);
      pack();
      SwingUtil.centreWindowWithinWindow(this, parent);

      new Thread(new Runnable() {
         public void run() {
            MyInternalFrame.closeAll();
            SystemProperties.close();
            System.exit(0);
         }
      }).start();
      setVisible(true);
   }
}
