package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.CalculatorHelper;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraFilter;

public class JiraTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.J_Type, Column.J_Sprint, Column.J_Project,
         Column.J_FixVersion, Column.J_Delivery, Column.J_Resolution, Column.J_BuildNo, Column.J_Dev_Estimate, Column.J_Dev_Spent };
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

      log.debug("The row being edited is " + row + " and we previously edited " + this.tempRow);
      if (this.tempRow != row) {
         log.debug("trace...");
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
         return SwingUtil.cellGreen;
      case J_FixVersion:
         if (!isFixVersionOk(boardModel.getValueAt(Column.Release, jiraRowInBoardModel), value))
            return SwingUtil.cellRed;
         break;
      case J_Sprint:
         if (!isSprintOk(boardModel.getValueAt(Column.BoardStatus, jiraRowInBoardModel), value))
            return SwingUtil.cellRed;
         break;
      case J_Project:
         if (!isProjectOk(value))
            return SwingUtil.cellRed;
         break;
      case J_Dev_Estimate:
         if (!isJiraNumberOk(boardModel.getValueAt(Column.Dev_Estimate, jiraRowInBoardModel), value))
            return SwingUtil.cellRed;
         break;
      case J_Dev_Spent:
         if (!isJiraNumberOk(boardModel.getValueAt(Column.Dev_Actual, jiraRowInBoardModel), value))
            return SwingUtil.cellRed;
         break;
      }
      return null;
   }

   boolean isProjectOk(Object value) {
      return value != null && value.toString().trim().length() != 0;
   }

   boolean isSprintOk(Object boardStatus, Object value) {
      return true;
   }

   boolean isFixVersionOk(Object boardValue, Object jiraValue) {
      log.debug("boardValue: \"" + boardValue + "\" jiraValue: " + jiraValue);
      if ( jiraValue == null  && (boardValue == null))
         return true;
      String jiraFixVersions = (String) jiraValue;
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
