package com.jonas.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jonas.agile.devleadtool.dto.JiraStringDTO;

public class JiraRegexParser {

   private Pattern jiraPattern = Pattern.compile("^([a-z\\-0-9]*)", Pattern.CASE_INSENSITIVE);
   private Pattern estimatePattern = Pattern.compile("'([0-9.,a-z]*)");
   private Pattern remainderPattern = Pattern.compile(":([0-9.,a-z]*)");

   private String getStringAsNullIfEmpty(Matcher matcher) {
      String group = matcher.group(1);
      return group.trim().length() == 0 ? null : group;
   }

   public List<String> separateIntoJiras(String string) {
      return this.separateString(string, " \t\n");
   }

   public JiraStringDTO separateJira(String matcherString) {
      JiraStringDTO jiraStringDTO = new JiraStringDTO();

      setJiraNameOnDTO(matcherString, jiraStringDTO);
      setEstimatesAndActualsOnDTO(matcherString, jiraStringDTO);
      setRemainderOnDTO(matcherString, jiraStringDTO);

      return jiraStringDTO;
   }

   List<String> separateString(String string, String delimeters) {
      List<String> separateStrings = new ArrayList<String>();
      if (string == null)
         return separateStrings;
      StringTokenizer st = new StringTokenizer(string, delimeters);
      while (st.hasMoreTokens()) {
         separateStrings.add(st.nextToken());
      }
      return separateStrings;
   }

   private void setEstimatesAndActualsOnDTO(String matcherString, JiraStringDTO jiraStringDTO) {
      Matcher matcher = estimatePattern.matcher(matcherString);
      int occurence = 0;
      while (matcher.find()) {
         switch (occurence++) {
         case 0:
            jiraStringDTO.setDevEstimate(getStringAsNullIfEmpty(matcher));
            break;
         case 1:
            jiraStringDTO.setDevActual(getStringAsNullIfEmpty(matcher));
            break;
         case 2:
            jiraStringDTO.setQAEstimate(getStringAsNullIfEmpty(matcher));
            break;
         }
      }
   }

   private void setJiraNameOnDTO(String matcherString, JiraStringDTO jiraStringDTO) {
      Matcher matcher = jiraPattern.matcher(matcherString);
      if (matcher.find()) {
         jiraStringDTO.setJira(getStringAsNullIfEmpty(matcher));
      }
   }

   private void setRemainderOnDTO(String matcherString, JiraStringDTO jiraStringDTO) {
      Matcher matcher = remainderPattern.matcher(matcherString);
      if (matcher.find()) {
         jiraStringDTO.setDevRemainder(getStringAsNullIfEmpty(matcher));
      }
   }

   public String getPrefixWithHyphen(String prefix) {
      return prefix == null || prefix.trim().length() == 0 ? "" : prefix + "-";
   }

}
