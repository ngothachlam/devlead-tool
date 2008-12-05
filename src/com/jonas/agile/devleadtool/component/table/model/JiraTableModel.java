package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

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
         String boardEstimate = (String) boardModel.getValueAt(Column.Dev_Estimate, jira);
         String stringValue = (String) value;
         log.debug("Jira " + jira + " boardEstimate: " + boardEstimate + " jiraEstimate: " + value);
         if (boardEstimate == null && stringValue.trim().length() > 0)
            return SwingUtil.cellRED;
         if (!boardEstimate.equals(value))
            return SwingUtil.cellRED;
      }
      return null;
   }
}
