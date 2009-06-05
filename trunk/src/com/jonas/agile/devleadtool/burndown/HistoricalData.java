package com.jonas.agile.devleadtool.burndown;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.common.logging.MyLogger;

public class HistoricalData extends ContentsDto {

   private Integer sprintLocation = null;
   private static final Logger log = MyLogger.getLogger(HistoricalData.class);

   private Vector<Integer> daysInSprint = new Vector<Integer>();

   public HistoricalData(Vector<String> header, Vector<Vector<Object>> body) {
      super(header, body);

      log.debug("HistoricalData ");
      for (int counter = 0; counter < header.size(); counter++) {
         log.debug("\t" + counter);
         if (header.get(counter).equals(ColumnType.Sprint.toString())) {
            sprintLocation = counter;
            log.debug("\tSprint location is " + counter);
            break;
         }
      }

      for (Vector<Object> vector : body) {
         // we know the dayinsprint is 2nd column
         Integer dayInSprint = Integer.parseInt(vector.get(1).toString());

         if (!daysInSprint.contains(dayInSprint)) {
            daysInSprint.add(dayInSprint);
         }
      }
   }

   public boolean hasHeader() {
      return getHeader() != null && getHeader().size() > 2;
   }

   public String getHeaderAsCSV(String delimiter) {
      StringBuffer sb = new StringBuffer();
      for (String column : getHeader()) {
         sb.append(column).append(delimiter);
      }
      return deleteLastCharAndReplaceWithNewline(sb).toString();
   }

   private StringBuffer deleteLastCharAndReplaceWithNewline(StringBuffer sb) {
      return sb.deleteCharAt(sb.length() - 1).append("\n");
   }

   public BodyLineStatDTO getBodyLinesThatAreNotForThisDayInSprint(Sprint sprint, int dayOfSprint, Integer colForJira, Integer colForSprint, Integer colForDayInSprint) {
      Vector<Vector<Object>> newVector = new Vector<Vector<Object>>();
      for (Vector<Object> oldVector : getBody()) {
         if (!isVectorForThisSprintAndDayInSprint(oldVector, sprint, dayOfSprint)) {
            newVector.add(oldVector);
         }
      }
      return new BodyLineStatDTO(newVector, colForJira, colForSprint, colForDayInSprint);
   }

   private boolean isVectorForThisSprintAndDayInSprint(Vector<Object> oldVector, Sprint sprint, Integer dayOfSprint) {
      String vectorDayOfSprint = oldVector.get(1).toString();
      Object object = oldVector.get(sprintLocation);
      String vectorSprint = object.toString();

      if (log.isDebugEnabled()) {
         log.debug("vectorDayOfSprint: " + vectorDayOfSprint + " dayOfSprint.toString(): " + dayOfSprint.toString() + " vectorSprint: " + vectorSprint + " sprint.toString(): " + sprint.toString());
      }
      if (vectorDayOfSprint.equals(dayOfSprint.toString()) && vectorSprint.equals(sprint.toString())) {
         log.debug("...true");
         return true;
      }
      log.debug("...false");
      return false;
   }
}
