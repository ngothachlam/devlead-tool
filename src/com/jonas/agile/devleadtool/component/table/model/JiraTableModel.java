package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;

public class JiraTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.BoardStatus, Column.J_Type, Column.Release, Column.J_FixVersion, Column.Planned_Sprint,
         Column.Resolved_Sprint, Column.Closed_Sprint, Column.J_Status, Column.J_Resolution, Column.J_BuildNo,
         Column.Dev_Estimate, Column.Dev_Actual, Column.J_Dev_Estimate, Column.J_Dev_Spent, Column.Note };

   public JiraTableModel() {
      super(columns);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   @Override
   public boolean isRed(Object value, int row, int column) {
      return false;
   }
}
