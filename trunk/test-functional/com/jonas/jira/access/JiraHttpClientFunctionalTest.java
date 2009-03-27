package com.jonas.jira.access;

import java.io.IOException;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpException;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import com.jonas.common.xml.JonasXpathEvaluator;
import com.jonas.jira.JiraFilter;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.utils.JiraBuilder;

public class JiraHttpClientFunctionalTest extends TestCase {

   JiraHttpClient client_Atlassain = JiraHttpClient.ATLASSIN;
   JiraHttpClient client_Aol = JiraHttpClient.AOLBB;
   
   @Override
   protected void setUp() throws Exception {
      // TODO Auto-generated method stub
      super.setUp();
      //FIXME should not have to login!! Should be automatic in the HTTPClient. 
      client_Atlassain.loginToJira();
      client_Aol.loginToJira();
      
   }

   public void testGetJira() throws IOException, HttpException, JiraException, JDOMException {
      JiraIssue jira = client_Atlassain.getJira("TST-124", new JonasXpathEvaluator("/rss/channel/item", new XMLOutputter()), JiraBuilder.getInstance());
      
      assertEquals("what the heck a test", jira.getSummary());
   }
   
   public void testApacheCommonsAttempt() throws IOException, HttpException, JiraException, JDOMException {
      JiraIssue jira = client_Aol.getJira("LLU-4139", new JonasXpathEvaluator("/rss/channel/item", new XMLOutputter()), JiraBuilder.getInstance());
      
      assertEquals("llu-inventory-build-2933 / llu-master-build-4066", jira.getBuildNo());
      assertEquals("1.0", jira.getEstimate());
      assertEquals("1.0", jira.getSpent());
   }
   
   public void testShouldGetFilter() throws HttpException, IOException, JiraException, JDOMException{
      List<JiraIssue> jiras = client_Aol.getJirasFromFilter(JiraFilter.DevsupportPrioFilter_UnClosed, new JonasXpathEvaluator("/rss/channel/item", new XMLOutputter()), JiraBuilder.getInstance());
      
      for (JiraIssue jiraIssue : jiras) {
         System.out.println(jiraIssue.getKey());
      }
      
      assertTrue(jiras.size() > 0);
   }
   
}
