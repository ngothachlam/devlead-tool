package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.Serializable;

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

	private static final Logger log = MyLogger.getLogger(CheckBoxTableCellRenderer.class);

	private JCheckBox checkbox = new JCheckBox();

	public CheckBoxTableCellRenderer() {
		super(new BorderLayout());
		checkbox.setHorizontalAlignment(JLabel.CENTER);
		this.add(checkbox, SwingUtilities.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		log.debug("CheckBox for column: " + column + " with value: " + value + " (class: " + debugClassOfValue(value) + ")");

		setFont(table.getFont());

		MyTableModel model = (MyTableModel) table.getModel();
		
		if (model.isRed(value, table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column))) {
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

	private Serializable debugClassOfValue(Object value) {
		return (value != null ? value.getClass() : "null");
	}

	public void setBorder(Border border) {
		super.setBorder(border);
	}

	private void setSelected(boolean b) {
		checkbox.setSelected(b);
	}

}
