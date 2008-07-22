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
		// xlsFile.delete();
	}

	public void testSouldSaveAndLoadFromCSVCorrectly() throws IOException {
		PlannerDAO dao = new PlannerDAOExcelImpl();

		BoardTableModel model_original = getTestModel();
		dao.saveBoardModel(xlsFile, model_original);

		BoardTableModel model_loaded = dao.loadBoardModel(xlsFile);

		assertEquals(1, model_loaded.getRowCount());
		assertEquals(7, model_loaded.getColumnCount());
		assertHeaderInModel(model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" }, 0);
		assertRowInModel(model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" }, 0);
		
		//Test to amend and save
		model_loaded.addRow(new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
		xlsFile.delete();
		
		dao.saveBoardModel(xlsFile, model_loaded);
		
		model_loaded = dao.loadBoardModel(xlsFile);
		
		assertEquals(2, model_loaded.getRowCount());
		assertEquals(7, model_loaded.getColumnCount());
		assertRowInModel(model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" }, 0);
		assertRowInModel(model_loaded, new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" }, 1);
	}

	private void assertHeaderInModel(BoardTableModel model_loaded, Object[] objects, int i) {
	}

	private void assertRowInModel(BoardTableModel model_loaded, Object[] expected, int row) {
		for (int j = 0; j < expected.length; j++) {
			assertEquals(expected[j], model_loaded.getValueAt(row, j));
		}
	}

	private BoardTableModel getTestModel() {
		BoardTableModel boardTableModel = new BoardTableModel();
		return boardTableModel;
	}
}
