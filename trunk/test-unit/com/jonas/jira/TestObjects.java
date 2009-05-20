package com.jonas.jira;

import java.io.File;
import org.jdom.Element;

import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.common.DateHelper;

public class TestObjects {
   public static final Sprint TEST_SPRINT_CURRENT = new Sprint("testCurrentSprint", DateHelper.getTodaysDateWithOffset(-6), DateHelper.getTodaysDateWithOffset(7), 10);
   public static final Sprint TEST_SPRINT_LAST = new Sprint("testLastSprint", DateHelper.getTodaysDateWithOffset(-21), DateHelper.getTodaysDateWithOffset(-7), 10);
   public static File fileWithoutBuildNo = new File("test-functional/jiraXML.xml");
   public static File fileWithBuildNo = new File("test-functional/jiraXMLwBuildNo.xml");
   public static File fileSimple = new File("test-functional/jiraXMLSimple.xml");
   public static File fileWithBuildAndEstimate = new File("test-functional/jiraXMLWithBuildAndEstimate.xml");

   public static Element elementSimple;

   public static JiraProject Project_TST1 = new JiraProject(null, "tstKey1", "tstId1", null);
   public static JiraProject Project_TST2 = new JiraProject(null, "tstKey2", "tstId2", null);

   public static JiraVersion Version_10 = new JiraVersion("11382", JiraProject.LLU, "Version 10", false).cache();
   public static JiraVersion Version_11 = new JiraVersion("11432", JiraProject.LLU, "Version 11", false).cache();
   public static JiraVersion Version_11Next = new JiraVersion("11449", JiraProject.LLU, "Version 11 - Next Sprint (3)", false).cache();

   public static JiraVersion Version_9 = new JiraVersion("11264", JiraProject.LLU, "Version 9", false).cache();
   public static JiraVersion Version_AtlassainTST = new JiraVersion("13187", JiraProject.ATLASSIN_TST, "Dev Version", false).cache();
   public static JiraVersion Version_Backlog = new JiraVersion("11388", JiraProject.LLU, "Backlog", false).cache();
   public static JiraVersion Version_PamsBacklog = new JiraVersion("11458", JiraProject.LLU, "Pam's Backlog", false).cache();

}
