/**
 * 
 */
package com.jonas.agile.devleadtool.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.jonas.agile.devleadtool.component.table.MyTable;

public class RemoveJTableSelectedRowsListener implements ActionListener {
	private final MyTable table;

	/**
	 * @param table
	 */
	public RemoveJTableSelectedRowsListener(MyTable table) {
		this.table = table;
	}

	public void actionPerformed(ActionEvent e) {
		table.removeSelectedRows();
	}
}