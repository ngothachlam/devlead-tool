package com.jonas.agile.devleadtool.component.table;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BoardTableModel extends DefaultTableModel {

	private static String[] tableHeader = { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete", "URL" };

	private static Object[] tableContents = { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
	new String("") };

	// public BoardTableModel() {
	// this(new Object[][] { tableContents }, tableHeader);
	// }
	//
	// public BoardTableModel(Object[][] contents, String[] tableHeader) {
	// super(contents, tableHeader);
	// }

	public BoardTableModel() {
		super(new Object[][] { tableContents }, tableHeader);
	}

	private Object[] getEmptyRow() {
		// Vector<Object> vector = new Vector<Object>();
		// vector.add(new String(""));
		// vector.add(Boolean.FALSE);
		// vector.add(Boolean.FALSE);
		// vector.add(Boolean.FALSE);
		// vector.add(Boolean.FALSE);
		// vector.add(Boolean.FALSE);
		// vector.add(new String(""));
		// return vector;
		return new Object[] { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, new String("") };
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column < 6;
	}

	// Only required if the table is updated by the app so that it becomes visible to the user.
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		super.setValueAt(value, rowIndex, columnIndex);
		if (value instanceof Boolean) {
			fireTableRowsUpdated(0, this.getRowCount());
		} else if (columnIndex == 0) {
			setValueAt("LLU-" + value.toString(), rowIndex, 6);
			if (rowIndex + 1 == this.getRowCount()) {
				this.addEmptyRow();
			}
		}
	}

	public void removeSelectedRows(JTable table) {
		while (table.getSelectedRowCount() > 0) {
			this.removeRow(table.getSelectedRow());
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}

	public int noOfCheckboxesTicked(int row) {
		int numberTicks = 0;
		for (int i = 1; i < getColumnCount(); i++) {
			Object value = getValueAt(row, i);
			if (value instanceof Boolean && ((Boolean) value).booleanValue()) {
				numberTicks++;
			}
		}
		return numberTicks;
	}

	public int noOfCheckboxesTickedNext(int row, int col) {
		int numberTicks = 0;
		for (int i = 1; i < getColumnCount(); i++) {
			Object valueAt = getValueAt(row, i);
			if (valueAt instanceof Boolean && col == i && (!((Boolean) valueAt).booleanValue())) {
				numberTicks++;
			} else if (valueAt instanceof Boolean && col != i && ((Boolean) valueAt).booleanValue()) {
				numberTicks++;
			}
		}
		return numberTicks;
	}

	public void addEmptyRow() {
		this.addRow(getEmptyRow());
	}

	public int countOfSameValueInColumn(Object value, int column) {
		int countOfSimilar = 0;
		for (int i = 0; i < this.getRowCount(); i++) {
			if (this.getValueAt(i, column).equals(value)) {
				if (value instanceof String) {
					if (((String) value).length() > 0) {
						countOfSimilar++;
					}
				} else
					countOfSimilar++;
			}
		}
		return countOfSimilar;
	}

	public boolean shouldBeRedBackground(Object value, int row, int column) {
		boolean theValue = false;
		switch (column) {
		case 0:
			theValue = countOfSameValueInColumn(value, column) > 1;
			break;
		case 6:
			theValue = false;
			break;
		default:
			theValue = noOfCheckboxesTicked(row) == 0 || (noOfCheckboxesTicked(row) > 1 && value.equals(Boolean.TRUE));
			break;
		}
		return theValue;
	}

}
