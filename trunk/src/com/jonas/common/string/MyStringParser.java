package com.jonas.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class MyStringParser {

   private Pattern splitPattern = Pattern.compile("'");
   
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
   
   public String getRegexGroup(String text, int group){
      String[] split = splitPattern.split(text);
      if(group >= split.length)
         return "";
      return split[group];
   }

}
