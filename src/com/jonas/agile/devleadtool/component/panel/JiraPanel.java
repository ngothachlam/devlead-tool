package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapService;
import _105._38._155._10.jira.rpc.soap.jirasoapservice_v2.JiraSoapServiceServiceLocator;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.data.SystemProperties;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.access.ClientConstants;
import com.jonas.jira.access.JiraClient;

public class JiraPanel extends MyComponentPanel {

	private Logger log = MyLogger.getLogger(JiraPanel.class);
	private JiraClient jiraClient = null;

	public JiraPanel(PlannerHelper client) {
		super(new BorderLayout());
		jiraClient = JiraClient.JiraClientAolBB;
		JScrollPane scrollpane = new JScrollPane();
		this.addCenter(scrollpane);
		this.addSouth(getButtonPanel());
	}

	private Component getButtonPanel() {
		JPanel buttons = new JPanel();

		List<PanelProject> projects = new ArrayList<PanelProject>();
		projects.add(new PanelProject(JiraProject.LLU_DEV_SUPPORT, JiraClient.JiraClientAolBB));
		projects.add(new PanelProject(JiraProject.LLU_SYSTEMS_PROVISIONING, JiraClient.JiraClientAolBB));
		projects.add(new PanelProject(JiraProject.ATLASSIN_TST, JiraClient.JiraClientAtlassin));
		final JComboBox jiraProjects = new JComboBox(projects.toArray());
		final JComboBox jiraProjectFixVersion = new JComboBox();

		JButton fixVersionButton = new JButton("Refresh");
		fixVersionButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				log.debug("getting fixVersion : " + jiraProjects.getSelectedItem());
				jiraProjectFixVersion.removeAllItems();
				PanelProject selectedItem = (PanelProject) jiraProjects.getSelectedItem();
				JiraVersion[] fixVersions = selectedItem.getClient().getFixVersionsFromProject(selectedItem.getProject(), false);
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

	public static void main(String[] args) {
		JiraPanel panel = new JiraPanel(new PlannerHelper("test"));
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setSize(300, 200);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				SystemProperties.close();
				System.exit(0);
			}

		});
	}

	class PanelProject {

		private final JiraProject project;
		private final JiraClient client;

		public PanelProject(JiraProject project, JiraClient client) {
			this.project = project;
			this.client = client;
		}

		public JiraClient getClient() {
			return client;
		}

		@Override
		public String toString() {
			return project.getName();
		}

		private JiraProject getProject() {
			return project;
		}
		
		

	}

}
