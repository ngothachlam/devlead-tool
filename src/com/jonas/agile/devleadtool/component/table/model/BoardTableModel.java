package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.J_Resolution, Column.Release, Column.Merge, Column.BoardStatus,
         Column.Dev_Estimate, Column.Dev_Actual, Column.prio, Column.Note };
   private Logger log = MyLogger.getLogger(BoardTableModel.class);

   public BoardTableModel() {
      super(columns, true);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header, true);
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      String stringValue = (String) value;
      switch (column) {
      case Dev_Actual:
         if (stringValue == null || stringValue.trim().length() <= 0) {
            if (isBoardValueEither(row, Helper.getRequiredActuals())) {
               return SwingUtil.cellRED;
            }
         } else {
            if (isBoardValueEither(row, Helper.getNonRequiredActuals())) {
               return SwingUtil.cellRED;
            }
         }
         break;
      case Dev_Estimate:
         if (stringValue == null || stringValue.trim().length() <= 0) {
            if (isBoardValueEither(row, Helper.getRequiredEstimates())) {
               log.debug("true!");
               return SwingUtil.cellRED;
            }
         }
         break;
      }
      return null;
   }

   public String getRelease(String jira) {
      if (doesJiraExist(jira))
         return (String) getValueAt(Column.Release, jira);
      return "";
   }

   public BoardStatusValue getStatus(String jira) {
      int row = getRowWithJira(jira);
      log.debug("row: " + row + " for jira: " + jira);
      if (row >= 0) {
         BoardStatusValue valueAt = (BoardStatusValue) getValueAt(Column.BoardStatus, jira);
         log.debug("valueat: " + valueAt);
         return valueAt;
      }
      return BoardStatusValue.NA;
   }

   public boolean isBoardValueEither(int row, Set<BoardStatusValue> set) {
      Object value = getValueAt(Column.BoardStatus, row);
      return set.contains(value);
   }

   private static class Helper {

      private static Set<BoardStatusValue> requiredEstimates = new HashSet<BoardStatusValue>();
      private static Set<BoardStatusValue> nonRequiredActuals = new HashSet<BoardStatusValue>();
      private static Set<BoardStatusValue> requiredActuals = new HashSet<BoardStatusValue>();

      private Helper() {
         requiredEstimates.add(BoardStatusValue.Open);
         requiredEstimates.add(BoardStatusValue.Bug);
         requiredEstimates.add(BoardStatusValue.InProgress);
         requiredEstimates.add(BoardStatusValue.Resolved);
         requiredEstimates.add(BoardStatusValue.QA_Progress);
         requiredEstimates.add(BoardStatusValue.Complete);
         requiredEstimates.add(BoardStatusValue.ForShowCase);

         nonRequiredActuals.add(BoardStatusValue.Open);
         nonRequiredActuals.add(BoardStatusValue.Bug);
         nonRequiredActuals.add(BoardStatusValue.InProgress);

         requiredActuals.add(BoardStatusValue.Resolved);
         requiredActuals.add(BoardStatusValue.QA_Progress);
         requiredActuals.add(BoardStatusValue.Complete);
         requiredActuals.add(BoardStatusValue.ForShowCase);

      }

      public static Set<BoardStatusValue> getRequiredActuals() {
         return requiredActuals;
      }

      public static Set<BoardStatusValue> getNonRequiredActuals() {
         return nonRequiredActuals;
      }

      private static Set<BoardStatusValue> getRequiredEstimates() {
         return requiredEstimates;
      }
   }
}
