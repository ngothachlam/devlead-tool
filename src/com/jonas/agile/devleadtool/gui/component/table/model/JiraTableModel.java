package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.common.CalculatorHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;

public class JiraTableModel extends MyTableModel {

   private final static Set<String> nonAcceptedJiraFields = new HashSet<String>();
   static {
      nonAcceptedJiraFields.add("TBD");
   }

   private static final Column[] columns = { Column.Jira, Column.Description, Column.Type, Column.Sprint, Column.Project,
         Column.FixVersion, Column.Owner, Column.Environment, Column.Delivery, Column.Resolution, Column.BuildNo, Column.J_DevEst, Column.J_DevAct };

   private Logger log = MyLogger.getLogger(JiraTableModel.class);
   private MyTableModel boardModel;
   private int tempRow = -1;
   private int jiraRowInBoardModel;

   public JiraTableModel() {
      super(columns);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   public void setBoardModel(MyTableModel boardModel) {
      this.boardModel = boardModel;
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      if (shouldRenderColors()) {
         return null;
      }

      if (boardModel == null) {
         return null;
      }

      log.debug("The row getting color for is " + row + " (col: "+column+")and we previously edited row " + this.tempRow);
      if (this.tempRow != row) {
         String jira = (String) getValueAt(Column.Jira, row);
         jiraRowInBoardModel = boardModel.getRowWithJira(jira);
         log.debug("... so we are editing a new row! Lets get the jira, which is " + jira + " and the board row for this jira: "
               + jiraRowInBoardModel);
         this.tempRow = row;
      }

      if (jiraRowInBoardModel == -1)
         return null;

      log.debug("... The Jira was found in the board. We now want to check the " + column + " as it's value is \"" + value + "\"");
      switch (column) {
      case Jira:
         setToolTipText(row, getColumnIndex(column), "Exists in the board!");
         return SwingUtil.cellGreen;
      case FixVersion:
         if (!isFixVersionOk(boardModel.getValueAt(Column.Release, jiraRowInBoardModel), value)) {
            setToolTipText(row, getColumnIndex(column), "Is incorrectly filled out based on the BoardStatus value or it is TBD!");
            return SwingUtil.cellRed;
         }
         break;
      case Sprint:
         if (!isSprintOk(boardModel.getValueAt(Column.BoardStatus, jiraRowInBoardModel), value)) {
            setToolTipText(row, getColumnIndex(column), "Is incorrectly filled out based on the BoardStatus value!");
            return SwingUtil.cellRed;
         }
         break;
      case Project:
         if (!isProjectOk(value)) {
            setToolTipText(row, getColumnIndex(column), "Should not be empty!");
            return SwingUtil.cellRed;
         }
         break;
      case J_DevEst:
         if (!isJiraNumberOk(boardModel.getValueAt(Column.DevEst, jiraRowInBoardModel), value)) {
            setToolTipText(row, getColumnIndex(column), "Is incorrectly filled out based on the BoardStatus value!");
            return SwingUtil.cellRed;
         }
         break;
      case J_DevAct:
         if (!isJiraNumberOk(boardModel.getValueAt(Column.DevAct, jiraRowInBoardModel), value)) {
            setToolTipText(row, getColumnIndex(column), "Is incorrectly filled out based on the BoardStatus value!");
            return SwingUtil.cellRed;
         }
         break;
      case Delivery:
         if (false) {
            setToolTipText(row, getColumnIndex(column), "Not implemented yet!!");
            return SwingUtil.cellRed;
         }
         break;
      }
      return null;
   }

   boolean isProjectOk(Object value) {
      if (value == null) {
         return false;
      }
      
      if (nonAcceptedJiraFields.contains(value.toString())) {
         return false;
      }

      return value.toString().trim().length() != 0;
   }

   boolean isSprintOk(Object boardStatus, Object value) {
      if (value == null) {
         return false;
      }
      
      if (nonAcceptedJiraFields.contains(value.toString())) {
         return false;
      }
      
      return value.toString().trim().length() != 0;
   }

   boolean isFixVersionOk(Object boardValue, Object jiraValue) {
      log.debug("boardValue: \"" + boardValue + "\" jiraValue: " + jiraValue);
      if (jiraValue == null) {
         if (boardValue == null || boardValue.toString().trim().length() == 0)
            return true;
         return false;
      }

      String jiraFixVersions = jiraValue.toString();
      String boardValueAsString = (String) boardValue;
      return jiraFixVersions.equals(boardValueAsString);
   }

   boolean isJiraNumberOk(Object boardValue, Object jiraValue) {
      log.debug("... We are trying to check if either the board or jira has numberical or string values (boardValue: " + boardValue
            + ", jiraValue: " + jiraValue + ")");
      String boardString = boardValue == null ? null : boardValue.toString().trim();
      String jiraString = jiraValue == null ? null : jiraValue.toString().trim();

      if (boardString == null && jiraString == null) {
         return true;
      } else if (boardString == null) {
         return isDoubleValueNullOrZero(jiraString);
      } else if (jiraString == null) {
         return isDoubleValueNullOrZero(boardString);
      }

      if (boardValue.equals(jiraValue))
         return true;

      Double boardDouble = null;
      Double jiraDouble = null;

      boardDouble = CalculatorHelper.getDouble(boardString);
      jiraDouble = CalculatorHelper.getDouble(jiraString);

      if (boardDouble == null || boardDouble == 0d) {
         if ((jiraDouble == null || jiraDouble == 0d))
            return true;
         return false;
      }
      if (boardDouble.equals(jiraDouble))
         return true;
      return false;
   }

   private boolean isDoubleValueNullOrZero(String jiraString) {
      Double jiraDouble;
      jiraDouble = CalculatorHelper.getDouble(jiraString);
      if (jiraDouble == null || jiraDouble == 0d) {
         return true;
      }
      return false;
   }

}
