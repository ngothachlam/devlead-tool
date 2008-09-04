package com.jonas.testHelpers;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import com.jonas.jira.access.JiraException;

public class JiraXMLHelper extends HttpClient {
   private String baseUrl = "http://10.155.38.105/jira";

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
      JiraXMLHelper helper = new JiraXMLHelper();
      try {
         helper.loginToJira();
         System.out.println(helper.getXML("browse/LLU-4072?decorator=none&view=rss"));
      } catch (IOException e) {
         e.printStackTrace();
      } catch (JiraException e) {
         e.printStackTrace();
      }

   }

}
