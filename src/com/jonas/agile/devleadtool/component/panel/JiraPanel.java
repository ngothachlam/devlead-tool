package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.ProgressDialog;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.SwingWorker;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.testHelpers.TryoutTester;

public class JiraPanel extends MyComponentPanel {

	private Logger log = MyLogger.getLogger(JiraPanel.class);
	private JiraTableModel model;
	private final PlannerHelper helper;

	public JiraPanel(PlannerHelper helper) {
		this(helper, new JiraTableModel());
	}

	public JiraPanel(PlannerHelper helper, JiraTableModel jiraModel) {
		super(new BorderLayout());
		this.helper = helper;

		MyTable list = new MyTable();
		model = jiraModel;
		list.setModel(model);
		JScrollPane scrollpane = new JScrollPane(list);

		this.addCenter(scrollpane);
		this.addSouth(getButtonPanel());
	}

	private Component getButtonPanel() {
		JPanel buttons = new JPanel();

		List<JiraProject> projects = JiraProject.getProjects();

		final JComboBox jiraProjectsCombo = new JComboBox(projects.toArray());
		final JComboBox jiraProjectFixVersionCombo = new JComboBox();
		final JButton fixVersionButton = new JButton("Refresh");
		final JButton getJirasButton = new JButton("Get Jiras");
		final JButton clearJirasButton = new JButton("Clear Jiras");

		jiraProjectsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jiraProjectFixVersionCombo.removeAllItems();
				jiraProjectFixVersionCombo.setEditable(false);
			}
		});
		fixVersionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.debug("getting fixVersion : " + jiraProjectsCombo.getSelectedItem());
				jiraProjectFixVersionCombo.removeAllItems();
				final Object[] selectedObjects = jiraProjectsCombo.getSelectedObjects();
				final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Refreshing Fix Versions...",
						"Refreshing Fix Versions...", selectedObjects.length);
				SwingWorker worker = new SwingWorker() {
					public Object construct() {
						dialog.setIndeterminate(false);
						for (int i = 0; i < selectedObjects.length; i++) {
							JiraProject selectedProject = (JiraProject) selectedObjects[i];
							dialog.setNote("Refreshing Fix Versions for " + selectedProject.getJiraKey() + "...");
							JiraVersion[] fixVersions;
							try {
								fixVersions = selectedProject.getJiraClient().getFixVersionsFromProject(selectedProject, false);
								for (int j = 0; j < fixVersions.length; j++) {
									jiraProjectFixVersionCombo.addItem(fixVersions[j]);
								}
							} catch (RemoteException e1) {
								AlertDialog.alertException(helper, e1);
							}
						}
						return null;
					}

					@Override
					public void finished() {
						dialog.setComplete();
					}
				};
				worker.start();
			}
		});
		getJirasButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Object[] selects = jiraProjectFixVersionCombo.getSelectedObjects();
				final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Copying Jiras to Tab...",
						"Logging in...", 0);
				SwingWorker worker = new SwingWorker() {
					public Object construct() {
						dialog.setIndeterminate(false);
						for (int i = 0; i < selects.length; i++) {
							final JiraVersion version = (JiraVersion) selects[i];
							final JiraClient client = version.getProject().getJiraClient();
							try {
								dialog.setNote("Logging in!");
								client.login();

								JiraIssue[] jiras = client.getJirasFromFixVersion(version);
								dialog.increaseMax("Copying Jiras with Fix Version " + version.getName(), jiras.length);
								for (int j = 0; j < jiras.length; j++) {
									log.debug(jiras[j]);
									dialog.increseProgress();
									model.addRow(jiras[j]);
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
					public void finished() {
						dialog.setComplete();
					}
				};
				worker.start();
			}
		});
		clearJirasButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while (model.getRowCount() > 0) {
					model.removeRow(0);
				}
			}
		});

		buttons.add(jiraProjectsCombo);
		buttons.add(fixVersionButton);
		buttons.add(jiraProjectFixVersionCombo);
		buttons.add(getJirasButton);
		buttons.add(clearJirasButton);

		return buttons;
	}

	public static void main(String[] args) {
		JFrame frame = TryoutTester.getFrame();
		PlannerHelper plannerHelper = new PlannerHelper(frame, "test");
		JiraPanel panel = new JiraPanel(plannerHelper);
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	public JiraTableModel getJiraModel() {
		return model;
	}

	public void setEditable(boolean selected) {
		model.setEditable(selected);
	}

}
