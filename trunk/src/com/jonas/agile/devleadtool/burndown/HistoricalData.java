package com.jonas.agile.devleadtool.burndown;

import java.util.Vector;

public class HistoricalData {

   private final Vector<String> cols;
   private final Vector<Vector<Object>> data;

   private Vector<Integer> daysInSprint = new Vector<Integer>();

   public HistoricalData(Vector<String> cols, Vector<Vector<Object>> data) {
      this.cols = cols;
      this.data = data;

      for (Vector<Object> vector : data) {
         // we know the dayinsprint is 2nd column
         Integer dayInSprint = Integer.parseInt(vector.get(1).toString());
         if (!daysInSprint.contains(dayInSprint)) {
            System.out.println("Day in Sprint: " + dayInSprint);
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

}
