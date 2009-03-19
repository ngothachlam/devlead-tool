package com.jonas.agile.devleadtool.data;

import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import junit.framework.TestCase;

public class BoardStatusValueToJiraStatusMapTest extends TestCase {

   public void testShouldMapOk() {
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Approved, "Closed (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Bug, "Reopened (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Complete, "Closed (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.ForShowCase, "Closed (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.InDevProgress, "In Progress (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.InDevProgress, "Open (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.InQAProgress, "Resolved (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.NA, "Open (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.NA, "Reopened (Fixed)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Open, "Open (Resolved)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Parked, "Open (Resolved)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Resolved, "Open (Resolved)"));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.UnKnown, "Open (Resolved)"));
      
      assertFalse(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Open, "Resolved (Fixed)"));
      assertFalse(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Open, "Resolved (Fixed)"));
      
      fdsf
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.UnKnown, "Open (Resolved)"));
      assertFalse(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.UnKnown, "Resolved (Fixed)"));
   }

}
