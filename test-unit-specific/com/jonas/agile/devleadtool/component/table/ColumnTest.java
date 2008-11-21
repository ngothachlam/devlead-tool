package com.jonas.agile.devleadtool.component.table;

import java.util.List;
import junit.framework.TestCase;

public class ColumnTest extends TestCase {

   public void testShouldHaveAllColumnsOk() {
      assertEquals(true, Column.Jira.isEditable());
   }

   public void testShouldGetCorrectColumn() {
      assertEquals(Column.Jira, Column.getEnum("Jira"));
      assertEquals(Column.J_Dev_Estimate, Column.getEnum("J_Dev_Estimate"));
   }

   public void testJFixVersionShouldParseOk() {
      List<String> list = getJFixVersionParse("[LLU 11.2]");
      assertEquals(1, list.size());
      assertEquals("LLU 11.2", list.get(0));
      
      list = getJFixVersionParse("[LLU 11.2, LLU 12 - Sprint 5 (current)]");
      assertEquals(2, list.size());
      assertEquals("LLU 11.2", list.get(0));
      assertEquals("LLU 12 - Sprint 5 (current)", list.get(1));
      
      list = getJFixVersionParse("[ LLU 11.2 ,  LLU 12 - Sprint 5 (current) ]");
      assertEquals(2, list.size());
      assertEquals(" LLU 11.2 ", list.get(0));
      assertEquals(" LLU 12 - Sprint 5 (current) ", list.get(1));
      
      list = getJFixVersionParse("[ LLU 11,2 ,  LLU 12 - Sprint 5 (current) ]");
      assertEquals(2, list.size());
      assertEquals(" LLU 11,2 ", list.get(0));
      assertEquals(" LLU 12 - Sprint 5 (current) ", list.get(1));
      
      list = getJFixVersionParse(null);
      assertEquals(0, list.size());
   }

   private List<String> getJFixVersionParse(String string) {
      Object parse = Column.J_FixVersion.parse(string);
      assertTrue(parse instanceof List<?>);
      return (List<String>) parse;
   }

   
   public void testShouldParseOk(){
      assertEquals( false, Column.Jira.isJiraColumn());
      assertEquals( true, Column.Jira.isEditable());
      assertEquals( true, Column.Jira.isToLoad());
      assertEquals("KEY", Column.Jira.parse("key"));
   }
}
