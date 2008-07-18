package com.jonas.agile.devleadtool.component.table.model;

import javax.swing.JPanel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.w3c.dom.views.AbstractView;

public interface MyTableModel extends TableModel {

	boolean shouldBackgroundBeRed(Object value, int row, int column);

}
