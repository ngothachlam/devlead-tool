package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import com.ProgressDialog;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.SwingWorker;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraListener;
import com.jonas.testHelpers.TryoutTester;

public class PlanPanel extends MyComponentPanel {
	enum SyncMethod {
		ADD_FROM_BUTTON, BULK_FROM_MODEL
	};

	private final class SyncWithJiraActionListener implements ActionListener {
		private final JCheckBox syncWithJiraCheckbox;
		private final JTextField field;
		private Logger log = MyLogger.getLogger(this.getClass());
		private SyncMethod method;

		private SyncWithJiraActionListener(JCheckBox syncWithJiraCheckbox, JTextField field) {
			this.syncWithJiraCheckbox = syncWithJiraCheckbox;
			this.field = field;
			method = PlanPanel.SyncMethod.ADD_FROM_BUTTON;
		}

		public SyncWithJiraActionListener() {
			this.syncWithJiraCheckbox = null;
			this.field = null;
			method = PlanPanel.SyncMethod.BULK_FROM_MODEL;
		}

		public void actionPerformed(ActionEvent e) {
			log.debug(e);
			if (method == PlanPanel.SyncMethod.ADD_FROM_BUTTON) {
				final String jiraToGet = field.getText();
				if (!syncWithJiraCheckbox.isSelected())
					model.addRow(new JiraIssue(jiraToGet, null, null));
				else {
					final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Copying Jira to Plan...",
							"Getting information from Jira", 0);
					dialog.setIndeterminate(false);
					SwingWorker worker = new SwingWorker() {
						public Object construct() {
							log.debug("syncing Jira" + jiraToGet);
							// TODO make jiraListener for login, etc work.
							return helper.getJiraIssueFromName(jiraToGet, new JiraListener() {
								public void notifyOfAccess(JiraAccessUpdate accessUpdate) {
									switch (accessUpdate) {
									case LOGGING_IN:
										dialog.setNote("Logging in!");
										break;
									case GETTING_FIXVERSION:
										dialog.setNote("Getting FixVersion!");
										break;
									case GETTING_JIRA:
										dialog.setNote("Getting Jira!");
										break;
									default:
										break;
									}
								}
							});
						}

						public void finished() {
							log.debug("got jira response!");
							model.addRow((JiraIssue) get());
							dialog.setComplete();
						}
					};
					worker.start();
				}
			} else {
				final int[] rows = table.getSelectedRows();
				final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Syncing with Jira...", "Starting...",
						rows.length);
				dialog.setIndeterminate(false);
				SwingWorker worker = new SwingWorker() {
					public Object construct() {
						for (int i = 0; i < rows.length; i++) {
							final String jiraToGet = (String) model.getValueAt(rows[i], 0);
							dialog.increseProgress("Syncing " + jiraToGet);
							log.debug("Syncing Jira" + jiraToGet);
							JiraIssue jira = helper.getJiraIssueFromName(jiraToGet, new JiraListener() {
								public void notifyOfAccess(JiraAccessUpdate accessUpdate) {
									switch (accessUpdate) {
									case LOGGING_IN:
										dialog.setNote("Syncing " + jiraToGet + " - Logging in!");
										break;
									case GETTING_FIXVERSION:
										dialog.setNote("Syncing " + jiraToGet + " - Getting FixVersion!");
										break;
									case GETTING_JIRA:
										dialog.setNote("Syncing " + jiraToGet + " - Accessing Jira info!");
										break;
									default:
										break;
									}
								}
							});
							model.setRow(jira, rows[i]);
						}
						return null;
					}

					public void finished() {
						log.debug("Syncing Finished!");
						dialog.setComplete();
					}
				};
				worker.start();
			}
		}
	}

	private PlanTableModel model;
	private final PlannerHelper helper;
	private MyTable table;

	public PlanPanel(PlannerHelper client) {
		this(client, new PlanTableModel());
	}

	public PlanPanel(PlannerHelper helper, PlanTableModel planModel) {
		super(new BorderLayout());
		this.helper = helper;
		model = planModel;

		table = new MyTable();
		table.setModel(model);
		JScrollPane scrollpane = new JScrollPane(table);

		table.setAutoCreateRowSorter(true);

		this.addNorth(getTopPanel());
		this.addCenter(scrollpane);
		this.addSouth(getBottomPanel());
	}

	private Component getTopPanel() {
		JPanel buttons = new JPanel();
		final JLabel label = new JLabel("Jira:");
		final JTextField field = new JTextField(4);
		final JButton addJira = new JButton("Add");
		final JCheckBox syncWithJiraCheckbox = new JCheckBox("jiraSync?", false);

		addJira.addActionListener(new SyncWithJiraActionListener(syncWithJiraCheckbox, field));

		buttons.add(label);
		buttons.add(field);
		buttons.add(syncWithJiraCheckbox);
		buttons.add(addJira);
		return buttons;
	}

	private Component getBottomPanel() {
		JPanel buttons = new JPanel();
		JButton syncSelectedWithJiraButton = new JButton("sync With Jira");

		syncSelectedWithJiraButton.addActionListener(new SyncWithJiraActionListener());

		buttons.add(syncSelectedWithJiraButton);
		return buttons;
	}

	public PlanTableModel getPlanModel() {
		return model;
	}

	public static void main(String[] args) {
		JFrame frame = TryoutTester.getFrame();
		JPanel panel = new PlanPanel(new PlannerHelper(frame, "test"));
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	public void setEditable(boolean selected) {
		model.setEditable(selected);
	}

	public boolean doesJiraExist(String jira) {
		return model.doesJiraExist(jira);
	}

}
