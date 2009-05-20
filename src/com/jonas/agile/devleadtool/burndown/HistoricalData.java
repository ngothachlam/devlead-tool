package com.jonas.agile.devleadtool.burndown;

import java.util.Vector;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.sprint.Sprint;

public class HistoricalData {

   private final Vector<String> cols;
   private final Vector<Vector<Object>> data;
   private Integer sprintLocation = null;

   private Vector<Integer> daysInSprint = new Vector<Integer>();

   public HistoricalData(Vector<String> cols, Vector<Vector<Object>> data) {
      this.cols = cols;
      this.data = data;

      for (int counter = 0; counter < cols.size(); counter++) {
         if (cols.get(counter).equals(ColumnType.Sprint.toString())) {
            sprintLocation = counter;
            break;
         }
      }

      for (Vector<Object> vector : data) {
         // we know the dayinsprint is 2nd column
         Integer dayInSprint = Integer.parseInt(vector.get(1).toString());

         if (!daysInSprint.contains(dayInSprint)) {
            daysInSprint.add(dayInSprint);
         }
      }
   }

   public Vector<String> getHeaders() {
      return cols;
   }

   public Vector<Vector<Object>> getBody() {
      return data;
   }

   public boolean hasHeader() {
      return cols != null && cols.size() > 2;
   }

   public String getHeaderAsCSV(String delimiter) {
      StringBuffer sb = new StringBuffer();
      for (String column : cols) {
         sb.append(column).append(delimiter);
      }
      return deleteLastCharAndReplaceWithNewline(sb).toString();
   }

   private StringBuffer deleteLastCharAndReplaceWithNewline(StringBuffer sb) {
      return sb.deleteCharAt(sb.length() - 1).append("\n");
   }

   public Vector<Vector<Object>> getBodyLinesThatAreNotForThisDayInSprint(Sprint sprint, int dayOfSprint) {
      Vector<Vector<Object>> newVector = new Vector<Vector<Object>>();
      for (Vector<Object> oldVector : data) {
         if (!isVectorForThisSprintAndDayInSprint(oldVector, sprint, dayOfSprint)) {
            newVector.add(oldVector);
         }
      }
      return newVector;
   }

   private boolean isVectorForThisSprintAndDayInSprint(Vector<Object> oldVector, Sprint sprint, Integer dayOfSprint) {
      String vectorDayOfSprint = oldVector.get(1).toString();
      Object object = oldVector.get(sprintLocation);
      String vectorSprint = object.toString();

      if (vectorDayOfSprint.equals(dayOfSprint.toString()) && vectorSprint.equals(sprint.toString())) {
         return true;
      }
      return false;
   }

}
