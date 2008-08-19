package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class CheckBoxTableCellEditor extends DefaultCellEditor {

	public CheckBoxTableCellEditor() {
		super(new JCheckBox());
	}

   @Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component cell = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		MyTableModel model = (MyTableModel) table.getModel();
		((JCheckBox) getComponent()).setHorizontalAlignment(JLabel.CENTER);
		if (model.isRed(value, row, column)) {
			cell.setBackground(Color.red);
		} else {
			cell.setBackground(table.getSelectionBackground());
		}
		return cell;
	}
}