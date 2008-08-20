package com.jonas.jira.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.TestObjects;

public class JiraHttpClientTest extends JonasTestCase {

	public void testApacheCommonsAttempt() throws IOException, HttpException, JDOMException, JiraException {
		JiraHttpClient client = new JiraHttpClient(ClientConstants.JIRA_URL_ATLASSIN);
		client.loginToJira();
		asserFixVersionExistsAndHasJirasAgainstIt(client, TestObjects.Version_AtlassainTST);
	}

	private void asserFixVersionExistsAndHasJirasAgainstIt(JiraHttpClient client, JiraVersion version) throws HttpException, IOException,
			JDOMException, JiraException {
		List<JiraIssue> jiras = client.getJiras(version);
		assertTrue(jiras.size() > 1);
		for (int i = 0; i < jiras.size(); i++) {
			System.out.println(jiras.get(i));
		}
		JiraVersion fixVersion = jiras.get(0).getFixVersions().get(0);
		assertEquals(version, JiraVersion.getVersionById(fixVersion.getId()));
	}

	public void testShouldBuildJiraXMLCorrectly() throws IOException, JDOMException {
		String xml = getFileContents(TestObjects.file);
		JiraHttpClient client = new JiraHttpClient(ClientConstants.JIRA_URL_ATLASSIN);
		List<JiraIssue> jiras = client.buildJirasFromXML(xml);
		assertEquals(1, jiras.size());
		JiraIssue jiraIssueOne = jiras.get(0);
		assertEquals("LLU-4119", jiraIssueOne.getKey());
		assertEquals(1, jiraIssueOne.getFixVersions().size());
		assertEquals("Unresolved", jiraIssueOne.getResolution());
		assertEquals("Open", jiraIssueOne.getStatus());
		assertEquals("&apos;Quality Gateway&apos; tests set up", jiraIssueOne.getSummary());

		JiraVersion jiraVersion = jiraIssueOne.getFixVersions().get(0);
		assertEquals("Version 11 - Next Sprint (2)", jiraVersion.getName());
		assertEquals(JiraProject.LLU_SYSTEMS_PROVISIONING, jiraVersion.getProject());
		assertEquals("11462", jiraVersion.getId());
	}

	public void testShouldBuildJiraXMLCorrectlyWithBuildNo() throws IOException, JDOMException {
		String xml = getFileContents(TestObjects.fileWithBuildNo);
		JiraHttpClient client = new JiraHttpClient(ClientConstants.JIRA_URL_ATLASSIN);
		List<JiraIssue> jiras = client.buildJirasFromXML(xml);
		assertEquals(1, jiras.size());
		JiraIssue jiraIssueOne = jiras.get(0);
		assertEquals("LLU-4052", jiraIssueOne.getKey());
		assertEquals(1, jiraIssueOne.getFixVersions().size());
		assertEquals("Unresolved", jiraIssueOne.getResolution());
		assertEquals("Open", jiraIssueOne.getStatus());
		assertEquals("Change SuiteDispatcher Log from Error to Debug when no jobs are found", jiraIssueOne.getSummary());
		assertEquals("testBuildNo", jiraIssueOne.getBuildNo());

		JiraVersion jiraVersion = jiraIssueOne.getFixVersions().get(0);
		assertEquals("Backlog", jiraVersion.getName());
		assertEquals(JiraProject.LLU_SYSTEMS_PROVISIONING, jiraVersion.getProject());
		assertEquals("11388", jiraVersion.getId());
	}

	private String getFileContents(File file) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader input = new BufferedReader(new FileReader(file));
		try {
			String line = null; // not declared within while loop
			while ((line = input.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
		} finally {
			input.close();
		}
		return sb.toString();
	}

	public void testShouldGetXPathOk() throws JDOMException, IOException {
		String customFields = "	<customfields>"
				+ "   <customfield id=\"customfield_10160\" key=\"com.atlassian.jira.plugin.system.customfieldtypes:textfield\">"
				+ "      <customfieldname>Build Number</customfieldname>" + "      <customfieldvalues>"
				+ "         <customfieldvalue>testBuildNo</customfieldvalue>" + "     </customfieldvalues>" + "   </customfield>"
				+ "   <customfield id=\"customfield_10251\" key=\"com.atlassian.jira.plugin.system.customfieldtypes:select\">"
				+ "      <customfieldname>Sprint (Dev Start)</customfieldname>" + "      <customfieldvalues>"
				+ "         <customfieldvalue>" + "            <![CDATA[Unknown]]>" + "         </customfieldvalue>"
				+ "      </customfieldvalues>" + "   </customfield>" + "</customfields>";

		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(new StringReader(customFields));
		XPath xpath = XPath.newInstance("customfields/customfield[@id='customfield_10160']/customfieldvalues/customfieldvalue");
		List<Element> list = xpath.selectNodes(doc);
		for (Element element : list) {
			System.out.println(element.getText());
		}
	}

}
