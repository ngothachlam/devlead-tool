package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;

public class ReconciliationTableModel extends MyTableModel {

   private static Column[] columns = {Column.Jira, Column.Release, Column.Dev_Estimate, Column.QA_Estimate, Column.Dev_Remain};
   private final MyTable boardTableModel;

   public ReconciliationTableModel(MyTable boardTableModel) {
      super(columns);
      this.boardTableModel = boardTableModel;
   }

   @Override
   public Color getColor(Object value, int row, Column column) {
      return null;
   }

}
