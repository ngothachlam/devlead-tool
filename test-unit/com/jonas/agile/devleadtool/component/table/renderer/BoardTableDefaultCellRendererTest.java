package com.jonas.agile.devleadtool.component.table.renderer;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;

import junit.framework.TestCase;

public class BoardTableDefaultCellRendererTest extends TestCase {

	private StringTableCellRenderer board;

	private BoardTableModel model;

	protected void setUp() throws Exception {
		super.setUp();
		model = new BoardTableModel();
		board = new StringTableCellRenderer(model);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCellColors() {
	}

}
