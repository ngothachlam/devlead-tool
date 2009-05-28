package com.jonas.jira.jirastat.criteria;

import java.util.Stack;

public class JiraHttpCriteria {

   private StringBuffer sb = new StringBuffer();
   private Stack<String> save = new Stack<String>();
   private int baseUrlLength = 0;

   protected JiraHttpCriteria append(String string) {
      sb.append(string);
      return this;
   }

   @Override
   public String toString() {
      return sb.toString();
   }

   public void save() {
      save.push(sb.toString());
   }

   public void reset(boolean isToRemove) {
      if (save.size() > 0) {
         if (isToRemove)
            sb = new StringBuffer(save.pop());
         else
            sb = new StringBuffer(save.peek());
      } else
         sb = new StringBuffer("");
   }

   public JiraHttpCriteria setBaseUrl(String baseUrl) {
      sb.insert(0, baseUrl);
      baseUrlLength = baseUrl.length();
      return this;
   }

   public JiraHttpCriteria setSubBaseUrl(String subBaseUrl) {
      sb.insert(baseUrlLength, subBaseUrl);
      return this;
   }
}
