package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.J_Resolution, Column.Release, Column.Merge,
         Column.BoardStatus, Column.Dev_Estimate, Column.Dev_Actual, Column.prio, Column.Note };
   private Logger log = MyLogger.getLogger(BoardTableModel.class);
   private CellColorHelper cellColorHelper = CellColorHelper.getInstance();

   public BoardTableModel() {
      super(columns);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      log.debug("column: " + column + " value: \"" + value + "\" row: " + row);
      String stringValue = (String) value;
      switch (column) {
      case Dev_Actual:
         if (stringValue == null || stringValue.trim().length() <= 0) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredActuals())) {
               return SwingUtil.cellRed;
            }
         } else {
            if (isBoardValueEither(row, cellColorHelper.getNonRequiredActuals())) {
               return SwingUtil.cellRed;
            }
         }
         break;
      case Dev_Estimate:
         if (stringValue == null || stringValue.trim().length() <= 0) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredEstimates())) {
               log.debug("true!");
               return SwingUtil.cellRed;
            }
         } else {

         }
         break;
      case BoardStatus:
         //TODO this is not working! the renderer for BoardStatusValue must not get in here for some reason!
         BoardStatusValue newValue = BoardStatusValue.get(stringValue);
         switch (newValue) {
         case Resolved:
         case InQAProgress:
            return SwingUtil.cellBlue;
         case Approved:
         case Complete:
         case ForShowCase:
            return SwingUtil.cellGreen;
         case Bug:
            return SwingUtil.cellRed;
         default:
            break;
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
      boolean contains = set.contains(value);
      Iterator<BoardStatusValue> iter = set.iterator();
      while (iter.hasNext()) {
         BoardStatusValue type = (BoardStatusValue) iter.next();
         log.debug("\tset contains: " + type);
      }
      log.debug("BoardStatus value for row " + row + " is \"" + value + "\"(" + value.getClass() + ") and is required: " + contains);
      return contains;
   }

   private static class CellColorHelper {

      private static CellColorHelper instance = new CellColorHelper();
      private Set<BoardStatusValue> requiredEstimates = new HashSet<BoardStatusValue>();
      private Set<BoardStatusValue> nonRequiredActuals = new HashSet<BoardStatusValue>();
      private Set<BoardStatusValue> requiredActuals = new HashSet<BoardStatusValue>();

      private CellColorHelper() {
         requiredEstimates.add(BoardStatusValue.Open);
         requiredEstimates.add(BoardStatusValue.Bug);
         requiredEstimates.add(BoardStatusValue.InDevProgress);
         requiredEstimates.add(BoardStatusValue.Resolved);
         requiredEstimates.add(BoardStatusValue.InQAProgress);
         requiredEstimates.add(BoardStatusValue.Complete);
         requiredEstimates.add(BoardStatusValue.ForShowCase);
         requiredEstimates.add(BoardStatusValue.Approved);

         nonRequiredActuals.add(BoardStatusValue.Open);
         nonRequiredActuals.add(BoardStatusValue.Bug);
         nonRequiredActuals.add(BoardStatusValue.InDevProgress);

         requiredActuals.add(BoardStatusValue.Resolved);
         requiredActuals.add(BoardStatusValue.InQAProgress);
         requiredActuals.add(BoardStatusValue.Complete);
         requiredActuals.add(BoardStatusValue.ForShowCase);
         requiredActuals.add(BoardStatusValue.Approved);
      }

      public static CellColorHelper getInstance() {
         return instance;
      }

      public Set<BoardStatusValue> getRequiredActuals() {
         return requiredActuals;
      }

      public Set<BoardStatusValue> getNonRequiredActuals() {
         return nonRequiredActuals;
      }

      private Set<BoardStatusValue> getRequiredEstimates() {
         return requiredEstimates;
      }
   }
}
