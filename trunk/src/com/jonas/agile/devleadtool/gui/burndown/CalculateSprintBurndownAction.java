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
      double remainingEstimates = 0d;
      double totalEstimates = 0d;
      private Set<String> jiraProjects = new HashSet<String>();
      private Logger log = MyLogger.getLogger(BurnDownDataDTO.class);

      protected BurnDownDataDTO(Map<String, JiraBurndownStat> jiras) {
         this.jiras = jiras;
      }

      public void calculateBurndownData() {
         for (JiraBurndownStat jiraStat : jiras.values()) {

            totalEstimates += jiraStat.getTotalEstimate();

            String jiraProject = PlannerHelper.getProjectKey(jiraStat.getJira());
            if (!jiraProjects.contains(jiraProject)) {
               jiraProjects.add(jiraProject);
            }

            remainingEstimates += jiraStat.getRemainingEstimate();

            if (log.isDebugEnabled()) {
               log.debug("\t jira: " + jiraStat.getJira() + 
                     " remaining Estimate: " + remainingEstimates + " (added by " + jiraStat.getRemainingEstimate() + ")" + 
                     " total Estimate: " + totalEstimates+ " (added by " + jiraStat.getTotalEstimate() + ")") ;
            }

         }
      }

      public double getRemainingEstimates() {
         return remainingEstimates;
      }

      public double getTotalEstimates() {
         return totalEstimates;
      }

      public Set<String> getJiraProjects() {
         return jiraProjects;
      }

      public Comparable getKey() {
         return "Progression";
      }

   }

   private class JiraBurndownStat {
      private String jira;
      private double totalEstimate = 0d;
      private double remainingEstimate = 0d;
      private Logger log = MyLogger.getLogger(BoardStatsFrame.class);

      public JiraBurndownStat(String jira, MyTable boardTable, int row) {
         this.jira = jira;

         Sprint sprint = (Sprint) boardTable.getValueAt(ColumnType.Sprint, row);
         SprintTime time = sprint.calculateTime();

         BoardStatusValue boardStatus = (BoardStatusValue) boardTable.getValueAt(ColumnType.BoardStatus, row);

         switch (time) {
            case currentSprint:
               this.totalEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DEst, row));
               this.totalEstimate += StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QEst, row));

               switch (boardStatus) {
                  case Failed:
                  case InProgress:
                     this.remainingEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DRem, row));
                     this.remainingEstimate += StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QRem, row));
                     break;
                  case Resolved:
                     this.remainingEstimate += StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QRem, row));
                     break;
                  default:
                     this.remainingEstimate = StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.DEst, row));
                     this.remainingEstimate += StringHelper.getDoubleOrZero(boardTable.getValueAt(ColumnType.QEst, row));
                     break;
               }
               break;
         }
      }

      public String getJira() {
         return jira;
      }

      public double getTotalEstimate() {
         return totalEstimate;
      }

      public double getRemainingEstimate() {
         return remainingEstimate;
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
      grapher.calculateAndPrintBurndown(burnDownDataDTO.getTotalEstimates(), burnDownDataDTO.getRemainingEstimates(), burnDownDataDTO.getJiraProjects(), burnDownDataDTO.getKey());
   }

}