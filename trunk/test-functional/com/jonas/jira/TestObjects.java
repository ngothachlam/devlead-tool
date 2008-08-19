package com.jonas.jira;

import java.io.File;

public interface TestObjects {
	public static JiraVersion Version10 = new JiraVersion("11382", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 10", false);
	public static JiraVersion Version11 = new JiraVersion("11432", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 11", false);
	public static JiraVersion Version11Next = new JiraVersion("11449", JiraProject.LLU_SYSTEMS_PROVISIONING,
			"Version 11 - Next Sprint (3)", false);
	public static JiraVersion Backlog = new JiraVersion("11388", JiraProject.LLU_SYSTEMS_PROVISIONING, "Backlog", false);
	public static JiraVersion PamsBacklog = new JiraVersion("11458", JiraProject.LLU_SYSTEMS_PROVISIONING, "Pam's Backlog", false);
	public static JiraVersion Version9 = new JiraVersion("11264", JiraProject.LLU_SYSTEMS_PROVISIONING, "Version 9", false);
	public static JiraVersion Atlassain_TST = new JiraVersion("13187", JiraProject.ATLASSIN_TST, "Dev Version", false);
	
	public static File file = new File("test-functional/jiraXML.xml");
	public static File fileWithBuildNo = new File("test-functional/jiraXMLwBuildNo.xml");

}
