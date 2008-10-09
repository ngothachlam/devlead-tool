package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;

public class JiraTableModel extends MyTableModel {

   //FIXME how do you run to get a special column of same object but with different object?
   
   private static final Column[] columns = { Column.Jira, Column.Description, Column.B_BoardStatus, Column.J_Type, Column.B_Release, Column.J_FixVersion, Column.J_Status, Column.J_Resolution, Column.J_BuildNo,
         Column.J_Dev_Estimate, Column.J_Dev_Spent, Column.Note };

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
