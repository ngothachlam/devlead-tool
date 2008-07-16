package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

import com.jonas.agile.devleadtool.component.table.BoardTableModel;

public class BoardTableCheckBoxCellRenderer extends JPanel implements TableCellRenderer {

	private final BoardTableModel model;

	private JCheckBox checkbox;

	public BoardTableCheckBoxCellRenderer(BoardTableModel model) {
		super(new BorderLayout());
		this.model = model;
		checkbox = new JCheckBox();
		checkbox.setHorizontalAlignment(JLabel.CENTER);
		this.add(checkbox, SwingUtilities.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (doesProgressCellNeedToBeRed(value, row)) {
			setBackground(Color.red);
			if (hasFocus && isSelected) {
				setBorder(UIManager.getBorder("Table.focusSelectedCellHighlightBorder"));
			} else if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			}
		} else {
			Border border = null;
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
				if (hasFocus) {
					border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
				}
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
				if (hasFocus) {
					border = UIManager.getBorder("Table.focusCellHighlightBorder");
				}
			}
			setBorder(border);
		}

		setSelected(((Boolean) value).booleanValue() ? true : false);
		return this;
	}

	private void setSelected(boolean b) {
		checkbox.setSelected(b);
	}

	private void debug(String string) {
		System.out.println(System.currentTimeMillis() + " ~ " + string);
	}

	private boolean doesProgressCellNeedToBeRed(Object value, int row) {
		return (!model.isOneCheckboxTicked(row) && model.noOfCheckboxesTicked(row) == 0)
				|| (model.noOfCheckboxesTicked(row) > 1 && value.equals(Boolean.TRUE));
	}

}
