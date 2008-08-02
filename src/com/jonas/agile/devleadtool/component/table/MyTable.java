package com.jonas.agile.devleadtool.component.table;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class MyTable extends JTable {

	public MyTable() {
		super();
	}

	public void setColumnRenderer(int i, TableCellRenderer renderer) {
		TableColumnModel tcm = getColumnModel();
		TableColumn tc = tcm.getColumn(i);
		tc.setCellRenderer(renderer);
	}

}
