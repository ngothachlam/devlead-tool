package com.jonas.agile.devleadtool;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.panel.SaveDialog;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraClient;

public class PlannerHelper {

	private String title;

	private BoardTableModel model;

	private InternalFrame internalFrame;

	private Logger log = MyLogger.getLogger(PlannerHelper.class);

	private JFrame frame;

	public PlannerHelper(JFrame frame, String title) {
		this.frame = frame;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setActiveInternalFrame(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}

	public InternalFrame getActiveInternalFrame() {
		return internalFrame;
	}

	public void saveModels(File selFile, PlannerDAO dao) {
		try {
			InternalFrame activeInternalFrame = this.getActiveInternalFrame();
			dao.saveBoardModel(selFile, activeInternalFrame.getBoardModel());
			dao.saveJiraModel(selFile, activeInternalFrame.getJiraModel());
			dao.savePlanModel(selFile, activeInternalFrame.getPlanModel());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return new File(this.getActiveInternalFrame().getExcelFile());
	}

	public void setFile(File file) {
		this.getActiveInternalFrame().setExcelFile(file.getAbsolutePath());
	}

	public JiraIssue getJiraIssueFromName(String jira) {
		JiraProject project = JiraProject.getProjectByKey(getProjectKey(jira));
		log.debug("Project: " + project);
		JiraClient client = project.getJiraClient();
		try {
			client.login();
			log.debug("Client: " + client);
			return client.getJira(jira, project);
		} catch (Exception e) {
			AlertDialog.alertException(frame, e);
		}
		return null;
	}

	protected String getProjectKey(String jira) {
		if (jira.contains("-")) {
			return jira.substring(0, jira.indexOf("-"));
		}
		return jira;
	}

	public JFrame getParentFrame() {
		return frame;
	}

	public void addToPlan(String jira) {
		JiraIssue jiraIssue = getJiraIssueFromName(jira);
		getActiveInternalFrame().addToPlan(jiraIssue);
	}

}
