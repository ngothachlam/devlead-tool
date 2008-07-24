package com.jonas.agile.devleadtool.component.table.model;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PlanTableModel extends DefaultTableModel implements MyTableModel {

	public void addEmptyRow() {
	}

	public void removeSelectedRows(JTable table) {
	}

	public boolean shouldBackgroundBeRed(Object value, int row, int column) {
		return false;
	}

}
