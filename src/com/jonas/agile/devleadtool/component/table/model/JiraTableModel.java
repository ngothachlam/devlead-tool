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

   private static final Column[] columns = { Column.Jira, Column.Description, Column.B_BoardStatus, Column.J_Type, Column.B_Release,
         Column.J_Sprint, Column.J_FixVersion, Column.J_Status, Column.J_Resolution, Column.J_BuildNo, Column.J_Dev_Estimate, Column.J_Dev_Spent };
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
   public Color getColor(Object value, int row, int columnNo) {
      if (boardModel == null)
         return null;
      Column column = getColumn(columnNo);
      String jira = (String) getValueAt(Column.Jira, row);

      switch (column) {
      case J_Dev_Estimate:
         if (!isEstimatesBetweenTablesOk(boardModel.getValueAt(Column.Dev_Estimate, jira), value))
            return SwingUtil.cellRED;
      }
      return null;
   }

   boolean isEstimatesBetweenTablesOk(Object boardValue, Object jiraValue) {
      return true;

      // String stringValue = (String) value;
      // log.debug("Jira " + jira + " boardEstimate: " + boardEstimate + " jiraEstimate: " + value);
      // if (boardEstimate == null && value != null && stringValue.trim().length() > 0)
      // return SwingUtil.cellRED;
      // if (!boardEstimate.equals(value)) {
      // Double boardEstimateD = CalculatorHelper.getDouble(boardEstimate);
      // if (boardEstimateD != null && boardEstimateD.equals(CalculatorHelper.getDouble(stringValue)))
      // return null;
      // else if (boardEstimateD == null && CalculatorHelper.getDouble(stringValue) == null)
      // return null;
      // return SwingUtil.cellRED;
      // }

   }

}
