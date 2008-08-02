package com.jonas.agile.devleadtool.component.table.model;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PlanTableModel extends MyTableModel {

	public PlanTableModel(Object[][] objects, String[] tableHeader) {
      super(objects, tableHeader);
   }

   public void addEmptyRow() {
	}

	public void removeSelectedRows(JTable table) {
	}

	public boolean isRed(Object value, int row, int column) {
		return false;
	}

}
