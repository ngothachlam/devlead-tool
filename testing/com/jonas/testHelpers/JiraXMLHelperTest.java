package com.jonas.testHelpers;

import java.io.IOException;
import com.jonas.jira.JiraCustomFields;
import com.jonas.jira.access.ClientConstants;
import com.jonas.jira.access.JiraException;

public class JiraXMLHelperTest {
   private final static String BASEURL = "http://10.155.38.105/jira";

   public static void main(String[] args) {
      JiraXMLHelper helper = new JiraXMLHelper(ClientConstants.JIRA_URL_AOLBB);
      try {
         helper.loginToJira();
         // System.out.println(helper.getXML(JiraFilter.Sprint_Specific.getUrl() + "12.4"));
         // System.out.println(helper.getXML("secure/IssueNavigator.jspa?view=rss&fixfor=-2&pid=10070&reset=true&decorator=none&tempMax=20&customfield_10282=12-6"));
         System.out.println(helper.getXML("browse/LLU-4474?decorator=none&view=rss"));
      } catch (IOException e) {
         e.printStackTrace();
      } catch (JiraException e) {
         e.printStackTrace();
      }

   }

}
