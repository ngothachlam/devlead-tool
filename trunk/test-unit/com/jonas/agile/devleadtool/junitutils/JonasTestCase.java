package com.jonas.agile.devleadtool.junitutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.TestObjects;

public class JonasTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		TestObjects.createTestObjects();
	}

	private List<Object> interfaceMocks = new ArrayList<Object>();
	private List<Object> classMocks = new ArrayList<Object>();

	protected <T> T createInterfaceMock(Class<T> theClass) {
		T createMock = EasyMock.createMock(theClass);
		interfaceMocks.add(createMock);
		return createMock;
	}

	protected <T> T createClassMock(Class<T> theClass) {
		T createMock = org.easymock.classextension.EasyMock.createMock(theClass);
		classMocks.add(createMock);
		return createMock;
	}

	protected void replay() {
		for (Object iterable_element : interfaceMocks) {
			EasyMock.replay(iterable_element);
		}
		for (Object iterable_element : classMocks) {
			org.easymock.classextension.EasyMock.replay(iterable_element);
		}
	}

	protected void verify() {
		for (Object iterable_element : interfaceMocks) {
			EasyMock.verify(iterable_element);
		}
		for (Object iterable_element : classMocks) {
			org.easymock.classextension.EasyMock.verify(iterable_element);
		}
	}

	protected void assertJiraDetails(JiraIssue jira, String expectedKey, String expectedSummary, String expectedStatus, String expectedResolution) {
		assertEquals(expectedKey, jira.getKey());
		assertEquals(expectedSummary, jira.getSummary());
		assertEquals(expectedStatus, jira.getStatus());
		assertEquals(expectedResolution, jira.getResolution());
	}
	protected void assertJiraDetails(JiraIssue jira, String expectedKey, String expectedSummary, String expectedStatus, String expectedResolution, JiraVersion[] jiraVersions) {
		assertJiraDetails(jira, expectedKey, expectedSummary, expectedStatus, expectedResolution);
		if (jiraVersions.length > 0) {
			assertEquals("Did expect fixversions!", jiraVersions.length, jira.getFixVersions().size());
			for (JiraVersion jiraVersion : jiraVersions) {
				assertTrue("Expected to have this fixversion!", jira.getFixVersions().contains(jiraVersion));
			}
		} else {
			assertEquals("Did not expect any fixversions!", 0, jira.getFixVersions().size());
		}
		assertEquals(expectedResolution, jira.getResolution());
	}

	protected List<Element> getDomFromFile(File file, String xPath) {
		try {
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(file);
			XPath xpath = XPath.newInstance(xPath);
			return xpath.selectNodes(doc);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void setupMockActualsForElement(Element e, String key, String summary, String status, String resolution) {
		EasyMock.expect(e.getChildText("key")).andReturn(key);
		EasyMock.expect(e.getChildText("summary")).andReturn(summary);
		EasyMock.expect(e.getChildText("status")).andReturn(status);
		EasyMock.expect(e.getChildText("resolution")).andReturn(resolution);
	}

}
