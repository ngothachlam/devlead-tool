package com.jonas.agile.devleadtool.component.table.editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;

public class ComboTableCellEditor extends DefaultCellEditor {
	private final MyTableModel model;
	private Logger log = MyLogger.getLogger(ComboTableCellEditor.class);

	public ComboTableCellEditor(MyTableModel model, JComboBox comboBox) {
		// FIXME : LLU Hardcoded!!!
		super(comboBox);
		this.model = model;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component cell = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		if (model.isRed(value, row, column)) {
			cell.setBackground(Color.red);
		} else {
			cell.setBackground(table.getSelectionBackground());
		}
		// FIXME does this work automatically with Arrays instead of lists?
		return cell;
	}
}
