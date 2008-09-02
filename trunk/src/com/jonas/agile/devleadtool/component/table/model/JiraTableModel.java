package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;

import javax.swing.JTable;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.BarRecord;

import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

public class JiraTableModel extends MyTableModel {

	public static final String COLUMNNAME_HYPERLINK = "URL";
	private static Vector<Column> columnNames = new Vector<Column>();

	static {
		columnNames.add(Column.Jira);
		columnNames.add(Column.Description);
		columnNames.add(Column.FixVersion);
		columnNames.add(Column.Status);
		columnNames.add(Column.Resolution);
		columnNames.add(Column.BuildNo);
		columnNames.add(Column.URL);
		columnNames.add(Column.BoardStatus);
	}

	@Override
	protected Object[] getEmptyRow() {
		return new Object[] { "", "", "", "", "", "", "", BoardStatus.UnKnown };
	}

	private Logger log = MyLogger.getLogger(JiraTableModel.class);

	public JiraTableModel() {
		super(columnNames, 0);
	}

	public JiraTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
		// FIXME - this needs to work dynamically if columns are added and removed in the spreadsheet!! I.e. if more or less
		// columns added than intended!
		super(contents, header);
		log.debug("Initiated from existing contents and header!");
	}

	public boolean addRow(JiraIssue jiraIssue) {
		if (!exists(jiraIssue.getKey())) {
			Object[] objects = new Object[] { jiraIssue.getKey(), jiraIssue.getSummary(), jiraIssue.getFixVersions(),
					jiraIssue.getStatus(), jiraIssue.getResolution(), jiraIssue.getBuildNo(), jiraIssue.getKey(),
					BoardStatus.UnKnown };
			super.addRow(objects);
			return true;
		}
		log.debug("jira already in model: " + jiraIssue.getKey());
		return false;
	}

	protected boolean exists(String name) {
		for (int row = 0; row < getRowCount(); row++) {
			if (getValueAt(row, 0).equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isRed(Object value, int row, int column) {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column >= 6 ? false : (isEditable() ? false : false);
	}

	// Only required if the table is updated by the app so that it becomes visible to the user.
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		log.debug("Setting value" + "[value: " + value + ", row:" + rowIndex + ", col:" + columnIndex + "]");
		super.setValueAt(value, rowIndex, columnIndex);
	}

}
