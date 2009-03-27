package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.util.concurrent.ExecutionException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.agile.devleadtool.data.SystemProperties;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyPanel;
import com.jonas.common.swing.SwingUtil;

public class ClosePlannerDialog extends JDialog {
   private final class Worker extends SwingWorker<Boolean, Object> {
      private final ClosePlannerDialog closePlannerDialog;

      public Worker(ClosePlannerDialog closePlannerDialog) {
         this.closePlannerDialog = closePlannerDialog;
      }

      @Override
      protected Boolean doInBackground() throws Exception {
         try {
            MyInternalFrame.closeAll();
            return true;
         } catch (PropertyVetoException e) {
            log.debug("Cancelled saving!");
            return false;
         }
      }

      @Override
      protected void done() {
         Boolean closed = true;
         try {
            closed = get();
         } catch (InterruptedException e) {
            e.printStackTrace();
         } catch (ExecutionException e) {
            e.printStackTrace();
         }
         log.debug("Closed: " + closed);
         if (closed) {
            SystemProperties.close();
            System.exit(0);
         } else {
            closePlannerDialog.dispose();
         }
      }
   }

   private Logger log = MyLogger.getLogger(ClosePlannerDialog.class);

   public ClosePlannerDialog(final JFrame parent, final PlannerHelper plannerHelper) {
      super(parent, "Closing...", true);
      parent.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

      MyPanel panel = new MyPanel(new BorderLayout()).bordered(15, 15, 15, 15);
      panel.addCenter(new JLabel("Closing... ", JLabel.CENTER));
      setContentPane(panel);
      pack();
      SwingUtil.centreWindowWithinWindow(this, parent);

      SwingWorker<Boolean, Object> worker = new Worker(this);
      worker.execute();
      setVisible(true);
   }
}
