package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.CalculatorHelper;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class JiraTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.B_BoardStatus, Column.J_Type, Column.B_Release, Column.J_Sprint,
         Column.J_FixVersion, Column.J_Status, Column.J_Resolution, Column.J_BuildNo, Column.J_Dev_Estimate, Column.J_Dev_Spent };
   private Logger log = MyLogger.getLogger(JiraTableModel.class);
   private MyTableModel boardModel;
   private int tempRow = -1;
   private int rowWithJiraInBoard;

   public JiraTableModel() {
      super(columns, false);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header, false);
   }

   public void setBoardModel(MyTableModel boardModel) {
      this.boardModel = boardModel;
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      if (boardModel == null)
         return null;

      log.debug("this.tempRow: " + this.tempRow + " row: " + row +  " column: " + column);
      if (this.tempRow != row) {
         String jira = (String) getValueAt(Column.Jira, row);
         rowWithJiraInBoard = boardModel.getRowWithJira(jira);
         log.debug("jira: " + jira + " rowWithJira: " + rowWithJiraInBoard);
         this.tempRow = row;
      }

      if (rowWithJiraInBoard == -1)
         return null;
         
      log.debug("column: " + column + " value: " + value);
      switch (column) {
      // FIXME when the Jira column is selected - could we cache the jira row data in the board until all cols have been calculated here?
      case Jira:
         return SwingUtil.cellGreen;
      case J_Dev_Estimate:
         if (!isJiraNumberOk(boardModel.getValueAt(Column.Dev_Estimate, rowWithJiraInBoard), value))
            return SwingUtil.cellRed;
         break;
      case J_Dev_Spent:
         if (!isJiraNumberOk(boardModel.getValueAt(Column.Dev_Actual, rowWithJiraInBoard), value))
            return SwingUtil.cellRed;
         break;
      }
      return null;
   }

   boolean isJiraNumberOk(Object boardValue, Object jiraValue) {
      log.debug("boardValue: " + boardValue + " jiraValue: " + jiraValue);
      String boardString = boardValue == null ? null : boardValue.toString().trim();
      String jiraString = jiraValue == null ? null : jiraValue.toString().trim();

      Double boardDouble = null;
      Double jiraDouble = null;

      if (boardString == null && jiraString == null) {
         return true;
      } else if (boardString == null) {
         jiraDouble = CalculatorHelper.getDouble(jiraString);
         if (jiraDouble == null || jiraDouble == 0d) {
            return true;
         }
         return false;
      } else if (jiraString == null) {
         boardDouble = CalculatorHelper.getDouble(boardString);
         if (boardDouble == null || boardDouble == 0d) {
            return true;
         }
         return false;
      }

      boardDouble = CalculatorHelper.getDouble(boardString);
      jiraDouble = CalculatorHelper.getDouble(jiraString);

      if (boardValue.equals(jiraValue))
         return true;
      if (boardDouble == null) {
         if ((jiraDouble == null || jiraDouble == 0d))
            return true;
         return false;
      }
      if (boardDouble.equals(jiraDouble))
         return true;
      return false;
   }

}
