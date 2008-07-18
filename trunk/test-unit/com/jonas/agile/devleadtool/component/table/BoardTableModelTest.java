package com.jonas.agile.devleadtool.component.table;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;

import junit.framework.TestCase;

public class BoardTableModelTest extends TestCase {
	
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

	public void testShouldCountColValuesCorrectly() {
		assertEquals(0, model.countOfSameValueInColumn(new String("123"), 0));
		model.setValueAt("123", 0, 0);
		assertEquals(1, model.countOfSameValueInColumn(new String("123"), 0));
		model.setValueAt("1234", 0, 0);
		assertEquals(0, model.countOfSameValueInColumn(new String("123"), 0));
		assertEquals(1, model.countOfSameValueInColumn(new String("1234"), 0));
		model.setValueAt("1234", 1, 0);
		assertEquals(2, model.countOfSameValueInColumn(new String("1234"), 0));
		model.setValueAt("", 0, 0);
		model.setValueAt("", 1, 0);
		assertEquals(0, model.countOfSameValueInColumn(new String(""), 0));
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
	
	public void testShouldCalculateRedBooleanColsNextCorrectly() {
		
		assertEquals(1,model.noOfCheckboxesTickedNext(0,1));
		model.setValueAt(Boolean.TRUE, 0, 1); // checked col 1
		assertEquals(0,model.noOfCheckboxesTickedNext(0,1));
		model.setValueAt(Boolean.FALSE, 0, 1);// none checked
		assertEquals(1,model.noOfCheckboxesTickedNext(0,1));
		model.setValueAt(Boolean.TRUE, 0, 1);// checked col 1
		assertEquals(0,model.noOfCheckboxesTickedNext(0,1));
		model.setValueAt(Boolean.TRUE, 0, 2);// checked col 1,2
		assertEquals(1,model.noOfCheckboxesTickedNext(0,1));
		assertEquals(1,model.noOfCheckboxesTickedNext(0,2));
		assertEquals(3,model.noOfCheckboxesTickedNext(0,3));
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
