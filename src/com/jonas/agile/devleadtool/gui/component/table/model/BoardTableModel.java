package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.data.BoardStatusValueToJiraStatusMap;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.string.StringHelper;
import com.jonas.common.swing.SwingUtil;

public class BoardTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.J_Resolution, Column.Release, Column.Merge,
         Column.BoardStatus, Column.Old, Column.Dev_Estimate, Column.Dev_Remain, Column.Dev_Actual, Column.QA_Estimate, Column.prio, Column.Note };
   private Logger log = MyLogger.getLogger(BoardTableModel.class);
   private BoardCellColorHelper cellColorHelper = BoardCellColorHelper.getInstance();

   public BoardTableModel() {
      super(columns);
   }

   public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      log.debug("column: " + column + " value: \"" + value + "\" row: " + row);
      String stringValue;
      
      if(value == null){
         setToolTipText(row, getColumnIndex(column), "Should be filled out based!");
         return SwingUtil.cellRed;
      }
      
      switch (column) {
      case J_Resolution:
         stringValue = (String) value;
         if (!isEmptyString(stringValue)) {
            BoardStatusValue boardStatus = (BoardStatusValue) getValueAt(Column.BoardStatus, row);
            if (!BoardStatusValueToJiraStatusMap.isMappedOk(boardStatus, stringValue)) {
               setToolTipText(row, getColumnIndex(column), "Does not match with the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         }
         break;
      case Dev_Remain:
         stringValue = (String) value;
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredDevRemains())) {
               setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         } else {
            if (!isBoardValueEither(row, cellColorHelper.getRequiredDevRemains())) {
               setToolTipText(row, getColumnIndex(column), "Should not be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            } else if (StringHelper.isDouble(value) && !StringHelper.isDouble(getValueAt(Column.Dev_Estimate, row))) {
               setToolTipText(row, getColumnIndex(column), "Cannot be numeric if Dev Estimate is not!");
               return SwingUtil.cellRed;
            }
         }
         break;
      case Dev_Estimate:
         stringValue = (String) value;
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredDevEstimates())) {
               setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         } else {

         }
         break;
      case Dev_Actual:
         stringValue = (String) value;
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredDevActuals())) {
               setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         } else {
            if (isBoardValueEither(row, cellColorHelper.getRequiredBlankDevActuals())) {
               setToolTipText(row, getColumnIndex(column), "Should not be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         }
         break;
      case QA_Estimate:
         stringValue = (String) value;
         if (isEmptyString(stringValue)) {
            if (isBoardValueEither(row, cellColorHelper.getRequiredQAEstimates())) {
               setToolTipText(row, getColumnIndex(column), "Should be filled out based on the BoardStatus value!");
               return SwingUtil.cellRed;
            }
         }
         break;
      case BoardStatus:
         BoardStatusValue newValue = (BoardStatusValue) value;
         switch (newValue) {
         case Resolved:
         case InQAProgress:
            return SwingUtil.cellLightBlue;
         case Approved:
         case Complete:
         case ForShowCase:
            return SwingUtil.cellLightGreen;
         case Bug:
            setToolTipText(row, getColumnIndex(column), "This is a bug!");
            return SwingUtil.cellLightRed;
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
      if (isJiraPresent(jira))
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
      return contains;
   }
}
