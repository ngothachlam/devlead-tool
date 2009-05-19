package com.jonas.agile.devleadtool.burndown;

import java.util.Vector;

public class HistoricalDataDTO {

   private final Vector<String> cols;
   private final Vector<Vector<Object>> data;

   public HistoricalDataDTO(Vector<String> cols, Vector<Vector<Object>> data) {
      this.cols = cols;
      this.data = data;
   }

   public Vector<String> getHeaders() {
      return cols;
   }
   
   public Vector<Vector<Object>> getBody(){
      return data;
   }

}
