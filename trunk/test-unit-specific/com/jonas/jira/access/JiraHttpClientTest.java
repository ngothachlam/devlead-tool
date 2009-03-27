package com.jonas.jira.access;

import java.io.IOException;
import org.apache.commons.httpclient.HttpException;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class JiraHttpClientTest extends JonasTestCase {

   JiraHttpClient client = JiraHttpClient.AOLBB;

   public void testApacheCommonsAttempt() throws IOException, HttpException, JiraException {
      client.loginToJira();
   }
   //TODO add more tests!!
}
