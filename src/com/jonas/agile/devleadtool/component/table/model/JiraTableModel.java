package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.jonas.jira.JiraIssue;

public class JiraTableModel extends DefaultTableModel implements MyTableModel {

	private static Vector columnNames = new Vector();

	static {
		columnNames.add("Jira");
		columnNames.add("FixVersion");
		columnNames.add("Status");
		columnNames.add("Resolution");
	}

	public JiraTableModel() {
		super(columnNames, 0);
	}

	public void addEmptyRow() {
	}
	
	public void addRow(JiraIssue jiraIssue) {
		Object[] objects = new Object[]{jiraIssue.getName(),jiraIssue.getFixVersion(), jiraIssue.getStatus(), jiraIssue.getResolution()};
		this.addRow(objects);
	}

	public void removeSelectedRows(JTable table) {
	}

	public boolean shouldBackgroundBeRed(Object value, int row, int column) {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
}
