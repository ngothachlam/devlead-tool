package com.jonas.jira.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.TestObjects;

public class JiraBuilderTest extends TestCase {

	List<Element> listWithBuildNo = null;
	List<Element> listWithOutBuildNo = null;

	protected void setUp() throws Exception {
		super.setUp();
	}

	public JiraBuilderTest() {
		listWithBuildNo = setMockObject(TestObjects.fileWithBuildNo);
		listWithOutBuildNo = setMockObject(TestObjects.file);
	}

	private List<Element> setMockObject(File file) {
		try {
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(file);
			XPath xpath = XPath.newInstance("/rss/channel/item");
			return xpath.selectNodes(doc);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private final class TestIteratorImplementation implements Iterator {
		int i = 1;
		private List<Element> list;

		public TestIteratorImplementation(List<Element> list) {
			this.list = list;
		}

		public boolean hasNext() {
			return i > 0;
		}

		public Object next() {
			i--;
			return list.get(0);
		}

		public void remove() {
			i--;
		}
	}

	List<Element> mockList = EasyMock.createMock(List.class);

	public void testShouldBuildJirasOkWithBuildNo() {
		EasyMock.expect(mockList.iterator()).andReturn(new TestIteratorImplementation(listWithBuildNo));
		EasyMock.replay(mockList);

		List<JiraIssue> jiras = JiraBuilder.buildJiras(mockList);

		EasyMock.verify(mockList);

		JiraIssue jiraIssue = jiras.get(0);
		assertTrue(jiraIssue != null);
		assertEquals("LLU-4052", jiraIssue.getKey());
		assertEquals("Change SuiteDispatcher Log from Error to Debug when no jobs are found", jiraIssue.getSummary());
		assertEquals("Backlog", jiraIssue.getFixVersions().get(0).getName());
		assertEquals("testBuildNo", jiraIssue.getBuildNo());
	}

	public void testShouldBuildJirasOkWithoutBuildNo() {
		EasyMock.expect(mockList.iterator()).andReturn(new TestIteratorImplementation(listWithOutBuildNo));
		EasyMock.replay(mockList);

		List<JiraIssue> jiras = JiraBuilder.buildJiras(mockList);

		EasyMock.verify(mockList);

		JiraIssue jiraIssue = jiras.get(0);
		assertTrue(jiraIssue != null);
		assertEquals("LLU-4119", jiraIssue.getKey());
		assertEquals("&apos;Quality Gateway&apos; tests set up", jiraIssue.getSummary());
		assertEquals("Version 11 - Next Sprint (2)", jiraIssue.getFixVersions().get(0).getName());
		assertEquals(null, jiraIssue.getBuildNo());
	}

	// public void testShouldBuildBuildNoOk() throws JDOMException, IOException {
	// assertEquals("testBuildNo", JiraBuilder.getBuildNo(list.get(0),
	// "/item/customfields/customfield[@id='customfield_10160']/customfieldvalues/customfieldvalue"));
	// }
}
