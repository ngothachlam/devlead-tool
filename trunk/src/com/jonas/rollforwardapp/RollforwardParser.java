package com.jonas.rollforwardapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RollforwardParser {

   private Pattern pattern = Pattern.compile("http://your.host.address/viewcvs.cgi/(.*\\.sql)/\\?", Pattern.MULTILINE);

   public List<String> parseJiraHTMLAndGetSqlRollForwards(String html) {
      return matchAndReturnFirstGroup(html, pattern);
   }

   protected List<String> matchAndReturnFirstGroup(String string, Pattern pattern) {
      List<String> results = new ArrayList<String>();

      Matcher m = pattern.matcher(string);
      while (m.find()) {
         //group 0 is the overall match. group 1 is the first instance using parenthesis.
         results.add(m.group(1));
      }
      return results;
   }

}
