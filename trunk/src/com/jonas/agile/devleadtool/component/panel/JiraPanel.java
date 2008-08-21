package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.ProgressDialog;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.listener.HyperLinkOpenerAdapter;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.common.HyperLinker;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.SwingWorker;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.testHelpers.TryoutTester;

public class JiraPanel extends MyComponentPanel {

	private final PlannerHelper helper;
	private Logger log = MyLogger.getLogger(JiraPanel.class);
	private MyTable table;

	public JiraPanel(PlannerHelper helper) {
		this(helper, new JiraTableModel());
	}

	public JiraPanel(final PlannerHelper helper, JiraTableModel jiraModel) {
		super(new BorderLayout());
		this.helper = helper;

		table = new MyTable();
		JiraTableModel model = jiraModel;
		table.setModel(model);
		table.setAutoCreateRowSorter(true);
		table.setDragEnabled(true);

		JScrollPane scrollpane = new MyScrollPane(table);

		this.addCenter(scrollpane);
		this.addSouth(getButtonPanel());
		this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));

		table.addMouseListener(new HyperLinkOpenerAdapter(table, helper, JiraTableModel.COLUMNNAME_HYPERLINK, 0));
	}

	public static void main(String[] args) {
		JFrame frame = TryoutTester.getFrame();
		PlannerHelper plannerHelper = new PlannerHelper(frame, "test");
		JiraPanel panel = new JiraPanel(plannerHelper);
		frame.setContentPane(panel);
		frame.setVisible(true);
	}

	private Component getButtonPanel() {
		JPanel buttons = new JPanel();

		List<JiraProject> projects = JiraProject.getProjects();

		final JComboBox jiraProjectsCombo = new JComboBox(projects.toArray());
		final JComboBox jiraProjectFixVersionCombo = new JComboBox();
		final JButton fixVersionButton = new JButton("Refresh");
		final JButton getJirasButton = new JButton("Get Jiras");
		final JButton clearJirasButton = new JButton("Clear Jiras");
		final JButton openJirasButton = new JButton("Open Jiras");

		jiraProjectsCombo.addActionListener(new AlteringProjectListener(jiraProjectFixVersionCombo));
		fixVersionButton.addActionListener(new RefreshingFixVersionListener(jiraProjectFixVersionCombo, jiraProjectsCombo));
		getJirasButton.addActionListener(new DownloadJirasListener(jiraProjectFixVersionCombo));
		clearJirasButton.addActionListener(new ClearJirasListener());
		openJirasButton.addActionListener(new OpenJirasListener());

		buttons.add(jiraProjectsCombo);
		buttons.add(fixVersionButton);
		buttons.add(jiraProjectFixVersionCombo);
		buttons.add(getJirasButton);
		buttons.add(clearJirasButton);
		buttons.add(openJirasButton);

		return buttons;
	}

	public JiraTableModel getJiraModel() {
		return ((JiraTableModel) table.getModel());
	}

	public void setEditable(boolean selected) {
		((JiraTableModel) table.getModel()).setEditable(selected);
	}

	private final class ClearJirasListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JiraTableModel model = ((JiraTableModel) table.getModel());
			while (model.getRowCount() > 0) {
				model.removeRow(0);
			}
		}
	}

	private final class RefreshingFixVersionListener implements ActionListener {
		private final JComboBox jiraProjectFixVersionCombo;
		private final JComboBox jiraProjectsCombo;

		private RefreshingFixVersionListener(JComboBox jiraProjectFixVersionCombo, JComboBox jiraProjectsCombo) {
			this.jiraProjectFixVersionCombo = jiraProjectFixVersionCombo;
			this.jiraProjectsCombo = jiraProjectsCombo;
		}

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
						try {
							JiraVersion[] fixVersions = selectedProject.getJiraClient().getFixVersionsFromProject(
									selectedProject, false);
							for (JiraVersion jiraVersion : fixVersions) {
								jiraProjectFixVersionCombo.addItem(jiraVersion);
							}
						} catch (RemoteException e1) {
							AlertDialog.alertException(helper, e1);
						}
					}
					return null;
				}

				@Override
				public void finished() {
					dialog.setCompleteSoonish();
				}
			};
			worker.start();
		}
	}

	private final class AlteringProjectListener implements ActionListener {
		private final JComboBox jiraProjectFixVersionCombo;

		private AlteringProjectListener(JComboBox jiraProjectFixVersionCombo) {
			this.jiraProjectFixVersionCombo = jiraProjectFixVersionCombo;
		}

		public void actionPerformed(ActionEvent e) {
			jiraProjectFixVersionCombo.removeAllItems();
			jiraProjectFixVersionCombo.setEditable(false);
		}
	}

	private final class DownloadJirasListener implements ActionListener {
		private final JComboBox jiraProjectFixVersionCombo;

		private DownloadJirasListener(JComboBox jiraProjectFixVersionCombo) {
			this.jiraProjectFixVersionCombo = jiraProjectFixVersionCombo;
		}

		public void actionPerformed(ActionEvent e) {
			final Object[] selects = jiraProjectFixVersionCombo.getSelectedObjects();
			final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Copying Jiras to Tab...", "Logging in...",
					0);
			SwingWorker worker = new SwingWorker() {
				private String error = null;

				public Object construct() {
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
								log.debug(jiras[j]);
								dialog.increseProgress();
								((JiraTableModel) table.getModel()).addRow(jiras[j]);
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
					if (error != null) {
						dialog.setCompleteAsap();
						AlertDialog.message(helper, error);
					} else
						dialog.setCompleteSoonish();
				}
			};
			worker.start();
		}
	}

	private final class OpenJirasListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int[] rows = table.getSelectedRows();
			for (int j = 0; j < rows.length; j++) {
				String jira = (String) table.getModel().getValueAt(table.convertRowIndexToModel(rows[j]), 0);
				String jira_url = helper.getJiraUrl(jira);
				HyperLinker.displayURL(jira_url + "/browse/" + jira);
			}
		}
	}

}
