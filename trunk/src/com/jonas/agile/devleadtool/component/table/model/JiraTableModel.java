package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;

import javax.swing.JTable;

import org.apache.log4j.Logger;

import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

public class JiraTableModel extends MyTableModel {

	private static Vector<String> columnNames = new Vector<String>();

	static {
		columnNames.add("Jira");
		columnNames.add("FixVersion");
		columnNames.add("Status");
		columnNames.add("Resolution");
	}

	private Logger log = MyLogger.getLogger(JiraTableModel.class);

	public JiraTableModel() {
		super(columnNames, 0);
	}

	public JiraTableModel(Vector<Vector<Object>> contents, Vector<Object> header) {
		super(contents, header);
		log.debug("Initiated from existing contents and header!");
	}

	public boolean addRow(JiraIssue jiraIssue) {
		if (!exists(jiraIssue.getName())) {
			Object[] objects = new Object[] { jiraIssue.getName(), jiraIssue.getFixVersions(), jiraIssue.getStatus(),
					jiraIssue.getResolution() };
			super.addRow(objects);
			return true;
		}
		log.debug("jira already in model: " + jiraIssue.getName());
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
		return false;
	}

	public void removeSelectedRows(JTable table) {
		while (table.getSelectedRowCount() > 0) {
			this.removeRow(table.getSelectedRow());
		}
	}

	// Only required if the table is updated by the app so that it becomes visible to the user.
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		log.debug("Setting value" + "[value: " + value + ", row:" + rowIndex + ", col:" + columnIndex + "]");
		super.setValueAt(value, rowIndex, columnIndex);
	}

}
