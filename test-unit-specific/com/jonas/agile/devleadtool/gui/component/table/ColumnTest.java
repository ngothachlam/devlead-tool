package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import junit.framework.TestCase;

public class ColumnTest extends TestCase {

   public void testShouldHaveAllColumnsOk() {
      assertEquals(true, ColumnType.Jira.isEditable());
   }

   public void testShouldGetCorrectColumn() {
      assertEquals(ColumnType.Jira, ColumnType.getEnum("Jira"));
      assertEquals(ColumnType.J_DevEst, ColumnType.getEnum("J_Dev_Estimate"));
   }

   public void testShouldParseOk(){
      assertEquals( false, ColumnType.Jira.isJiraColumn());
      assertEquals( true, ColumnType.Jira.isEditable());
      assertEquals( true, ColumnType.Jira.isToLoad());
      assertEquals("KEY", ColumnType.Jira.parseFromPersistanceStore("key"));
   }
   
   public void testShouldParseEstimateOk(){
      assertEquals("", ColumnType.DEst.parseFromPersistanceStore(null));
      assertEquals("", ColumnType.DEst.parseFromPersistanceStore(""));
      assertEquals("key", ColumnType.DEst.parseFromPersistanceStore("key"));
      assertEquals("key", ColumnType.DEst.parseToPersistanceStore("key"));
      assertEquals("1.0", ColumnType.DEst.parseFromPersistanceStore("1.0"));
      assertEquals(1.0d, ColumnType.DEst.parseToPersistanceStore("1.0"));
      assertEquals("1", ColumnType.DEst.parseFromPersistanceStore("1"));
      assertEquals(1d, ColumnType.DEst.parseToPersistanceStore("1"));
   }
   public void testShouldParseFixVersionOk(){
      assertEquals("", ColumnType.FixVersion.parseFromPersistanceStore(null));
      assertEquals("[a]", ColumnType.FixVersion.parseFromPersistanceStore("[a]"));
      assertEquals("", ColumnType.FixVersion.parseToPersistanceStore(null));
      assertEquals("[a]", ColumnType.FixVersion.parseToPersistanceStore("[a]"));
   }
}
