package com.jonas.agile.devleadtool.data;

import java.util.Set;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;

public class BoardStatusValueToJiraStatusMap {

//   private static Map<BoardStatusValue, String> mapping = new HashMap<BoardStatusValue, String>();
//
//   private static final BoardStatusValueToJiraStatusMap a01 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.Approved, "Closed");
//   private static final BoardStatusValueToJiraStatusMap a02 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.Bug, "Reopened");
//   private static final BoardStatusValueToJiraStatusMap a03 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.Complete, "Closed");
//   private static final BoardStatusValueToJiraStatusMap a04 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.ForShowCase, "Closed");
//   private static final BoardStatusValueToJiraStatusMap a05 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.InDevProgress, "In Progress");
//   private static final BoardStatusValueToJiraStatusMap a06 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.InQAProgress, "Resolved");
//   private static final BoardStatusValueToJiraStatusMap a07 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.NA, "Open");
//   private static final BoardStatusValueToJiraStatusMap a08 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.Open, "Open");
//   private static final BoardStatusValueToJiraStatusMap a09 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.Parked, "Open");
//   private static final BoardStatusValueToJiraStatusMap a10 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.Resolved, "Resolved");
//   private static final BoardStatusValueToJiraStatusMap a11 = new BoardStatusValueToJiraStatusMap(BoardStatusValue.UnKnown, "Open");
//
//   private BoardStatusValueToJiraStatusMap(BoardStatusValue value, String... jiraStatuses) {
//      Set<String> set = new HashSet<String>();
//      for (String jiraStatus : jiraStatuses) {
//         set.add(jiraStatus);
//      }
////      mapping.put(value, set);
//   }
//
//   public static String getJiraStatusFromBoardValue(BoardStatusValue boardStatus) {
//      return mapping.get(boardStatus);
//   }
   
   public static boolean isMappedOk(BoardStatusValue boardStatus, String jiraStatus) {
      Set<String> jiraStatuses = boardStatus.getJiraStatuses();
      for (String string : jiraStatuses) {
         if(jiraStatus.contains(string))
            return true;
      }
      return false;
   }

}
