package com.jonas.testing.jirastat.criterias;

import com.jonas.jira.JiraFilter;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraCriteriaBuilder {

   JiraHttpCriteria criteria = new JiraHttpCriteria();

   public JiraHttpCriteria getCriteria() {
      return criteria;
   }
   
   public JiraCriteriaBuilder deliveryBetween(String first, String later) {
      criteria.append("&customfield_10188%3Aprevious=").append(first);
      criteria.append("&customfield_10188%3Anext=").append(later);
      return this;
   }

   public JiraCriteriaBuilder createdBetween(String first, String later) {
      criteria.append("&created%3Aprevious=").append(first);
      criteria.append("&created%3Anext=").append(later);
      return this;
   }

   public JiraCriteriaBuilder notClosed() {
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

   public JiraCriteriaBuilder fixVersion(JiraProject project,JiraVersion version) {
      return this.project(project).fixVersion(version);
   }
   
   public JiraCriteriaBuilder fixVersion(JiraVersion version) {
      criteria.append("&fixfor=").append(version.getId());
      return this;
   }

   public void save() {
      criteria.save();
   }

   public void reset() {
      criteria.reset();
   }

}
