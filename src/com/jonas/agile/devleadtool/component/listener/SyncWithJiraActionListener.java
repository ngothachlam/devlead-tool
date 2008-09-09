/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.access.JiraIssueNotFoundException;
import com.jonas.jira.access.JiraListener;

/**
 * This class syncs for a given MyTable the selected rows and gets the relevant JiraIssues.
 * 
 * @author Jonas Olofsson
 */

public class SyncWithJiraActionListener implements ActionListener {
   private static final Logger log = MyLogger.getLogger(SyncWithJiraActionListener.class);
   private final PlannerHelper helper;
   private final List<SyncWithJiraActionListenerListener> listeners = new ArrayList<SyncWithJiraActionListenerListener>();
   private final MyTable table;

   public SyncWithJiraActionListener(MyTable table, PlannerHelper helper) {
      this.table = table;
      this.helper = helper;
   }

   public void actionPerformed(ActionEvent e) {
      log.debug(e);
      final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Syncing with Jira...", "Starting...", 0);
      dialog.setIndeterminate(false);
      new SwingWorkerImpl(dialog).execute();
   }

   public void addListener(SyncWithJiraActionListenerListener listener) {
      listeners.add(listener);
   }

   public void removeListener(SyncWithJiraActionListenerListener listener) {
      listeners.remove(listener);
   }

   private void notifyThatJiraSynced(JiraIssue jira, int tableRowSynced) {
      for (SyncWithJiraActionListenerListener listener : listeners) {
         listener.jiraSynced(jira, tableRowSynced);
      }
   }

   private void notifyThatJiraSyncFinished() {
      for (SyncWithJiraActionListenerListener listener : listeners) {
         listener.jiraSyncedCompleted();
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
            for (int row : rows) {
               if (isCancelled())
                  break;
               // int convertedTableRowToModel = table.convertRowIndexToModel(row);
               // final String jiraToGet = (String) (table.getModel()).getValueAt(convertedTableRowToModel, 0);
               final String jiraToGet = (String) table.getValueAt(Column.Jira, row);
               dialog.increseProgress("Syncing " + jiraToGet);
               log.debug("Syncing Jira" + jiraToGet);
               JiraIssue jira;
               try {
                  jira = helper.getJiraIssueFromName(jiraToGet, new JiraListenerImpl(dialog, jiraToGet));
               } catch (JiraIssueNotFoundException e) {
                  AlertDialog.alertException(helper, e);
                  e.printStackTrace();
                  continue;
               }
               log.debug("jira: " + jira + " rowSelected: " + row);
               notifyThatJiraSynced(jira, row);
            }
         } catch (Exception e) {
            AlertDialog.alertException(helper, e);
            e.printStackTrace();
         }
         return null;
      }

      public void done() {
         log.debug("Syncing Finished!");
         dialog.setCompleteWithDelay(300);
         notifyThatJiraSyncFinished();
      }
   }
}