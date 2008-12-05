package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
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

   boolean isBoardValueEither(int row, BoardStatusValue... values) {
      Object value = getValueAt(Column.BoardStatus, row);
      log.debug("board status: " + value);
      for (int i = 0; i < values.length; i++) {
         BoardStatusValue boardStatusValue = values[i];
         log.debug("checking against : " + boardStatusValue);
         if (boardStatusValue.equals(value)) {
            log.debug("match!");
            return true;
         }
      }
      return false;
   }

   public String getRelease(String jira) {
      if (doesJiraExist(jira))
         return (String) getValueAt(Column.Release, jira);
      return "";
   }

   @Override
   public Color getColor(Object value, int row, int column) {
      Column col = getColumn(column);
      String stringValue = (String) value;
      switch (col) {
      case Dev_Actual:
         if (stringValue == null || stringValue.trim().length() <= 0) {
            if (isBoardValueEither(row, BoardStatusValue.Resolved, BoardStatusValue.Complete)) {
               return SwingUtil.cellRED;
            }
         } else {
            if (isBoardValueEither(row, BoardStatusValue.Bug, BoardStatusValue.Open, BoardStatusValue.InProgress)) {
               return SwingUtil.cellRED;
            }
         }
         break;
      case Dev_Estimate:
         if (stringValue == null || stringValue.trim().length() <= 0) {
            if (isBoardValueEither(row, BoardStatusValue.Open, BoardStatusValue.InProgress, BoardStatusValue.Resolved, BoardStatusValue.QA_Progress,
                  BoardStatusValue.Complete)) {
               log.debug("true!");
               return SwingUtil.cellRED;
            }
         }
         break;
      }
      return null;
   }
}
