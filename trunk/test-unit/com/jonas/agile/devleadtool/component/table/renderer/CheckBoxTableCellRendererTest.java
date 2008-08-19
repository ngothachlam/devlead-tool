package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;

import javax.swing.JTable;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class CheckBoxTableCellRendererTest extends TestCase {

	CheckBoxTableCellRenderer renderer;
	MyTableModel myTableModel_Mock;
	JTable myTable_Mock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		myTable_Mock = org.easymock.classextension.EasyMock.createMock(JTable.class);
		myTableModel_Mock = org.easymock.classextension.EasyMock.createMock(MyTableModel.class);
		renderer = new CheckBoxTableCellRenderer();

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testShouldSetCorrectColor() {
		EasyMock.expect(myTable_Mock.getModel()).andReturn(myTableModel_Mock);
		EasyMock.expect(myTableModel_Mock.isRed(true, 0, 0)).andReturn(false);
		org.easymock.classextension.EasyMock.replay(myTable_Mock);
		org.easymock.classextension.EasyMock.replay(myTableModel_Mock);

		assertEquals(Color.red, renderer.getTableCellRendererComponent(new JTable(), Boolean.TRUE, true, true, 0, 0).getBackground());

		org.easymock.classextension.EasyMock.verify(myTable_Mock);
		org.easymock.classextension.EasyMock.verify(myTableModel_Mock);

	}
}
