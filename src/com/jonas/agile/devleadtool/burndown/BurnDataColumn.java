package com.jonas.agile.devleadtool.burndown;


public class BurnDataColumn implements Comparable<BurnDataColumn>{

   private Double x, y;
   

   public BurnDataColumn(Double x, Double y) {
      this.x = x;
      this.y = y;
   }

   public Double getY() {
      return y;
   }

   public Double getX() {
      return x;
   }

   @Override
   public int compareTo(BurnDataColumn o) {
      Double y2 = o.getX();
      Double y3 = getX();
      return (int) (y3.doubleValue()-y2.doubleValue());
   }
   
}
