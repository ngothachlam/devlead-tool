package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import com.jonas.agile.devleadtool.component.table.BoardTableModel;

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
		BoardTableModel model = new BoardTableModel();
		dao.saveModel(model);
		System.out.println(xlsFile.getAbsolutePath());
	}

	public void testSouldLoadFromCSVCorrectly() throws IOException {
		PlannerDAO dao = new PlannerDAOExcelImpl(xlsFile);

		System.out.println(xlsFile.getAbsolutePath());

		assertTrue(false);
	}

}
