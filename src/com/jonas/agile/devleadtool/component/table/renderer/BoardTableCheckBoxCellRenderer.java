package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;

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

import sun.util.calendar.CalendarUtils;

import com.jonas.agile.devleadtool.component.table.BoardTableModel;

public class BoardTableCheckBoxCellRenderer extends JPanel implements TableCellRenderer {

	private static final Color COLOR_NONSELECT_ERROR = new Color(200, 0, 0);

	private static final Color COLOR_SELECTION_ERROR = new Color(225, 0, 0);

	private static final Color COLOR_FOCUS_ERROR = new Color(250, 0, 0);

	// private static final Color COLOR_FOCUS_OK = new Color(220, 0, 0);

	private final BoardTableModel model;

	private JCheckBox checkbox;

	public BoardTableCheckBoxCellRenderer(BoardTableModel model) {
		super(new BorderLayout());
		this.model = model;
		checkbox = new JCheckBox();
		checkbox.setHorizontalAlignment(JLabel.CENTER);
		this.add(checkbox, SwingUtilities.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (hasFocus) {
			if (doesProgressCellNeedToBeRed(value, row)) {
				setTheBackground(COLOR_FOCUS_ERROR);
			} else {
				setTheForeground(table.getForeground());
				setTheBackground(table.getSelectionBackground());
			}
		} else if (isSelected) {
			if (doesProgressCellNeedToBeRed(value, row)) {
				setTheBackground(COLOR_SELECTION_ERROR);
			} else {
				setTheForeground(table.getSelectionForeground());
				setTheBackground(table.getSelectionBackground());
			}
		} else {
			if (doesProgressCellNeedToBeRed(value, row)) {
				setTheBackground(COLOR_NONSELECT_ERROR);
			} else {
				setTheBackground(table.getBackground());
			}
		}
		setFont(table.getFont());
		if (!hasFocus) {
			setBorder(UIManager.getBorder("Table.focusSelectedCellHighlightBorder"));
		} else {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			// setTheForeground(UIManager.getColor("Table.focusCellForeground"));
			// setTheBackground(UIManager.getColor("Table.focusCellBackground"));
			// if (doesProgressCellNeedToBeRed(value, row)) {
			// setTheBackground(COLOR_FOCUS_ERROR);
			// } else {
			// setTheBackground(COLOR_FOCUS_OK);
			// }
		}
		if (table.isCellEditable(row, column)) {
			setToolTipText("Not Editable!");
		}

		setSelected(((Boolean) value).booleanValue() ? true : false);
		return this;
	}

	public void setTheForeground(Color color) {
		// super.setForeground(fg);
		checkbox.setForeground(color);
	}

	public void setTheBackground(Color color) {
		// super.setBackground(bg);
		checkbox.setBackground(color);
	}

	@Override
	public void setBorder(Border border) {
		super.setBorder(border);
	}

	private Component testOutput(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		debug("row: " + row + " column: " + column);
		if (doesProgressCellNeedToBeRed(value, row)) {
			debug("2");
			setForeground(table.getSelectionForeground());
			// setBackground(COLOR_ERROR_SELECTION);
			if (hasFocus && isSelected) {
				debug("3");
				setBorder(UIManager.getBorder("Table.focusSelectedCellHighlightBorder"));
			} else if (hasFocus) {
				debug("4");
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			}
		} else {
			debug("5");
			Border border = null;
			if (isSelected) {
				debug("6");
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
				if (hasFocus) {
					debug("7");
					border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
				}
			} else {
				debug("8");
				setForeground(table.getForeground());
				setBackground(table.getBackground());
				if (hasFocus) {
					debug("9");
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
		System.out.println(Calendar.getInstance().getTime() + " ~ " + string);
	}

	private boolean doesProgressCellNeedToBeRed(Object value, int row) {
		return (!model.isOneCheckboxTicked(row) && model.noOfCheckboxesTicked(row) == 0)
			|| (model.noOfCheckboxesTicked(row) > 1 && value.equals(Boolean.TRUE));
	}

}
