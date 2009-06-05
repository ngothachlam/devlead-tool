package com.jonas.stats.charts.excel;

public class StatChartCategory {

   public final static StatChartCategory REQUESTS = new StatChartCategory("Requests");

   private final String name;

   public StatChartCategory(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   @Override
   public String toString() {
      return name;
   } 
   
}
