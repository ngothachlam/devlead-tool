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
         Column.BoardStatus, Column.Old, Column.Dev_Estimate, Column.Dev_Actual, Column.QA_Estimate, Column.prio, Column.Note };
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
      if (!isString(value)) {
         return null;
      }
      String stringValue = (String) value;
      switch (column) {
      case Dev_Actual:
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredDevActuals())) {
               return SwingUtil.cellRed;
            }
         } else {
            if (isBoardValueEither(row, cellColorHelper.getRequiredBlankDevActuals())) {
               return SwingUtil.cellRed;
            }
         }
         break;
      case QA_Estimate:
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredQAEstimates())) {
               return SwingUtil.cellRed;
            }
         }
         break;
      case Dev_Estimate:
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredDevEstimates())) {
               return SwingUtil.cellRed;
            }
         } else {

         }
         break;
      case BoardStatus:
         // TODO this is not working! the renderer for BoardStatusValue must not get in here for some reason!
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

   private boolean isString(Object value) {
      return value instanceof String;
   }

   private boolean isEmptyString(String stringValue) {
      return stringValue == null || stringValue.trim().length() <= 0;
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
         BoardStatusValue type = iter.next();
         log.debug("\tset contains: " + type);
      }
      log.debug("BoardStatus value for row " + row + " is \"" + value + "\"(" + value.getClass() + ") and is required: " + contains);
      return contains;
   }

   private static class CellColorHelper {

      private static CellColorHelper instance = new CellColorHelper();
      private Set<BoardStatusValue> requiredDevEstimates = new HashSet<BoardStatusValue>();
      private Set<BoardStatusValue> requiredDevActuals = new HashSet<BoardStatusValue>();
      private Set<BoardStatusValue> requiredBlankDevActuals = new HashSet<BoardStatusValue>();

      private CellColorHelper() {
         requiredDevEstimates.add(BoardStatusValue.Approved);
         requiredDevEstimates.add(BoardStatusValue.Bug);
         requiredDevEstimates.add(BoardStatusValue.Complete);
         requiredDevEstimates.add(BoardStatusValue.ForShowCase);
         requiredDevEstimates.add(BoardStatusValue.InDevProgress);
         requiredDevEstimates.add(BoardStatusValue.InQAProgress);
         requiredDevEstimates.add(BoardStatusValue.Open);
         requiredDevEstimates.add(BoardStatusValue.Parked);
         requiredDevEstimates.add(BoardStatusValue.Resolved);

         requiredDevActuals.add(BoardStatusValue.Approved);
         requiredDevActuals.add(BoardStatusValue.Complete);
         requiredDevActuals.add(BoardStatusValue.ForShowCase);
         requiredDevActuals.add(BoardStatusValue.InQAProgress);
         requiredDevActuals.add(BoardStatusValue.Resolved);

         requiredBlankDevActuals.add(BoardStatusValue.Bug);
         requiredBlankDevActuals.add(BoardStatusValue.InDevProgress);
         requiredBlankDevActuals.add(BoardStatusValue.Open);
      }

      public Set<BoardStatusValue> getRequiredQAEstimates() {
         return requiredDevEstimates;
      }

      public static CellColorHelper getInstance() {
         return instance;
      }

      public Set<BoardStatusValue> getRequiredDevActuals() {
         return requiredDevActuals;
      }

      public Set<BoardStatusValue> getRequiredBlankDevActuals() {
         return requiredBlankDevActuals;
      }

      private Set<BoardStatusValue> getRequiredDevEstimates() {
         return requiredDevEstimates;
      }
   }
}
