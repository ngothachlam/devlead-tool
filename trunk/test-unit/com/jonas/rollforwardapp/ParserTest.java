package com.jonas.rollforwardapp;

import java.io.IOException;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpException;
import com.jonas.jira.access.ClientConstants;
import com.jonas.jira.access.JiraException;
import com.jonas.testHelpers.JiraXMLHelper;

public class ParserTest extends TestCase {

   RollforwardParser parser = new RollforwardParser();
   JiraXMLHelper helper = new JiraXMLHelper(ClientConstants.JIRA_URL_AOLBB);

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
   
   public void testShouldParseLineOfHTMLCorrectlyAmongstOthers() {
      
      String toBeParsed = ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"+
      "<html>"+
      "<head>"+
      "    <title>[#LLU-4308] [FC-MsgStore] Store MLC/TPDS responses against LSIP - AOL Agile User Story & Bug Tracking </title>"+
      ""+
      "    <meta name=\"decorator\" content=\"navigator\">"+
      "    <META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
      "    <META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">"+
      "    <META HTTP-EQUIV=\"Expires\" CONTENT=\"-1\">"+
      "    <link rel=\"shortcut icon\" href=\"/jira/images/icons/favicon.ico\">"+
      "    <link rel=\"icon\" type=\"image/png\" href=\"/jira/images/icons/favicon.png\">"+
      "  <link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"/jira/styles/global_printable.css\">"+
      "<link type=\"text/css\" rel=\"StyleSheet\" href=\"/jira/styles/global-static.css\" />"+
      "<link type=\"text/css\" rel=\"StyleSheet\" href=\"/jira/styles/global.css\" />"+
      ""+
      "    <script language=\"JavaScript\" type=\"text/javascript\" src=\"/jira/includes/js/global.js\"></script>"+
      "    <script language=\"JavaScript\" type=\"text/javascript\" src=\"/jira/includes/js/x-jira.js\"></script>"+
      "    <script language=\"JavaScript\" type='text/javascript' src=\"/jira/dwr/interface/RendererPreviewAjaxUtil.js\"></script>"+
      "    <script language=\"JavaScript\" type='text/javascript' src=\"/jira/dwr/engine.js\"></script>"+
      "    <script language=\"JavaScript\" type='text/javascript' src=\"/jira/dwr/util.js\"></script>"+
      "    <script language=\"JavaScript\" type=\"text/javascript\" src=\"/jira/includes/js/jira-global.js\"></script>"+
      ""+
      "    <style type=\"text/css\">@import url(/jira/includes/js/calendar/skins/aqua/theme.css);</style>"+
      "    <script type=\"text/javascript\" src=\"/jira/includes/js/calendar/calendar.js\"></script>"+
      "    <script type=\"text/javascript\" src=\"/jira/includes/js/calendar/lang/calendar-en.js\"></script>"+
      "    <script type=\"text/javascript\" src=\"/jira/includes/js/calendar/calendar-setup.js\"></script>"+
      "    <script type=\"text/javascript\">"+
      "      // Hack to avoid bug in jscalendar - JRA-7713"+
      "      if (!Calendar._TT[\"WEEKEND\"]) Calendar._TT[\"WEEKEND\"] = \"0,6\";"+
      "      if (!Calendar._TT[\"DAY_FIRST\"]) Calendar._TT[\"DAY_FIRST\"] = \"Display %s first\";"+
      "    </script>"+
      ""+
      "</head>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<body bgcolor=\"#f0f0f0\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" link=\"#003366\" vlink=\"#003366\" alink=\"#660000\">"+
      ""+
      "<table class=\"header\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=100%>"+
      "  <tr>"+
      ""+
      "    "+
      "      <td bgcolor=\"#003366\">"+
      "     <table width=100% cellpadding=0 cellspacing=0 border=0><tr>"+
      "        <td valign=top width=5% nowrap><a href=\"/jira/secure/Dashboard.jspa\"><img class=\"logo\" src=\"/jira/images/jira_logo_small.gif\" width=\"111\" height=\"30\" border=0 alt=\"AOL Agile User Story & Bug Tracking\"></a></td>"+
      "            <td valign=bottom width=65% nowrap><a href=\"/jira/secure/Dashboard.jspa\"><font color=\"#ffffff\" size=1>AOL Agile User Story & Bug Tracking</font></a>&nbsp;</td>"+
      "        <td valign=bottom align=right width=30% nowrap>"+
      "           <font color=\"#ffffff\" size=1>"+
      "           "+
      "                    User: Jonas Olofsson&nbsp; &nbsp;"+
      ""+
      "                "+
      ""+
      "                "+
      "                "+
      ""+
      ""+
      "    <a href=\"/jira/secure/popups/recenthistory.jsp\" onClick=\"window.open('/jira/secure/popups/recenthistory.jsp','user_history', 'width=620, height=610, resizable'); return false;\" title=\"Quick link to recently viewed issues\"><font color=#ffffff>History</font></a> |"+
      "    "+
      "    <a href=\"/jira/secure/popups/savedfilters.jsp\" onClick=\"window.open('/jira/secure/popups/savedfilters.jsp', 'saved_filters', 'width=620, height=235, resizable, scrollbars=yes'); return false;\" title=\"Quick link to saved filters\"><font color=#ffffff>Filters</font></a> |"+
      ""+
      "    <a href=\"/jira/secure/ViewProfile.jspa\" title=\"View and change your details and preferences\"><font color=\"#ffffff\">Profile</font></a> |"+
      "    <a href=\"/jira/logout\" title=\"Log out and cancel any automatic login.\"><font color=\"#ffffff\">Log Out</font></a>"+
      "    "+
      "        &nbsp; &nbsp;"+
      "    </font>"+
      ""+
      "    "+
      "    "+
      ""+
      "    <a href=\"/jira/browse/LLU-4308?decorator=printable\"><img src=\"/jira/images/icons/print.gif\" width=16 height=16 align=absmiddle border=0 alt=\"View a printable version of the current page.\" title=\"View a printable version of the current page.\"></a>"+
      "    "+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "    "+
      "    "+
      "            <a href=\"http://www.atlassian.com/software/jira/docs/v3.4.1/index.html?clicked=jirahelp\" target=\"_jirahelp\">"+
      "    "+
      "            <img src=\"/jira/images/icons/help_blue.gif\" width=\"16\" height=\"16\""+
      "            "+
      "            "+
      "                "+
      "                    "+
      "                        align=absmiddle"+
      "                    "+
      "                    "+
      "                "+
      "            "+
      "            border=\"0\" alt=\"Get help!\""+
      "            title=\"Get online help about Using and setting up JIRA\"></a>"+
      ""+
      ""+
      "    &nbsp;"+
      "        </td>"+
      "     </tr></table>"+
      "    </td>"+
      ""+
      "  </tr>"+
      "</table>"+
      "<table class=\"menu\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=100%>"+
      ""+
      "<tr><td bgcolor=#3c78b5>"+
      "  <table border=0 cellspacing=0 cellpadding=0>"+
      "  <tr>"+
      "     <td width=5><img src=\"/jira/images/border/spacer.gif\" width=5 height=20 border=0></td>"+
      ""+
      "        "+
      ""+
      ""+
      ""+
      ""+
      "  <td nowrap  onClick=\"window.document.location='/jira/secure/Dashboard.jspa'\" align=center valign=middle class=\"navItem\" onMouseOver=\"this.className='navItemOver'\" onMouseOut=\"this.className='navItem'\">&nbsp;&nbsp;"+
      "    <a  id=\"home_link\"   title=\"A configurable overview of JIRA\"  href=\"/jira/secure/Dashboard.jspa\""+
      "     accessKey=\"h\" "+
      "     onClick=\"return false\" ><u>H</u>OME</a> &nbsp;&nbsp;</td>"+
      ""+
      ""+
      ""+
      ""+
      "           "+
      ""+
      ""+
      ""+
      ""+
      "  <td nowrap  onClick=\"window.document.location='/jira/secure/BrowseProject.jspa'\" align=center valign=middle class=\"navItem\" onMouseOver=\"this.className='navItemOver'\" onMouseOut=\"this.className='navItem'\">&nbsp;&nbsp;"+
      "    <a  id=\"browse_link\"   title=\"Browse currently active project 'LLU Systems Provisioning'\"  href=\"/jira/secure/BrowseProject.jspa\""+
      "     accessKey=\"b\" "+
      "     onClick=\"return false\" ><u>B</u>ROWSE PROJECT</a> &nbsp;&nbsp;</td>"+
      ""+
      "           "+
      ""+
      ""+
      ""+
      ""+
      "  <td nowrap  onClick=\"window.document.location='/jira/secure/IssueNavigator.jspa'\" align=center valign=middle class=\"navItem\" onMouseOver=\"this.className='navItemOver'\" onMouseOut=\"this.className='navItem'\">&nbsp;&nbsp;"+
      "    <a  id=\"find_link\"   title=\"Find issues in the projects you have permissions to view\"  href=\"/jira/secure/IssueNavigator.jspa\""+
      "     accessKey=\"f\" "+
      "     onClick=\"return false\" ><u>F</u>IND ISSUES</a> &nbsp;&nbsp;</td>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      "           "+
      ""+
      ""+
      ""+
      ""+
      "  <td nowrap  onClick=\"window.document.location='/jira/secure/CreateIssue!default.jspa'\" align=center valign=middle class=\"navItem\" onMouseOver=\"this.className='navItemOver'\" onMouseOut=\"this.className='navItem'\">&nbsp;&nbsp;"+
      "    <a  id=\"create_link\"   title=\"Create a new issue / bug / feature request / etc\"  href=\"/jira/secure/CreateIssue!default.jspa\""+
      "     accessKey=\"c\" "+
      "     onClick=\"return false\" ><u>C</u>REATE NEW ISSUE</a> &nbsp;&nbsp;</td>"+
      ""+
      ""+
      ""+
      ""+
      "                "+
      ""+
      ""+
      ""+
      ""+
      "  <td nowrap  onClick=\"window.document.location='/jira/secure/project/ViewProjects.jspa'\" align=center valign=middle class=\"navItem\" onMouseOver=\"this.className='navItemOver'\" onMouseOut=\"this.className='navItem'\">&nbsp;&nbsp;"+
      ""+
      "    <a  id=\"admin_link\"   title=\"Manage this JIRA instance\"  href=\"/jira/secure/project/ViewProjects.jspa\""+
      "     accessKey=\"a\" "+
      "     onClick=\"return false\" ><u>A</u>DMINISTRATION</a> &nbsp;&nbsp;</td>"+
      ""+
      ""+
      ""+
      "        <script type=\"text/javascript\">"+
      "            var calledReallyShow = false;"+
      "            var calledReallyHide = false;"+
      "            var mouseOnText = false;"+
      "            var mouseInTip = false;"+
      ""+
      "            function showToolTip()"+
      "            {"+
      "                mouseOnText = true;"+
      "                mouseInTip = false;"+
      "                if(!calledReallyShow)"+
      "                {"+
      "                    calledReallyShow = true;"+
      "                    self.setTimeout(\"reallyShowTip()\", 800);"+
      "                }"+
      "            }"+
      ""+
      "            function recordInTip()"+
      "            {"+
      "                mouseInTip = true;"+
      "                mouseOnText = false;"+
      "            }"+
      ""+
      "            function recordOutTip()"+
      "            {"+
      "                mouseInTip = false;"+
      "                fireReallyHide();"+
      "            }"+
      ""+
      "            function reallyShowTip()"+
      "            {"+
      "                calledReallyShow = false;"+
      "                if(mouseOnText)"+
      "                {"+
      "                    document.getElementById('quicksearchhelp').style.display='';"+
      "                    document.getElementById('quicksearchhelp').style.top= findPosY(document.getElementById('quickSearchInput')) + 25;"+
      "                }"+
      "            }"+
      ""+
      "            function hideToolTip()"+
      "            {"+
      "                mouseOnText = false;"+
      "                fireReallyHide();"+
      "            }"+
      ""+
      "            function fireReallyHide()"+
      "            {"+
      "                if(!calledReallyHide)"+
      "                {"+
      "                    calledReallyHide = true;"+
      "                    self.setTimeout(\"reallyHideTip()\", 800);"+
      "                }"+
      "            }"+
      ""+
      "            function reallyHideTip()"+
      "            {"+
      "                calledReallyHide = false;"+
      "                if(!mouseInTip)"+
      "                {"+
      "                    document.getElementById('quicksearchhelp').style.display='none';"+
      "                }"+
      "            }"+
      ""+
      "            function findPosY(obj)"+
      "            {"+
      "                var curtop = 0;"+
      "                if (obj.offsetParent)"+
      "                {"+
      "                    while (obj.offsetParent)"+
      "                    {"+
      "                        curtop += obj.offsetTop"+
      "                        obj = obj.offsetParent;"+
      "                    }"+
      "                }"+
      "                else if (obj.y)"+
      "                    curtop += obj.y;"+
      "                return curtop;"+
      "            }"+
      "        </script>"+
      ""+
      ""+
      ""+
      "        <td align=right class=\"navItem\" valign=middle bgcolor=\"#3c78b5\" width=100%>"+
      "            <form name=\"quicksearch\" action=\"/jira/secure/QuickSearch.jspa\" method=\"post\" style=\"padding: 0; margin: 0;\">"+
      ""+
      "                <span class=\"navItem\" onmouseover=\"showToolTip()\" onmouseout=\"hideToolTip()\">&nbsp; &nbsp;"+
      "                    <u>Q</u>UICK SEARCH:"+
      "                </span>"+
      "                <span class=\"navItem\">"+
      "                    <input title=\"Go directly to an issue by typing a valid issue key, or run a free-text search.\" type=\"text\" size=25 name=\"searchString\" accessKey=\"q\" id=\"quickSearchInput\" valign=\"absmiddle\">&nbsp;"+
      "                </span>"+
      "            </form>"+
      ""+
      "        </td>"+
      ""+
      "        <td width=5><img src=\"/jira/images/border/spacer.gif\" width=5 height=20 border=0></td>"+
      ""+
      "  </tr>"+
      "  </table>"+
      "</td>"+
      "</tr>"+
      "</table>"+
      ""+
      ""+
      "<div class=\"alertHeader\">"+
      "Welcome to the happy little Jira server - please enter clear, explicit information into new JIRA's"+
      "</div>"+
      ""+
      ""+
      "<div onmouseover=\"recordInTip()\" onmouseout=\"recordOutTip()\" id=\"quicksearchhelp\" class=\"informationBox\""+
      "     style=\"display: none; text-align: center; width: 20em; position: absolute; top: 55px; right: 10px; padding: 0.5em;\">"+
      "    Learn more about <a href=\"http://www.atlassian.com/software/jira/docs/v3.4.1/quicksearch.html?clicked=jirahelp\" target=\"_jirahelp\">Quick Search</a>"+
      "</div>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<style>"+
      "<!--"+
      ".fieldLabelArea"+
      "{"+
      "    width: 10%;"+
      "}"+
      ""+
      "#pinCommentContainer"+
      "{"+
      "    float: right;"+
      "    font-size: 10px;"+
      "}"+
      ""+
      "#pinCommentContainer a"+
      "{"+
      "    margin: 0px 0.2em;"+
      "}"+
      "-->"+
      "</style>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<table cellpadding=0 cellspacing=0 border=0 width=\"100%\">"+
      "<tr>"+
      "<td width=200 bgcolor=\"f0f0f0\" valign=top>"+
      "    "+
      ""+
      ""+
      "<table cellpadding=3 cellspacing=0 border=0 width=200>"+
      "<tr><td bgcolor=dddddd>"+
      "    <span style=\"float:left\"><b>Issue Details</span> <span class=\"small\"  style=\"float:right\">[<a href=\"/jira/browse/LLU-4308?decorator=none&view=rss\" class=\"small\">XML</a>]</span> </b>"+
      ""+
      "</td></tr>"+
      "</table>"+
      ""+
      "<img src=\"/jira/images/bluepixel.gif\" width=200 height=1 border=0><br>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<table cellpadding=3 cellspacing=0 border=0>"+
      ""+
      ""+
      "<tr>"+
      "  <td valign=top width=1%><b>Key:</b></td>"+
      "  <td valign=top width=100%>"+
      ""+
      "     <b><a id=\"issue_key_LLU-4308\" href=\"/jira/browse/LLU-4308\">LLU-4308</a></b>"+
      "  </td>"+
      "</tr>"+
      "<tr>"+
      "  <td valign=top><b>Type:</b></td>"+
      "  <td valign=top>"+
      "     "+
      ""+
      ""+
      ""+
      ""+
      "<img src=\"/jira/images/icons/newfeature.gif\" height=16 width=16 border=0 align=absmiddle alt=\"Story\" title=\"Story - A description of a new piece of functionality\">"+
      ""+
      ""+
      ""+
      "     Story"+
      "  </td>"+
      "</tr>"+
      "<tr>"+
      "  <td valign=top><b>Status:</b></td>"+
      "  <td valign=top>"+
      "     "+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<img src=\"/jira/images/icons/status_closed.gif\" height=16 width=16 border=0 align=absmiddle alt=\"Closed\" title=\"Closed - The issue is considered finished, the resolution is correct. Issues which are not closed can be reopened.\">"+
      ""+
      ""+
      ""+
      "     Closed"+
      "  </td>"+
      ""+
      "</tr>"+
      ""+
      ""+
      "<tr>"+
      "  <td valign=top><b>Resolution:</b></td>"+
      "  <td valign=top>"+
      "     DEV Complete"+
      "  </td>"+
      "</tr>"+
      ""+
      ""+
      ""+
      ""+
      "    "+
      "    <tr>"+
      "        <td valign=top><b>Priority:</b></td>"+
      "        <td valign=top>"+
      "            "+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<img src=\"/jira/images/icons/priority_major.gif\" height=16 width=16 border=0 align=absmiddle alt=\"Sev 3 - Medium\" title=\"Sev 3 - Medium - Major loss of function.\">"+
      ""+
      ""+
      ""+
      "            Sev 3 - Medium"+
      "        </td>"+
      "    </tr>"+
      ""+
      "    "+
      ""+
      "    "+
      "    <tr>"+
      "        <td valign=top><b>Assignee:</b></td>"+
      "        <td valign=top>"+
      "            "+
      "            "+
      "                Unassigned"+
      "            "+
      "        </td>"+
      "    </tr>"+
      "    "+
      ""+
      "    "+
      "    "+
      "    <tr>"+
      "        <td valign=top><b>Reporter:</b></td>"+
      ""+
      "        <td valign=top>"+
      "            "+
      "                <a href=\"/jira/secure/ViewProfile.jspa?name=pleong0708\">Pam Leong</a>"+
      "            "+
      "            "+
      "        </td>"+
      "    </tr>"+
      "    "+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<tr>"+
      "  <td valign=top><b>Watchers:</b></td>"+
      "  <td valign=top>"+
      "     "+
      "        0&nbsp;<span class=\"small\">(<a id=\"view_watchers\" href=\"/jira/secure/ManageWatchers!default.jspa?id=42737\">View</a>)</span>"+
      ""+
      "        "+
      "  </td>"+
      "</tr>"+
      ""+
      "</table>"+
      ""+
      ""+
      ""+
      "<div id=\"workflowactions\" class=\"workflowactions\">"+
      "    <table cellpadding=3 cellspacing=0 border=0 width=200>"+
      "    <tr><td bgcolor=dddddd>"+
      "        <b>Available Workflow Actions</b>"+
      "    </td></tr>"+
      ""+
      "    </table>"+
      "    <img src=\"/jira/images/bluepixel.gif\" width=200 height=1 border=0><br>"+
      ""+
      "    "+
      "    <table cellpadding=4 cellspacing=0 border=0><tr><td>"+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "        <b><a title=\"Reopen Issue\""+
      "              href=\"/jira/secure/WorkflowUIDispatcher.jspa?id=42737&action=3\" id=\"action_id_3\">Reopen Issue</a></b><br>"+
      "    </td></tr></table>"+
      "    "+
      ""+
      "</div>"+
      ""+
      ""+
      "<div class=\"operations\">"+
      "<table cellpadding=3 cellspacing=0 border=0 width=200>"+
      "<tr><td bgcolor=dddddd>"+
      "    <b>Operations</b>"+
      "</td></tr>"+
      "</table>"+
      "<img src=\"/jira/images/bluepixel.gif\" width=200 height=1 border=0><br>"+
      ""+
      ""+
      ""+
      "<table cellpadding=3 cellspacing=0 border=0 id=\"operationsSection\">"+
      ""+
      ""+
      "    <tr><td>"+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "        <b><a id=\"assign_issue\" href=\"/jira/secure/AssignIssue!default.jspa?id=42737\">Assign</a></b> this issue (<b><a href=\"/jira/secure/AssignIssue.jspa?assignee=jonasjolofsson&amp;id=42737\">to me</a></b>)"+
      "    </td></tr>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      "    <tr><td>"+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "        <b><a id=\"clone_issue\" href=\"/jira/secure/CloneIssueDetails!default.jspa?id=42737\">Clone</a></b> this issue"+
      "    </td></tr>"+
      ""+
      ""+
      ""+
      "    <tr><td>"+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "        <b><a accesskey=\"m\" onclick=\"try {return showComment();}catch(e){return true;};\" id=\"comment_issue\" href=\"/jira/secure/AddComment!default.jspa?id=42737\">Comment</a></b> on this issue"+
      "    </td></tr>"+
      ""+
      ""+
      ""+
      "    <tr><td>"+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "        Create <b><a id=\"create_subtask\" href=\"/jira/secure/CreateSubTaskIssue!default.jspa?parentIssueId=42737\">sub-task</a></b>"+
      "</td></tr>"+
      ""+
      ""+
      ""+
      "    <tr><td>"+
      ""+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "        <b><a id=\"delete_issue\" href=\"/jira/secure/DeleteIssue!default.jspa?id=42737\">Delete</a></b> this issue"+
      "    </td></tr>"+
      ""+
      " "+
      ""+
      ""+
      ""+
      "    <tr><td>"+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "        <b><a id=\"link_issue\" href=\"/jira/secure/LinkExistingIssue!default.jspa?id=42737\">Link</a></b> this issue to another issue"+
      "    </td></tr>"+
      ""+
      ""+
      ""+
      "    "+
      "        <tr><td>"+
      "            <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "            <b><a id=\"move_issue\" href=\"/jira/secure/MoveIssue!default.jspa?id=42737\">Move</a></b> this issue"+
      "        </td></tr>"+
      "    "+
      ""+
      "    "+
      ""+
      ""+
      ""+
      ""+
      "    "+
      ""+
      "    "+
      "    <tr><td>"+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "            "+
      "                <b><a id=\"watcher_issue\" href=\"/jira/secure/ManageWatchers!default.jspa?id=42737\">"+
      ""+
      "                Watching</a></b>:<br>"+
      "            "+
      "            "+
      "            "+
      "            "+
      "                You are not watching this issue. <b><a href=\"/jira/browse/LLU-4308?watch=true\">Watch it</a></b> to be notified of changes"+
      "            "+
      "    </td></tr>"+
      "    "+
      ""+
      "    "+
      "    <tr><td>"+
      "        <img src=\"/jira/images/icons/bullet_creme.gif\" height=8 width=8 border=0 align=absmiddle>"+
      "            <b>Worklog</b>:<br>"+
      ""+
      "            "+
      "                Worked on this issue? <b><a id=\"log_work\" href=\"/jira/secure/LogWork!default.jspa?id=42737\">Log work done</a></b>"+
      "            "+
      "            <br>"+
      "    </td></tr>"+
      "    "+
      ""+
      ""+
      ""+
      ""+
      ""+
      "</table>"+
      "</div>"+
      ""+
      ""+
      ""+
      "</td>"+
      "<td bgcolor=ffffff valign=top>"+
      ""+
      "<table id=\"viewIssueTable\" width=\"100%\" cellpadding=\"10\" cellspacing=\"0\" border=\"0\"><tr><td>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<table cellpadding=1 cellspacing=0 border=0 bgcolor=bbbbbb width=100% align=\"center\">"+
      "<tr><td width=100% colspan=2 valign=top>"+
      "<table cellpadding=4 cellspacing=0 border=0 width=100% bgcolor=ffffff>"+
      ""+
      "<tr>"+
      "  <td bgcolor=f0f0f0 width=100% colspan=2 valign=top>"+
      "    <table width=100% cellpadding=0 cellspacing=0>"+
      "<tr><td bgcolor=f0f0f0 width=80% valign=top>"+
      "        <b><a href=\"/jira/browse/LLU\">LLU Systems Provisioning</a></b><br>"+
      ""+
      "        "+
      ""+
      "     <h3 class=\"formtitle\"><img src=\"/jira/images/icons/link_out_bot.gif\" width=16 height=16 border=0> [FC-MsgStore] Store MLC/TPDS responses against LSIP</h3>"+
      "     <font size=1>"+
      ""+
      "        Created: <span class=\"date\">08/Dec/08 11:35 AM</span> &nbsp;"+
      "     Updated: <span class=\"date\">27/Jan/09 03:11 PM</span>"+
      ""+
      "        "+
      "            "+
      "        "+
      "     </font>"+
      "    </td>"+
      "    "+
      "    "+
      ""+
      "        "+
      "        <td valign=top align =\"right\">"+
      "            <table cellpadding=1 cellspacing=0 border=0 bgcolor=#bbbbbb><tr><td>"+
      ""+
      "            <table cellpadding=2 cellspacing=0 border=0 bgcolor=f0f0f0>"+
      "                <tr>"+
      "                    <td bgcolor=dddddd>"+
      "                        <img src=\"/jira/images/icons/undo_16.gif\" width=16 height=16 border=0 align=absmiddle>"+
      "                        <b><a href=\"/jira/secure/IssueNavigator.jspa\">Return to search</a></b>"+
      "                        "+
      "                    </td>"+
      "               </tr>"+
      "            </table>"+
      ""+
      "            </td></tr></table>"+
      "        </td>"+
      "        "+
      "        "+
      "    "+
      "    </tr></table>"+
      "</tr>"+
      ""+
      ""+
      "    "+
      "    <tr>"+
      "        <td width=20%><b>Component/s:</b></td>"+
      "        <td width=80%>"+
      "            "+
      "                "+
      ""+
      "    "+
      "       <a title=\"IMS Inventory Management System\" href=\"/jira/secure/IssueNavigator.jspa?reset=true&mode=hide&sorter/order=ASC&sorter/field=priority&pid=10070&component=10414\">IMS</a>"+
      ""+
      "    "+
      ""+
      ""+
      ""+
      "            "+
      "        </td>"+
      "    </tr>"+
      "    "+
      ""+
      ""+
      "<tr>"+
      "    <td width=20%><b>Affects Version/s:</b></td>"+
      "    <td width=80%>"+
      "        "+
      "            "+
      ""+
      ""+
      "    None"+
      ""+
      ""+
      "        "+
      "    </td>"+
      "</tr>"+
      ""+
      ""+
      "<tr>"+
      "    <td width=20%><b>Fix Version/s:</b></td>"+
      "    <td width=80%>"+
      "        "+
      "            "+
      ""+
      "    "+
      "       <a title=\"LLU 12  \" href=\"/jira/secure/IssueNavigator.jspa?reset=true&mode=hide&sorter/order=ASC&sorter/field=priority&pid=10070&fixfor=11484\">LLU 12</a>"+
      "    "+
      ""+
      ""+
      ""+
      "        "+
      "    </td>"+
      "</tr>"+
      ""+
      ""+
      "    "+
      "    "+
      "    "+
      "        "+
      "    "+
      "    "+
      ""+
      ""+
      "</table>"+
      ""+
      "</td></tr></table>"+
      ""+
      ""+
      ""+
      ""+
      "    "+
      "        <br>"+
      "        <table cellpadding=0 cellspacing=0 border=0 bgcolor=bbbbbb WIDTH=\"100%\" align=\"center\"><tr><td>"+
      "        <table cellpadding=4 cellspacing=1 border=0 width=100% bgcolor=bbbbbb>"+
      ""+
      "        "+
      "            "+
      ""+
      ""+
      ""+
      "<tr>"+
      "  <td bgcolor=f0f0f0>"+
      "     <b>Original Estimate:</b>"+
      ""+
      "  </td>"+
      "  <td bgcolor=ffffff valign=top nowrap>"+
      "     "+
      "     "+
      "        4 hours"+
      "     "+
      "  </td>"+
      "  <td bgcolor=f0f0f0>"+
      "     <b>Remaining Estimate:</b>"+
      "  </td>"+
      "  <td bgcolor=ffffff valign=top nowrap>"+
      "     "+
      "     "+
      "        2 hours, 24 minutes"+
      "     "+
      "  </td>"+
      ""+
      "  <td bgcolor=f0f0f0>"+
      "     <b>Time Spent:</b>"+
      "  </td>"+
      "  <td bgcolor=ffffff valign=top nowrap>"+
      "     "+
      "     "+
      "        1 hour, 36 minutes"+
      "     "+
      "  </td>"+
      "</tr>"+
      ""+
      "        "+
      ""+
      "        "+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "        "+
      ""+
      "        "+
      ""+
      ""+
      "<tr>"+
      ""+
      "  <td bgcolor=f0f0f0 width=\"20%\" valign=top>"+
      "     <b>Issue Links:</b>"+
      ""+
      "     "+
      "     <br>"+
      "     <a href=\"/jira/secure/ManageLinks.jspa?id=42737\">Manage Links</a>"+
      "     "+
      "  </td>"+
      "  <td bgcolor=ffffff valign=top colspan=5 class=\"noPadding\">"+
      "     <table cellpadding=4 cellspacing=0 border=0 width=\"100%\">"+
      ""+
      "     "+
      "        "+
      "            <tr>"+
      "                <td colspan=2 bgcolor=f0f0f0>"+
      "                    <b>Dependency</b><br>"+
      "                </td>"+
      "            </tr>"+
      "            <tr>"+
      "                "+
      ""+
      "<td bgcolor=ffffff valign=top width=50%>"+
      "  "+
      "  "+
      "  &nbsp;"+
      ""+
      "  "+
      "</td>"+
      "                "+
      ""+
      "<td bgcolor=ffffff valign=top width=50%>"+
      "  "+
      "  "+
      "     <table cellpadding=0 cellspacing=0 width=100%>"+
      "     <tr><td colspan=2 valign=top>"+
      "        This issue <i>enables</i>:"+
      "     </td></tr>"+
      "        "+
      "        <tr><td width=1%>"+
      "           "+
      "           <img src=\"/jira/images/icons/link_in_top.gif\" width=16 height=16>"+
      ""+
      "           </td>"+
      "           "+
      ""+
      "<td width=100%>"+
      ""+
      "<font size=1><a href=\"/jira/browse/LLU-4107\" title=\"[FC-MsgStore] Store MLC response\">"+
      "<strike>LLU-4107</strike>"+
      ""+
      "</a> [FC-MsgStore] Store MLC response</font>"+
      "</td>"+
      "<td nowrap width=1%>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<img src=\"/jira/images/icons/priority_major.gif\" height=16 width=16 border=0 align=absmiddle alt=\"Sev 3 - Medium\" title=\"Sev 3 - Medium - Major loss of function.\">"+
      ""+
      ""+
      ""+
      "</td>"+
      "<td nowrap width=1%>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<img src=\"/jira/images/icons/status_closed.gif\" height=16 width=16 border=0 align=absmiddle alt=\"Closed\" title=\"Closed - The issue is considered finished, the resolution is correct. Issues which are not closed can be reopened.\">"+
      ""+
      ""+
      ""+
      "</td>"+
      ""+
      "        </tr>"+
      "        "+
      "        <tr><td width=1%>"+
      "           <img src=\"/jira/images/icons/link_in_bot.gif\" width=16 height=16>"+
      "           "+
      "           </td>"+
      ""+
      "           "+
      ""+
      "<td width=100%>"+
      ""+
      "<font size=1><a href=\"/jira/browse/LLU-4108\" title=\"[FC-MsgStore] Store TPDS response\">"+
      "<strike>LLU-4108</strike>"+
      ""+
      "</a> [FC-MsgStore] Store TPDS response</font>"+
      "</td>"+
      "<td nowrap width=1%>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<img src=\"/jira/images/icons/priority_major.gif\" height=16 width=16 border=0 align=absmiddle alt=\"Sev 3 - Medium\" title=\"Sev 3 - Medium - Major loss of function.\">"+
      ""+
      ""+
      ""+
      "</td>"+
      "<td nowrap width=1%>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<img src=\"/jira/images/icons/status_closed.gif\" height=16 width=16 border=0 align=absmiddle alt=\"Closed\" title=\"Closed - The issue is considered finished, the resolution is correct. Issues which are not closed can be reopened.\">"+
      ""+
      ""+
      ""+
      "</td>"+
      ""+
      "        </tr>"+
      "        "+
      "     </table>"+
      "  "+
      "  "+
      "  "+
      "</td>"+
      "            </tr>"+
      "        "+
      "     "+
      "     </table>"+
      ""+
      "  </td>"+
      "</tr>"+
      ""+
      ""+
      "        "+
      ""+
      ""+
      ""+
      "        </table>"+
      "        </td></tr></table>"+
      "    "+
      ""+
      ""+
      "<br>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<table id=\"tab1\" class=\"gridTabBox\" cellpadding=\"3\" cellspacing=\"1\" align=\"center\" width=\"100%\">"+
      "    "+
      "    "+
      "        "+
      "             "+
      "            "+
      "                <tr id=\"rowForcustomfield_10282\">"+
      "                    <td bgcolor=\"f0f0f0\" width=\"20%\" valign=\"top\"><b>Sprint:</b></td>"+
      "                    <td bgcolor=\"ffffff\" width=\"80%\">"+
      "                              12-8"+
      "  "+
      "                    </td>"+
      "                </tr>"+
      "            "+
      "        "+
      "    "+
      "        "+
      "             "+
      "            "+
      "                <tr id=\"rowForcustomfield_10014\">"+
      "                    <td bgcolor=\"f0f0f0\" width=\"20%\" valign=\"top\"><b>Success Criteria:</b></td>"+
      ""+
      "                    <td bgcolor=\"ffffff\" width=\"80%\">"+
      "                              MLC response store table has a column LSIP that is always populated when the message is stored.  "+
      "<br/>"+
      "Tie pair checker response store table has a column LSIP that is always popualted when the message is stored"+
      "  "+
      "                    </td>"+
      "                </tr>"+
      "            "+
      "        "+
      "    "+
      "        "+
      "             "+
      "            "+
      "                <tr id=\"rowForcustomfield_10290\">"+
      "                    <td bgcolor=\"f0f0f0\" width=\"20%\" valign=\"top\"><b>LLU Projects:</b></td>"+
      "                    <td bgcolor=\"ffffff\" width=\"80%\">"+
      "                          Flexi Cease"+
      ""+
      "                    </td>"+
      ""+
      "                </tr>"+
      "            "+
      "        "+
      "    "+
      "</table>"+
      ""+
      ""+
      ""+
      "<br>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<br>"+
      ""+
      "<table cellpadding=2 cellspacing=0 border=0 width=100% align=center>"+
      "<tr>"+
      "  <td bgcolor=bbbbbb width=1% nowrap align=center>"+
      "        &nbsp;<font color=ffffff><b>Description</b></font>&nbsp;"+
      "  </td>"+
      "  <td>&nbsp;</td>"+
      "</tr>"+
      ""+
      "</table>"+
      ""+
      "<table cellpadding=0 cellspacing=1 border=0 width=100% bgcolor=bbbbbb align=center><tr><td>"+
      "<table cellpadding=2 cellspacing=0 border=0 width=100%>"+
      "<tr>"+
      "  <td bgcolor=ffffff valign=top>"+
      "        As LLU Operations, I need the MLC/TPC messages stored in the database to be mapped against LSIPs.  This is so that we can track any business questions back to the service it was provided on."+
      "        <br>"+
      "  </td>"+
      "</tr>"+
      "</table>"+
      "</td></tr></table>"+
      ""+
      ""+
      ""+
      ""+
      "<!--"+
      "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\""+
      "         xmlns:dc=\"http://purl.org/dc/elements/1.1/\""+
      "         xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">"+
      "<rdf:Description rdf:about=\"http://10.155.38.105/jira/browse/LLU-4308\""+
      "    dc:title=\"[#LLU-4308] [FC-MsgStore] Store MLC/TPDS responses against LSIP\""+
      "    dc:subject=\"LLU Systems Provisioning\""+
      "    dc:description=\"As LLU Operations, I need the MLC/TPC messages stored in the database to be mapped against LSIPs.  T ...\""+
      "    dc:date=\"2008-12-08 11:35:43,960\""+
      "    dc:identifier=\"http://10.155.38.105/jira/browse/LLU-4308\""+
      "    trackback:ping=\"http://10.155.38.105/jira/rpc/trackback/LLU-4308\"/>"+
      ""+
      "<rdf:Description rdf:about=\"http://10.155.38.105/jira/secure/ViewIssue.jspa?id=42737\""+
      "    dc:title=\"[#LLU-4308] [FC-MsgStore] Store MLC/TPDS responses against LSIP\""+
      "    dc:subject=\"LLU Systems Provisioning\""+
      "    dc:description=\"As LLU Operations, I need the MLC/TPC messages stored in the database to be mapped against LSIPs.  T ...\""+
      "    dc:date=\"2008-12-08 11:35:43,960\""+
      "    dc:identifier=\"http://10.155.38.105/jira/secure/ViewIssue.jspa?id=42737\""+
      "    trackback:ping=\"http://10.155.38.105/jira/rpc/trackback/LLU-4308\"/>"+
      ""+
      "<rdf:Description rdf:about=\"http://10.155.38.105/jira/secure/ViewIssue.jspa?key=LLU-4308\""+
      "    dc:title=\"[#LLU-4308] [FC-MsgStore] Store MLC/TPDS responses against LSIP\""+
      "    dc:subject=\"LLU Systems Provisioning\""+
      "    dc:description=\"As LLU Operations, I need the MLC/TPC messages stored in the database to be mapped against LSIPs.  T ...\""+
      "    dc:date=\"2008-12-08 11:35:43,960\""+
      "    dc:identifier=\"http://10.155.38.105/jira/secure/ViewIssue.jspa?key=LLU-4308\""+
      "    trackback:ping=\"http://10.155.38.105/jira/rpc/trackback/LLU-4308\"/>"+
      "</rdf:RDF>-->"+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<br>"+
      ""+
      ""+
      ""+
      ""+
      "<table cellpadding=2 cellspacing=0 border=0 width=100% align=center>"+
      "<tr>"+
      "  "+
      "     <td bgcolor=bbbbbb width=1% nowrap align=center>"+
      ""+
      "        &nbsp;<font color=ffffff><b>All</b></font>&nbsp;"+
      "     </td>"+
      "  "+
      "  "+
      ""+
      "    "+
      "    "+
      "        <td width=1% nowrap align=center>"+
      "            &nbsp;<b><a href=\"/jira/browse/LLU-4308?page=comments\">Comments</a></b>&nbsp;"+
      "        </td>"+
      "    "+
      ""+
      "    "+
      "    "+
      ""+
      "    "+
      "  "+
      "  "+
      "     <td width=1% nowrap align=center>"+
      "        &nbsp;<b><a href=\"/jira/browse/LLU-4308?page=worklog\">Work Log</a></b>&nbsp;"+
      ""+
      "     </td>"+
      "  "+
      "    "+
      ""+
      "  "+
      "  "+
      "     <td width=1% nowrap align=center>"+
      "        &nbsp;<b><a href=\"/jira/browse/LLU-4308?page=history\">Change History</a></b>&nbsp;"+
      "     </td>"+
      "  "+
      ""+
      "    "+
      ""+
      "  "+
      "  "+
      "    <!--"+
      "        This has been commented out because it doesn't really tell the user anything."+
      "     <td width=1% nowrap align=center>"+
      "        &nbsp;<b><a href=\"ViewIssue.jspa?key=LLU-4308&page=workflow\">Workflow History</a></b>&nbsp;"+
      "     </td>"+
      "    -->"+
      "  "+
      ""+
      "    "+
      "        "+
      "        "+
      "            <td width=\"1%\" nowrap align=center>"+
      "                &nbsp;<b><a href=\"/jira/browse/LLU-4308?page=com.atlassian.jira.plugin.ext.subversion:subversion-commits-tabpanel\">Subversion Commits</a></b>&nbsp;"+
      "            </td>"+
      ""+
      "        "+
      "    "+
      ""+
      "  <td>&nbsp;</td>"+
      "    <td width=1% bgcolor=e5e5e5 nowrap>"+
      "        &nbsp;"+
      "        Sort Order:"+
      "        "+
      "            <a href=\"/jira/browse/LLU-4308?actionOrder=desc\"><img src=\"/jira/images/icons/down.gif\" height=14 width=11 align=absmiddle title=\"Ascending order - Click to sort in descending order\" border=0 /></a>"+
      "        "+
      "        "+
      "    </td>"+
      "</tr>"+
      "</table>"+
      ""+
      ""+
      "<div class=\"issuePanelContainer\">"+
      ""+
      "    "+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_200241\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=pleong0708\">Pam Leong</a> <font size=-2>[<font color=336699>09/Dec/08 11:32 AM</font>]</font>"+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      ""+
      "            Original Estimate"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                    4 hours"+
      "                                                       <font size=1>[    14400"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      ""+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Remaining Estimate"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                    4 hours"+
      "                                                       <font size=1>[    14400"+
      "]</font>"+
      ""+
      "                        </td>"+
      "    </tr>"+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Fix Version/s"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      ""+
      "                                        LLU 12"+
      "                                                       <font size=1>[    11484"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Fix Version/s"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      ""+
      "                                        Pam's Backlog"+
      "                                                        <font size=1>[    11458"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    </tr>"+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Sprint"+
      "        </b></td>"+
      ""+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        12-8"+
      "                                </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      ""+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_200274\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=pleong0708\">Pam Leong</a> <font size=-2>[<font color=336699>09/Dec/08 02:07 PM</font>]</font>"+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      ""+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            LLU Projects"+
      "        </b></td>"+
      ""+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        Flexi Cease"+
      "                                </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      ""+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_200430\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=ravikumarasinge\">Ravi Kumarasinghe</a> <font size=-2>[<font color=336699>11/Dec/08 09:48 AM</font>]</font>"+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      ""+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Assignee"+
      "        </b></td>"+
      ""+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        Ravi Kumarasinghe"+
      "                                                       <font size=1>[    ravikumarasinge"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=2 cellspacing=0 border=0 width=\"100%\">"+
      "<tr>"+
      "    <td bgcolor=\"#f0f0f0\" width=10%><b>Repository</b></td>"+
      "    <td bgcolor=\"#f0f0f0\" width=10%><b>Revision</b></td>"+
      "    <td bgcolor=\"#f0f0f0\" width=10%><b>Date</b></td>"+
      "    <td bgcolor=\"#f0f0f0\" width=10%><b>User</b></td>"+
      "    <td bgcolor=\"#f0f0f0\"><b>Message</b></td>"+
      ""+
      "</tr>"+
      "<tr>"+
      "    <td bgcolor=\"#ffffff\" width=10% valign=top rowspan=3>My LLU Repository</td>"+
      "    <td bgcolor=\"#ffffff\" width=10% valign=top rowspan=3><a href=\"http://your.host.address/viewcvs?view=rev&rev=131266\">#131266</a></td>"+
      "    <td bgcolor=\"#ffffff\" width=10% valign=top rowspan=3>Thu Dec 11 11:03:09 GMT 2008</td>"+
      "    <td bgcolor=\"#ffffff\" width=10% valign=top rowspan=3>ravikumarasinge</td>"+
      "    <td bgcolor=\"#ffffff\">    <a href=\"http://10.155.38.105/jira/browse/LLU-4308\" title=\"[FC-MsgStore] Store MLC/TPDS responses against LSIP\"><strike>LLU-4308</strike></a>|MSA,RK: Store LSIP in TPDS responses"+
      ""+
      "</td>"+
      "</tr>"+
      "<tr>"+
      "    <td bgcolor=\"#f0f0f0\"><b>Files Changed</b></td>"+
      "</tr>"+
      "<tr>"+
      "    <td bgcolor=\"#ffffff\">"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/conf/db/rollforwards/91200000045_create_tpds_response_audit_table.sql/?rev=131266&view=diff&r1=131266&r2=131265&p1=/llu-inventory/trunk/conf/db/rollforwards/91200000045_create_tpds_response_audit_table.sql&p2=/llu-inventory/trunk/conf/db/rollforwards/91200000045_create_tpds_response_audit_table.sql\">/llu-inventory/trunk/conf/db/rollforwards/91200000045_create_tpds_response_audit_table.sql</a>"+
      ""+
      "                "+
      "                <br>"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/properties.xml/?rev=131266&view=diff&r1=131266&r2=131265&p1=/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/properties.xml&p2=/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/properties.xml\">/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/properties.xml</a>"+
      ""+
      "                "+
      "                <br>"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSManager.java/?rev=131266&view=diff&r1=131266&r2=131265&p1=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSManager.java&p2=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSManager.java\">/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSManager.java</a>"+
      ""+
      "                "+
      "                <br>"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSResponseAudit.java/?rev=131266&view=diff&r1=131266&r2=131265&p1=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSResponseAudit.java&p2=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSResponseAudit.java\">/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSResponseAudit.java</a>"+
      ""+
      "                "+
      "                <br>"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/content.txt/?rev=131266&view=diff&r1=131266&r2=131265&p1=/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/content.txt&p2=/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/content.txt\">/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/content.txt</a>"+
      ""+
      "                "+
      "                <br>"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/src/com/aol/tactical/inventory/service/flexicease/FlexiCeaseRequestHandler.java/?rev=131266&view=diff&r1=131266&r2=131265&p1=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/flexicease/FlexiCeaseRequestHandler.java&p2=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/flexicease/FlexiCeaseRequestHandler.java\">/llu-inventory/trunk/src/com/aol/tactical/inventory/service/flexicease/FlexiCeaseRequestHandler.java</a>"+
      ""+
      "                "+
      "                <br>"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSManagerImpl.java/?rev=131266&view=diff&r1=131266&r2=131265&p1=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSManagerImpl.java&p2=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSManagerImpl.java\">/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSManagerImpl.java</a>"+
      ""+
      "                "+
      "                <br>"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSResponseAudit.hbm.xml/?rev=131266&view=diff&r1=131266&r2=131265&p1=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSResponseAudit.hbm.xml&p2=/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSResponseAudit.hbm.xml\">/llu-inventory/trunk/src/com/aol/tactical/inventory/service/tpds/TPDSResponseAudit.hbm.xml</a>"+
      ""+
      "                "+
      "                <br>"+
      "                </td>"+
      "</tr>"+
      "</table>"+
      ""+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=2 cellspacing=0 border=0 width=\"100%\">"+
      "<tr>"+
      "    <td bgcolor=\"#f0f0f0\" width=10%><b>Repository</b></td>"+
      "    <td bgcolor=\"#f0f0f0\" width=10%><b>Revision</b></td>"+
      "    <td bgcolor=\"#f0f0f0\" width=10%><b>Date</b></td>"+
      "    <td bgcolor=\"#f0f0f0\" width=10%><b>User</b></td>"+
      ""+
      "    <td bgcolor=\"#f0f0f0\"><b>Message</b></td>"+
      "</tr>"+
      "<tr>"+
      "    <td bgcolor=\"#ffffff\" width=10% valign=top rowspan=3>My LLU Repository</td>"+
      "    <td bgcolor=\"#ffffff\" width=10% valign=top rowspan=3><a href=\"http://your.host.address/viewcvs?view=rev&rev=131268\">#131268</a></td>"+
      "    <td bgcolor=\"#ffffff\" width=10% valign=top rowspan=3>Thu Dec 11 11:11:38 GMT 2008</td>"+
      "    <td bgcolor=\"#ffffff\" width=10% valign=top rowspan=3>ravikumarasinge</td>"+
      ""+
      "    <td bgcolor=\"#ffffff\">    <a href=\"http://10.155.38.105/jira/browse/LLU-4308\" title=\"[FC-MsgStore] Store MLC/TPDS responses against LSIP\"><strike>LLU-4308</strike></a>|MSA,RK: Store LSIP in TPDS responses"+
      "</td>"+
      "</tr>"+
      "<tr>"+
      "    <td bgcolor=\"#f0f0f0\"><b>Files Changed</b></td>"+
      "</tr>"+
      "<tr>"+
      "    <td bgcolor=\"#ffffff\">"+
      "                                                <font color=\"#999933\" size=\"-2\"><b title=\"Modify\">MODIFY</b></font>"+
      ""+
      "                                <a href=\"http://your.host.address/viewcvs.cgi//llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/content.txt/?rev=131268&view=diff&r1=131268&r2=131267&p1=/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/content.txt&p2=/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/content.txt\">/llu-inventory/trunk/fitnesse/FitNesseRoot/AllTests/InventoryManagementServices/ReserveNetworkResource/FlexiCeaseOn/TpdsRejected/content.txt</a>"+
      ""+
      "                "+
      "                <br>"+
      "                </td>"+
      "</tr>"+
      "</table>"+
      ""+
      "<br>"+
      ""+
      "        "+
      "            "+
      "<div class=\"actionContainer\">"+
      "<div class=\"action-links subText\" >"+
      "[ <a href=\"/jira/browse/LLU-4308#action_78197\" title=\"A permanent link to this comment.\">Permlink</a> ]"+
      ""+
      "</div>"+
      "<div class=\"action-details\">"+
      "    <a name=\"action_78197\" />"+
      "                                        "+
      "                "+
      "        Comment by <a href=\"/jira/secure/ViewProfile.jspa?name=ravikumarasinge\">Ravi Kumarasinghe</a> <font size=-2>[<span class=date>12/Dec/08 01:39 PM</span>]</font>"+
      ""+
      "                    &nbsp;<a href=\"/jira/secure/DeleteComment!default.jspa?id=78197\">Delete</a>"+
      "        </div>"+
      ""+
      "</div>"+
      "<div class=\"action-body\">"+
      "          llu-master-build-4215 / llu-inventory-build-3037 "+
      "  </div>"+
      ""+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_200541\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=ravikumarasinge\">Ravi Kumarasinghe</a> <font size=-2>[<font color=336699>12/Dec/08 01:39 PM</font>]</font>"+
      ""+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      ""+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Status"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        Open"+
      "                                                        <font size=1>[    1"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        Resolved"+
      "                                                       <font size=1>[    5"+
      "]</font>"+
      ""+
      "                        </td>"+
      "    </tr>"+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Resolution"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      ""+
      "                                        DEV Complete"+
      "                                                       <font size=1>[    7"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Assignee"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      ""+
      "                                        Ravi Kumarasinghe"+
      "                                                        <font size=1>[    ravikumarasinge"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        - LLU - QA Queue"+
      "                                                       <font size=1>[    qa queue"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "    </table>"+
      ""+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_201036\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=apatel1008\">Arven Patel</a> <font size=-2>[<font color=336699>19/Dec/08 02:55 PM</font>]</font>"+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      ""+
      "            Link"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        This issue enables <a href=\"http://10.155.38.105/jira/browse/LLU-4108\" title=\"[FC-MsgStore] Store TPDS response\"><strike>LLU-4108</strike></a>"+
      "                                                       <font size=1>[    <a href=\"http://10.155.38.105/jira/browse/LLU-4108\" title=\"[FC-MsgStore] Store TPDS response\"><strike>LLU-4108</strike></a>"+
      ""+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_201038\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=apatel1008\">Arven Patel</a> <font size=-2>[<font color=336699>19/Dec/08 02:56 PM</font>]</font>"+
      ""+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      ""+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Link"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        This issue enables <a href=\"http://10.155.38.105/jira/browse/LLU-4107\" title=\"[FC-MsgStore] Store MLC response\"><strike>LLU-4107</strike></a>"+
      "                                                       <font size=1>[    <a href=\"http://10.155.38.105/jira/browse/LLU-4107\" title=\"[FC-MsgStore] Store MLC response\"><strike>LLU-4107</strike></a>"+
      ""+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">"+
      "<tr><td bgcolor=\"f0f0f0\">"+
      "    <a name=\"action_78448\" />"+
      "                                        "+
      "                "+
      "        Work logged by <a href=\"/jira/secure/ViewProfile.jspa?name=pleong0708\">Pam Leong</a> <font size=-2>[<font color=336699>22/Dec/08 09:57 AM</font>]</font>"+
      ""+
      "    </td>"+
      "    <td bgcolor=\"f0f0f0\" align=\"right\">"+
      "    <font size=\"-2\">"+
      "    [ <a href=\"/jira/browse/LLU-4308#action_78448\" title=\"A permanent link to this comment.\">Permlink</a> ]"+
      "    </font>"+
      "    </td>"+
      "</tr>"+
      "<tr><td bgcolor=ffffff colspan=2>"+
      "    <b>Time Worked:</b> 0 minutes"+
      ""+
      "</td></tr>"+
      "<tr><td bgcolor=\"ffffff\" colspan=\"2\">"+
      "            &lt;no comment&gt;"+
      "    </td></tr>"+
      "</table>"+
      "<br>"+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_201070\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=pleong0708\">Pam Leong</a> <font size=-2>[<font color=336699>22/Dec/08 09:57 AM</font>]</font>"+
      ""+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      ""+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Remaining Estimate"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                    4 hours"+
      "                                                        <font size=1>[    14400"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                    2 hours"+
      "                                                       <font size=1>[    7200"+
      "]</font>"+
      ""+
      "                        </td>"+
      "    </tr>"+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Time Spent"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      ""+
      "                                    2 hours"+
      "                                                       <font size=1>[    7200"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">"+
      "<tr><td bgcolor=\"f0f0f0\">"+
      "    <a name=\"action_78941\" />"+
      ""+
      "                                        "+
      "                "+
      "        Work logged by <a href=\"/jira/secure/ViewProfile.jspa?name=jonasjolofsson\">Jonas Olofsson</a> <font size=-2>[<font color=336699>08/Jan/09 01:57 PM</font>]</font>"+
      "    </td>"+
      "    <td bgcolor=\"f0f0f0\" align=\"right\">"+
      "    <font size=\"-2\">"+
      "    [ <a href=\"/jira/browse/LLU-4308#action_78941\" title=\"A permanent link to this comment.\">Permlink</a> ]"+
      "    </font>"+
      ""+
      "    </td>"+
      "</tr>"+
      "<tr><td bgcolor=ffffff colspan=2>"+
      "    <b>Time Worked:</b> 1 hour, 36 minutes"+
      "</td></tr>"+
      "<tr><td bgcolor=\"ffffff\" colspan=\"2\">"+
      "            &lt;no comment&gt;"+
      "    </td></tr>"+
      "</table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_203794\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=jonasjolofsson\">Jonas Olofsson</a> <font size=-2>[<font color=336699>08/Jan/09 01:57 PM</font>]</font>"+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      ""+
      "            Remaining Estimate"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                    2 hours"+
      "                                                        <font size=1>[    7200"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                    24 minutes"+
      "                                                       <font size=1>[    1440"+
      "]</font>"+
      ""+
      "                        </td>"+
      "    </tr>"+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Time Spent"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                    2 hours"+
      "                                                        <font size=1>[    7200"+
      "]</font>"+
      ""+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                    3 hours, 36 minutes"+
      "                                                       <font size=1>[    12960"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      ""+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_203966\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=jonasjolofsson\">Jonas Olofsson</a> <font size=-2>[<font color=336699>09/Jan/09 12:45 PM</font>]</font>"+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      ""+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Time Spent"+
      "        </b></td>"+
      ""+
      "    <td bgcolor=ffffff width=40%>"+
      "                                            <font size=1>[    12960"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                           <font size=1>[    5760"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      "        <tr>"+
      ""+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Remaining Estimate"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                            <font size=1>[    1440"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                           <font size=1>[    8640"+
      "]</font>"+
      ""+
      "                        </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_204579\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=jonasjolofsson\">Jonas Olofsson</a> <font size=-2>[<font color=336699>14/Jan/09 04:52 PM</font>]</font>"+
      ""+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      ""+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            LLU Sprint"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                    </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        12-8"+
      "                                </td>"+
      "    </tr>"+
      "    </table>"+
      ""+
      "<br>"+
      ""+
      "        "+
      "            "+
      "<div class=\"actionContainer\">"+
      "<div class=\"action-links subText\" >"+
      "[ <a href=\"/jira/browse/LLU-4308#action_79885\" title=\"A permanent link to this comment.\">Permlink</a> ]"+
      "</div>"+
      "<div class=\"action-details\">"+
      "    <a name=\"action_79885\" />"+
      "                                        "+
      "                "+
      "        Comment by <a href=\"/jira/secure/ViewProfile.jspa?name=jonasjolofsson\">Jonas Olofsson</a> <font size=-2>[<span class=date>27/Jan/09 03:11 PM</span>]</font>"+
      ""+
      "                    &nbsp;<a href=\"/jira/secure/DeleteComment!default.jspa?id=79885\">Delete</a>"+
      "        </div>"+
      ""+
      "</div>"+
      "<div class=\"action-body\">"+
      "          green dotted"+
      "  </div>"+
      ""+
      "<br>"+
      ""+
      "        "+
      "            <table cellpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\" class=\"blank\">"+
      ""+
      "    <tr><td bgcolor=f0f0f0 colspan=3>"+
      "    <a name=\"action_206119\" />"+
      "                                        "+
      "        "+
      "        Change by <a href=\"/jira/secure/ViewProfile.jspa?name=jonasjolofsson\">Jonas Olofsson</a> <font size=-2>[<font color=336699>27/Jan/09 03:11 PM</font>]</font>"+
      "    </td></tr>"+
      ""+
      "            <tr>"+
      ""+
      "            <td bgcolor=\"dddddd\" width=\"20%\"><b>Field</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>Original Value</b></td>"+
      "            <td bgcolor=\"dddddd\" width=\"40%\"><b>New Value</b></td>"+
      "        </tr>"+
      "    "+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Status"+
      "        </b></td>"+
      ""+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        Resolved"+
      "                                                        <font size=1>[    5"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        Closed"+
      "                                                       <font size=1>[    6"+
      "]</font>"+
      "                        </td>"+
      "    </tr>"+
      ""+
      "        <tr>"+
      "    <td bgcolor=ffffff width=20%><b>"+
      "            Assignee"+
      "        </b></td>"+
      "    <td bgcolor=ffffff width=40%>"+
      "                                        - LLU - QA Queue"+
      "                                                        <font size=1>[    qa queue"+
      "]</font>"+
      "                        </td>"+
      "    <td bgcolor=ffffff width=40%>"+
      ""+
      "                    </td>"+
      "    </tr>"+
      "    </table>"+
      "<br>"+
      ""+
      "        "+
      "    "+
      "    "+
      "    "+
      ""+
      "</div>"+
      "<br/>"+
      ""+
      ""+
      "<script language=\"JavaScript\" src=\"/jira/includes/js/pincomment.js\"></script>"+
      ""+
      "<div id=\"commentDiv\" style=\"display:none;\">"+
      ""+
      "    "+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<form action=\"/jira/secure/AddComment.jspa\" method=\"post\" name=\"jiraform\" onsubmit=\"if (this.submitted) return false; this.submitted = true; return true\" >"+
      ""+
      "  "+
      "  <table class=\"jiraform maxWidth\" id=\"\""+
      "    >"+
      "  "+
      ""+
      "  "+
      "  <tr>"+
      "     <td colspan=\"2\" class=\"jiraformheader\">"+
      "        "+
      "        <h3 class=\"formtitle\"><span id=\"pinCommentContainer\">[<a id=\"pinComment\" href=\"#\" onclick=\"toggleMenu('commentDiv', this);return false;\"></a>]</span>Add Comment</h3></td>"+
      "  </tr>"+
      "  "+
      "  "+
      "  "+
      ""+
      "  "+
      "  "+
      ""+
      "  <tr>"+
      ""+
      "            <td class=\"fieldLabelArea\">Comment:</td>"+
      "            <td>"+
      ""+
      ""+
      ""+
      ""+
      "    <textarea name=\"comment\""+
      "              id=\"comment\""+
      "              rows=\"15\"              wrap=\"virtual\"              cols=\"\"              accesskey=\"m\"              class=\"textarea\""+
      "    ></textarea>"+
      "</td>"+
      "        </tr>"+
      "        "+
      "            "+
      ""+
      ""+
      ""+
      "  "+
      ""+
      ""+
      ""+
      "  <tr "+
      "        "+
      "        "+
      "    >"+
      ""+
      "     "+
      "     "+
      "        "+
      "        "+
      "           <td class=\"fieldLabelArea\">"+
      "        "+
      "     "+
      "        "+
      "        "+
      "     "+
      "     Viewable By:"+
      "        "+
      "        "+
      "     </td>"+
      ""+
      "  "+
      "  "+
      ""+
      ""+
      ""+
      ""+
      "  <td bgcolor=\"ffffff\" class=\"fieldValueArea\">"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<select name=\"commentLevel\""+
      "        id=\"commentLevel_select\""+
      "      "+
      "         "+
      "      "+
      "      "+
      "         "+
      "      "+
      "      "+
      "         "+
      "         "+
      "      "+
      "      "+
      "         "+
      "      "+
      "      "+
      "         "+
      "      "+
      "      "+
      "         "+
      "      "+
      "      "+
      "         "+
      "      "+
      ">"+
      "   "+
      "      <option"+
      "     value=\"\""+
      "     "+
      "     >All Users</option>"+
      "   "+
      "   "+
      "      "+
      "   "+
      ""+
      "   "+
      "   "+
      "   "+
      "   "+
      "   "+
      "    <option value=\"jira-users\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        jira-users"+
      "      </option>"+
      "   "+
      "    <option value=\"jira-developers\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        jira-developers"+
      "      </option>"+
      "   "+
      "    <option value=\"jira-managers\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        jira-managers"+
      "      </option>"+
      ""+
      "   "+
      "    <option value=\"Access Project Admin\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        Access Project Admin"+
      "      </option>"+
      "   "+
      "    <option value=\"Access\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        Access"+
      "      </option>"+
      "   "+
      "    <option value=\"llu-users\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        llu-users"+
      "      </option>"+
      "   "+
      "    <option value=\"jira-administrators\" "+
      "              "+
      "              "+
      "              >"+
      ""+
      "        "+
      "        jira-administrators"+
      "      </option>"+
      "   "+
      "    <option value=\"LLU Development Support QA Queue\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        LLU Development Support QA Queue"+
      "      </option>"+
      "   "+
      "    <option value=\"LLU Development Support PriorityList Stakeholders\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        LLU Development Support PriorityList Stakeholders"+
      "      </option>"+
      "   "+
      "    <option value=\"Software Development Bristol\" "+
      "              "+
      "              "+
      "              >"+
      "        "+
      "        Software Development Bristol"+
      "      </option>"+
      ""+
      "   "+
      "   "+
      "</select>"+
      ""+
      "<span class=\"selectDescription\" id=\"commentLevel_summary\"></span>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      "  </td>"+
      "</tr>"+
      ""+
      "        "+
      ""+
      "        "+
      ""+
      ""+
      "<tr class=\"hidden\"><td>"+
      ""+
      ""+
      "<input type=\"hidden\""+
      "       "+
      "       "+
      "           id=\"id\""+
      "       "+
      "       name=\"id\""+
      "      "+
      "         value=\"42737\""+
      "      "+
      "/>"+
      "</td></tr>"+
      ""+
      ""+
      "  <tr>"+
      "     <td colspan=\"2\" class=\"fullyCentered jiraformfooter\" >"+
      "        "+
      "           <input type=\"submit\" name=\"Add \" value=\"Add \""+
      "        "+
      "                       accesskey=\"S\""+
      "                       title=\"Press Alt+S to submit form\""+
      "        "+
      "                        "+
      "                       class=\"spaced\""+
      "                />"+
      ""+
      ""+
      "        "+
      "        "+
      "           "+
      ""+
      "            <input type=\"button\" class=\"spaced\" onclick=\"return hideComment();\" value=\"Cancel\" accesskey=\"`\" />"+
      "        "+
      "        "+
      "        "+
      "     </td>"+
      ""+
      "  </tr>"+
      ""+
      "    "+
      ""+
      "    "+
      "  </table>"+
      "  "+
      ""+
      ""+
      ""+
      "</form>"+
      "  "+
      "  "+
      "    <script language=\"javascript\">"+
      "      try { document.jiraform.elements[0].focus(); } catch (e) {}"+
      "  </script>"+
      "  "+
      ""+
      ""+
      ""+
      "<script language=\"JavaScript\" type=\"text/javascript\">"+
      "<!--"+
      "    "+
      ""+
      "    initPinComments('Always show',"+
      "                    'Always show add comment area',"+
      "                    'Initially hide',"+
      "                    'Initially hide comment area');"+
      ""+
      "    restoreMenu('commentDiv','pinComment');"+
      ""+
      "    function showComment()"+
      "    {"+
      "        try"+
      "        {"+
      "            var commentDiv = document.getElementById(\"commentDiv\");"+
      "            commentDiv.style.display = 'block';"+
      "            restoreMenu('commentDiv','pinComment')"+
      "            setTimeout('document.getElementById(\"comment\").focus();', 20);"+
      "            return false;"+
      "        } catch (e) { return true; }"+
      "    }"+
      ""+
      "    function hideComment()"+
      "    {"+
      "        try"+
      "        {"+
      "            var commentDiv = document.getElementById(\"commentDiv\");"+
      "            commentDiv.style.display = 'none';"+
      "            return true;"+
      "        } catch (e) { return false; }"+
      "    }"+
      ""+
      "//-->"+
      "</script>"+
      ""+
      "</div>"+
      ""+
      ""+
      "</td></tr></table>"+
      "</td></tr></table>"+
      ""+
      ""+
      ""+
      ""+
      ""+
      ""+
      "<div class=\"footer\">"+
      "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">"+
      ""+
      "<tr>"+
      "     <td bgcolor=\"bbbbbb\"><img src=\"/jira/images/border/spacer.gif\" height=\"1\" width=\"100\" border=\"0\"></td>"+
      "</tr>"+
      "<tr>"+
      ""+
      "  "+
      "</tr>"+
      ""+
      ""+
      "  <tr>"+
      "      <td height=\"12\" background=\"/jira/images/border/border_bottom.gif\"><img src=\"/jira/images/border/spacer.gif\" width=\"1\" height=\"1\" border=\"0\"></td>"+
      "  </tr>"+
      " </table>"+
      ""+
      "  <span class=\"poweredbymessage\">"+
      "        Powered by <a href=\"http://www.atlassian.com/jira-bug-tracking/?edition=Enterprise\" class=\"smalltext\">Atlassian JIRA&trade;</a> the <a href=\"http://www.atlassian.com/jira-bug-tracking/?edition=Enterprise\">Professional Issue Tracker</a>. <span style=\"color: #666666;\">(Enterprise Edition, Version: 3.4.1-#107)</span> - <a href=\"http://jira.atlassian.com/default.jsp?clicked=footer\">Bug/feature request</a>"+
      ""+
      "     - <a href=\"/jira/secure/Administrators.jspa\">Contact Administrators</a>"+
      "  </span>"+
      "</div>"+
      ""+
      "</body>"+
      "</html>"+
      ""+
      "";
      List<String> actualRollforwards = parser.parseJiraHTMLAndGetSqlRollForwards(toBeParsed);
      
      assertEquals(1, actualRollforwards.size());
      assertEquals("/llu-inventory/trunk/conf/db/rollforwards/91200000045_create_tpds_response_audit_table.sql", actualRollforwards.get(0));
   }

}
