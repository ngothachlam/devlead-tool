package com.jonas.jira.jirastat.criteria;

import java.util.Stack;

public class JiraHttpCriteria {

   private StringBuffer sb = new StringBuffer();
   private Stack<String> save = new Stack<String>();
   private String baseUrl = null;
   private String subBaseUrl = null;

   protected JiraHttpCriteria append(String string) {
      sb.append(string);
      return this;
   }

   @Override
   public String toString() {
      return getIfNotNull(baseUrl) + getIfNotNull(subBaseUrl) + sb.toString();
   }

   private String getIfNotNull(String string) {
      return string != null ? string : "";
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

   public JiraHttpCriteria setHostUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
   }

   public JiraHttpCriteria setBaseUrl(String subBaseUrl) {
      this.subBaseUrl = subBaseUrl;
      return this;
   }
}
