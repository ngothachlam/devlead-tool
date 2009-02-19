package com.jonas.rollforwardapp;

import java.io.IOException;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpException;
import com.jonas.jira.access.JiraException;
import com.jonas.testHelpers.JiraXMLHelper;

public class ParserTest extends TestCase {

   RollforwardParser parser = new RollforwardParser();
   JiraXMLHelper helper = new JiraXMLHelper();

   public void testShouldParseOk() throws HttpException, IOException, JiraException {
      helper.loginToJira();

      String html = helper.getXML("browse/LLU-4306?page=com.atlassian.jira.plugin.ext.subversion:subversion-commits-tabpanel");

      List<String> rollforwardFilenames = parser.parseJiraHTMLAndGetSqlRollForwards(html);

      for (String string : rollforwardFilenames) {
         System.out.println("rollforward: " + string);
      }
   }

   public void testShouldParseLineOfSimpleHTMLWithoutSVNCommentCorrectly() {
      String toBeParsed = "<a href=\"http://you.host.address/viewcvs.cgi//llu-service/trunk/91200_index_o.sql/?rev=131918&amp;view=markup\">";

      List<String> actualRollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(toBeParsed);

      assertEquals(0, actualRollforwards.size());
      assertNotNull(actualRollforwards);
   }

   public void testShouldParseLineOfSimpleHTMLCorrectly() {
      List<String> result = parser
            .parseJiraHTMLAndGetSqlRollForwards("<a href=\"http://your.host.address/viewcvs.cgi//llu-service/trunk/91200_index_o.sql/?rev=131918&amp;view=markup\">");
      assertEquals("/llu-service/trunk/91200_index_o.sql", result.get(0));
      assertEquals(1, result.size());
   }

   public void testShouldParseLineOfSimpleHTMLCorrectly2() {

      String toBeParsed = "<a href=\"http://your.host.address/viewcvs.cgi//llu-service/trunk/91200_index_o.sql/?rev=131918&amp;view=markup\">";

      List<String> actualRollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(toBeParsed);

      assertEquals("/llu-service/trunk/91200_index_o.sql", actualRollforwards.get(0));
      assertEquals(1, actualRollforwards.size());
   }

   public void testShouldParseLineOfHTMLCorrectly() {

      String toBeParsed = "    <td bgcolor=\"#ffffff\">"
            + "                                                <font color=\"#009900\" size=\"-2\"><b title=\"Add\">ADD</b></font>"
            + ""
            + "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-service/trunk/91200_index_o.sql/?rev=131918&amp;view=markup\">/llu-service/trunk/91200_index_o.s</a>"
            + ""
            + "\n"
            + "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-service/trunk/91201_index_o.sql/?rev=131918&amp;view=markup\">/llu-service/trunk/91202_index_o.s</a>"
            + "" + "\n";
      List<String> actualRollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(toBeParsed);

      assertEquals("/llu-service/trunk/91200_index_o.sql", actualRollforwards.get(0));
      assertEquals("/llu-service/trunk/91201_index_o.sql", actualRollforwards.get(1));
      assertEquals(2, actualRollforwards.size());
   }

}
