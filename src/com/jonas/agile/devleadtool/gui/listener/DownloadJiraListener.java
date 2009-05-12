/**
 * 
 */
package com.jonas.agile.devleadtool.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.dialog.ProgressDialog;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.jira.jirastat.criteria.JiraCriteriaBuilder;
import com.jonas.jira.jirastat.criteria.JiraHttpCriteria;

public class DownloadJiraListener implements ActionListener {
   private final JComboBox jiraProjectFixVersionCombo;
   private final List<SyncWithJiraActionListenerListener> listeners = new ArrayList<SyncWithJiraActionListenerListener>();
   private final Logger log = MyLogger.getLogger(DownloadJiraListener.class);
   private JFrame parentFrame;

   public DownloadJiraListener(JComboBox jiraProjectFixVersionCombo, JFrame parentFrame) {
      this.jiraProjectFixVersionCombo = jiraProjectFixVersionCombo;
      this.parentFrame = parentFrame;
   }

   public void actionPerformed(ActionEvent e) {
      final Object[] selects = jiraProjectFixVersionCombo.getSelectedObjects();
      final ProgressDialog dialog = new ProgressDialog(parentFrame, "Copying Jiras to Tab...", "Logging in...", 0, true);
      SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
         private String error = null;

         @Override
         public Object doInBackground() {
            dialog.setIndeterminate(false);
            for (int i = 0; i < selects.length; i++) {
               final JiraVersion version = (JiraVersion) selects[i];
               final JiraClient client = version.getProject().getJiraClient();
               log.debug("client jira url: " + client.getJiraUrl());
               try {
                  dialog.setNote("Logging in.");
                  JiraIssue[] jiras;
                  try {
                     client.login();
                     dialog.setNote("Getting Jiras From FixVersion \"" + version + "\".");

                     JiraHttpCriteria criteria = new JiraCriteriaBuilder().fixVersion(version.getProject(),version).getCriteria();
                     jiras = client.getJiras(criteria);
                  } catch (JiraException e) {
                     error = "Whilst " + dialog.getNote() + "\n" + e.getMessage();
                     return null;
                  }
                  dialog.increaseMax("Copying Jiras with Fix Version " + version.getName(), jiras.length);
                  for (int j = 0; j < jiras.length; j++) {
                     JiraIssue jiraIssue = jiras[j];
                     log.debug(jiraIssue);
                     dialog.increseProgress();
                     notifyThatJiraAdded(jiraIssue);
                  }
               } catch (IOException e1) {
                  AlertDialog.alertException(parentFrame, e1);
               } catch (JDOMException e1) {
                  AlertDialog.alertException(parentFrame, e1);
               }
            }
            return null;
         }

         @Override
         public void done() {
            if (error != null) {
               dialog.setCompleteWithDelay(0);
               AlertDialog.alertMessage(parentFrame, error);
            } else
               dialog.setCompleteWithDelay(300);
         }
      };
      worker.execute();
   }

   private void notifyThatJiraAdded(JiraIssue jiraIssue) {
      for (SyncWithJiraActionListenerListener listener : listeners) {
         log.debug("notified a listener that jira " + jiraIssue.getKey() + " added");
         listener.jiraAdded(jiraIssue);
      }
   }

   public void addListener(SyncWithJiraActionListenerListener listener) {
      listeners.add(listener);
   }

   public void removeListener(SyncWithJiraActionListenerListener listener) {
      listeners.remove(listener);
   }
}