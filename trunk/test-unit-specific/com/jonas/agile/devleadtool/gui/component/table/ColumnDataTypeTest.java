package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.Column;
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
