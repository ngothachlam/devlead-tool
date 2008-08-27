/**
 * 
 */
package com.jonas.agile.devleadtool.component.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

final class RemoveJTableSelectedRowsListener implements ActionListener {
	private final MyTable table;

	/**
	 * @param table
	 */
	RemoveJTableSelectedRowsListener(MyTable table) {
		this.table = table;
	}

	public void actionPerformed(ActionEvent e) {
		((MyTableModel) table.getModel()).removeSelectedRows(table);
	}
}