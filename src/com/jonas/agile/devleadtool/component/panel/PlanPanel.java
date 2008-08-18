package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import com.ProgressDialog;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.ComboTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.SwingWorker;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraIssueNotFoundException;
import com.jonas.jira.access.JiraListener;
import com.jonas.testHelpers.TryoutTester;

public class PlanPanel extends MyComponentPanel {
	enum SyncMethod {
		ADD_FROM_BUTTON, BULK_FROM_MODEL
	};

	private final class SyncWithJiraActionListener implements ActionListener {
		private final JCheckBox syncWithJiraCheckbox;
		private Logger log = MyLogger.getLogger(this.getClass());
		private SyncMethod method;

		private SyncWithJiraActionListener(JCheckBox syncWithJiraCheckbox) {
			this.syncWithJiraCheckbox = syncWithJiraCheckbox;
			method = PlanPanel.SyncMethod.ADD_FROM_BUTTON;
		}

		public SyncWithJiraActionListener() {
			this.syncWithJiraCheckbox = null;
			method = PlanPanel.SyncMethod.BULK_FROM_MODEL;
		}

		public void actionPerformed(ActionEvent e) {
			log.debug(e);
			if (method == PlanPanel.SyncMethod.ADD_FROM_BUTTON) {
				// final String jiraToGet = field.getText();
				// if (!syncWithJiraCheckbox.isSelected())
				((PlanTableModel)table.getModel()).addEmptyRow();
				// else {
				// final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Copying Jira to Plan...",
				// "Getting information from Jira", 0);
				// dialog.setIndeterminate(false);
				// SwingWorker worker = new SwingWorker() {
				// public Object construct() {
				// log.debug("syncing Jira" + jiraToGet);
				// // TODO make jiraListener for login, etc work.
				// return helper.getJiraIssueFromName(jiraToGet, new JiraListener() {
				// public void notifyOfAccess(JiraAccessUpdate accessUpdate) {
				// switch (accessUpdate) {
				// case LOGGING_IN:
				// dialog.setNote("Logging in!");
				// break;
				// case GETTING_FIXVERSION:
				// dialog.setNote("Getting FixVersion!");
				// break;
				// case GETTING_JIRA:
				// dialog.setNote("Getting Jira!");
				// break;
				// default:
				// break;
				// }
				// }
				// });
				// }
				//
				// public void finished() {
				// log.debug("got jira response!");
				// model.addRow((JiraIssue) get());
				// dialog.setCompleteSoonish();
				// }
				// };
				// worker.start();
				// }
			} else if (method == PlanPanel.SyncMethod.BULK_FROM_MODEL) {
				final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Syncing with Jira...", "Starting...", 0);
				dialog.setIndeterminate(false);
				SwingWorker worker = new SwingWorker() {
					public Object construct() {
						final int[] rows = table.getSelectedRows();
						dialog.increaseMax("Syncing...", rows.length);
						try {
							for (int rowSelected : rows) {
								final String jiraToGet = (String) table.getValueAt(rowSelected, 0);
								dialog.increseProgress("Syncing " + jiraToGet);
								log.debug("Syncing Jira" + jiraToGet);
								JiraIssue jira;
								try {
									jira = helper.getJiraIssueFromName(jiraToGet, new JiraListener() {
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
								} catch (JiraIssueNotFoundException e) {
									AlertDialog.alertException(helper, e);
									e.printStackTrace();
									continue;
								}
								table.setRow(jira, rowSelected);
							}
						} catch (Exception e) {
							AlertDialog.alertException(helper, e);
							e.printStackTrace();
						}
						return null;
					}

					public void finished() {
						log.debug("Syncing Finished!");
						dialog.setCompleteSoonish();
					}
				};
				worker.start();
			}
		}
	}

	private final PlannerHelper helper;
	private MyTable table;
	private Logger log = MyLogger.getLogger(PlanPanel.class);
	private JComboBox comboBox;

	public PlanPanel(PlannerHelper client) {
		this(client, new PlanTableModel());
	}

	public PlanPanel(PlannerHelper helper, PlanTableModel planModel) {
		super(new BorderLayout());
		this.helper = helper;
		PlanTableModel model = planModel;

		table = new MyTable();
		table.setModel(model);

		table.setDefaultRenderer(String.class, new StringTableCellRenderer(model));
		table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer(model));

		comboBox = new JComboBox(JiraProject.LLU_SYSTEMS_PROVISIONING.getFixVersions(false));
		table.setColumnEditor(1, new ComboTableCellEditor(model, comboBox));

		JScrollPane scrollpane = new JScrollPane(table);

		table.setAutoCreateRowSorter(true);
		// this.addNorth(getTopPanel());
		this.addCenter(scrollpane);
		this.addSouth(getBottomPanel());
	}

	private Component getBottomPanel() {
		JPanel buttons = new JPanel();
		// final JLabel label = new JLabel("Jira:");
		// final JTextField field = new JTextField(4);
		final JButton addJira = new JButton("Add");
		final JCheckBox syncWithJiraCheckbox = new JCheckBox("jiraSync?", false);
		JButton syncSelectedWithJiraButton = new JButton("sync With Jira");

		syncSelectedWithJiraButton.addActionListener(new SyncWithJiraActionListener());
		// addJira.addActionListener(new SyncWithJiraActionListener(syncWithJiraCheckbox, field));
		addJira.addActionListener(new SyncWithJiraActionListener(syncWithJiraCheckbox));

		// buttons.add(label);
		// buttons.add(field);
		buttons.add(syncWithJiraCheckbox);
		buttons.add(addJira);
		buttons.add(syncSelectedWithJiraButton);
		return buttons;
	}

	public PlanTableModel getPlanModel() {
		return ((PlanTableModel)table.getModel());
	}

	public static void main(String[] args) {
		JFrame frame = TryoutTester.getFrame();
		JPanel panel = new PlanPanel(new PlannerHelper(frame, "test"));
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	public void setEditable(boolean selected) {
		((PlanTableModel)table.getModel()).setEditable(selected);
	}

	public boolean doesJiraExist(String jira) {
		return ((PlanTableModel)table.getModel()).doesJiraExist(jira);
	}

}
