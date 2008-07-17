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
		debug(new Boolean(cell instanceof JTextField).toString());
		if (column == 0) {
			if (isCellToBecomeRed(value, column)) {
				cell.setBackground(Color.red);
			} else {
				if (isSelected) {
					setForeground(table.getSelectionForeground());
					setBackground(table.getSelectionBackground());
				} else {
					setForeground(table.getForeground());
					setBackground(table.getBackground());
				}
			}
		} 
		return cell;
	}

	private boolean isCellToBecomeRed(Object value, int column) {
		return model.countOfSameValueInColumn(value, column) > 1;
	}

	private void debug(String string) {
		System.out.println(System.currentTimeMillis() + " ~ " + string);
	}

}
