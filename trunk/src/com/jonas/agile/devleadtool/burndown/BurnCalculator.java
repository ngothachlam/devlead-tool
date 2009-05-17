package com.jonas.agile.devleadtool.burndown;


public interface BurnCalculator {

   public void calculateBurnData();

   public double getRemainingEstimates();

   public double getTotalEstimates();

   public Comparable getKey();

}
