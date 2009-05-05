package com.jonas.common.logging;

public class PerfTimer {

   private static long lastMillis;

   public static void log(String string) {
      long currentMillis = System.currentTimeMillis();
      System.out.print(string);
      System.out.print(" (took ");
      System.out.print(currentMillis - lastMillis);
      System.out.println("ms)");
      lastMillis = currentMillis;
   }

}
