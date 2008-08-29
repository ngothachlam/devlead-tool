package com.jonas.agile.devleadtool.component.table;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class BoardTableModelTest extends JonasTestCase {
	
	String[] tableHeader = { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete" };
	Object[][] tableContents = { getEmptyRow() };
	BoardTableModel model;
	
	protected void setUp() throws Exception {
		super.setUp();
		model = new BoardTableModel();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testShouldAddRowsWhilstEditingCorrectly() {
		assertEquals(1, model.getRowCount());
		model.setValueAt("123", 0, 0);
		assertEquals(2, model.getRowCount());
		model.setValueAt("1234", 0, 0);
		assertEquals(2, model.getRowCount());
		model.setValueAt("1234", 1, 0);
		assertEquals(3, model.getRowCount());
	}

	public void testShouldCountColValuesCorrectlyWithStringsNotCaseSensitive() {
		assertEquals(0, model.getCountOfSameValueInColumn(new String("llu-123"), 0));
		model.setValueAt("llu-123", 0, 0);
		assertEquals(1, model.getCountOfSameValueInColumn(new String("llu-123"), 0));
		model.setValueAt("llu-123", 1, 0);
		assertEquals(2, model.getCountOfSameValueInColumn(new String("llu-123"), 0));
		model.setValueAt("lLu-123", 2, 0);
		assertEquals(3, model.getCountOfSameValueInColumn(new String("llu-123"), 0));
		assertEquals(3, model.getCountOfSameValueInColumn(new String("Llu-123"), 0));
		assertEquals(0, model.getCountOfSameValueInColumn(new String("Llu-1234"), 0));
	}
	public void testShouldCountColValuesCorrectly() {
		assertEquals(0, model.getCountOfSameValueInColumn(new String("123"), 0));
		model.setValueAt("123", 0, 0);
		assertEquals(1, model.getCountOfSameValueInColumn(new String("123"), 0));
		model.setValueAt("1234", 0, 0);
		assertEquals(0, model.getCountOfSameValueInColumn(new String("123"), 0));
		assertEquals(1, model.getCountOfSameValueInColumn(new String("1234"), 0));
		model.setValueAt("1234", 1, 0);
		assertEquals(2, model.getCountOfSameValueInColumn(new String("1234"), 0));
		model.setValueAt("", 0, 0);
		model.setValueAt("", 1, 0);
		assertEquals(0, model.getCountOfSameValueInColumn(new String(""), 0));
	}
	public void testShouldCountColValuesCorrectlyAfterEdit() {
		model.setValueAt("1234", 0, 0);
		model.setValueAt("1234", 1, 0);
		
		assertEquals(2, model.getCountOfSameValueInColumn(new String("1234"), 0));
		model.setEditable(false);
		assertEquals(2, model.getCountOfSameValueInColumn(new String("1234"), 0));
	}
	
	public void testShouldSetEditableCorrectly() {
		assertTrue(model.isCellEditable(0, 0));
		assertTrue(model.isCellEditable(0, 1));
		assertTrue(model.isCellEditable(0, 2));
		assertTrue(model.isCellEditable(0, 3));
		assertTrue(model.isCellEditable(0, 4));
		assertTrue(model.isCellEditable(0, 5));
		assertFalse(model.isCellEditable(0, 6));
	}

	public void testShouldCalculateNoOfTicksCorrectly() {
		assertEquals(0, model.noOfCheckboxesTicked(0));
		model.setValueAt(Boolean.TRUE, 0, 1);
		assertEquals(1, model.noOfCheckboxesTicked(0));
		model.setValueAt(Boolean.TRUE, 0, 2);
		model.setValueAt(Boolean.TRUE, 0, 3);
		assertEquals(3, model.noOfCheckboxesTicked(0));
		model.setValueAt(Boolean.FALSE, 0, 2);
		assertEquals(2, model.noOfCheckboxesTicked(0));
	}
	
	public void testShouldReturnShouldBeRedCorrectly() {
		assertEquals(false,model.isRed(Boolean.TRUE,0,0));
		assertEquals(true,model.isRed(Boolean.TRUE,0,1));
		assertEquals(true,model.isRed(Boolean.TRUE,0,2));
		assertEquals(true,model.isRed(Boolean.TRUE,0,3));
		assertEquals(true,model.isRed(Boolean.TRUE,0,4));
		assertEquals(true,model.isRed(Boolean.TRUE,0,5));
		assertEquals(false,model.isRed(Boolean.TRUE,0,6));
		assertEquals(true,model.isRed(Boolean.TRUE,0,7));
	}
	
//	public void testShouldCalculateRedBooleanColsCorrectly() {
//		assertFalse(model.isOneCheckboxTicked(0));
//		model.setValueAt(Boolean.TRUE, 0, 2);
//		assertTrue(model.isOneCheckboxTicked(0));
//		model.setValueAt(Boolean.TRUE, 0, 3);
//		assertFalse(model.isOneCheckboxTicked(0));
//		model.setValueAt(Boolean.FALSE, 0, 2);
//		assertTrue(model.isOneCheckboxTicked(0));
//	}

	private Object[] getEmptyRow() {
		Object[] newRow = { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
		return newRow;
	}
	
}
