package com.jonas.agile.devleadtool;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import com.jonas.agile.devleadtool.component.MyInternalFrame;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;
import com.jonas.jira.access.listener.JiraListener;

public class PlannerHelper {

   public static String getProjectKey(String jira) {
      if (jira.contains("-")) {
         return jira.substring(0, jira.indexOf("-"));
      }
      return jira;
   }

   private JFrame frame;
   private MyInternalFrame internalFrame;

   Pattern jiraPattern = Pattern.compile("^[A-Z]+\\-\\d+$", Pattern.CASE_INSENSITIVE);
   Matcher match = jiraPattern.matcher("");

   private Logger log = MyLogger.getLogger(PlannerHelper.class);


   private PlannerCommunicator plannerCommunicator = new PlannerCommunicator(this);

   private String title;

   public PlannerHelper(JFrame frame, String title) {
      this.frame = frame;
      this.title = title;
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
         //FIXME how often to login!
         client.login();
         return client.getJira(jira);
      } finally {
         if (jiraListener != null)
            JiraListener.removeJiraListener(jiraListener);
      }
   }


   public String getJiraUrl(String jira) throws NotJiraException {
      log.debug("getting Jira URL for " + jira);
      String projectKey = getProjectKey(jira);
      JiraProject project = JiraProject.getProjectByKey(projectKey);
      if (project == null || !isJiraString(jira)) {
         throw new NotJiraException("\"" + jira + "\" is not a jira string!");
      }
      return project.getJiraClient().getJiraUrl();
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

   protected boolean isJiraString(String jiraNo) {
      match.reset(jiraNo);
      if (match.matches()) {
         return true;
      }
      return false;
   }

   public void setActiveInternalFrame(MyInternalFrame internalFrame) {
      this.internalFrame = internalFrame;
   }

}
