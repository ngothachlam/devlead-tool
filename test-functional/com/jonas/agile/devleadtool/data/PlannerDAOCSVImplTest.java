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
//		xlsFile.delete();
	}

	public void testSouldSaveFromCSVCorrectly() throws IOException {
		PlannerDAO dao = new PlannerDAOExcelImpl(xlsFile);
		save(dao);
		System.out.println(xlsFile.getAbsolutePath());
	}

	private void save(PlannerDAO dao) throws IOException {
		BoardTableModel model = new BoardTableModel();
		dao.saveModel(model);
	}

	public void testSouldLoadFromCSVCorrectly() throws IOException {
		PlannerDAO dao = new PlannerDAOExcelImpl(xlsFile);

		save(dao);
		
		BoardTableModel model = dao.loadModel();
		System.out.println(xlsFile.getAbsolutePath());

		assertEquals(1, model.getRowCount());
	}

}
