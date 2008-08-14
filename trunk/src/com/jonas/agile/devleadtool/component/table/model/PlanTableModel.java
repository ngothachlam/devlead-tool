package com.jonas.agile.devleadtool.component.table.model;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class PlanTableModel extends MyTableModel {

	private static Vector<String> columnNames = new Vector<String>();

	static {
		columnNames.add("Jira");
		columnNames.add("FixVersion");
		columnNames.add("Status");
		columnNames.add("Resolution");
	}

	@Override
	protected Object[] getEmptyRow() {
		return new Object[] { "", "", "", "" };
	}

	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
		fireTableRowsUpdated(0, this.getRowCount() - 1);
	}

	private Logger log = MyLogger.getLogger(PlanTableModel.class);

	public PlanTableModel() {
		super(columnNames, 0);
	}

	public PlanTableModel(Vector<Vector<Object>> contents, Vector<Object> header) {
		super(contents, header);
	}

	public void addEmptyRow() {
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return isEditable() ? true : false;
	}

	public boolean isRed(Object value, int row, int column) {
		log.debug("isRed: " + value + " row: " + row + " col: " + column);
		switch (column) {
		case 1:
			return value == null || value.toString().length() == 0;
		default:
			break;
		}
		return false;
	}

	public boolean addRow(JiraIssue jiraIssue) {
		List<JiraVersion> fixVersions = jiraIssue.getFixVersions();
		if (fixVersions.size() > 1) {
			throw new RuntimeException(jiraIssue + " has more than one fixversion! Model can't handle it at the moment!!");
		}
		JiraVersion jiraVersion = null;
		if (fixVersions != null && fixVersions.size() > 0) {
			jiraVersion = fixVersions.get(0);
		}
		Object[] objects = new Object[] { jiraIssue.getName(), jiraVersion, jiraIssue.getStatus(), jiraIssue.getResolution() };
		super.addRow(objects);
		log.debug("adding jira to plan: " + jiraIssue.getName());
		fireTableRowsUpdated(0, this.getRowCount() - 1);
		return true;
	}

	public boolean setRow(JiraIssue jiraIssue, int row) {
		log.debug("overwriting jira: " + jiraIssue.getName() + " to model on row: " + row);
		List<JiraVersion> fixVersions = jiraIssue.getFixVersions();
		if (fixVersions.size() > 1) {
			throw new RuntimeException(jiraIssue + " has more than one fixversion! Model can't handle it at the moment!!");
		}
		Object[] object = new Object[] { jiraIssue.getName(), fixVersions, jiraIssue.getStatus(), jiraIssue.getResolution() };
		for (int i = 0; i < object.length; i++) {
			setValueAt(object[i], row, i);
		}
		return true;
	}

	public boolean doesJiraExist(String jira) {
		for (int row = 0; row < getRowCount(); row++) {
			log.debug("checking row " + row);
			if (jira.equalsIgnoreCase((String) getValueAt(row, 0)))
				return true;
		}
		return false;
	}

}
