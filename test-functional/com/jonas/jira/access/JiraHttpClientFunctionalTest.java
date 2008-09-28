package com.jonas.jira.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.utils.JiraBuilder;
import junit.framework.TestCase;

public class JiraHttpClientFunctionalTest extends TestCase {

   JiraHttpClient client;
   
   private String getFileContents(File file) throws IOException {
      StringBuffer sb = new StringBuffer();
      BufferedReader input = new BufferedReader(new FileReader(file));
      try {
         String line = null; // not declared within while loop
         while ((line = input.readLine()) != null) {
            sb.append(line);
            sb.append(System.getProperty("line.separator"));
         }
      } finally {
         input.close();
      }
      return sb.toString();
   }
   
   public void testGetJira() throws IOException, HttpException, JiraException, JDOMException {
      JiraHttpClient client = new JiraHttpClient(ClientConstants.JIRA_URL_ATLASSIN);
//      client.loginToJira();
      JiraIssue jira = client.getJira("TST-124", new JonasXpathEvaluator("/rss/channel/item"), JiraBuilder.getInstance());
      
      assertEquals("what the heck a test", jira.getSummary());
   }
   
   public void testApacheCommonsAttempt() throws IOException, HttpException, JiraException, JDOMException {
      JiraHttpClient client = new JiraHttpClient(ClientConstants.JIRA_URL_AOLBB);
//      client.loginToJira();
      JiraIssue jira = client.getJira("LLU-4139", new JonasXpathEvaluator("/rss/channel/item"), JiraBuilder.getInstance());
      
      assertEquals("llu-inventory-build-2933 / llu-master-build-4066", jira.getBuildNo());
      assertEquals("1.0", jira.getEstimate());
      assertEquals("1.0", jira.getSpent());
   }
   
}
