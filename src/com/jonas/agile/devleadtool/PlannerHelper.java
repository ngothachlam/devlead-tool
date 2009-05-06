package com.jonas.agile.devleadtool;

import java.io.File;
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
import com.jonas.agile.devleadtool.properties.Property;
import com.jonas.agile.devleadtool.properties.SprinterProperties;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraClient;
import com.jonas.jira.access.JiraException;

public class PlannerHelper {

   private static Pattern jiraPattern = Pattern.compile("^[A-Z]+\\-\\d+$", Pattern.CASE_INSENSITIVE);

   private final static Logger log = MyLogger.getLogger(PlannerHelper.class);
   private static Matcher match = jiraPattern.matcher("");

   private JFrame frame;
   private MyInternalFrame internalFrame;

   private String title;

   private SprinterProperties properties;

   public SprintCache getSprintCache() {
      return getActiveInternalFrame().getSprintCache();
   }

   @Inject
   public PlannerHelper(MainFrame frame, @Named("plannerHelper.title") String title) {
      this.frame = frame;
      this.title = title;
   }

   public void setSprinterProperties(SprinterProperties properties) {
      this.properties = properties;
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

   public JiraIssue getJiraIssueFromName(String jira) throws JiraException, HttpException, IOException, JDOMException {
      String projectKey = getProjectKey(jira);
      JiraProject project = JiraProject.getProjectByKey(projectKey);
      if (project == null) {
         throw new JiraException("Jira \"" + jira + "\" doesn't have a project related to it!");
      }
      JiraClient client = project.getJiraClient();
      return client.getJira(jira);
   }

   public JFrame getParentFrame() {
      return frame;
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

   public File getSaveDirectory() {
      Object propertyObject = properties.getPropertyObject(Property.SAVE_DIRECTORY);
      return (File) propertyObject;
   }

   public File getExcelFile() {
      return getActiveInternalFrame().getExcelFile();
   }

}
