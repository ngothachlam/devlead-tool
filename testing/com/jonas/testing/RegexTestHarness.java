package com.jonas.testing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;

public class RegexTestHarness extends TestCase {

   public void testTegex() {
      regex("L-1");
      regex("L-1'2'3'4:5");
   }

   private void regex(String matcherString) {
      System.out.println("regex: "+ matcherString);
      Pattern pattern = Pattern.compile("^([a-z\\-0-9]*)", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(matcherString);

      matcher.find();
      String jira = matcher.group(1);
      System.out.println("\tjira: "+ jira);
      
      pattern = Pattern.compile("'([0-9]*)", Pattern.CASE_INSENSITIVE);
      matcher = pattern.matcher(matcherString);
      while (matcher.find()) {
         System.out.println("Found \"" + matcher.group() + "\" starting at " + matcher.start() + " ending at " + matcher.end());
         for (int group = 0; group <= matcher.groupCount(); group++) {
            System.out.println("\tGroup " + group + " \"" + matcher.group(group) + "\"");
         }
      }
      }
//      boolean found = false;
//      while (matcher.find()) {
//         System.out.println("Found \"" + matcher.group() + "\" starting at " + matcher.start() + " ending at " + matcher.end());
//         found = true;
//
//         for (int group = 0; group <= matcher.groupCount(); group++) {
//            System.out.println("\tGroup " + group + " \"" + matcher.group(group) + "\"");
//         }
//      }
//      if (!found) {
//         System.out.println("No match found.%n");
//      }
}
