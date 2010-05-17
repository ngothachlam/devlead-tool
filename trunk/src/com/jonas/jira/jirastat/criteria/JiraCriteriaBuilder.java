package com.jonas.jira.jirastat.criteria;

import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.jira.JiraCustomFields;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraCriteriaBuilder {

   private JiraHttpCriteria criteria = new JiraHttpCriteria();

   public String toString() {
      return getCriteriaAsString();
   }

   private String getCriteriaAsString() {
      return criteria.toString();
   }

   public JiraCriteriaBuilder deliveryBetween(String first, String later) {
      criteria.appendFirst(JiraCustomFields.LLUDeliveryDate + "%3Aprevious=").append(first);
      criteria.appendFirst(JiraCustomFields.LLUDeliveryDate + "%3Anext=").append(later);
      return this;
   }

   public JiraCriteriaBuilder createdBetween(String first, String later) {
      criteria.appendFirst("created%3Aprevious=").append(first);
      criteria.appendFirst("created%3Anext=").append(later);
      return this;
   }

   public JiraCriteriaBuilder statusOfNonClosed() {
      criteria.appendFirst("status=1&status=3&status=4&status=5");
      return this;
   }

   public JiraCriteriaBuilder project(JiraProject project) {
      criteria.appendFirst("pid=").append(project.getId());
      return this;
   }

   public JiraCriteriaBuilder filter(JiraFilter filter) {
      criteria.appendFirst(filter.getUrl());
      return this;
   }

   public JiraCriteriaBuilder fixVersion(JiraProject project, JiraVersion version) {
      return this.project(project).fixVersion(version);
   }

   public JiraCriteriaBuilder fixVersion(JiraVersion version) {
      criteria.appendFirst("fixfor=").append(version.getId());
      return this;
   }
   
   public JiraCriteriaBuilder issueType(IssueType issueType) {
      criteria.appendFirst("type=").append(issueType.getId());
      return this;
   }

   public void save() {
      criteria.save();
   }

   public void reset(boolean isToRemove) {
      criteria.reset(isToRemove);
   }

   public JiraCriteriaBuilder search(String maxResults) {
      criteria.setBaseUrl("/secure/IssueNavigator.jspa?");
      criteria.appendFirst("tempMax=").append(maxResults);
      criteria.appendFirst("decorator=none&view=rss").append("&reset=true");
      return this;
   }

   public JiraCriteriaBuilder sortByIssueKeyDesc() {
      criteria.append("$sorter/field=issuekey&sorter/order=DESC");
      return this;
   }

   public JiraCriteriaBuilder setHostUrl(String baseUrl) {
      criteria.setHostUrl(baseUrl);
      return this;
   }

   public JiraCriteriaBuilder jiraBrowse(String jira) {
      criteria.setBaseUrl("/browse/");
      criteria.append(jira);
      criteria.append("?decorator=none&view=rss");
      return this;
   }

}
