package com.jonas.agile.devleadtool.component.table;

import junit.framework.TestCase;

public class ColumnTest extends TestCase {
   
   public void testShouldHaveAllColumnsOk(){
      assertEquals(true, Column.Jira.isEditable());
   }

}
