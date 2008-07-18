package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class BoardTableCellEditor extends DefaultCellEditor {
	private final MyTableModel model;

	public BoardTableCellEditor(MyTableModel model) {
		super(new JCheckBox());
		this.model = model;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component cell = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		((JCheckBox) getComponent()).setHorizontalAlignment(JLabel.CENTER);
		if (model.shouldBackgroundBeRed(value, row, column)) {
			cell.setBackground(Color.red);
		} else {
			cell.setBackground(table.getSelectionBackground());
		}
		return cell;
	}
}