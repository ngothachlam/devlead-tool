package com.jonas.agile.devleadtool;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jonas.agile.devleadtool.gui.component.MyInternalFrame;
import com.jonas.agile.devleadtool.gui.component.frame.main.MainFrame;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.jira.access.listener.JiraListener;

public class PlannerHelper {

   private static Pattern jiraPattern = Pattern.compile("^[A-Z]+\\-\\d+$", Pattern.CASE_INSENSITIVE);

   private final static Logger log = MyLogger.getLogger(PlannerHelper.class);
   private static Matcher match = jiraPattern.matcher("");

   private JFrame frame;
   private MyInternalFrame internalFrame;
   private PlannerCommunicator plannerCommunicator = new PlannerCommunicator(this);

   private String title;

   public PlannerHelper() {
   }
   
   @Inject
   public PlannerHelper(MainFrame frame, @Named("plannerHelper.title") String title) {
      this.frame = frame;
      this.title = title;
   }

   public static String getJiraUrl(String jira) throws NotJiraException {
      log.debug("getting Jira URL for " + jira);
      String projectKey = getProjectKey(jira);
      JiraProject project = JiraProject.getProjectByKey(projectKey);
      if (project == null || !isJiraString(jira)) {
         throw new NotJiraException("\"" + jira + "\" is not a jira string!");
      }
      return project.getJiraClient().getJiraUrl();
   }

   public static String getProjectKey(String jira) {
      if (jira.contains("-")) {
         return jira.substring(0, jira.indexOf("-"));
      }
      return jira;
   }

   protected static boolean isJiraString(String jiraNo) {
      match.reset(jiraNo);
      if (match.matches()) {
         return true;
      }
      return false;
   }


   public MyInternalFrame getActiveInternalFrame() {
      return internalFrame;
   }

   public JiraIssue getJiraIssueFromName(String jira, JiraListener jiraListener) throws JiraException, HttpException, IOException, JDOMException {
      if (jiraListener != null)
         JiraListener.addJiraListener(jiraListener);
      try {
         String projectKey = getProjectKey(jira);
         JiraProject project = JiraProject.getProjectByKey(projectKey);
         if (project == null) {
            throw new JiraException("Jira \"" + jira + "\" doesn't have a project related to it!");
         }
         JiraClient client = project.getJiraClient();
         return client.getJira(jira);
      } finally {
         if (jiraListener != null)
            JiraListener.removeJiraListener(jiraListener);
      }
   }

   public JFrame getParentFrame() {
      return frame;
   }

   public PlannerCommunicator getPlannerCommunicator() {
      return plannerCommunicator;
   }

   public String getTitle() {
      return title;
   }

   public void setActiveInternalFrame(MyInternalFrame internalFrame) {
      this.internalFrame = internalFrame;
   }

   public void login() throws HttpException, IOException, JiraException {
      JiraClient.JiraClientAolBB.login();
   }

}
