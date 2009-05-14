package com.jonas.agile.devleadtool.burndown;

public class BurnDownCalculator {

   public Double calculateRemainingPointsForJira(boolean isInProgress, Double est, Double est2, Double rem, Double rem2) {
      if (isInProgress) {
         return new Double(rem + rem2);
      }
      return new Double(est + est2);
   }

}
