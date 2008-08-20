package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;

import javax.swing.JTable;

import org.easymock.EasyMock;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class CheckBoxTableCellRendererTest extends JonasTestCase {

	CheckBoxTableCellRenderer renderer;
	MyTableModel myTableModel_Mock;
	JTable myTable_Mock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		myTable_Mock = createClassMock(JTable.class);
		myTableModel_Mock = createClassMock(MyTableModel.class);
		renderer = new CheckBoxTableCellRenderer();

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testShouldSetCorrectColor() {
		EasyMock.expect(myTable_Mock.getModel()).andReturn(myTableModel_Mock);
		EasyMock.expect(myTableModel_Mock.isRed(true, 0, 0)).andReturn(false);
		replay();

		assertEquals(Color.red, renderer.getTableCellRendererComponent(new JTable(), Boolean.TRUE, true, true, 0, 0).getBackground());

		verify();
	}
}
