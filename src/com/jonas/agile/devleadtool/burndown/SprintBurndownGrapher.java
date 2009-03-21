package com.jonas.agile.devleadtool.burndown;

public interface SprintBurndownGrapher {

   public void calculateAndPrintBurndown(double totalDevEstimates, double remainingDevEstimates);
   
}
