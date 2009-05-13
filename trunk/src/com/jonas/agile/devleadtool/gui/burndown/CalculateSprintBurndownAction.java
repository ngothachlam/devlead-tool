package com.jonas.agile.devleadtool.gui.burndown;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.burndown.SprintBurndownGrapher;
import com.jonas.agile.devleadtool.gui.action.BasicAbstractGUIAction;
import com.jonas.agile.devleadtool.gui.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.gui.component.frame.BoardStatsFrame;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintTime;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.StringHelper;

public class CalculateSprintBurndownAction extends BasicAbstractGUIAction {

   private class BurnDownDataDTO {
      private final Map<String, JiraBurndownStat> jiras;
      double remainingDevEstimates = 0d;
      double remainingQaEstimates = 0d;
      double totalDevEstimates = 0d;
      double totalQaEstimates = 0d;
      private Set<String> jiraProjects = new HashSet<String>();

      protected BurnDownDataDTO(Map<String, JiraBurndownStat> jiras) {
         this.jiras = jiras;
      }

      public void calculateBurndownData() {
         for (JiraBurndownStat statrow : jiras.values()) {

            if (statrow.isInSprint()) {
               totalDevEstimates += statrow.getDevEstimate();
               totalQaEstimates += statrow.getQaEstimate();

               String jiraProject = PlannerHelper.getProjectKey(statrow.getJira());
               if (!jiraProjects.contains(jiraProject)) {
                  jiraProjects.add(jiraProject);
               }

               if (statrow.isInDevProgress()) {
                  remainingDevEstimates += statrow.getRemainingDevEstimate();
               } else {
                  remainingDevEstimates += statrow.getDevEstimate();
               }
               if (statrow.isInTestProgress()) {
                  remainingQaEstimates += statrow.getRemainingQaEstimate();
               } else {
                  remainingQaEstimates += statrow.getQaEstimate();
               }
            }
         }
      }

      public double getRemainingDevEstimates() {
         return remainingDevEstimates;
      }

      public double getTotalDevEstimates() {
         return totalDevEstimates;
      }

      public Set<String> getJiraProjects() {
         return jiraProjects;
      }

      public double getTotalQaEstimates() {
         return totalQaEstimates;
      }

      public double getRemainingQaEstimates() {
         return remainingQaEstimates;
      }

   }

   private class JiraBurndownStat {
      public boolean isInSprint() {
         return isInSprint;
      }

      private String jira;
      private double devEstimate = 0d;
      private double qaEstimate = 0d;
      private double remainingDevEstimate = 0d;
      private double remainingQaEstimate = 0d;
      private boolean isInDevProgress = false;
      private boolean isInQaProgress = false;
      private boolean isInSprint = false;
      private Logger log = MyLogger.getLogger(BoardStatsFrame.class);

      public JiraBurndownStat(String jira, MyTable boardTable, int row) {
         this.jira = jira;

         Sprint sprint = (Sprint) boardTable.getValueAt(ColumnType.Sprint, row);
         SprintTime time = sprint.calculateTime();

         BoardStatusValue boardStatus = (BoardStatusValue) boardTable.getValueAt(ColumnType.BoardStatus, row);
         log.debug("Jira: " + jira + " in sprint " + sprint + " (" + time + ") and boardStatus " + boardStatus);

         switch (time) {
            case currentSprint:
               this.isInSprint = true;
               this.devEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DEst, row));
               this.qaEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QEst, row));

               switch (boardStatus) {
                  case NA:
                  case UnKnown:
                  case Open:
                     break;
                  case Failed:
                  case InProgress:
                     this.isInDevProgress = true;
                     this.isInQaProgress = true;
                     this.remainingDevEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DRem, row));
                     this.remainingQaEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QRem, row));
                     break;
                  case Resolved:
                     this.isInQaProgress = true;
                     this.remainingQaEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QRem, row));
                     break;
                  case Complete:
                  case Approved:
                  case ForShowCase:
                     break;
               }
               log.debug("\t " + boardStatus + " Dest: " + devEstimate + " QEst: " + qaEstimate + " DRem: " + remainingDevEstimate + " QRem: " + remainingQaEstimate);
               break;
         }
      }

      public double getRemainingQaEstimate() {
         return remainingQaEstimate;
      }

      public boolean isInTestProgress() {
         return isInQaProgress;
      }

      public String getJira() {
         return jira;
      }

      public double getDevEstimate() {
         return devEstimate;
      }

      public double getQaEstimate() {
         return qaEstimate;
      }

      public double getRemainingDevEstimate() {
         return remainingDevEstimate;
      }

      public boolean isInDevProgress() {
         return isInDevProgress;
      }
   }

   private class JiraStatsDataDTO {

      private Set<String> duplicateJiras;
      private Map<String, JiraBurndownStat> jiras;
      private final MyTable sourceTable;

      public JiraStatsDataDTO(MyTable sourceTable) {
         this.sourceTable = sourceTable;
      }

      public void calculateJiraStats() {
         jiras = new HashMap<String, JiraBurndownStat>();
         duplicateJiras = new HashSet<String>();
         for (int row = 0; row < sourceTable.getRowCount(); row++) {
            String jira = (String) sourceTable.getValueAt(ColumnType.Jira, row);
            if (!jiras.containsKey(jira)) {
               JiraBurndownStat statRow = new JiraBurndownStat(jira, sourceTable, row);
               jiras.put(jira, statRow);
            } else {
               duplicateJiras.add(jira);
            }
         }
      }

      public Set<String> getDuplicateJiras() {
         return duplicateJiras;
      }

      public Map<String, JiraBurndownStat> getJiras() {
         return jiras;
      }

   }

   private final SprintBurndownGrapher grapher;

   private final MyTable sourceTable;

   public CalculateSprintBurndownAction(String name, String description, Frame parentFrame, MyTable sourceTable, SprintBurndownGrapher grapher) {
      super(name, description, parentFrame);
      this.sourceTable = sourceTable;
      this.grapher = grapher;
   }

   private void alertOnDuplicateJirasIfTheraAreAny(Set<String> duplicateJiras) {
      if (duplicateJiras.size() > 0) {
         StringBuffer sb = new StringBuffer();
         for (String duplicateJira : duplicateJiras) {
            sb.append(duplicateJira).append(" ");
         }

         AlertDialog
               .alertMessage(
                     getParentFrame(),
                     "Duplicate Jiras!",
                     "The following jiras have been found more than once in the board Panel. The first instance of the jiras will be included in the graph. \n\nTo ensure correct burndown, delete the duplicate jiras and ensure the data in the Board Panel is correct",
                     sb.toString());
      }
   }

   @Override
   public void doActionPerformed(ActionEvent e) {
      JiraStatsDataDTO jiraStatsDataDTO = new JiraStatsDataDTO(sourceTable);
      jiraStatsDataDTO.calculateJiraStats();

      BurnDownDataDTO burnDownDataDTO = new BurnDownDataDTO(jiraStatsDataDTO.getJiras());
      burnDownDataDTO.calculateBurndownData();

      alertOnDuplicateJirasIfTheraAreAny(jiraStatsDataDTO.getDuplicateJiras());
      grapher
            .calculateAndPrintBurndown(burnDownDataDTO.getTotalDevEstimates(), burnDownDataDTO.getRemainingDevEstimates(), burnDownDataDTO.getTotalQaEstimates(), burnDownDataDTO.getRemainingQaEstimates(), burnDownDataDTO.getJiraProjects());
   }

}