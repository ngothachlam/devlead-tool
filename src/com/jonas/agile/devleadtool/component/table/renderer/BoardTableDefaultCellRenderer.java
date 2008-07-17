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

	protected static Border noFocusBorder = new MatteBorder(3, 3, 3, 3, Color.blue);

	public BoardTableDefaultCellRenderer(BoardTableModel model) {
		this.model = model;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cell = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (column == 0) {
			if (model.shouldBeRedBackground(value, 0, column)) {
				cell.setBackground(Color.red);
			} else {
				if (hasFocus) {
					cell.setBackground(Color.green);
					setBackground(Color.blue);
					cell.setBackground(SwingUtil.getTableCellFocusBackground(table));
				}
				if (isSelected) {
					cell.setBackground(Color.black);
					setBackground(Color.orange);
					cell.setForeground(table.getSelectionForeground());
					cell.setBackground(table.getSelectionBackground());
				} else {
					cell.setBackground(Color.pink);
					setBackground(Color.yellow);
					cell.setForeground(table.getForeground());
					cell.setBackground(table.getBackground());
				}
			}
		} 
		return cell;
	}

	private void debug(String string) {
		System.out.println(System.currentTimeMillis() + " ~ " + string);
	}

}
