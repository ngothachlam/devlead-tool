package com.jonas.jira.access;

import net.sourceforge.jwebunit.junit.WebTester;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class JiraSprintUpdater extends HttpClient {

   private String baseUrl;
   private WebTester webTester;
   private Logger log = MyLogger.getLogger(JiraSprintUpdater.class);

   public JiraSprintUpdater(String baseUrl) {
      super();
      this.baseUrl = baseUrl;
      this.webTester = new WebTester();
      webTester.setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
      webTester.getTestContext().setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv: 1.8.1.3) Gecko/20070309 Firefox/2.0.0.3");
      webTester.getTestContext().setBaseUrl(baseUrl);
   }

   public void updateSprint(String jira, String jiraId, String sprint) {
      log.debug("Update Sprint 1");
      webTester.gotoPage(baseUrl + "/secure/EditIssue!default.jspa?id=" + jiraId);
      log.debug("Update Sprint 2");
      // System.out.println("Server response: " + webTester.getServeurResponse());
      if (!webTester.getServeurResponse().startsWith("200 OK")) {
         throw new RuntimeException(webTester.getServeurResponse());
      }
      log.debug("Update Sprint 3");
      webTester.assertFormPresent("jiraform");
      log.debug("Update Sprint 4");
      webTester.assertLinkPresentWithExactText(jira);
      log.debug("Update Sprint 5");
      webTester.setTextField("customfield_10282", sprint);
      log.debug("Update Sprint 6");
      webTester.submit("Update");
      log.debug("Update Sprint 7");
   }

   public void loginToJira() {
      log.debug("Login To Jira 1");
      webTester.beginAt("/login.jsp");
      log.debug("Login To Jira 2");
      webTester.assertFormPresent("loginform");
      log.debug("Login To Jira 3");
      webTester.setTextField("os_username", "soaptester");
      log.debug("Login To Jira 4");
      webTester.setTextField("os_password", "soaptester");
      log.debug("Login To Jira 5");
      webTester.submit();
      log.debug("Login To Jira 6");
   }

   public void createMergeJira(String jira, String jiraId, String sprint) {
      log.trace("createMergeJira 1");
      webTester.gotoPage(baseUrl + "/secure/EditIssue!default.jspa?id=" + jiraId);
      log.trace("createMergeJira 2");
      // System.out.println("Server response: " + webTester.getServeurResponse());
      if (!webTester.getServeurResponse().startsWith("200 OK")) {
         throw new RuntimeException(webTester.getServeurResponse());
      }
      log.trace("createMergeJira 3");
      webTester.assertFormPresent("jiraform");
      log.trace("createMergeJira 4");
      webTester.assertLinkPresentWithExactText(jira);
      log.trace("createMergeJira 5");
      webTester.setTextField("customfield_10282", sprint);
      log.trace("createMergeJira 6");
      webTester.submit("Update");
      log.trace("createMergeJira 7");
   }

   
   public void createClone(String jira, String jiraId){
      webTester.gotoPage(baseUrl + "secure/CloneIssueDetails!default.jspa?id=" + jiraId);
      if (!webTester.getServeurResponse().startsWith("200 OK")) {
         throw new RuntimeException(webTester.getServeurResponse());
      }
      webTester.assertFormPresent("jiraform");
      webTester.getFormElementValue("summary");
//      webTester.setTextField("summary", "");
      
   }
}
