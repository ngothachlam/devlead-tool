package com.jonas.jira;

import java.util.Iterator;
import java.util.List;

import org.easymock.EasyMock;
import org.jdom.Element;

import junit.framework.TestCase;

public class JiraIssueTest extends TestCase {
	List mockList = EasyMock.createMock(List.class);

	public void testShouldBuildJirasOk() {
		EasyMock.expect(mockList.iterator()).andReturn(new Iterator() {
			int i = 1;

			public boolean hasNext() {
				return i > 0;
			}

			public Object next() {
				i--;
				return getMockElement();
			}

			public void remove() {
				i--;
			}
		});
		EasyMock.replay(mockList);
		List<JiraIssue> jiras = JiraIssue.buildJiras(mockList, JiraVersion.Version10);
		EasyMock.verify(mockList);

		JiraIssue jiraIssue = jiras.get(0);
		assertTrue(jiraIssue != null);
		JiraVersion fixVersion = jiraIssue.getFixVersion();
		assertEquals("Version 10", fixVersion.getName());
	}

	public Element getMockElement() {
		Element mockElement = new Element("test");
		Element child = new Element("fixVersion");
		child.setText("Version 10");
		mockElement.addContent(child);
		return mockElement;
	}

}
