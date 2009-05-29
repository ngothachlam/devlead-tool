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
      System.out.println(sb.toString() + " baseUrl: " + baseUrl + " subBaseUrl: " + subBaseUrl);
//      int baseUrlLength = 0;
//      if (baseUrl != null) {
//         baseUrlLength = baseUrl.length();
////         sb.insert(0, baseUrl);
//      }
//      if (subBaseUrl != null) {
//         sb.insert(baseUrlLength, subBaseUrl);
//      }
      return (baseUrl != null ? baseUrl : "") + (subBaseUrl != null ? subBaseUrl : "")  + sb.toString();
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
