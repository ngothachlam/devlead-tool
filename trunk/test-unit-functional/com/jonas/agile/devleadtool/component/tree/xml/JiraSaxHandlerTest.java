package com.jonas.agile.devleadtool.component.tree.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.jonas.agile.devleadtool.controller.listener.JiraParseListener;

public class JiraSaxHandlerTest extends TestCase {

   private JiraParseListenerTestHelper jiraParseListener;
   JiraParseListenerTestHelper jiraParseListenerTestHelper = new JiraParseListenerTestHelper();
   private JiraSaxHandler saxHandler;

   private XMLReader xmlReader;
   private void assertJiraDTO(String key, String id, String resolution, String sprint, String status, String summary, boolean syncable, int originalEstimate, String project, String... fixVersions) {
      JiraDTO jiraParsed = jiraParseListener.getJiraParsed(key);
      assertEquals(id, jiraParsed.getId() );
      assertEquals(key, jiraParsed.getKey() );
      assertEquals(resolution, jiraParsed.getResolution() );
      assertEquals(sprint, jiraParsed.getSprint() );
      assertEquals(status, jiraParsed.getStatus() );
      assertEquals(summary, jiraParsed.getSummary() );
      assertEquals(syncable, jiraParsed.getSyncable() );
      List list = new ArrayList();
      for (String fixVersion : fixVersions) {
         list.add(fixVersion);
      }
      assertEquals(list, jiraParsed.getFixVersions() );
      assertEquals(originalEstimate, jiraParsed.getOriginalEstimate() );
      assertEquals(project, jiraParsed.getProject() );
   }

   private InputSource getInputSource(String pathname) throws FileNotFoundException {
      File file = new File(pathname);
      Reader fileReader = new FileReader(file);
      Reader bufferedReader = new BufferedReader(fileReader);
      InputSource input = new InputSource(bufferedReader);
      return input;
   }

   private JiraParseListenerTestHelper getJiraParseListener() {
      return jiraParseListenerTestHelper;
      
   }

   protected void setUp() throws Exception {
      super.setUp();
      xmlReader = XMLReaderFactory.createXMLReader();
      saxHandler = new JiraSaxHandler();
      xmlReader.setContentHandler(saxHandler);
      jiraParseListener = getJiraParseListener();
      saxHandler.addJiraParseListener(jiraParseListener);
   }

   protected void tearDown() throws Exception {
      saxHandler.clearAllListeners();
   }

   public void testShouldParseOneJiraOk() throws FileNotFoundException, IOException, SAXException {
      xmlReader.parse(getInputSource("test-data/JiraWithAllFields.xml"));
      
      assertTrue(jiraParseListener.getCountOfJirasParsed() > 0);
      
      assertJiraDTO("LLU-4474", "44057", "Unresolved", "13-1", "Open", "[Merge for LLU-4455] Bulk Tie Pair Fix - unable to Upload a csv file to fix FAULTY Tie Pairs", false, 0, "Flexi Cease", "LLU 13");
   }
   
   public void testShouldParseManyJirasOk() throws FileNotFoundException, IOException, SAXException {

      xmlReader.parse(getInputSource("test-data/ListOfSprintJirasMany.xml"));
      
      assertTrue(jiraParseListener.getCountOfJirasParsed() > 0);
      
      assertJiraDTO("LLU-4211", "41698", "Unresolved", "12.4", "Open", "Fens \"Unhandled Exception Occurred\" failure", false, 0, "", "LLU 12");
      assertJiraDTO("LLU-4104", "40487", "Unresolved", "12.4", "Open", "Merge -Deadlock Servlet processes records which are in an end state", false, 0, "", "Pam&apos;s Backlog");
      assertJiraDTO("LLU-4190", "41524", "Fixed", "12-4", "Closed", "Provide E2E fitnesse test for BBMS Northbound Status updates", false, 0, "");
      assertJiraDTO("LLU-4148", "40895", "DEV Complete", "12-4", "Resolved", "[FC-ReserveLogic] Process MLC state is jumpered to a line that is not available - SM", false, 28800, "", "LLU 12");
      assertJiraDTO("LLU-4474", "44057", "Unresolved", "13-1", "Open", "[Merge for LLU-4455] Bulk Tie Pair Fix - unable to Upload a csv file to fix FAULTY Tie Pairs", false, 0, "Flexi Cease", "LLU 13");
   }

   private final class JiraParseListenerTestHelper implements JiraParseListener {
      final Map<String, JiraDTO> jiras = new HashMap<String, JiraDTO>();

      public int getCountOfJirasParsed(){
         return jiras.size();
      }
      public JiraDTO getJiraParsed(String key){
         return jiras.get(key);
      }

      @Override
      public void notifyParsed(JiraDTO jira) {
         System.out.println("parsed " + jira.getKey());
         jiras.put(jira.getKey(), jira);
      }

      @Override
      public void notifyParsingFinished() {
         System.out.println("parsing finished!");
      }

      @Override
      public void notifyParsingStarted() {
         System.out.println("parsing started!");
      }
   }

}
