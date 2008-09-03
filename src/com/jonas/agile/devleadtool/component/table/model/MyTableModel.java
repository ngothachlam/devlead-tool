package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.jira.JiraProject;

public abstract class MyTableModel extends DefaultTableModel {

	protected boolean editable = true;

	MyTableModel(Object[][] objects, Column[] tableHeader) {
		super(objects, tableHeader);
	}

	MyTableModel(Vector contents, Vector<Column> headers) {
		super(contents, headers);
	}

	MyTableModel(Vector<Column> columnNames, int i) {
		super(columnNames, i);
	}

	public final void addEmptyRow() {
		this.addRow(getEmptyRow());
	}

	public Column getColumn(int columnNo) {
      return Column.getEnum( getColumnName(columnNo));
   }

	public Class<?> getColumnClass(int columnIndex) {
		Object valueAt = getValueAt(0, columnIndex);
		return valueAt != null ? valueAt.getClass() : getEmptyRow()[columnIndex].getClass();
	}

	public int getColumnNo(Column column) {
      for (int col = 0; col < getColumnCount(); col++) {
         if(getColumnName(col).equals(column.toString())){
            return col;
         }
      }
      return -1;
   }

	public int getCountOfSameValueInColumn(Object value, int column) {
		int countOfSimilar = 0;
		for (int i = 0; i < this.getRowCount(); i++) {
			Object valueAt = this.getValueAt(i, column);
			if (value instanceof String) {
				String stringValue = (String) value;
				String stringRowValue = (String) valueAt;
				if ((stringValue).length() > 0 && stringValue.equalsIgnoreCase(stringRowValue)) {
					countOfSimilar++;
				}
			} else if (valueAt.equals(value)) {
				countOfSimilar++;
			}
		}
		return countOfSimilar;
	}

	public JiraProject getProjectForRow(int row) {
		String jira = (String) getValueAt(row, 0);
		return JiraProject.getProjectByKey(PlannerHelper.getProjectKey(jira));
	}

	/**
	 * Returns -1 if cannot be found otherwise first instance of occurence.
	 * @param value
	 * @param column
	 * @return 
	 */
	public int getRowOfSameValueInColumn(Object value, int column) {
		for (int i = 0; i < this.getRowCount(); i++) {
			Object valueAt = this.getValueAt(i, column);
			if (value instanceof String) {
				String stringValue = (String) value;
				String stringRowValue = (String) valueAt;
				if ((stringValue).length() > 0 && stringValue.equalsIgnoreCase(stringRowValue)) {
					return i;
				}
			} else if (valueAt.equals(value)) {
				return i;
			}
		}
		return -1;
	}

	public abstract boolean isCellEditable(int row, int column);

	public boolean isEditable() {
		return editable;
	}

	public abstract boolean isRed(Object value, int row, int column);
	public void removeSelectedRows(JTable table) {
		while (table.getSelectedRowCount() > 0) {
			int tableSelectedRow = table.getSelectedRow();
			this.removeRow(table.convertRowIndexToModel(tableSelectedRow));
		}
	}

   public void setEditable(boolean selected) {
		editable = selected;
		fireTableStructureChanged();
		// TODO need to fireUpdate on Table
	}

   protected abstract Object[] getEmptyRow();

}
