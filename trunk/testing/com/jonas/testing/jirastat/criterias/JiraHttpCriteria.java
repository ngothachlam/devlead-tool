package com.jonas.testing.jirastat.criterias;

public class JiraHttpCriteria {

   private StringBuffer sb = new StringBuffer();

   protected JiraHttpCriteria append(String string) {
      sb.append(string);
      return this;
   }

   @Override
   public String toString() {
      return sb.toString();
   }

}
