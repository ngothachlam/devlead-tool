package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;

import javax.swing.JTable;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class CheckBoxTableCellRendererTest extends TestCase {

	CheckBoxTableCellRenderer renderer;
	MyTableModel myTableModel_Mock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		myTableModel_Mock = EasyMock.createMock(MyTableModel.class);
		renderer = new CheckBoxTableCellRenderer(myTableModel_Mock);

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testShouldSetCorrectColor() {
		EasyMock.expect(myTableModel_Mock.shouldBackgroundBeRed(true, 0, 0)).andReturn(false);

		EasyMock.replay(myTableModel_Mock);

		assertEquals(Color.red, renderer.getTableCellRendererComponent(new JTable(), Boolean.TRUE, true, true, 0, 0)
				.getBackground());

		EasyMock.verify(myTableModel_Mock);

	}
}
