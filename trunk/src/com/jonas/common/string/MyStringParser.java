package com.jonas.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MyStringParser {

   public List<String> separateString(String string, String delimeters) {
      List<String> separateStrings = new ArrayList<String>();
      if(string == null)
         return separateStrings;
      StringTokenizer st = new StringTokenizer(string, delimeters);
      while (st.hasMoreTokens()) {
         separateStrings.add(st.nextToken());
      }
      return separateStrings;
   }

}
