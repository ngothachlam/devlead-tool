package com.jonas.agile.devleadtool.component.table.renderer;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

import junit.framework.TestCase;

public class StringTableCellRendererTest extends TestCase {

	private StringTableCellRenderer board;

	private MyTableModel model;

	protected void setUp() throws Exception {
		super.setUp();
		model = new BoardTableModel();
		board = new StringTableCellRenderer();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCellColors() {
	}

}
