package com.jonas.jira.utils;

import java.util.List;
import org.easymock.EasyMock;
import org.jdom.Element;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.agile.devleadtool.junitutils.TestIterator;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.TestObjects;

public class JiraBuilderTest extends JonasTestCase {

   private static final String XPATH_JIRA_RSS_CHANNEL_ITEM = "/rss/channel/item";
   List<Element> listWithBuildNo = getDomFromFile(TestObjects.fileWithBuildNo, XPATH_JIRA_RSS_CHANNEL_ITEM);
   List<Element> listWithEstimate = getDomFromFile(TestObjects.fileWithBuildAndEstimate, XPATH_JIRA_RSS_CHANNEL_ITEM);
   List<Element> listWithOutBuildNo = getDomFromFile(TestObjects.fileWithoutBuildNo, XPATH_JIRA_RSS_CHANNEL_ITEM);

   public void testShouldBuildJiraOkUsingElement() {
      Element e = createClassMock(Element.class);
      setupMockActualsForElement(e, "LLU-1", "Blah", "BlahStatus", "BlahResolution");

      replay();

      JiraIssue jira = (new JiraBuilder()).buildJira(e);

      verify();

      assertJiraDetails(jira, "LLU-1", "Blah", "BlahStatus", "BlahResolution", new JiraVersion[] {});
      assertEquals(0, jira.getFixVersions().size());
      assertEquals(null, jira.getBuildNo());
   }

   public void testShouldBuildJiraOkUsingElementAndFixVersions() {
      Element e = createClassMock(Element.class);
      List<JiraVersion> fixVersions = createInterfaceMock(List.class);

      setupMockActualsForElement(e, "LLU-1", "Blah", "BlahStatus", "BlahResolution");
      EasyMock.expect(fixVersions.get(0)).andReturn(TestObjects.Version_10);
      EasyMock.expect(fixVersions.size()).andReturn(1);

      replay();

      JiraIssue jira = (new JiraBuilder()).buildJira(e, fixVersions);

      verify();

      assertJiraDetails(jira, "LLU-1", "Blah", "BlahStatus", "BlahResolution", new JiraVersion[] { TestObjects.Version_10 });
      assertEquals(1, jira.getFixVersions().size());
      assertEquals(TestObjects.Version_10, jira.getFixVersions().get(0));
      assertEquals(null, jira.getBuildNo());
   }

   public void testShouldBuildJiraOkUsingElementAndFixVersionsWithMoreThanOneFixVersion() {
      Element e = createClassMock(Element.class);
      List<JiraVersion> fixVersions = createInterfaceMock(List.class);

      setupMockActualsForElement(e, "LLU-1", "Blah", "BlahStatus", "BlahResolution");
      EasyMock.expect(fixVersions.get(0)).andReturn(TestObjects.Version_10);
      EasyMock.expect(fixVersions.size()).andReturn(2);

      replay();

      JiraIssue jira = (new JiraBuilder()).buildJira(e, fixVersions);

      verify();

      assertJiraDetails(jira, "LLU-1", "Blah", "BlahStatus", "BlahResolution", new JiraVersion[] { TestObjects.Version_10 });
      assertEquals(1, jira.getFixVersions().size());
      assertEquals(TestObjects.Version_10, jira.getFixVersions().get(0));
      assertEquals(null, jira.getBuildNo());
   }

   public void testShouldBuildJirasOkUsingListOfElementsWithBuildNo() {
      List<Element> mockList = createInterfaceMock(List.class);
      EasyMock.expect(mockList.iterator()).andReturn(new TestIterator(listWithBuildNo, 1));

      replay();

      List<JiraIssue> jiras = (new JiraBuilder()).buildJiras(mockList);

      verify();

      assertEquals(1, jiras.size());
      JiraIssue jiraIssue = jiras.get(0);
      assertJiraDetails(jiraIssue, "LLU-4052", "Change SuiteDispatcher Log from Error to Debug when no jobs are found", "Open", "Unresolved",
            new JiraVersion[] { TestObjects.Version_Backlog });
      assertEquals("testBuildNo", jiraIssue.getBuildNo());
   }

   public void testShouldBuildJirasOkUsingListOfElementsWithBuildNoAndEstimate() {
      List<Element> mockList = createInterfaceMock(List.class);
      EasyMock.expect(mockList.iterator()).andReturn(new TestIterator(listWithEstimate, 1));

      replay();

      List<JiraIssue> jiras = (new JiraBuilder()).buildJiras(mockList);

      verify();

      assertEquals(1, jiras.size());
      JiraIssue jiraIssue = jiras.get(0);
      assertJiraDetails(jiraIssue, "LLU-4072", "R900 - Quality Gateway E2E tests", "Open", "Unresolved", new JiraVersion[] { TestObjects.Version_Backlog });
      assertEquals(null, jiraIssue.getBuildNo());
      assertEquals("57600", jiraIssue.getEstimate());
   }

   public void testShouldBuildJirasOkUsingListOfElementsWithoutBuildNo() {
      List<Element> mockList = createInterfaceMock(List.class);
      EasyMock.expect(mockList.iterator()).andReturn(new TestIterator(listWithOutBuildNo, 1));

      replay();

      List<JiraIssue> jiras = (new JiraBuilder()).buildJiras(mockList);

      verify();

      assertEquals(1, jiras.size());
      JiraIssue jiraIssue = jiras.get(0);
      assertJiraDetails(jiraIssue, "LLU-4119", "&apos;Quality Gateway&apos; tests set up", "Open", "Unresolved",
            new JiraVersion[] { TestObjects.Version_Backlog });
      assertEquals(null, jiraIssue.getBuildNo());
      // assertEquals("Version 11 - Next Sprint (2)", jiraIssue.getFixVersions().get(0).getName());
   }

   // public void testShouldBuildBuildNoOk() throws JDOMException, IOException {
   // assertEquals("testBuildNo", JiraBuilder.getBuildNo(list.get(0),
   // "/item/customfields/customfield[@id='customfield_10160']/customfieldvalues/customfieldvalue"));
   // }
}
