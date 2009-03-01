package com.jonas.common.logging;

public class Peformancer {

   private static long lastTimestamp = System.currentTimeMillis(); 
   
   public static void checkpoint(String string) {
      long thisTimeStamp = System.currentTimeMillis();
      StringBuffer sb = new StringBuffer(string);
      sb.append(" (Millis passed: ").append(thisTimeStamp - lastTimestamp).append(")");
      System.out.println(sb.toString());
      lastTimestamp = thisTimeStamp;
   }

}
