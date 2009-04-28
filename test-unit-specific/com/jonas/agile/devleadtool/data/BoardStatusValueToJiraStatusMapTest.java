package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import junit.framework.TestCase;

public class BoardStatusValueToJiraStatusMapTest extends TestCase {

   public void testShouldMapOk() {
      assertTrue(isMappingOk(BoardStatusValue.Approved, "Closed (Fixed)"));
      assertTrue(isMappingOk(BoardStatusValue.Bug, "Reopened (Fixed)"));
      assertTrue(isMappingOk(BoardStatusValue.Complete, "Closed (Fixed)"));
      assertTrue(isMappingOk(BoardStatusValue.ForShowCase, "Closed (Fixed)"));
      assertTrue(isMappingOk(BoardStatusValue.InProgress, "In Progress (Fixed)"));
      assertTrue(isMappingOk(BoardStatusValue.InProgress, "Open (Fixed)"));
      assertTrue(isMappingOk(BoardStatusValue.NA, "Open (Fixed)"));
      assertTrue(isMappingOk(BoardStatusValue.Open, "Open (Resolved)"));
//      assertTrue(isMappingOk(BoardStatusValue.Parked, "Open (Resolved)"));
      assertTrue(isMappingOk(BoardStatusValue.Resolved, "Resolved (Fixed)"));
      assertTrue(isMappingOk(BoardStatusValue.Resolved, "Resolved (Resolved)"));
      assertTrue(isMappingOk(BoardStatusValue.UnKnown, "Open (Resolved)"));

      assertFalse(isMappingOk(BoardStatusValue.Open, "Resolved (Open)"));
      assertFalse(isMappingOk(BoardStatusValue.UnKnown, "Resolved (Fixed)"));
   }

   private boolean isMappingOk(BoardStatusValue approved, String jiraStatus) {
      // long currentMillis = System.currentTimeMillis();
      boolean mappedOk = BoardStatusValueToJiraStatusMap.isMappedOk(approved, jiraStatus);
      // System.out.println(System.currentTimeMillis() - currentMillis);
      return mappedOk;
   }

}
