package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.data.SystemProperties;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;
import com.jonas.testHelpers.PanelTryoutTester;

public class JiraPanel extends MyComponentPanel {

	private Logger log = MyLogger.getLogger(JiraPanel.class);
	private JiraTableModel jiraTableModel;

	public JiraPanel(PlannerHelper client) {
		super(new BorderLayout());

		MyTable list = new MyTable();
		jiraTableModel = new JiraTableModel();
		list.setModel(jiraTableModel);
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

		jiraProjectsCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				jiraProjectFixVersionCombo.removeAllItems();
				jiraProjectFixVersionCombo.setEditable(false);
			}
		});
		fixVersionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.debug("getting fixVersion : " + jiraProjectsCombo.getSelectedItem());
				jiraProjectFixVersionCombo.removeAllItems();
				Object[] selectedObjects = jiraProjectsCombo.getSelectedObjects();
				for (int i = 0; i < selectedObjects.length; i++) {
					JiraProject selectedProject = (JiraProject) selectedObjects[i];
					JiraVersion[] fixVersions = selectedProject.getJiraClient().getFixVersionsFromProject(selectedProject, false);
					for (int j = 0; j < fixVersions.length; j++) {
						jiraProjectFixVersionCombo.addItem(fixVersions[j]);
					}
				}
			}
		});
		getJirasButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] selects = jiraProjectFixVersionCombo.getSelectedObjects();
				for (int i = 0; i < selects.length; i++) {
					JiraVersion version = (JiraVersion) selects[i];
					JiraClient client = version.getProject().getJiraClient();
					client.login();
					JiraIssue[] jiras = client.getJirasFromFixVersion(version);
					for (int j = 0; j < jiras.length; j++) {
						log.debug(jiras[j]);
						jiraTableModel.addRow(jiras[j]);
					}
				}
			}
		});
		clearJirasButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while (jiraTableModel.getRowCount()>0) {
					jiraTableModel.removeRow(0);
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
		JiraPanel panel = new JiraPanel(new PlannerHelper("test"));
		PanelTryoutTester.viewPanel(panel);
	}

   public JiraTableModel getJiraModel() {
     return jiraTableModel;
   }

}
