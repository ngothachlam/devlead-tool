package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

	public static final String COLUMNNAME_HYPERLINK = "URL";
	public static final String COLUMNNAME_JIRA = "Jira";

	private static String[] tableHeader = { COLUMNNAME_JIRA, "Open", "Bugs", "In-Progress", "Resolved", "Complete", COLUMNNAME_HYPERLINK };

	private static Object[] tableContents = { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
			Boolean.FALSE, new String("") };

	static Logger log = MyLogger.getLogger(BoardTableModel.class);

	public BoardTableModel() {
		super(new Object[][] { tableContents }, tableHeader);
	}

	public BoardTableModel(Vector<Vector<Object>> contents, Vector<Object> header) {
		super(contents, header);
	}

	protected Object[] getEmptyRow() {
		return new Object[] { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
				new String("") };
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
			setValueAt(value.toString(), rowIndex, 6);
			if (rowIndex + 1 == this.getRowCount()) {
				this.addEmptyRow();
			}
		}
		fireTableRowsUpdated(0, this.getRowCount() - 1);
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

	public boolean isRed(Object value, int row, int column) {
		log.debug("isRed: " + value + " row=" + row + ",col=" + column);
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
