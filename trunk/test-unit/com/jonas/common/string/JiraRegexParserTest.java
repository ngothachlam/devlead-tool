package com.jonas.common.string;

import java.util.List;
import com.jonas.agile.devleadtool.dto.JiraStringDTO;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class JiraRegexParserTest extends JonasTestCase {

   JiraRegexParser parser;
   
   @Override
   protected void setUp() throws Exception {
      parser = new JiraRegexParser();
      super.setUp();
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldSeparateJiraStringOk() {
      List<String> separatedStrings = parser.separateIntoJiras("llu-1 llu-2\tllu-3 llu-4   llu-5");

      assertEquals(5, separatedStrings.size());
      assertEquals("llu-1", separatedStrings.get(0));
      assertEquals("llu-2", separatedStrings.get(1));
      assertEquals("llu-3", separatedStrings.get(2));
      assertEquals("llu-4", separatedStrings.get(3));
      assertEquals("llu-5", separatedStrings.get(4));
   }
   
   public void testShouldSeparateJiraStringIntoDTOOk() {
      assertJiraStringDTO(null, null, null, null, null, null, parser.separateJira("':"));
      assertJiraStringDTO("1", null, null, null, null, null, parser.separateJira("1':"));
      assertJiraStringDTO("1", null, null, null, null, null, parser.separateJira("1'::"));
      assertJiraStringDTO("1", null, "test", null, null, "2", parser.separateJira("1':test:2"));
      assertJiraStringDTO("llu-1", null, null, null, null, null, parser.separateJira("llu-1"));
      assertJiraStringDTO("llu-1", null, "1", null, null, null, parser.separateJira("llu-1''':1"));
      assertJiraStringDTO("llu-1", "2", "5", "3", "4", null, parser.separateJira("llu-1'2'3'4:5"));
      assertJiraStringDTO("llu-1", "2", "5", null, "4", null, parser.separateJira("llu-1'2''4:5"));
      assertJiraStringDTO("llu-1", "2", null, "3", "4", null, parser.separateJira("llu-1'2'3'4:"));
      assertJiraStringDTO("llu-1", null, "2", null, null, null, parser.separateJira("llu-1:2"));
      assertJiraStringDTO("llu-1", "2.6", "5.9", "3.7", "4.8", null, parser.separateJira("llu-1'2.6'3.7'4.8:5.9"));
      assertJiraStringDTO("llu-1", "a", "d", "b", "c2", "e", parser.separateJira("llu-1'a'b'c2:d:e:f"));
   }
   
   public void testShouldHyphenatePrefixProperly(){
      assertEquals("", parser.getPrefixWithHyphen(null));
      assertEquals("", parser.getPrefixWithHyphen(""));
      assertEquals("llu-", parser.getPrefixWithHyphen("llu"));
      
   }

   private void assertJiraStringDTO(String jira, String devEstimate, String devRemainder, String devActual, String qaEstimate,
         String qaRemainder, JiraStringDTO jiraStringDTO) {
      assertEquals(jira, jiraStringDTO.getJira());
      assertEquals(devEstimate, jiraStringDTO.getDevEstimate());
      assertEquals(devRemainder, jiraStringDTO.getDevRemainder());
      assertEquals(devActual, jiraStringDTO.getDevActual());
      assertEquals(qaEstimate, jiraStringDTO.getQAEstimate());
      assertEquals(qaRemainder, jiraStringDTO.getQaRemainder());
   }
}
