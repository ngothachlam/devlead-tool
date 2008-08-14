package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.jira.JiraProject;

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

	public abstract boolean isCellEditable(int row, int column);

	protected abstract Object[] getEmptyRow();

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

	public Class<?> getColumnClass(int columnIndex) {
		Object valueAt = getValueAt(0, columnIndex);
		return valueAt != null ? valueAt.getClass() : getEmptyRow()[columnIndex].getClass();
	}

	public JiraProject getProjectForRow(int row) {
		String jira = (String) getValueAt(row, 0);
		return JiraProject.getProjectByKey(PlannerHelper.getProjectKey(jira));
	}

}
