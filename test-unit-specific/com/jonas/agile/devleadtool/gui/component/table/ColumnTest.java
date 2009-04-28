package com.jonas.agile.devleadtool.gui.component.table;

import com.jonas.agile.devleadtool.gui.component.table.Column;
import junit.framework.TestCase;

public class ColumnTest extends TestCase {

   public void testShouldHaveAllColumnsOk() {
      assertEquals(true, Column.Jira.isEditable());
   }

   public void testShouldGetCorrectColumn() {
      assertEquals(Column.Jira, Column.getEnum("Jira"));
      assertEquals(Column.J_DevEst, Column.getEnum("J_Dev_Estimate"));
   }

   public void testShouldParseOk(){
      assertEquals( false, Column.Jira.isJiraColumn());
      assertEquals( true, Column.Jira.isEditable());
      assertEquals( true, Column.Jira.isToLoad());
      assertEquals("KEY", Column.Jira.parseFromPersistanceStore("key"));
   }
   
   public void testShouldParseEstimateOk(){
      assertEquals("", Column.DEst.parseFromPersistanceStore(null));
      assertEquals("", Column.DEst.parseFromPersistanceStore(""));
      assertEquals("key", Column.DEst.parseFromPersistanceStore("key"));
      assertEquals("key", Column.DEst.parseToPersistanceStore("key"));
      assertEquals("1.0", Column.DEst.parseFromPersistanceStore("1.0"));
      assertEquals(1.0d, Column.DEst.parseToPersistanceStore("1.0"));
      assertEquals("1", Column.DEst.parseFromPersistanceStore("1"));
      assertEquals(1d, Column.DEst.parseToPersistanceStore("1"));
   }
   public void testShouldParseFixVersionOk(){
      assertEquals("", Column.FixVersion.parseFromPersistanceStore(null));
      assertEquals("[a]", Column.FixVersion.parseFromPersistanceStore("[a]"));
      assertEquals("", Column.FixVersion.parseToPersistanceStore(null));
      assertEquals("[a]", Column.FixVersion.parseToPersistanceStore("[a]"));
   }
}
