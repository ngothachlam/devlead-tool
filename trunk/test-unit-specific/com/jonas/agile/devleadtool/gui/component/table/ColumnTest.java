package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import junit.framework.TestCase;

public class ColumnTest extends TestCase {

   public void testShouldHaveAllColumnsOk() {
      assertEquals(true, ColumnWrapper.get(ColumnType.Jira).isEditable());
   }

   public void testShouldGetCorrectColumn() {
      assertEquals(ColumnType.Jira, ColumnWrapper.get("Jira").getType());
      assertEquals(ColumnType.J_DevEst, ColumnWrapper.get("J_Dev_Estimate").getType());
   }

   public void testShouldParseOk(){
      assertEquals( false, ColumnWrapper.get(ColumnType.Jira).isJiraColumn());
      assertEquals( true, ColumnWrapper.get(ColumnType.Jira).isEditable());
      assertEquals( true, ColumnWrapper.get(ColumnType.Jira).isToLoad());
      assertEquals("KEY", ColumnWrapper.get(ColumnType.Jira).parseFromPersistanceStore("key"));
   }
   
   public void testShouldParseEstimateOk(){
      assertEquals("", ColumnWrapper.get(ColumnType.DEst).parseFromPersistanceStore(null));
      assertEquals("", ColumnWrapper.get(ColumnType.DEst).parseFromPersistanceStore(""));
      assertEquals("key", ColumnWrapper.get(ColumnType.DEst).parseFromPersistanceStore("key"));
      assertEquals("key", ColumnWrapper.get(ColumnType.DEst).parseToPersistanceStore("key"));
      assertEquals("1.0", ColumnWrapper.get(ColumnType.DEst).parseFromPersistanceStore("1.0"));
      assertEquals(1.0d, ColumnWrapper.get(ColumnType.DEst).parseToPersistanceStore("1.0"));
      assertEquals("1", ColumnWrapper.get(ColumnType.DEst).parseFromPersistanceStore("1"));
      assertEquals(1d, ColumnWrapper.get(ColumnType.DEst).parseToPersistanceStore("1"));
   }
   public void testShouldParseFixVersionOk(){
      assertEquals("", ColumnWrapper.get(ColumnType.FixVersion).parseFromPersistanceStore(null));
      assertEquals("[a]", ColumnWrapper.get(ColumnType.FixVersion).parseFromPersistanceStore("[a]"));
      assertEquals("", ColumnWrapper.get(ColumnType.FixVersion).parseToPersistanceStore(null));
      assertEquals("[a]", ColumnWrapper.get(ColumnType.FixVersion).parseToPersistanceStore("[a]"));
   }
}
