package com.jonas.agile.devleadtool.component.table;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.BoardTableDefaultCellRenderer;

public class MyTable extends JTable {

	public MyTable(MyTableModel model) {
		super(model);
	}

	public void setColumnRenderer(int i, TableCellRenderer renderer) {
		TableColumnModel tcm = getColumnModel();
		TableColumn tc = tcm.getColumn(i);
		tc.setCellRenderer(renderer);
	}

}
