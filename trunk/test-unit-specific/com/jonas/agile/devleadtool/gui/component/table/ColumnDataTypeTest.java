package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class ColumnDataTypeTest extends JonasTestCase {

   public void testGettersShouldWork() {
      assertEquals(String.class, ColumnWrapper.get(ColumnType.Jira).getDefaultClass());
      assertEquals("", ColumnWrapper.get(ColumnType.Jira).getDefaultValue());
      assertEquals(true, ColumnWrapper.get(ColumnType.Jira).isEditable());
      assertEquals(false, ColumnWrapper.get(ColumnType.Jira).isJiraColumn());
      assertEquals(true, ColumnWrapper.get(ColumnType.Jira).isToLoad());
   }
}
