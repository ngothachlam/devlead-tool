package com.jonas.agile.devleadtool.dto;

public class JiraStringDTO {

   private String devActual;
   private String devEstimate;
   private String devRemainder;
   private String jira;
   private String qAEstimate;
   
   public JiraStringDTO() {
      super();
   }
   
   public JiraStringDTO(String jira, String devEstimate, String estimate, String devActual, String devRemainder) {
      super();
      this.jira = jira;
      this.devEstimate = devEstimate;
      qAEstimate = estimate;
      this.devActual = devActual;
      this.devRemainder = devRemainder;
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

   public void setQAEstimate(String estimate) {
      qAEstimate = estimate;
   }

}
