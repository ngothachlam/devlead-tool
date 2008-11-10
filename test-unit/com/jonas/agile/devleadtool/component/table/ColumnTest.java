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
      Object parse = Column.J_FixVersion.parse("[LLU 11.2, LLU 12 - Sprint 5 (current)]");
      assertTrue(parse instanceof List<?>);
      List list = (List) parse;
      assertEquals(2, list.size());
   }

}
