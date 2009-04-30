package com.jonas.agile.devleadtool.dto;

public class JiraStringDTO {

   private String devActual;
   private String devEstimate;
   private String devRemainder;
   private String jira;
   private String qAEstimate;
   private String qaReminder;
   
   public JiraStringDTO() {
      super();
   }
   
   public String getDevActual() {
      return devActual;
   }

   public String getDevEstimate() {
      return devEstimate;
   }

   public String getDevRemainder() {
      return devRemainder;
   }

   public String getJira() {
      return jira;
   }

   public String getQAEstimate() {
      return qAEstimate;
   }

   public void setDevActual(String devActual) {
      this.devActual = devActual;
   }

   public void setDevEstimate(String devEstimate) {
      this.devEstimate = devEstimate;
   }

   public void setDevRemainder(String devRemainder) {
      this.devRemainder = devRemainder;
   }

   public void setJira(String jira) {
      this.jira = jira;
   }

   public void setQAEstimate(String qaEstimate) {
      qAEstimate = qaEstimate;
   }

   public String getQaRemainder() {
      return qaReminder;
   }

   public void setQaRemainder(String qaReminder) {
      this.qaReminder = qaReminder;
   }

}
