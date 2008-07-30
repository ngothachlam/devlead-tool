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
		try {
			URL url = new URL(ClientConstants.JIRA_URL_AOLBB);
			// TODO change this to be generic so you can choose Jira!!!!
			JiraSoapServiceServiceLocator jiraSoapServiceServiceLocator = new JiraSoapServiceServiceLocator();
			log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
			jiraSoapServiceServiceLocator.setJirasoapserviceV2EndpointAddress(ClientConstants.jIRA_URL_ATLASSIN
					+ "/rpc/soap/jirasoapservice-v2");
			log.debug(jiraSoapServiceServiceLocator.getJirasoapserviceV2Address());
			JiraSoapService jirasoapserviceV2 = jiraSoapServiceServiceLocator.getJirasoapserviceV2();
			jiraClient = new JiraClient(jirasoapserviceV2);
			JScrollPane scrollpane = new JScrollPane();
			this.addCenter(scrollpane);
			this.addSouth(getButtonPanel());
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private Component getButtonPanel() {
		JPanel buttons = new JPanel();

		List<JiraProject> projects = new ArrayList<JiraProject>();
		projects.add(JiraProject.LLU_DEV_SUPPORT);
		projects.add(JiraProject.LLU_SYSTEMS_PROVISIONING);
		projects.add(JiraProject.ATLASSIN_TST);
		final JComboBox jiraProjects = new JComboBox(projects.toArray());
		final JComboBox jiraProjectFixVersion = new JComboBox();

		JButton fixVersionButton = new JButton("Refresh");
		fixVersionButton.addActionListener(new ActionListener() {

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

}
