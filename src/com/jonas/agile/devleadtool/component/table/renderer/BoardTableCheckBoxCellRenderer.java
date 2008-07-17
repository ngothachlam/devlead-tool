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

	private final BoardTableModel model;

	private JCheckBox checkbox = new JCheckBox();

	public BoardTableCheckBoxCellRenderer(BoardTableModel model) {
		super(new BorderLayout());
		this.model = model;
		checkbox.setHorizontalAlignment(JLabel.CENTER);
		this.add(checkbox, SwingUtilities.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (hasFocus) {
			if (doesProgressCellNeedToBeRed(value, row)) {
				checkbox.setBackground(COLOR_FOCUS_ERROR);
			} else {
				Color selectionBackground = table.getSelectionBackground();
				// checkbox.setForeground(table.getForeground());
				selectionBackground.getBlue();
				checkbox.setBackground(new Color(selectionBackground.getRed(), selectionBackground.getGreen(), selectionBackground.getBlue()));
			}
		} else if (isSelected) {
			if (doesProgressCellNeedToBeRed(value, row)) {
				checkbox.setBackground(COLOR_SELECTION_ERROR);
			} else {
				// checkbox.setForeground(table.getSelectionForeground());
				checkbox.setBackground(table.getSelectionBackground());
			}
		} else {
			if (doesProgressCellNeedToBeRed(value, row)) {
				checkbox.setBackground(COLOR_NONSELECT_ERROR);
			} else {
				checkbox.setBackground(table.getBackground());
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
		if (!table.isCellEditable(row, column)) {
			checkbox.setEnabled(false);
		}else
			checkbox.setEnabled(true);
			

		setSelected(((Boolean) value).booleanValue() ? true : false);
		return this;
	}

	public void setBorder(Border border) {
		super.setBorder(border);
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
