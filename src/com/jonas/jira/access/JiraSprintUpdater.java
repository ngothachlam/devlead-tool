package com.jonas.jira.access;

import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.apache.commons.httpclient.HttpClient;

public class JiraSprintUpdater extends HttpClient {

   private String baseUrl;
   private WebTester webTester;

   public JiraSprintUpdater(String baseUrl) {
      super();
      this.baseUrl = baseUrl;
      this.webTester = new WebTester();
      webTester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
      webTester.getTestContext().setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv: 1.8.1.3) Gecko/20070309 Firefox/2.0.0.3");
      webTester.getTestContext().setBaseUrl(baseUrl);
   }

   public void updateSprint(String jira, String jiraId, String sprint) {
      webTester.gotoPage(baseUrl + "/secure/EditIssue!default.jspa?id=" + jiraId);
      System.out.println("Server response: " + webTester.getServeurResponse());
      if (!webTester.getServeurResponse().startsWith("200 OK")) {
         throw new RuntimeException(webTester.getServeurResponse());
      }
      webTester.assertFormPresent("jiraform");
      webTester.assertLinkPresentWithExactText(jira);
      webTester.setTextField("customfield_10282", sprint);
      webTester.submit("Update");
   }

   public void loginToJira() {
      webTester.beginAt("/login.jsp");
      webTester.assertFormPresent("loginform");
      webTester.setTextField("os_username", "soaptester");
      webTester.setTextField("os_password", "soaptester");
      webTester.submit();
   }

}
