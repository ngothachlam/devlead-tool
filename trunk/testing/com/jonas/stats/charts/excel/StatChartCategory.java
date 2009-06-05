package com.jonas.stats.charts.excel;

public class StatChartCategory {

   @Override
   public String toString() {
      return name;
   }

   public String getName() {
      return name;
   }

   private final String name;

   public StatChartCategory(String name) {
      this.name = name;
   }

   public final static StatChartCategory REQUESTS = new StatChartCategory("Requests"); 
   
}
