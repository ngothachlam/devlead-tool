/**
 * 
 */
package com.jonas.agile.devleadtool.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.access.listener.JiraListener;

/**
 * This class syncs for a given MyTable the selected rows and gets the relevant JiraIssues.
 * 
 * use DownloadJirasListener
 * 
 * @author Jonas Olofsson
 */

public class SyncWithJiraListener implements ActionListener {
   private static final Logger log = MyLogger.getLogger(SyncWithJiraListener.class);
   private final PlannerHelper helper;
   private final List<SyncWithJiraActionListenerListener> listeners = new ArrayList<SyncWithJiraActionListenerListener>();
   private final MyTable table;

   public SyncWithJiraListener(MyTable table, PlannerHelper helper) {
      this.table = table;
      this.helper = helper;
   }

   public void actionPerformed(ActionEvent e) {
      log.debug(e);
      final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Syncing with Jira...", "Starting...", 0, true);
      dialog.setIndeterminate(false);
      new SwingWorkerImpl(dialog).execute();
   }

   public void addListener(SyncWithJiraActionListenerListener listener) {
      listeners.add(listener);
   }

   public void removeListener(SyncWithJiraActionListenerListener listener) {
      listeners.remove(listener);
   }

   private void notifyThatJiraSynced(JiraIssue jira) {
      for (SyncWithJiraActionListenerListener listener : listeners) {
         listener.jiraSynced(jira);
      }
   }

   private final class JiraListenerImpl extends JiraListener {
      private final ProgressDialog dialog;
      private final String jiraToGet;

      private JiraListenerImpl(ProgressDialog dialog, String jiraToGet) {
         this.dialog = dialog;
         this.jiraToGet = jiraToGet;
      }

      public void notifyOfAccess(JiraAccessUpdate accessUpdate) {
         switch (accessUpdate) {
         case LOGGING_IN:
            String string = "Syncing " + jiraToGet + " - Logging in!";
            log.debug(string);
            dialog.setNote(string);
            break;
         case GETTING_FIXVERSION:
            String string2 = "Syncing " + jiraToGet + " - Getting FixVersion!";
            log.debug(string2);
            dialog.setNote(string2);
            break;
         case GETTING_JIRA:
            String string3 = "Syncing " + jiraToGet + " - Accessing Jira info!";
            log.debug(string3);
            dialog.setNote(string3);
            break;
         default:
            break;
         }
      }
   }

   private final class SwingWorkerImpl extends SwingWorker<Object, Object> {
      private final ProgressDialog dialog;

      private SwingWorkerImpl(ProgressDialog dialog) {
         this.dialog = dialog;
      }

      public Object doInBackground() {
         final int[] rows = table.getSelectedRows();
         dialog.increaseMax("Syncing...", rows.length);
         try {
            helper.login();
            int breakNo = 0;
            for (int row : rows) {
               if (isCancelled())
                  break;
               // int convertedTableRowToModel = table.convertRowIndexToModel(row);
               // final String jiraToGet = (String) (table.getModel()).getValueAt(convertedTableRowToModel, 0);
               final String jiraToGet = (String) table.getValueAt(Column.Jira, row);
               dialog.increseProgress("Syncing " + jiraToGet);
               log.debug("Syncing Jira" + jiraToGet + " on the selected row " + row + " out of " + rows.length);
               JiraIssue jira;
               try {
                  jira = helper.getJiraIssueFromName(jiraToGet, new JiraListenerImpl(dialog, jiraToGet));
               } catch (Exception e) {
                  breakNo++;
                  AlertDialog.alertException(helper.getParentFrame(), e);
                  e.printStackTrace();
                  if (breakNo >= 2)
                     break;
                  continue;
               }
               log.debug("jiraToGet: " + jiraToGet + " jira: " + jira.getKey() + " rowSelected: " + row);
               notifyThatJiraSynced(jira);
            }
         } catch (Exception e) {
            AlertDialog.alertException(helper.getParentFrame(), e);
            e.printStackTrace();
         }
         return null;
      }

      public void done() {
         log.debug("Syncing Finished!");
         dialog.setCompleteWithDelay(300);
      }
   }
}