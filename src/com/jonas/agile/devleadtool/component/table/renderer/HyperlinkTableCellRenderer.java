package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class HyperlinkTableCellRenderer extends DefaultTableCellRenderer {

	public HyperlinkTableCellRenderer() {
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//		cell.setFont(font);
		cell.setHorizontalAlignment(JLabel.CENTER);
		return cell;
	}

}
