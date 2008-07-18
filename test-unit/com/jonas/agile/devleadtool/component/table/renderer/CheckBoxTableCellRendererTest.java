package com.jonas.agile.devleadtool.component.table.renderer;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;

import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

import junit.framework.TestCase;

public class CheckBoxTableCellRendererTest extends TestCase {

	CheckBoxTableCellRenderer renderer_errors;

	CheckBoxTableCellRenderer renderer_ok;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		renderer_errors = new CheckBoxTableCellRenderer(new TestTableModel(true));
		renderer_ok = new CheckBoxTableCellRenderer(new TestTableModel(false));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testShouldSetCorrectColor() {
		// TODO implement easyMock!!!
		assertEquals(Color.red, renderer_errors.getTableCellRendererComponent(new JTable(), Boolean.TRUE, true, true, 0, 0).getBackground());
	}

	private final class TestTableModel implements MyTableModel {

		boolean shouldBeRed = false;

		public void addEmptyRow() {
		}

		public void removeSelectedRows(JTable table) {
		}

		public boolean shouldBackgroundBeRed(Object value, int row, int column) {
			return shouldBeRed;
		}

		public void addTableModelListener(TableModelListener arg0) {
		}

		public Class<?> getColumnClass(int arg0) {
			return null;
		}

		public int getColumnCount() {
			return 0;
		}

		public String getColumnName(int arg0) {
			return null;
		}

		public int getRowCount() {
			return 0;
		}

		public Object getValueAt(int arg0, int arg1) {
			return null;
		}

		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}

		public void removeTableModelListener(TableModelListener arg0) {
		}

		public void setValueAt(Object arg0, int arg1, int arg2) {
		}

		public TestTableModel(boolean shouldBeRed) {
			super();
			this.shouldBeRed = shouldBeRed;
		}
	}

}
