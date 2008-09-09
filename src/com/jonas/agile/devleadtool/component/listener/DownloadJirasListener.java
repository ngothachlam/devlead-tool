/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;

public class DownloadJirasListener implements ActionListener {
	private final PlannerHelper helper;
	/**
	 * 
	 */
	private final JComboBox jiraProjectFixVersionCombo;
	private final List<SyncWithJiraActionListenerListener> listeners = new ArrayList<SyncWithJiraActionListenerListener>();
	private final Logger log = MyLogger.getLogger(DownloadJirasListener.class);
	
   private final MyTable table;
   public DownloadJirasListener(JComboBox jiraProjectFixVersionCombo, MyTable table, PlannerHelper helper) {
		this.jiraProjectFixVersionCombo = jiraProjectFixVersionCombo;
		this.table = table;
		this.helper = helper;
	}

   public void actionPerformed(ActionEvent e) {
		final Object[] selects = jiraProjectFixVersionCombo.getSelectedObjects();
		final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Copying Jiras to Tab...", "Logging in...", 0);
		SwingWorker worker = new SwingWorker() {
			private String error = null;

			public Object doInBackground() {
				dialog.setIndeterminate(false);
				for (int i = 0; i < selects.length; i++) {
					final JiraVersion version = (JiraVersion) selects[i];
					final JiraClient client = version.getProject().getJiraClient();
					try {
						dialog.setNote("Logging in.");
						JiraIssue[] jiras;
						try {
							client.login();
							dialog.setNote("Getting Jiras From FixVersion \"" + version + "\".");
							jiras = client.getJirasFromFixVersion(version);
						} catch (JiraException e) {
							error = "Whilst " + dialog.getNote() + "\n" + e.getMessage();
							return null;
						}
						dialog.increaseMax("Copying Jiras with Fix Version " + version.getName(), jiras.length);
						for (int j = 0; j < jiras.length; j++) {
							JiraIssue jiraIssue = jiras[j];
                     log.debug(jiraIssue);
							dialog.increseProgress();
//							table.addJira(jiraIssue.getKey());
							notifyThatJiraAdded(jiraIssue);
						}
					} catch (IOException e1) {
						AlertDialog.alertException(helper, e1);
					} catch (JDOMException e1) {
						AlertDialog.alertException(helper, e1);
					}
				}
				return null;
			}

			

         @Override
			public void done() {
				if (error != null) {
					dialog.setCompleteWithDelay(0);
					AlertDialog.message(helper, error);
				} else
					dialog.setCompleteWithDelay(300);
				notifyThatJiraSyncFinished();
			}
		};
		worker.execute();
	}
   
   private void notifyThatJiraAdded(JiraIssue jiraIssue) {
      for (SyncWithJiraActionListenerListener listener : listeners) {
         listener.jiraAdded(jiraIssue);
      }
   }
   
   private void notifyThatJiraSyncFinished() {
      for (SyncWithJiraActionListenerListener listener : listeners) {
         listener.jiraSyncedCompleted();
      }
   }
	
	public void addListener(SyncWithJiraActionListenerListener listener) {
      listeners.add(listener);
   }

	public void removeListener(SyncWithJiraActionListenerListener listener) {
      listeners.remove(listener);
   }
}