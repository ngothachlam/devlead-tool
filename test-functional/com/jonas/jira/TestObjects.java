package com.jonas.jira;

import java.io.File;
import org.jdom.Element;

public class TestObjects {
	public static File fileWithoutBuildNo = new File("test-functional/jiraXML.xml");
	public static File fileWithBuildNo = new File("test-functional/jiraXMLwBuildNo.xml");
	public static File fileSimple = new File("test-functional/jiraXMLSimple.xml");
	
	public static Element elementSimple ;
	
	public static JiraProject Project_TST1;
	public static JiraProject Project_TST2;
	
	public static JiraVersion Version_10;
	public static JiraVersion Version_11;
	public static JiraVersion Version_11Next;

	public static JiraVersion Version_9;
	public static JiraVersion Version_AtlassainTST;
	public static JiraVersion Version_Backlog;
	public static JiraVersion Version_PamsBacklog;

	public static void createTestObjects(){
	   
	   
		Project_TST1 = new JiraProject(null, "tstProject1", "tstKey1", "tstId1");
		Project_TST2 = new JiraProject(null, "tstProject2", "tstKey2", "tstId2");
		
		Version_10 = new JiraVersion("11382", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 10", false);
		Version_11 = new JiraVersion("11432", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 11", false);
		Version_11Next = new JiraVersion("11449", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 11 - Next Sprint (3)", false);
		
		Version_9 = new JiraVersion("11264", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 9", false);
		Version_AtlassainTST = new JiraVersion("13187", JiraProject.ATLASSIN_TST, "Dev Version", false);
		Version_Backlog = new JiraVersion("11388", JiraProject.LLU_SYSTEMS_PROVISIONING, "Backlog", false);
		Version_PamsBacklog = new JiraVersion("11458", JiraProject.LLU_SYSTEMS_PROVISIONING, "Pam's Backlog", false);
	}
	
}
