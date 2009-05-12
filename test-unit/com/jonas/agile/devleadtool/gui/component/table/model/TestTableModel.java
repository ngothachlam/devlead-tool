package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.Vector;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;

public class TestTableModel extends MyTableModel {

   private static final ColumnType[] columns = { ColumnType.Jira, ColumnType.Description, ColumnType.prio, ColumnType.Note, ColumnType.BuildNo, ColumnType.J_DevEst, ColumnType.DEst};

   public TestTableModel() {
      super(columns);
   }

   public TestTableModel(Vector<Vector<Object>> contents, Vector<ColumnType> header) {
      super(columns, contents, header);
   }

   @Override
   public Color getColor(Object value, int row, ColumnType column) {
      return null;
   }

}
