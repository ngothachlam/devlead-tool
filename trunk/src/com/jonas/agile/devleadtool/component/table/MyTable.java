package com.jonas.agile.devleadtool.component.table;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.jonas.agile.devleadtool.component.table.renderer.BoardTableDefaultCellRenderer;

public class MyTable extends JTable {

	public MyTable(BoardTableModel model) {
		super(model);
	}

	public void setColumnRenderer(int i, BoardTableDefaultCellRenderer boardTableDefaultCellRenderer) {
		TableColumnModel tcm = getColumnModel();
		TableColumn tc = tcm.getColumn(i);
		tc.setCellRenderer(boardTableDefaultCellRenderer);
	}

}
