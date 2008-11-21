package com.jonas.common;


public class CalculatorHelper {

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
}
