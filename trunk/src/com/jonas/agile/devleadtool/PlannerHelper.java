package com.jonas.agile.devleadtool;

import java.io.File;
import java.io.IOException;
import com.jonas.agile.devleadtool.component.InternalFrame;
import com.jonas.agile.devleadtool.component.panel.SaveDialog;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.data.PlannerDAO;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.JiraClient;

public class PlannerHelper {

   private String title;

   private BoardTableModel model;

   private InternalFrame internalFrame;

   public PlannerHelper(String title) {
      super();
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
         // TODO
         dao.saveBoardModel(selFile, activeInternalFrame.getBoardModel());
         dao.saveJiraModel(selFile, activeInternalFrame.getJiraModel());
         dao.savePlanModel(selFile, activeInternalFrame.getPlanModel());
         // TODO add save for plan and Jira
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

   public void addJiraToPlan(String jira) {
      JiraProject project = JiraProject.getProjectByKey(getProjectKey(jira));
      JiraClient client = project.getJiraClient();
      client.getJira(jira);
   }

   protected String getProjectKey(String jira) {
      if (jira.contains("-")) {
         return jira.substring(0, jira.indexOf("-"));
      }
      return jira;
   }

}
