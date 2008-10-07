package com.jonas.agile.devleadtool.junitutils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import junit.framework.TestCase;
import org.easymock.classextension.EasyMock;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;
import com.jonas.jira.TestObjects;
import com.jonas.jira.utils.JiraBuilder;

public class JonasTestCase extends TestCase {

   private List<Object> classMocks = new ArrayList<Object>();
   private List<Object> interfaceMocks = new ArrayList<Object>();

   protected JiraBuilder mockJiraBuilder = createClassMock(JiraBuilder.class);
   
   protected JonasTestCase(){
      MyLogger.setup("properties/log4j.properties");
   }
   
   protected void assertJiraDetails(JiraIssue jira, String expectedKey, String expectedSummary, String expectedStatus, String expectedResolution, String expectedType) {
      assertEquals(expectedKey, jira.getKey());
      assertEquals(expectedSummary, jira.getSummary());
      assertEquals(expectedStatus, jira.getStatus());
      assertEquals(expectedResolution, jira.getResolution());
      assertEquals(expectedType, jira.getType());
   }

   protected void assertJiraDetails(JiraIssue jira, String expectedKey, String expectedSummary, String expectedStatus, String expectedResolution,
         String expectedType, JiraVersion[] jiraVersions) {
      assertJiraDetails(jira, expectedKey, expectedSummary, expectedStatus, expectedResolution, expectedType);
      List<JiraVersion> fixVersions = jira.getFixVersions();
      if (jiraVersions.length > 0) {
         assertEquals("Did expect fixversions!", jiraVersions.length, fixVersions.size());
         for (JiraVersion jiraVersion : jiraVersions) {
            assertTrue("Expected to have this fixversion! (got:" + jiraVersion + ")", fixVersions.contains(jiraVersion));
         }
      } else {
         assertEquals("Did not expect any fixversions!", 0, fixVersions.size());
      }
      assertEquals(expectedResolution, jira.getResolution());
   }

   protected <T> T createClassMock(Class<T> theClass) {
      T mock = EasyMock.createMock(theClass);
      classMocks.add(mock);
      return mock;
   }

//   protected <T> T createNiceClassMock(Class<T> theClass) {
//      T mock = EasyMock.createNiceMock(theClass);
//      classMocks.add(mock);
//      return mock;
//   }

   protected <T> T createMock(Class<T> class1, Method... method) {
      T mock = EasyMock.createMock(class1, method);
      classMocks.add(mock);
      return mock;

   }

   protected <T> T createInterfaceMock(Class<T> theClass) {
      T createMock = EasyMock.createMock(theClass);
      interfaceMocks.add(createMock);
      return createMock;
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

   protected void replay() {
      for (Object iterable_element : interfaceMocks) {
         EasyMock.replay(iterable_element);
      }
      for (Object iterable_element : classMocks) {
         EasyMock.replay(iterable_element);
      }
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      TestObjects.createTestObjects();
      // EasyMock.expect(mockFactory.getJiraBuilder()).andReturn(mockJiraBuilder).anyTimes();
   }

   protected void setupMockActualsForElementExtendedWithEstimate(Element e) {
      setupMockActualsForElement(e, "LLU-1", "Blah", "BlahStatus", "BlahResolution", "BlahType");
      EasyMock.expect(e.getChildText("timeoriginalestimate")).andReturn("2 days");
   }

   protected void setupMockActualsForElement(Element e, String key, String summary, String status, String resolution, String type) {
      EasyMock.expect(e.getChildText("key")).andReturn(key);
      EasyMock.expect(e.getChildText("summary")).andReturn(summary);
      EasyMock.expect(e.getChildText("status")).andReturn(status);
      EasyMock.expect(e.getChildText("resolution")).andReturn(resolution);
      EasyMock.expect(e.getChildText("type")).andReturn(type);
   }

   protected void verify() {
      for (Object iterable_element : interfaceMocks) {
         EasyMock.verify(iterable_element);
      }
      for (Object iterable_element : classMocks) {
         EasyMock.verify(iterable_element);
      }
   }

   protected void reset() {
      for (Object iterable_element : interfaceMocks) {
         EasyMock.reset(iterable_element);
      }
      for (Object iterable_element : classMocks) {
         EasyMock.reset(iterable_element);
      }
   }

   protected Vector<Object> getTestContentRow(int noOfColumns, String data) {
      Vector<Object> vector = new Vector<Object>(noOfColumns);
      int column = 0;
      while (column < noOfColumns) {
         vector.add(data + column);
         column++;
      }
      return vector;
   }

}
