package com.jonas.agile.devleadtool.burndown;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.common.logging.MyLogger;

public class BodyLineStatDTO {

   private final Vector<Vector<Object>> newVector;
   private final int colForJira;
   private final int colForDayInSprint;
   private final int colForSprint;
   private Logger log = MyLogger.getLogger(BodyLineStatDTO.class);

   public BodyLineStatDTO(Vector<Vector<Object>> newVector, int colForJira, int colForSprint, int colForDayInSprint) {
      this.newVector = newVector;
      this.colForJira = colForJira;
      this.colForSprint = colForSprint;
      this.colForDayInSprint = colForDayInSprint;
   }

   public Vector<Vector<Object>> getNewVector() {
      return newVector;
   }

   public boolean alreadyExists(String jira, Integer dayOfSprint, Sprint sprint) {
      if (log.isDebugEnabled()) {
         log.debug("exists jira: " + jira + " (colForJira:" + colForJira + ") in dayOfSprint: " + dayOfSprint + " (colForDayInSprint:" + colForDayInSprint + ") and sprint: " + sprint + " (colForSprint: " + colForSprint + ")");
      }
      for (Vector<Object> row : newVector) {
         String jiraInRow = row.get(colForJira).toString();
         String dayOfSprintInRow = row.get(colForDayInSprint).toString();
         String sprintInRow = row.get(colForSprint).toString();

         if (log.isDebugEnabled()) {
            log.debug("\tjiraInRow: \"" + jiraInRow + "\" (" + jiraInRow.equals(jira) + ") in dayOfSprintInRow: \"" + dayOfSprintInRow + "\" (" + dayOfSprintInRow.equals(dayOfSprint) + ") and sprintInRow: \"" + sprintInRow + "\"");
         }
         if (jiraInRow.equals(jira.toString()) && dayOfSprintInRow.equals(dayOfSprint.toString()) && sprintInRow.equals(sprint.toString())) {
            log.debug("MATCH FOUND!!");
            return true;
         }
      }
      return false;
   }

}
