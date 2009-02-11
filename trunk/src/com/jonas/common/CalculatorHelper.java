package com.jonas.common;

import java.text.DecimalFormat;

public class CalculatorHelper {
   private static DecimalFormat decimalFormat = new DecimalFormat("#");

   public static String getSecondsAsDays(String seconds) {
      int intSeconds = new Float(seconds).intValue();
      float floatDays = getSecondsAsDays(intSeconds);
      return String.valueOf(floatDays);
   }

   public static float getSecondsAsDays(int seconds) {
      float secondsConverter = 60 * 60 * 8;
      return seconds / secondsConverter;
   }

   public static String cutString(String string, int cutAfter, String replacementString) {
      return string.length() > cutAfter ? string.substring(0, cutAfter) + replacementString : string;
   }

   public static String getSecondsAsDaysAndString(int seconds) {
      float days = getSecondsAsDays(seconds);
      return Float.toString(days);
   }

   public static Double getDouble(String boardEstimate) {
      try {
         return Double.parseDouble(boardEstimate);
      } catch (NumberFormatException ex) {
         return null;
      }
   }

   public static String getPercentage(float percentage) {
      Float float1 = new Float(percentage);
      if (float1.isNaN())
         return "0%";
      return decimalFormat.format(percentage) + "%";
   }

   public static String getPercentage(String percentage) {
      return decimalFormat.format(percentage) + "%";
   }
}
