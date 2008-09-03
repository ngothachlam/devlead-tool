package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.ProgressDialog;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.listener.DownloadJirasListener;
import com.jonas.agile.devleadtool.component.listener.HyperLinkOpenerAdapter;
import com.jonas.agile.devleadtool.component.listener.RemoveJTableSelectedRowsListener;
import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.HyperLinker;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.testHelpers.TryoutTester;

public class JiraPanel extends MyComponentPanel {

	final PlannerHelper helper;
	Logger log = MyLogger.getLogger(JiraPanel.class);
	MyTable table;

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

		table.addMouseListener(new HyperLinkOpenerAdapter(helper, Column.URL, Column.Jira));
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

		jiraProjectsCombo.addActionListener(new AlteringProjectListener(jiraProjectFixVersionCombo));

		buttons.add(jiraProjectsCombo);
		addButton(buttons, "Refresh", new RefreshingFixVersionListener(jiraProjectFixVersionCombo, jiraProjectsCombo));
		buttons.add(jiraProjectFixVersionCombo);
		addButton(buttons, "Get Jiras", new DownloadJirasListener(jiraProjectFixVersionCombo, table, helper));
		addButton(buttons, "Remove", new RemoveJTableSelectedRowsListener(table));
		addButton(buttons, "Open Jiras", new OpenJirasListener());
		addButton(buttons, "BoardStatus", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyTableModel model = (MyTableModel) table.getModel();
				for (int i = 0; i < model.getRowCount(); i++) {
					int boardStatusNo = model.getColumnNo(Column.BoardStatus);
					int jiraNameNo = model.getColumnNo(Column.Jira);
					String jira = (String) model.getValueAt(i, jiraNameNo);
					List<BoardStatus> jirasBoardStatusList = helper.getPlannerCommunicator().getJiraStatusFromBoard(jira);
					model.setValueAt(jirasBoardStatusList, i, boardStatusNo);
				}
			}
		});

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
				public Object doInBackground() {
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
				public void done() {
					dialog.setCompleteWithDelay(300);
				}
			};
			worker.execute();
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

	private final class OpenJirasListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int[] rows = table.getSelectedRows();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < rows.length; j++) {
				String jira = (String) table.getModel().getValueAt(table.convertRowIndexToModel(rows[j]), 0);
				String jira_url = null;
				boolean error = false;
				try {
					jira_url = helper.getJiraUrl(jira);
				} catch (NotJiraException e1) {
					if(sb.length()>0){
						sb.append(", ");
					}
					sb.append(jira);
					error = true;
				}
				if (!error) {
					HyperLinker.displayURL(jira_url + "/browse/" + jira);
				}
			}
			if (sb.length() > 0){
				sb.append(" are incorrect!");
				AlertDialog.alertException(helper, e.toString());
			}
		}
	}

}
