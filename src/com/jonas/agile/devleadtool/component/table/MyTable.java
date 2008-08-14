package com.jonas.agile.devleadtool.component.table;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.jonas.agile.devleadtool.component.table.editor.ComboTableCellEditor;
import com.jonas.jira.JiraIssue;

public class MyTable extends JTable {

	public MyTable() {
		super();
	}

	public void setColumnRenderer(int i, TableCellRenderer renderer) {
		TableColumnModel tcm = getColumnModel();
		TableColumn tc = tcm.getColumn(i);
		tc.setCellRenderer(renderer);
	}

	public void setRow(JiraIssue jira, int i) {
		setValueAt(jira.getName(), i, 0);
		//TODO only works with one fix-version below!!
		setValueAt(jira.getFixVersions().get(0), i, 1);
		setValueAt(jira.getStatus(), i, 2);
		setValueAt(jira.getResolution(), i, 3);
	}

	public void setColumnEditor(int i, ComboTableCellEditor editor) {
		TableColumnModel tcm = getColumnModel();
		TableColumn tc = tcm.getColumn(i);
		tc.setCellEditor(editor);
		
	}

}
