package com.jonas.common;

import java.awt.Color;

public class ColorUtil {

   public static Color darkenColor(Color color, int alpha) {
      int red = color.getRed() + alpha;
      int green = color.getGreen() + alpha;
      int blue = color.getBlue() + alpha;
      return new Color(getRGB(red), getRGB(green), getRGB(blue));
   }

   static int getRGB(int value) {
      return getDivision(value, 255);
   }

   static int getDivision(int i, int limiter) {
      int remainder = i % (limiter + 1);
      int j = i >= limiter ? limiter : remainder;
      return j < 0 ? 0 : j;
   }

}
