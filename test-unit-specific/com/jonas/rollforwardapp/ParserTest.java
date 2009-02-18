package com.jonas.rollforwardapp;

import java.io.IOException;
import org.apache.commons.httpclient.HttpException;
import com.jonas.jira.access.JiraException;
import com.jonas.testHelpers.JiraXMLHelper;
import junit.framework.TestCase;

public class ParserTest extends TestCase {

   RollforwardParser parser = new RollforwardParser();
   JiraXMLHelper helper = new JiraXMLHelper();

   public void testShouldParseOk() throws HttpException, IOException, JiraException {
      helper.loginToJira();

      String html = helper.getXML("browse/LLU-4306?page=com.atlassian.jira.plugin.ext.subversion:subversion-commits-tabpanel");
      System.out.println(html);

      String[] rollforwardFilenames = parser.parseJiraHTMLAndGetSqlRollForwards(html);
   }

   public void testShouldParseLineOfHTMLCorrectly() {

      String expectedRollforward = "/llu-service-management/trunk/conf/db/rollforwards/91200000010_index_on_productorder_downstream_status_code.sql";
      String actualRollforward = parser
            .parseLineOfHTML("<font color=\"#009900\" size=\"-2\"><b title=\"Add\">ADD</b></font>\n"
                  + "    \t\t   <a href=\"http://your.host.address/viewcvs.cgi//llu-service-management/trunk/conf/"
                  + "db/rollforwards/91200000010_index_on_productorder_downstream_status_code.sql/?rev=131918&amp;"
                  + "view=markup\">/llu-service-management/trunk/conf/db/rollforwards/91200000010_index_on_productorder_downstream_status_code.sql</a>\n"
                  + "\n" + "\n" + "                <br>\n" + "                </td>\n");

      String toBeParsed = "    <td bgcolor=\"#ffffff\">"+
      "                                                <font color=\"#009900\" size=\"-2\"><b title=\"Add\">ADD</b></font>"+
      ""+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-service-management/trunk/conf/db/rollforwards/91200000010_index_on_productorder_downstream_status_code.sql/?rev=131918&amp;view=markup\">/llu-service-management/trunk/conf/db/rollforwards/91200000010_index_on_productorder_downstream_status_code.sql</a>"+
      ""+
      "";
      
      assertEquals(expectedRollforward, actualRollforward);
   }

}
