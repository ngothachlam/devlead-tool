package com.jonas.jira;

import junit.framework.TestCase;

public class JiraProjectTest extends TestCase {
	
	public void testJiraProject(){
		assertJiraProject("LLU Systems Provisioning", "10070");
		assertJiraProject("LLU Dev Support", "10192");
	}

	private void assertJiraProject(String string, String key) {
		assertEquals( string, JiraProject.getProject(string).getName() );
		assertEquals( key, JiraProject.getProject(string).getSelectId() );
	}

}
