package com.jonas.jira.jirastat.criteria;

import com.jonas.jira.JiraCustomFields;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraCriteriaBuilder {

   JiraHttpCriteria criteria = new JiraHttpCriteria();

   public String getCriteriaAsString() {
      return criteria.toString();
   }

   public JiraCriteriaBuilder deliveryBetween(String first, String later) {
      criteria.append("&" + JiraCustomFields.LLUDeliveryDate + "%3Aprevious=").append(first);
      criteria.append("&" + JiraCustomFields.LLUDeliveryDate + "%3Anext=").append(later);
      return this;
   }

   public JiraCriteriaBuilder createdBetween(String first, String later) {
      criteria.append("&created%3Aprevious=").append(first);
      criteria.append("&created%3Anext=").append(later);
      return this;
   }

   public JiraCriteriaBuilder nonClosed() {
      criteria.append("&status=1&status=3&status=4&status=5");
      return this;
   }

   public JiraCriteriaBuilder project(JiraProject project) {
      criteria.append("&pid=").append(project.getId());
      return this;
   }

   public JiraCriteriaBuilder buildBreakType() {
      criteria.append("&type=45");
      return this;
   }

   public JiraCriteriaBuilder filter(JiraFilter filter) {
      criteria.append(filter.getUrl());
      return this;
   }

   public JiraCriteriaBuilder fixVersion(JiraProject project, JiraVersion version) {
      return this.project(project).fixVersion(version);
   }

   public JiraCriteriaBuilder fixVersion(JiraVersion version) {
      criteria.append("&fixfor=").append(version.getId());
      return this;
   }

   public void save() {
      criteria.save();
   }

   public void reset(boolean isToRemove) {
      criteria.reset(isToRemove);
   }
   
   public JiraCriteriaBuilder setStandardFindCriterias(String maxResults) {
      criteria.setSubBaseUrl("/secure/IssueNavigator.jspa?");
      criteria.append("&sorter/field=issuekey&sorter/order=DESC");
      criteria.append("&tempMax=").append(maxResults);
      criteria.append("decorator=none&view=rss&reset=true");
      return this;
   }

   public JiraCriteriaBuilder setBaseUrl(String baseUrl) {
      criteria.setBaseUrl(baseUrl);
      return this;
   }

}
