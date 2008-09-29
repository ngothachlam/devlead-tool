package com.jonas.agile.devleadtool.component.table;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class ColumnDataTypeTest extends JonasTestCase {

   public void testShouldHaveAllColumns() {
      assertEquals(23, Column.values().length);
   }
      
   public void testGettersShouldWork() {
      assertEquals(String.class, Column.Jira.getDefaultClass());
      assertEquals("", Column.Jira.getDefaultValue());
   }
}
