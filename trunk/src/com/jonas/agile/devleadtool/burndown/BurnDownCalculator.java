package com.jonas.agile.devleadtool.burndown;


public interface BurnDownCalculator {

   public void calculateBurndownData();

   public double getRemainingEstimates();

   public double getTotalEstimates();

   public Comparable getKey();

}
