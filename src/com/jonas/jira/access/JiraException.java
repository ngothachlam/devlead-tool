package com.jonas.jira.access;


public class JiraException extends Exception {

   public JiraException(String string) {
      super(string);
   }

   public JiraException(Throwable arg0) {
      super(arg0);
   }

}
