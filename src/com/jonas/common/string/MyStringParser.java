package com.jonas.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MyStringParser {

   public List<String> separateString(String string, String delimeters) {
      StringTokenizer st = new StringTokenizer(string, delimeters);
      List<String> separateStrings = new ArrayList<String>();
      while (st.hasMoreTokens()) {
         separateStrings.add(st.nextToken());
      }
      return separateStrings;
   }

}
