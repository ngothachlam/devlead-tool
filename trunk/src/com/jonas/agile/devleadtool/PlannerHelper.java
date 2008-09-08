package com.jonas.agile.devleadtool;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.jira.access.JiraIssueNotFoundException;
import com.jonas.jira.access.JiraListener;

public class PlannerHelper {

	private String title;

	Pattern jiraPattern = Pattern.compile("^[A-Z]+\\-\\d+$", Pattern.CASE_INSENSITIVE);
	Matcher match = jiraPattern.matcher("");
	
	private InternalFrame internalFrame;

	private Logger log = MyLogger.getLogger(PlannerHelper.class);

	private JFrame frame;

	private PlannerCommunicator plannerCommunicator = new PlannerCommunicator(this);

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

	public void saveModels(PlannerDAO dao) {
		try {
			InternalFrame activeInternalFrame = this.getActiveInternalFrame();
			dao.saveBoardModel( activeInternalFrame.getBoardModel());
			dao.savePlanModel( activeInternalFrame.getPlanModel());
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

	public JiraIssue getJiraIssueFromName(String jira, JiraListener jiraListener) throws JiraException, JiraIssueNotFoundException, HttpException, IOException {
		if (jiraListener != null)
			JiraListener.addJiraListener(jiraListener);
		try {
			JiraProject project = JiraProject.getProjectByKey(getProjectKey(jira));
			log.debug("Project: " + project + " for jira " + jira);
			if (project == null) {
				throw new JiraException("Jira \"" + jira + "\" doesn't have a project related to it!");
			}
			JiraClient client = project.getJiraClient();
			client.login();
			log.debug("Client: " + client);
			return client.getJira(jira, project);
		} finally {
			if (jiraListener != null)
				JiraListener.removeJiraListener(jiraListener);
		}
	}

	public static String getProjectKey(String jira) {
		if (jira.contains("-")) {
			return jira.substring(0, jira.indexOf("-"));
		}
		return jira;
	}

	public JFrame getParentFrame() {
		return frame;
	}

	public void addToPlan(String jira, boolean syncWithJira) throws HttpException, JiraException, JiraIssueNotFoundException, IOException {
//		InternalFrame activeInternalFrame = getActiveInternalFrame();
//		if (!activeInternalFrame.doesJiraExistInPlan(jira)) {
//			JiraIssue jiraIssue = syncWithJira ? getJiraIssueFromName(jira, null) : new JiraIssue(jira, "unknown", "unknown", "unknown");
//			activeInternalFrame.addToPlan(jiraIssue);
//		}
		InternalFrame activeInternalFrame = getActiveInternalFrame();
		if (!activeInternalFrame.doesJiraExistInPlan(jira)) {
		   activeInternalFrame.addToPlan(jira);
		}
	}

	public String getJiraUrl(String jira) throws NotJiraException {
		log.debug("getting Jira URL for " + jira);
		String projectKey = getProjectKey(jira);
		JiraProject project = JiraProject.getProjectByKey(projectKey);
		if (project == null || !isJiraString(jira)){
			throw new NotJiraException("\""+jira+"\" is not a jira string!");
		}
		return project.getJiraClient().getJiraUrl();
	}

	protected boolean isJiraString(String jiraNo) {
		match.reset(jiraNo);
		if (match.matches()) {
			return true;
		}
		return false;
	}

	public PlannerCommunicator getPlannerCommunicator() {
		return plannerCommunicator;
	}
}
