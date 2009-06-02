package com.jonas.jira;

import java.util.ArrayList;
import java.util.List;
import com.jonas.jira.JiraProject;
import com.jonas.jira.jirastat.criteria.JiraCriteriaBuilder;

public class JiraFilter {

   private static List<JiraFilter> list = new ArrayList<JiraFilter>();

   public static final JiraFilter DevsupportPrioFilter_UnClosed = new JiraFilter(JiraProject.LLUDEVSUP, "Dev Support (UnClosed)", JiraCustomFields.LLUListPrio + "%3AlessThan=00001000000000.000&" + JiraCustomFields.LLUListPrio
         + "%3AgreaterThan=00000000000000.000&pid=10192&status=1&status=3&status=4&status=5&sorter/field=created&sorter/order=ASC" + "&sorter/field=" + JiraCustomFields.LLUDeliveryDate + "&sorter/order=ASC" + "&sorter/field="
         + JiraCustomFields.LLUListPrio + "&sorter/order=DESC");
   // FIXME use Dynamic filter to get the sprint start!!
   public static final JiraFilter Sprint_Specific = new JiraFilter(JiraProject.LLU, "Sprint Specific", "&pid=10070&customfield_10282=");
   public static final JiraFilter DevsupportPrioFilter_UnResolved = new JiraFilter(JiraProject.LLUDEVSUP, "Dev Support (UnResolved)", JiraCustomFields.LLUListPrio + "%3AlessThan=00001000000000.000" + "&" + JiraCustomFields.LLUListPrio
         + "%3AgreaterThan=00000000000000.000" + "&pid=10192" + "&resolution=-1&sorter/field=created&sorter/order=ASC&sorter/field=" + JiraCustomFields.LLUDeliveryDate + "&sorter/order=ASC&sorter/field=" + JiraCustomFields.LLUListPrio
         + "&sorter/order=DESC");
   public static final JiraFilter TALK_FOR_CLOSING = new JiraFilter(JiraProject.TALK, "Talk for closing", "fixfor=10215&pid=10021&status=1&status=3&status=4&status=5");
   public static final JiraFilter LLU_10_CLOSED = new JiraFilter(JiraProject.LLU, "LLU 10 Closed", "fixfor=11382&pid=10070&status=6");

   private final String name;
   private final JiraProject jiraProject;
   private String url;
   private JiraCriteriaBuilder curl;

   public JiraFilter(JiraProject jiraProject, String name, JiraCriteriaBuilder url) {
      this.jiraProject = jiraProject;
      this.name = name;
      this.curl = url;

      list.add(this);
   }

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
      return url != null ? url : curl.toString();
   }

   @Override
   public String toString() {
      return name;
   }

   public static List<JiraFilter> getFilters() {
      return list;
   }

   public boolean isExtraInfoRequired() {
      // TODO Auto-generated method stub
      throw new RuntimeException("Method not implemented yet!");
   }

   public Object getExtraInfoRequired() {
      // TODO Auto-generated method stub
      throw new RuntimeException("Method not implemented yet!");
   }

}
