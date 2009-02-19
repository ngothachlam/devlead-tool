package com.jonas.testHelpers;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import com.jonas.agile.devleadtool.component.panel.JiraPanel;
import com.jonas.jira.JiraProject;
import com.jonas.jira.access.ClientConstants;
import com.jonas.jira.access.JiraException;

public class JiraXMLHelper extends HttpClient {
   private final static String BASEURL = "http://10.155.38.105/jira";
//   private String baseUrl = "http://jira.atlassian.com";
   private String baseUrl;

   public JiraXMLHelper(String baseUrl){
      this.baseUrl = baseUrl;
   }
   
   public String getXML(String url) throws IOException {
      GetMethod method = new GetMethod(baseUrl + "/" + url);
      executeMethod(method);
      byte[] responseAsBytes = method.getResponseBody();
      method.releaseConnection();
      String string = new String(responseAsBytes);

      return string;
   }

   public void loginToJira() throws IOException, HttpException, JiraException {
      PostMethod loginMethod = new PostMethod(baseUrl + "/login.jsp");
      loginMethod.addParameter("os_username", "soaptester");
      loginMethod.addParameter("os_password", "soaptester");
      executeMethod(loginMethod);
   }

   public static void main(String[] args) {
      JiraXMLHelper helper = new JiraXMLHelper(ClientConstants.JIRA_URL_AOLBB);
      try {
         helper.loginToJira();
//         System.out.println(helper.getXML(JiraFilter.Sprint_Specific.getUrl() +  "12.4"));
//         System.out.println(helper.getXML("secure/IssueNavigator.jspa?view=rss&fixfor=-2&pid=10070&reset=true&decorator=none&tempMax=20&customfield_10282=12-6"));
         System.out.println(helper.getXML("browse/LLUDEVSUP-606?decorator=none&view=rss"));
//       System.out.println(helper.getXML("secure/IssueNavigator.jspa?view=rss&&customfield_10241%3AlessThan=00001000000000.000&customfield_10241%3AgreaterThan=00000000000000.000&pid=10192&status=1&status=3&status=4&status=5&sorter/field=created&sorter/order=ASC&sorter/field=customfield_10188&sorter/order=ASC&sorter/field=customfield_10241&sorter/order=DESC&tempMax=25&reset=true&decorator=none"));
//         System.out.println(helper.getXML("secure/IssueNavigator.jspa?view=rss&&customfield_10241%3AlessThan=00001000000000.000&customfield_10241%3AgreaterThan=00000000000000.000&pid=10192&status=1&status=3&status=4&status=5&sorter/field=created&sorter/order=ASC&sorter/field=customfield_10188&sorter/order=ASC&sorter/field=customfield_10241&sorter/order=DESC&tempMax=25&reset=true&decorator=none"));
      } catch (IOException e) {
         e.printStackTrace();
      } catch (JiraException e) {
         e.printStackTrace();
      }

   }

}
