package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class StringTableCellRenderer extends DefaultTableCellRenderer {

	private static final Logger LOGGER = MyLogger.getLogger(StringTableCellRenderer.class);
	private final MyTableModel model;

	public StringTableCellRenderer(MyTableModel model) {
		this.model = model;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cell = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (model.isRed(value, row, column)) {
			if (hasFocus)
				cell.setBackground(SwingUtil.COLOR_FOCUS_ERROR);
			else if (isSelected)
				cell.setBackground(SwingUtil.COLOR_SELECTION_ERROR);
			else
				cell.setBackground(SwingUtil.COLOR_NONSELECT_ERROR);

		} else {
			if (hasFocus) {
				cell.setBackground(SwingUtil.getTableCellFocusBackground());
			} else if (isSelected) {
				cell.setForeground(table.getSelectionForeground());
				cell.setBackground(table.getSelectionBackground());
			} else {
				cell.setForeground(table.getForeground());
				cell.setBackground(table.getBackground());
			}
		}
		return cell;
	}
}
