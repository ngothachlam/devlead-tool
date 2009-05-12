package com.jonas.jira.jirastat.criteria;

public class JiraHttpCriteria {

   private StringBuffer sb = new StringBuffer();
   private String save;

   protected JiraHttpCriteria append(String string) {
      sb.append(string);
      return this;
   }

   @Override
   public String toString() {
      return sb.toString();
   }

   public void save() {
      save = sb.toString();
   }

   public void reset() {
      sb = new StringBuffer(save);
   }

}
