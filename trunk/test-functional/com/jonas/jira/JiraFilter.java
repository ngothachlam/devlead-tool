package com.jonas.jira;

public class JiraFilter {


   public static final JiraFilter DevsupportPrioFilter = new JiraFilter(JiraProject.LLU_DEV_SUPPORT, "Dev Support (UnClosed)", 
         "/secure/IssueNavigator.jspa?view=rss&" +
         "&customfield_10241%3AlessThan=00001000000000.000" +
         "&customfield_10241%3AgreaterThan=00000000000000.000" +
         "&pid=10192&status=1&status=3&status=4&status=5" +
         "&sorter/field=created&sorter/order=ASC" +
         "&sorter/field=customfield_10188" +
         "&sorter/order=ASC&sorter/field=customfield_10241" +
         "&sorter/order=DESC" +
         "&tempMax=25&reset=true&decorator=none");

   private final String url;
   private final String name;
   private final JiraProject jiraProject;

   public JiraFilter(JiraProject jiraProject, String name, String url) {
      this.jiraProject = jiraProject;
      this.name = name;
      this.url = url;
   }

   public String getName() {
      return name;
   }

   public JiraProject getProject() {
      return jiraProject;
   }

   public String getUrl() {
      return url;
   }

   @Override
   public String toString() {
      return name;
   }

}
