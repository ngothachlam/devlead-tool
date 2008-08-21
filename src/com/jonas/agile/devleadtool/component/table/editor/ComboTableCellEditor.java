package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.panel.MyComboBox;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;

public class ComboTableCellEditor extends DefaultCellEditor {
	private Logger log = MyLogger.getLogger(ComboTableCellEditor.class);
	private MyComboBox comboBox;

	public MyComboBox getComboBox() {
		return comboBox;
	}

	public ComboTableCellEditor(MyComboBox comboBox) {
		super(comboBox);
		this.comboBox = comboBox;
	}

	public Component getTableCellEditorComponent(MyTable table, Object value, boolean isSelected, int row, int column) {
		Component cell = super.getTableCellEditorComponent(table, value, isSelected, row, column);

		MyTableModel model = ((MyTableModel) table.getModel());

		if (table.isRed(value, row, column)) {
			cell.setBackground(Color.red);
		} else {
			cell.setBackground(table.getSelectionBackground());
		}
		// FIXME does this work automatically with Arrays instead of lists?
		return cell;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return getTableCellEditorComponent((MyTable) table, value, isSelected, row, column);
	}
}
