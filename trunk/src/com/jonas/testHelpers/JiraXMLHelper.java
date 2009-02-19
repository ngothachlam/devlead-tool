package com.jonas.testHelpers;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import com.jonas.jira.access.JiraException;

public class JiraXMLHelper extends HttpClient {

   protected String baseUrl;

   public JiraXMLHelper(String baseUrl) {
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
}