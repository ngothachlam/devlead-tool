package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class MyTableModel extends DefaultTableModel {

	protected boolean editable = true;

	public MyTableModel(Object[][] objects, String[] tableHeader) {
		super(objects, tableHeader);
	}

	public MyTableModel(Vector contents, Vector headers) {
		super(contents, headers);
	}

	public MyTableModel(Vector columnNames, int i) {
		super(columnNames, i);
	}

	public abstract boolean isRed(Object value, int row, int column);

	public void removeSelectedRows(JTable table) {
		while (table.getSelectedRowCount() > 0) {
			this.removeRow(table.getSelectedRow());
		}
	}

	public void setEditable(boolean selected) {
		editable = selected;
		fireTableStructureChanged();
		// TODO need to fireUpdate on Table
	}

	public boolean isEditable() {
		return editable;
	}

}
