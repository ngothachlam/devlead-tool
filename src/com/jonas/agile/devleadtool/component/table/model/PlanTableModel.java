package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

public class PlanTableModel extends MyTableModel {

	private static Vector<String> columnNames = new Vector<String>();

	static {
		columnNames.add("Jira");
		columnNames.add("FixVersion");
		columnNames.add("Status");
		columnNames.add("Resolution");
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

	public void removeSelectedRows(JTable table) {
	}

	public boolean isRed(Object value, int row, int column) {
		return false;
	}

	public boolean addRow(JiraIssue jiraIssue) {
		Object[] objects = new Object[] { jiraIssue.getName(), jiraIssue.getFixVersions(), jiraIssue.getStatus(),
				jiraIssue.getResolution() };
		super.addRow(objects);
		log.debug("adding jira to plan: " + jiraIssue.getName());
		return true;
	}
	
	public boolean setRow(JiraIssue jiraIssue, int row) {
		log.debug("overwriting jira: " + jiraIssue.getName() + " to model on row: " + row);
		Object[] object = new Object[] { jiraIssue.getName(), jiraIssue.getFixVersions(), jiraIssue.getStatus(),
				jiraIssue.getResolution() };
		for (int i = 0; i < object.length; i++) {
			setValueAt(object[i], row, i);
		}
		return true;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return isEditable() ? super.isCellEditable(row, column) : false;
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
