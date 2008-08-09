package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class CheckBoxTableCellRenderer extends JPanel implements TableCellRenderer {

	private final MyTableModel model;
	private static final Logger LOGGER = MyLogger.getLogger(CheckBoxTableCellRenderer.class);

	private JCheckBox checkbox = new JCheckBox();

	public CheckBoxTableCellRenderer(MyTableModel model) {
		super(new BorderLayout());
		this.model = model;
		checkbox.setHorizontalAlignment(JLabel.CENTER);
		this.add(checkbox, SwingUtilities.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {

		setFont(table.getFont());

		if (model.isRed(value, row, column)) {
			if (hasFocus)
				checkbox.setBackground(SwingUtil.COLOR_FOCUS_ERROR);
			else if (isSelected)
				checkbox.setBackground(SwingUtil.COLOR_SELECTION_ERROR);
			else
				checkbox.setBackground(SwingUtil.COLOR_NONSELECT_ERROR);
		} else {
			if (hasFocus)
				checkbox.setBackground(SwingUtil.getTableCellFocusBackground());
			else if (isSelected)
				checkbox.setBackground(table.getSelectionBackground());
			else
				checkbox.setBackground(table.getBackground());
		}

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		} else {
			setBorder(UIManager.getBorder("Table.focusSelectedCellHighlightBorder"));
		}

		if (!table.isCellEditable(row, column)) {
			checkbox.setEnabled(false);
		} else
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


}
