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
	private Color backgroundUnselected = null;
	private Color backgroundSelected = null;
	private Color backgroundFocused = null;

	protected static Border noFocusBorder = new MatteBorder(3, 3, 3, 3, Color.blue);

	public BoardTableCheckBoxCellRenderer(BoardTableModel model) {
		super(new BorderLayout());
		this.model = model;
		checkbox = new JCheckBox();
		checkbox.setHorizontalAlignment(JLabel.CENTER);
		this.add(checkbox, SwingUtilities.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		// originalBorder(table, value, isSelected, hasFocus, row, column);
		testBorder(table, value, isSelected, hasFocus, row);

		setSelected(((Boolean) value).booleanValue() ? true : false);
		return this;
	}

	private void originalBorder(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setFont(table.getFont());
		if (doesProgressCellNeedToBeRed(value, row)) {
			setBackground(Color.red);
		} else if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		if (!hasFocus) {
			setBorder(new EmptyBorder(1, 1, 1, 1));
		} else {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			if (table.isCellEditable(row, column)) {
				super.setForeground(UIManager.getColor("Table.focusCellForeground"));
				super.setBackground(UIManager.getColor("Table.focusCellBackground"));
			}
		}
	}

	private void testBorder(JTable table, Object value, boolean isSelected, boolean hasFocus, int row) {
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
	}

	// public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	// int row, int column) {
	// JComponent cell = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
	// column);
	// if (value instanceof Boolean) {
	// // cell = (JComponent) createNewCheckBoxIfRequired(value, cell);
	// if (doesProgressCellNeedToBeRed(value, row)) {
	// cell.setBackground(Color.red);
	// if (hasFocus && isSelected) {
	// cell.setBorder(UIManager.getBorder("Table.focusSelectedCellHighlightBorder"));
	// } else if (hasFocus) {
	// cell.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
	// }
	// } else {
	// Border border = null;
	// if (isSelected) {
	// cell.setForeground(table.getSelectionForeground());
	// cell.setBackground(table.getSelectionBackground());
	// if (hasFocus) {
	// border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
	// }
	// } else {
	// cell.setForeground(table.getForeground());
	// cell.setBackground(table.getBackground());
	// if (hasFocus) {
	// border = UIManager.getBorder("Table.focusCellHighlightBorder");
	// }
	// }
	// // border = noFocusBorder;
	// cell.setBorder(border);
	// }
	// } else if (column == 0) {
	// if (model.countOfSameValueInColumn(value, column) > 1) {
	// cell.setBackground(Color.red);
	// } else {
	// if (isSelected) {
	// setForeground(table.getSelectionForeground());
	// super.setBackground(table.getSelectionBackground());
	// } else {
	// setForeground(table.getForeground());
	// setBackground(table.getBackground());
	// }
	// }
	// }
	// return cell;
	// }

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
