package com.jonas.agile.devleadtool.gui.component;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MyScrollPane extends JScrollPane {

	public MyScrollPane(JTable table) {
		super(table);
		setWheelScrollingEnabled(true);
	}

}
