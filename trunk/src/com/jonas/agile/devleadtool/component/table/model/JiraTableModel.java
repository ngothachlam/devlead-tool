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
      String jira = (String) getValueAt(Column.Jira, row);

      switch (column) {
      case J_Dev_Estimate:
         if (!isJiraNumberOk(boardModel.getValueAt(Column.Dev_Estimate, jira), value))
            return SwingUtil.cellRED;
      case J_Dev_Spent:
         if (!isJiraNumberOk(boardModel.getValueAt(Column.Dev_Actual, jira), value))
            return SwingUtil.cellRED;
      }
      return null;
   }

   boolean isJiraNumberOk(Object boardValue, Object jiraValue) {
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
