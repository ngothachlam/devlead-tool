package com.jonas.agile.devleadtool.component.table;

import junit.framework.TestCase;

public class ColumnTest extends TestCase {
   
   public void testShouldHaveAllColumnsOk(){
      assertEquals(true, Column.Jira.isEditable());
   }
   
   public void testShouldGetCorrectColumn(){
      assertEquals(Column.Jira, Column.getEnum("Jira"));
      assertEquals(Column.J_Dev_Estimate, Column.getEnum("J_Dev_Estimate"));
   }

}
