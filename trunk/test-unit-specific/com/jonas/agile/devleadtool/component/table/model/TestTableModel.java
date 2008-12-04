package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;

public class TestTableModel extends MyTableModel {

   private static final Column[] columns = { Column.Jira, Column.Description, Column.B_BoardStatus, Column.prio, Column.Note, Column.J_BuildNo, Column.J_Dev_Estimate, Column.Dev_Estimate};

   public TestTableModel() {
      super(columns, false);
   }

   public TestTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header, false);
   }

   @Override
   public Color getColor(Object value, int row, int column) {
      return null;
   }

}
