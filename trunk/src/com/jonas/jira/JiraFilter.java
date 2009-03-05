package com.jonas.jira;

import java.util.ArrayList;
import java.util.List;
import com.jonas.jira.JiraProject;

public class JiraFilter {

   private static List<JiraFilter> list = new ArrayList<JiraFilter>();

   public static final JiraFilter DevsupportPrioFilter_UnClosed = new JiraFilter(JiraProject.LLUDEVSUP, "Dev Support (UnClosed)", 
         "/secure/IssueNavigator.jspa?view=rss&" +
         "&"+JiraCustomFields.LLUListPrio+"%3AlessThan=00001000000000.000" +
         "&"+JiraCustomFields.LLUListPrio+"%3AgreaterThan=00000000000000.000" +
         "&pid=10192&status=1" +
         "&status=3" +
         "&status=4" +
         "&status=5" +
         "&sorter/field=created" +
         "&sorter/order=ASC" +
         "&sorter/field=" + JiraCustomFields.LLUDeliveryDate+
         "&sorter/order=ASC" +
         "&sorter/field="+JiraCustomFields.LLUListPrio +
         "&sorter/order=DESC" +
         "&tempMax=10000" +
         "&reset=true" +
         "&decorator=none");
   
   //FIXME use Dynamic filter to get the sprint start!!
   public static final JiraFilter Sprint_Specific = new JiraFilter(JiraProject.LLU, "Sprint Specific", 
         "/secure/IssueNavigator.jspa?view=rss&" +
         "&pid=10070" +
         "&tempMax=1000" +
         "&reset=true" +
         "&decorator=none"+
         "&customfield_10282="      
   );

   public static final JiraFilter DevsupportPrioFilter_UnResolved = new JiraFilter(JiraProject.LLUDEVSUP, "Dev Support (UnResolved)", 
         "/secure/IssueNavigator.jspa?view=rss&" +
         "&"+JiraCustomFields.LLUListPrio+"%3AlessThan=00001000000000.000" +
         "&"+JiraCustomFields.LLUListPrio+"%3AgreaterThan=00000000000000.000" +
         "&pid=10192" +
         "&resolution=-1" +
         "&sorter/field=created" +
         "&sorter/order=ASC" +
         "&sorter/field=" + JiraCustomFields.LLUDeliveryDate +
         "&sorter/order=ASC" +
         "&sorter/field=" + JiraCustomFields.LLUListPrio +
         "&sorter/order=DESC" +
         "&tempMax=10000" +
         "&reset=true" +
         "&decorator=none");
   
   private final String url;
   private final String name;
   private final JiraProject jiraProject;

   public JiraFilter(JiraProject jiraProject, String name, String url) {
      this.jiraProject = jiraProject;
      this.name = name;
      this.url = url;
      
      list.add(this);
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

   public static List<JiraFilter> getFilters() {
      return list;
   }

}
