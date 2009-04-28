package com.jonas.agile.devleadtool.burndown;

import java.util.Set;

public interface SprintBurndownGrapher {

   public void calculateAndPrintBurndown(double totalDevEstimates, double remainingDevEstimates, double totalQaEstimates, double remainingQaEstimates, Set<String> projects);
   
}
