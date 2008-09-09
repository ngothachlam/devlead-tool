/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.listener.DestinationRetriever;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;

public class CopyToTableListener implements ActionListener {
   private Logger log = MyLogger.getLogger(this.getClass());
   private final MyTable sourceTable;
   private final PlannerHelper helper2;
   private final DestinationRetriever listener;

   public CopyToTableListener(MyTable sourceTable, DestinationRetriever listener, PlannerHelper helper) {
      this.sourceTable = sourceTable;
      this.listener = listener;
      helper2 = helper;
   }

   public void actionPerformed(ActionEvent e) {

      final int[] selectedRows = sourceTable.getSelectedRows();
      final ProgressDialog dialog = new ProgressDialog(helper2.getParentFrame(), "Copying...", "Copying selected messages from Board to Plan...",
            selectedRows.length);
      SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
         @Override
         protected Object doInBackground() {
            try {
               for (int i = 0; i < selectedRows.length; i++) {
                  String jiraString = (String) sourceTable.getValueAt(Column.Jira, selectedRows[i]);
                  listener.getDestinationTable().addJira(jiraString);
                  dialog.increseProgress();
               }
            } catch (Exception e) {
               AlertDialog.alertException(helper2, e);
               e.printStackTrace();
            }
            return null;
         }

         public void done() {
            dialog.setCompleteWithDelay(300);
         }
      };
      worker.execute();
   }
}