package com.jonas.rollforwardapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RollforwardParser {

   private Pattern pattern = Pattern.compile("http://your.host.address/viewcvs.cgi/(.*)/\\?)");

   public String[] parseJiraHTMLAndGetSqlRollForwards(String inputStr) {
      Matcher matcher = pattern.matcher(inputStr);

      boolean matchFound = matcher.find();
      if (matchFound) {
         int groupCount = matcher.groupCount();
         String[] result = new String[groupCount];
         for (int i = 0; i <= groupCount-1; i++) {
            result[i] = matcher.group(i+1);
         }
         return result;
      }
      return null;

   }

}
