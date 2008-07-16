package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.jonas.agile.devleadtool.component.table.BoardTableModel;

public class PlannerDAOExcelImpl implements PlannerDAO {

	private File xlsFile;

	public PlannerDAOExcelImpl(File xlsFile) throws IOException {
		super();
		this.xlsFile = xlsFile;
	}

	public BoardTableModel getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveModel(BoardTableModel model) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("board");

		HSSFRow row = sheet.createRow((short) 0);
		for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
			HSSFCell cell = row.createCell((short) colCount);
			Object valueAt = model.getColumnName(colCount);
			if (valueAt instanceof Boolean)
				cell.setCellValue(((Boolean) valueAt).booleanValue());
			if (valueAt instanceof String)
				cell.setCellValue(((String) valueAt));
		}
		// Create a row and put some cells in it. Rows are 0 based.
		for (int rowCount = 0; rowCount < model.getRowCount(); rowCount++) {
			row = sheet.createRow((short) rowCount + 1);
			// Create a cell and put a value in it.
			for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
				HSSFCell cell = row.createCell((short) colCount);
				Object valueAt = model.getValueAt(rowCount, colCount);
				if (valueAt instanceof Boolean)
					cell.setCellValue(((Boolean) valueAt).booleanValue());
				if (valueAt instanceof String)
					cell.setCellValue(((String) valueAt));
			}
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(xlsFile);
		wb.write(fileOut);
		fileOut.close();

	}

	public BoardTableModel loadModel() throws IOException {
		// TODO Auto-generated method stub
		// HSSFWorkbook wb = new HSSFWorkbook();
		// HSSFSheet sheet = wb.createSheet("board");

		InputStream inp = new FileInputStream(xlsFile);
		HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
		ExcelExtractor extractor = new ExcelExtractor(wb);

		extractor.setFormulasNotResults(true);
		extractor.setIncludeSheetNames(false);
		String text = extractor.getText();

		HSSFSheet sheet = wb.getSheet("board");
		for (Iterator rit = sheet.rowIterator(); rit.hasNext();) {
			HSSFRow row = (HSSFRow) rit.next();
			for (Iterator cit = row.cellIterator(); cit.hasNext();) {
				HSSFCell cell = (HSSFCell) cit.next();
				// Do something here
			}
		}

	}

}
