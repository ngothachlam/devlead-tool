package com.jonas.rollforwardapp;

import java.io.IOException;
import org.apache.commons.httpclient.HttpException;
import com.jonas.jira.access.JiraException;
import com.jonas.testHelpers.JiraXMLHelper;
import junit.framework.TestCase;

public class ParserTest extends TestCase {

   RollforwardParser parser = new RollforwardParser();
   JiraXMLHelper helper = new JiraXMLHelper();

   public void texstShouldParseOk() throws HttpException, IOException, JiraException {
      helper.loginToJira();

      String html = helper.getXML("browse/LLU-4306?page=com.atlassian.jira.plugin.ext.subversion:subversion-commits-tabpanel");
      System.out.println(html);

      String[] rollforwardFilenames = parser.parseJiraHTMLAndGetSqlRollForwards(html);
   }

   public void testShouldParseLineOfSimpleHTMLWithoutSVNCommentCorrectly() {
      String expectedRollforward = "/llu-service/trunk/91200_index_o.s";
      
      String toBeParsed = "<a href=\"http://you.host.address/viewcvs.cgi//llu-service/trunk/91200_index_o.s/?rev=131918&amp;view=markup\">";
      
      String[] actualRollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(toBeParsed);
      
      assertEquals(null, actualRollforwards);
   }
   public void testShouldParseLineOfSimpleHTMLCorrectly() {
      String[] result = parser.parseJiraHTMLAndGetSqlRollForwards("<a href=\"http://your.host.address/viewcvs.cgi//llu-service/trunk/91200_index_o.s/?rev=131918&amp;view=markup\">");      
      assertEquals("/llu-service/trunk/91200_index_o.s", result[0]);
      assertEquals(1, result.length);
   }
   
   public void testShouldParseLineOfSimpleHTMLCorrectly2() {

      String expectedRollforward = "/llu-service/trunk/91200_index_o.s";

      String toBeParsed = "<a href=\"http://your.host.address/viewcvs.cgi//llu-service/trunk/91200_index_o.s/?rev=131918&amp;view=markup\">";
      
      String[] actualRollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(toBeParsed);

      assertEquals(expectedRollforward, actualRollforwards[0]);
      assertEquals(1, actualRollforwards.length);
   }

   public void testShouldParseLineOfHTMLCorrectly() {

      String expectedRollforward = "/llu-service/trunk/91200_index_o.s";

      String toBeParsed = "    <td bgcolor=\"#ffffff\">"
            + "                                                <font color=\"#009900\" size=\"-2\"><b title=\"Add\">ADD</b></font>"
            + ""
            + "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-service/trunk/91200_index_o.s/?rev=131918&amp;view=markup\">/llu-service/trunk/91200_index_o.s</a>"
            + "" + "";
      String[] actualRollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(toBeParsed);

      assertEquals(expectedRollforward, actualRollforwards[0]);
      assertEquals(1, actualRollforwards.length);
   }

}
