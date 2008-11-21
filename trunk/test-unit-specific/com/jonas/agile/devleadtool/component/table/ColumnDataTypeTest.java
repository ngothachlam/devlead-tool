package com.jonas.agile.devleadtool.component.table;

import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class ColumnDataTypeTest extends JonasTestCase {

   public void testGettersShouldWork() {
      assertEquals(String.class, Column.Jira.getDefaultClass());
      assertEquals("", Column.Jira.getDefaultValue());
      assertEquals(true, Column.Jira.isEditable());
      assertEquals(false, Column.Jira.isJiraColumn());
      assertEquals(true, Column.Jira.isToLoad());
   }
}
