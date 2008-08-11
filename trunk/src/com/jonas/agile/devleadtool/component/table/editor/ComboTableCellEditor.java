package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class ComboTableCellEditor extends DefaultCellEditor {
	private final MyTableModel model;

	public ComboTableCellEditor(MyTableModel model) {
		super(new JComboBox());
		this.model = model;
		//FIXME : LLU Hardcoded!!!
		JiraVersion[] versions = JiraProject.getProjectByKey("LLU").getFixVersions(false);
		for (JiraVersion jiraVersion : versions) {
			((JComboBox) super.getComponent()).addItem(jiraVersion);
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component cell = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		// ((JComboBox) getComponent().setHorizontalAlignment(JLabel.CENTER);
		if (model.isRed(value, row, column)) {
			cell.setBackground(Color.red);
		} else {
			cell.setBackground(table.getSelectionBackground());
		}
		return cell;
	}
}
