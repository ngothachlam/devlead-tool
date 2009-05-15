package com.jonas.agile.devleadtool.burndown;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.logging.MyLogger;

public class BurnDownDataDTO {
      private final Map<String, JiraBurndownStat> jiras;
      double remainingEstimates = 0d;
      double totalEstimates = 0d;
      private Set<String> jiraProjects = new HashSet<String>();
      private Logger log = MyLogger.getLogger(BurnDownDataDTO.class);

      public BurnDownDataDTO(Map<String, JiraBurndownStat> jiras) {
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