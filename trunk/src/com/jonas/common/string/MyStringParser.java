package com.jonas.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MyStringParser {

   private static final String delims = " ,;.\t";

   public List<String> separateString(String string) {
      StringTokenizer st = new StringTokenizer(string, delims);
      List<String> separateStrings = new ArrayList<String>();
      while (st.hasMoreTokens()) {
         separateStrings.add(st.nextToken());
      }
      return separateStrings;
   }

}
