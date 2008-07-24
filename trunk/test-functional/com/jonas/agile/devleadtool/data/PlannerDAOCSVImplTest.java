package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;

public class PlannerDAOCSVImplTest extends TestCase {
	File xlsFile = new File("bin\\test.xls");

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSouldSaveAndLoadBoardFromCSVCorrectly() throws IOException {
		PlannerDAO dao = new PlannerDAOExcelImpl();
		BoardTableModel model_original = new BoardTableModel();

		// Save and Load on new file
		dao.saveBoardModel(xlsFile, model_original);
		BoardTableModel model_loaded = dao.loadBoardModel(xlsFile);

		assertEquals(1, model_loaded.getRowCount());
		assertEquals(7, model_loaded.getColumnCount());
		assertHeaderInModel(model_loaded, new Object[] { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete", "URL" });
		assertRowInModel(0, model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" });

		// Modify,  
		model_loaded.addRow(new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
		model_loaded.addRow(new Object[] { "1234", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "LLU-1234" });
		
		//Save and Load on the existing file
		dao.saveBoardModel(xlsFile, model_loaded);
		model_loaded = dao.loadBoardModel(xlsFile);

		assertEquals(3, model_loaded.getRowCount());
		assertEquals(7, model_loaded.getColumnCount());
		assertHeaderInModel(model_loaded, new Object[] { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete", "URL" });
		assertRowInModel(0, model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" });
		assertRowInModel(1, model_loaded, new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
		assertRowInModel(2, model_loaded, new Object[] { "1234", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "LLU-1234" });
	}

	public void testSouldSaveAndLoadPlanFromCSVCorrectly() throws IOException {
		PlannerDAO dao = new PlannerDAOExcelImpl();
		assertTrue(false);
	}

	public void testSouldSaveAndLoadJiraFromCSVCorrectly() throws IOException {
		PlannerDAO dao = new PlannerDAOExcelImpl();
		assertTrue(false);
	}

	private void assertHeaderInModel(BoardTableModel model_loaded, Object[] expected) {
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], model_loaded.getColumnName(j));
		}
	}

	private void assertRowInModel(int row, BoardTableModel model_loaded, Object[] expected) {
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], model_loaded.getValueAt(row, j));
		}
	}
}
