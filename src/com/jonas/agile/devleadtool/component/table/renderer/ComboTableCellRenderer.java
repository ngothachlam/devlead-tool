package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.Serializable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class ComboTableCellRenderer extends JPanel implements TableCellRenderer {

	private final MyTableModel model;
	private static final Logger log = MyLogger.getLogger(ComboTableCellRenderer.class);

	private JComboBox combo = new JComboBox();

	public ComboTableCellRenderer(MyTableModel model) {
		super(new BorderLayout());
		this.model = model;
		this.add(combo, SwingUtilities.CENTER);
		updateFixVersionsAvailable();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {

		log.debug("Combo for column: " + column + " with value: " + value + " (class: " + debugClassOfValue(value) + ")");
		setFont(table.getFont());

		if (model.isRed(value, row, column)) {
			if (hasFocus)
				combo.setBackground(SwingUtil.COLOR_FOCUS_ERROR);
			else if (isSelected)
				combo.setBackground(SwingUtil.COLOR_SELECTION_ERROR);
			else
				combo.setBackground(SwingUtil.COLOR_NONSELECT_ERROR);
		} else {
			if (hasFocus)
				combo.setBackground(SwingUtil.getTableCellFocusBackground());
			else if (isSelected)
				combo.setBackground(table.getSelectionBackground());
			else
				combo.setBackground(table.getBackground());
		}

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		} else {
			setBorder(UIManager.getBorder("Table.focusSelectedCellHighlightBorder"));
		}

		if (!table.isCellEditable(row, column)) {
			combo.setEnabled(false);
		} else
			combo.setEnabled(true);

		combo.setSelectedItem(value);
		return this;
	}

	public void setBorder(Border border) {
		super.setBorder(border);
	}

	private Serializable debugClassOfValue(Object value) {
		return (value != null ? value.getClass() : "null");
	}

	private void setSelected(Object b) {
		combo.setSelectedItem(b);
	}
	
	public void updateFixVersionsAvailable(){
		JiraVersion[] version = JiraProject.getProjectByKey("LLU").getFixVersions(false);
		for (int i = 0; i < version.length; i++) {
			combo.addItem(version[i]);
		}
	}

}
