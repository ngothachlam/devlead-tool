package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataListener;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.JiraClient;

public class JiraPanel extends MyComponentPanel {

	private Logger log = MyLogger.getLogger(JiraPanel.class);
	private final JiraClient jiraClient = new JiraClient();
	
	public JiraPanel(PlannerHelper client) {
		super(new BorderLayout());
		JScrollPane scrollpane = new JScrollPane();
		this.addCenter(scrollpane);
		this.addSouth(getButtonPanel());
	}
	
	private Component getButtonPanel() {
		JPanel buttons = new JPanel();
		
		List<JiraProject> projects = new ArrayList<JiraProject>();
		projects.add(JiraProject.LLU_DEV_SUPPORT);
		projects.add(JiraProject.LLU_SYSTEMS_PROVISIONING);
		final JComboBox jiraProjects = new JComboBox(projects.toArray());
		final JComboBox jiraProjectFixVersion = new JComboBox();

		JButton fixVersionButton = new JButton("Refresh");
		fixVersionButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				log.debug("getting fixVersion : " + jiraProjects.getSelectedItem());
				jiraProjectFixVersion.removeAllItems();
				JiraVersion[] fixVersions = jiraClient.getFixVersionsFromProject((JiraProject) jiraProjects.getSelectedItem());
				for (int i = 0; i < fixVersions.length; i++) {
					jiraProjectFixVersion.addItem(fixVersions[i]);
				}
			}
		});
		
		buttons.add(jiraProjects);
		buttons.add(fixVersionButton);
		buttons.add(jiraProjectFixVersion);
		return buttons;
	}

	public static void main(String[] args){
		JiraPanel panel = new JiraPanel(new PlannerHelper("test"));
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setSize(300,200);
		frame.setVisible(true);
	}

}
