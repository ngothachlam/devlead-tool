package com.jonas.jira.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.httpclient.HttpException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class JiraHttpClientTest extends JonasTestCase {


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

   public void testApacheCommonsAttempt() throws IOException, HttpException, JiraException {
      JiraHttpClient client = new JiraHttpClient(mockFactory, ClientConstants.JIRA_URL_ATLASSIN);
      
      client.loginToJira();
   }

   //TODO add more tests!!
}
