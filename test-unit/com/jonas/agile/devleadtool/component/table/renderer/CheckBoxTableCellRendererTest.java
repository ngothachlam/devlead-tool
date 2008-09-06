package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;

import org.easymock.EasyMock;

import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.common.SwingUtil;

public class CheckBoxTableCellRendererTest extends JonasTestCase {

	MyTable myTable_Mock;
	MyTableModel myTableModel_Mock;
	CheckBoxTableCellRenderer renderer;

	private JCheckBox getObjectWithBackground(Component tableCellRendererComponent) {
		return ((CheckBoxTableCellRenderer) tableCellRendererComponent).getCheckBox();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		myTable_Mock = createClassMock(MyTable.class);
		myTableModel_Mock = createClassMock(MyTableModel.class);
		renderer = new CheckBoxTableCellRenderer();

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testShouldSetCorrectColor() {
		EasyMock.expect(myTable_Mock.isRed(true, 0, 0)).andReturn(false);
		EasyMock.expect(myTable_Mock.getFont()).andStubReturn(new Font("", 1, 2));
		EasyMock.expect(myTable_Mock.isCellEditable(0, 0)).andReturn(true);
		replay();

		boolean isSelected = true;
		boolean hasFocus = true;
		Component tableCellRendererComponent = renderer.getTableCellRendererComponent(myTable_Mock, Boolean.TRUE, isSelected,
				hasFocus, 0, 0);
		assertEquals(SwingUtil.getTableCellFocusBackground(), getObjectWithBackground(tableCellRendererComponent).getBackground());

		verify();

		reset();

		EasyMock.expect(myTable_Mock.isRed(true, 0, 0)).andReturn(false);
		EasyMock.expect(myTable_Mock.getFont()).andStubReturn(new Font("", 1, 2));
		EasyMock.expect(myTable_Mock.isCellEditable(0, 0)).andReturn(true);
		EasyMock.expect(myTable_Mock.getSelectionBackground()).andReturn(Color.black);
		replay();

		isSelected = true;
		hasFocus = false;
		tableCellRendererComponent = renderer.getTableCellRendererComponent(myTable_Mock, Boolean.TRUE, isSelected, hasFocus, 0,
				0);
		assertEquals(Color.black, getObjectWithBackground(tableCellRendererComponent).getBackground());

		verify();

		reset();

		EasyMock.expect(myTable_Mock.isRed(true, 0, 0)).andReturn(false);
		EasyMock.expect(myTable_Mock.getFont()).andStubReturn(new Font("", 1, 2));
		EasyMock.expect(myTable_Mock.isCellEditable(0, 0)).andReturn(true);
		EasyMock.expect(myTable_Mock.getBackground()).andReturn(Color.blue);
		replay();

		isSelected = false;
		hasFocus = false;
		tableCellRendererComponent = renderer.getTableCellRendererComponent(myTable_Mock, Boolean.TRUE, isSelected, hasFocus, 0,
				0);
		assertEquals(Color.blue, getObjectWithBackground(tableCellRendererComponent).getBackground());

		verify();
	}

	public void testShouldSetCorrectColorWhenError() {
		EasyMock.expect(myTable_Mock.isRed(true, 0, 0)).andReturn(true);
		EasyMock.expect(myTable_Mock.getFont()).andStubReturn(new Font("", 1, 2));
		EasyMock.expect(myTable_Mock.isCellEditable(0, 0)).andReturn(true);
		replay();

		boolean isSelected = true;
		boolean hasFocus = true;
		Component tableCellRendererComponent = renderer.getTableCellRendererComponent(myTable_Mock, Boolean.TRUE, isSelected,
				hasFocus, 0, 0);
		assertEquals(SwingUtil.COLOR_RED_3, getObjectWithBackground(tableCellRendererComponent).getBackground());

		verify();

		reset();

		EasyMock.expect(myTable_Mock.isRed(true, 0, 0)).andReturn(true);
		EasyMock.expect(myTable_Mock.getFont()).andStubReturn(new Font("", 1, 2));
		EasyMock.expect(myTable_Mock.isCellEditable(0, 0)).andReturn(true);
		replay();

		isSelected = true;
		hasFocus = false;
		tableCellRendererComponent = renderer.getTableCellRendererComponent(myTable_Mock, Boolean.TRUE, isSelected, hasFocus, 0,
				0);
		assertEquals(SwingUtil.COLOR_RED_2, getObjectWithBackground(tableCellRendererComponent).getBackground());

		verify();

		reset();

		EasyMock.expect(myTable_Mock.isRed(true, 0, 0)).andReturn(true);
		EasyMock.expect(myTable_Mock.getFont()).andStubReturn(new Font("", 1, 2));
		EasyMock.expect(myTable_Mock.isCellEditable(0, 0)).andReturn(true);
		replay();

		isSelected = false;
		hasFocus = false;
		tableCellRendererComponent = renderer.getTableCellRendererComponent(myTable_Mock, Boolean.TRUE, isSelected, hasFocus, 0,
				0);
		assertEquals(SwingUtil.COLOR_RED_1, getObjectWithBackground(tableCellRendererComponent).getBackground());

		verify();
	}
}
