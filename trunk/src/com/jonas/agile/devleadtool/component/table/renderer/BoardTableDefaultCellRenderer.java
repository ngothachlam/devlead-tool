package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.jonas.agile.devleadtool.component.table.BoardTableModel;
import com.jonas.common.SwingUtil;

public class BoardTableDefaultCellRenderer extends DefaultTableCellRenderer {

	private final BoardTableModel model;

	private Color backgroundUnselected = null;

	private Color backgroundSelected = null;

	private Color backgroundFocused = null;

	public BoardTableDefaultCellRenderer(BoardTableModel model) {
		this.model = model;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cell = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (model.shouldBeRedBackground(value, row, column)) {
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

	private void debug(String string) {
		System.out.println(System.currentTimeMillis() + " ~ " + string);
	}

}
