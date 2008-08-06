package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;

public class BoardTableModel extends MyTableModel {

	private static String[] tableHeader = { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete", "URL" };

	private static Object[] tableContents = { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
	new String("") };

	public BoardTableModel() {
		super(new Object[][] { tableContents }, tableHeader);
	}
	
	public BoardTableModel(Vector<Vector<Object>> contents, Vector<Object> header) {
		super(contents, header);
	}

	private Object[] getEmptyRow() {
		return new Object[] { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, new String("") };
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return isEditable() ? column < 6 : false;
	}

	// Only required if the table is updated by the app so that it becomes visible to the user.
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		super.setValueAt(value, rowIndex, columnIndex);
		if (value instanceof Boolean) {
		} else if (columnIndex == 0) {
			setValueAt("LLU-" + value.toString(), rowIndex, 6);
			if (rowIndex + 1 == this.getRowCount()) {
				this.addEmptyRow();
			}
		}
		fireTableRowsUpdated(0, this.getRowCount()-1);
	}

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

	public boolean isRed(Object value, int row, int column) {
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