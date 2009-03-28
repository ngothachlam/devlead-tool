package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.Vector;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class TestTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.prio, Column.Note, Column.J_BuildNo, Column.J_Dev_Estimate, Column.Dev_Estimate};

   public TestTableModel() {
      super(columns);
   }

   public TestTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      return null;
   }

}
