package com.jonas.agile.devleadtool.burndown;

import java.util.Set;

public interface SprintBurndownGrapher {

   public void calculateAndPrintBurndown(double totalEstimates, double remainingEstimates, Set<String> projects, Comparable estimateKey);
   
}
