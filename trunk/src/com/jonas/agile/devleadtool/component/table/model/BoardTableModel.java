package com.jonas.agile.devleadtool.component.table.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.ColumnValue;
import com.jonas.common.logging.MyLogger;

public class BoardTableModel extends MyTableModel {

	private static Column[]	tableHeader		= { Column.Jira, Column.Open, Column.Bugs, Column.InProgress, Column.Resolved, Column.Complete, Column.URL, Column.inPanel };

	private static Object[]	tableContents	= { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, new String(""), ColumnValue.NA };

	static Logger				log				= MyLogger.getLogger(BoardTableModel.class);

	public BoardTableModel() {
		super(new Object[][] { tableContents }, tableHeader);
	}

	public BoardTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
		super(contents, header);
	}

	protected Object[] getEmptyRow() {
		return new Object[] { new String(""), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, new String(""), ColumnValue.NA };
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
			theValue = getCountOfSameValueInColumn(value, column) > 1;
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

	public BoardStatus getStatus(String jira) {
		int row = getRowOfSameValueInColumn(jira, getColumnNo(Column.Jira));
		log.debug("row: " + row + " for jira: " + jira);
		if (row >= 0) {
			BoardStatus result = null;
			// FIXME - doens't work obvoiusly!!!
			for (Column column : tableHeader) {
				switch (column) {
				case Open:
				case Bugs:
				case InProgress:
				case Resolved:
				case Complete:
					log.debug("column: " + column);
					if (getBoardStatus(row, column)) {
						log.debug("tralllalala!");
						result = BoardStatusToColumnMap.getBoardStatus(column);
					}
				default:
					break;
				}
			}
			log.debug("result: " + result);
			return result;
		} else
			return BoardStatus.Empty;
	}

	private boolean getBoardStatus(int row, Column column) {
		int columnTemp = getColumnNo(column);
		log.debug("column: " + column + " columnTemp: " + columnTemp + " row: " + row);
		Boolean valueAt = (Boolean) getValueAt(row, columnTemp);
		log.debug("valueAt: " + valueAt);
		return valueAt == null ? false : valueAt.booleanValue();
	}
}

class BoardStatusToColumnMap {

	private static final Map<Column, BoardStatus>	map		= new HashMap<Column, BoardStatus>();
	private static final BoardStatusToColumnMap		mapping1	= new BoardStatusToColumnMap(Column.Open, BoardStatus.Open);
	private static final BoardStatusToColumnMap		mapping2	= new BoardStatusToColumnMap(Column.Bugs, BoardStatus.Bugs);
	private static final BoardStatusToColumnMap		mapping3	= new BoardStatusToColumnMap(Column.InProgress, BoardStatus.InProgress);
	private static final BoardStatusToColumnMap		mapping4	= new BoardStatusToColumnMap(Column.Resolved, BoardStatus.Resolved);
	private static final BoardStatusToColumnMap		mapping5	= new BoardStatusToColumnMap(Column.Complete, BoardStatus.Complete);

	public BoardStatusToColumnMap(Column column, BoardStatus status) {
		map.put(column, status);
	}

	public static BoardStatus getBoardStatus(Column column) {
		return map.get(column);
	}

}