package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class ColumnDataTypeTest extends JonasTestCase {

   public void testGettersShouldWork() {
      assertEquals(String.class, ColumnType.Jira.getDefaultClass());
      assertEquals("", ColumnType.Jira.getDefaultValue());
      assertEquals(true, ColumnType.Jira.isEditable());
      assertEquals(false, ColumnType.Jira.isJiraColumn());
      assertEquals(true, ColumnType.Jira.isToLoad());
   }
}
