package com.jonas.agile.devleadtool.burndown;

import java.util.Set;

public interface SprintBurndownGrapher {

   public void calculateAndPrintBurndown(double totalDevEstimates, double remainingDevEstimates, Set<String> projects);
   
}
