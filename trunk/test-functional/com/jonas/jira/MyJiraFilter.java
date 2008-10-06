package com.jonas.jira;

public class MyJiraFilter {

   private final String url;

   public static final MyJiraFilter DevsupportPrioFilter = new MyJiraFilter("Dev Support (UnClosed)", 
         "/secure/IssueNavigator.jspa?view=rss&" +
         "&customfield_10241%3AlessThan=00001000000000.000" +
         "&customfield_10241%3AgreaterThan=00000000000000.000" +
         "&pid=10192&status=1&status=3&status=4&status=5" +
         "&sorter/field=created&sorter/order=ASC" +
         "&sorter/field=customfield_10188" +
         "&sorter/order=ASC&sorter/field=customfield_10241" +
         "&sorter/order=DESC" +
         "&tempMax=25&reset=true&decorator=none");

   private final String name;

   public MyJiraFilter(String name, String url) {
      this.name = name;
      this.url = url;
   }

   public String getName() {
      return name;
   }

   public String getUrl() {
      return url;
   }

}
