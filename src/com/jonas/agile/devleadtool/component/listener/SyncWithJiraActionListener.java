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
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.access.JiraIssueNotFoundException;
import com.jonas.jira.access.JiraListener;

public class SyncWithJiraActionListener implements ActionListener {
	/**
	 * 
	 */
	private Logger log = MyLogger.getLogger(this.getClass());
	private final MyTable table;
	private final PlannerHelper helper;

	public SyncWithJiraActionListener(MyTable table, PlannerHelper helper) {
		this.table = table;
		this.helper = helper;
	}

	public void actionPerformed(ActionEvent e) {
		log.debug(e);
		final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Syncing with Jira...", "Starting...", 0);
		dialog.setIndeterminate(false);
		SwingWorker worker = new SwingWorker() {
			public Object doInBackground() {
				final int[] rows = table.getSelectedRows();
				dialog.increaseMax("Syncing...", rows.length);
				try {
					for (int rowSelected : rows) {
						if (isCancelled())
							break;
						int convertedTableRowToModel = table.convertRowIndexToModel(rowSelected);
						final String jiraToGet = (String) (table.getModel()).getValueAt(convertedTableRowToModel, 0);
						dialog.increseProgress("Syncing " + jiraToGet);
						log.debug("Syncing Jira" + jiraToGet);
						JiraIssue jira;
						try {
							jira = helper.getJiraIssueFromName(jiraToGet, new JiraListener() {
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
							});
						} catch (JiraIssueNotFoundException e) {
							AlertDialog.alertException(helper, e);
							e.printStackTrace();
							continue;
						}
						log.debug("jira: " + jira + " rowSelected: " + rowSelected);
						((PlanTableModel) table.getModel()).setRow(jira, convertedTableRowToModel);
						// table.setRow(jira, rowSelected);
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
			}
		};
		worker.execute();
	}
}