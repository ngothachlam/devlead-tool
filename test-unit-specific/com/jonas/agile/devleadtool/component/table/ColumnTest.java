package com.jonas.agile.devleadtool.component.table;

import java.util.List;
import junit.framework.TestCase;

public class ColumnTest extends TestCase {

   public void testShouldHaveAllColumnsOk() {
      assertEquals(true, Column.Jira.isEditable());
   }

   public void testShouldGetCorrectColumn() {
      assertEquals(Column.Jira, Column.getEnum("Jira"));
      assertEquals(Column.J_Dev_Estimate, Column.getEnum("J_Dev_Estimate"));
   }

   public void testShouldParseOk(){
      assertEquals( false, Column.Jira.isJiraColumn());
      assertEquals( true, Column.Jira.isEditable());
      assertEquals( true, Column.Jira.isToLoad());
      assertEquals("KEY", Column.Jira.parseFromPersistanceStore("key"));
   }
   
   public void testShouldParseEstimateOk(){
      assertEquals("", Column.Dev_Estimate.parseFromPersistanceStore(null));
      assertEquals("", Column.Dev_Estimate.parseFromPersistanceStore(""));
      assertEquals("key", Column.Dev_Estimate.parseFromPersistanceStore("key"));
      assertEquals("key", Column.Dev_Estimate.parseToPersistanceStore("key"));
      assertEquals("1.0", Column.Dev_Estimate.parseFromPersistanceStore("1.0"));
      assertEquals(1.0d, Column.Dev_Estimate.parseToPersistanceStore("1.0"));
      assertEquals("1", Column.Dev_Estimate.parseFromPersistanceStore("1"));
      assertEquals(1d, Column.Dev_Estimate.parseToPersistanceStore("1"));
   }
   public void testShouldParseFixVersionOk(){
      assertEquals("", Column.J_FixVersion.parseFromPersistanceStore(null));
      assertEquals("[a]", Column.J_FixVersion.parseFromPersistanceStore("[a]"));
      assertEquals("", Column.J_FixVersion.parseToPersistanceStore(null));
      assertEquals("[a]", Column.J_FixVersion.parseToPersistanceStore("[a]"));
   }
}
