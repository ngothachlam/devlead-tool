package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class PlannerDAOExcelImpl implements PlannerDAO {

	public PlannerDAOExcelImpl() {
		super();
	}

	public void saveModel(File xlsFile, MyTableModel model) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("board");

		HSSFRow row = sheet.createRow((short) 0);
		for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
			HSSFCell cell = row.createCell((short) colCount);
			Object valueAt = model.getColumnName(colCount);
			if (valueAt instanceof Boolean)
				cell.setCellValue(((Boolean) valueAt).booleanValue());
			if (valueAt instanceof String)
				cell.setCellValue(new HSSFRichTextString((String) valueAt));
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
					cell.setCellValue(new HSSFRichTextString((String) valueAt));
			}
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(xlsFile);
		wb.write(fileOut);
		fileOut.close();

	}

	public BoardTableModel loadModel(File xlsFile) throws IOException {
		// HSSFWorkbook wb = new HSSFWorkbook();
		// HSSFSheet sheet = wb.createSheet("board");
		
		InputStream inp = new FileInputStream(xlsFile);
		HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
		ExcelExtractor extractor = new ExcelExtractor(wb);

		extractor.setFormulasNotResults(true);
		extractor.setIncludeSheetNames(false);
		String text = extractor.getText();

		HSSFSheet sheet = wb.getSheet("board");
		Vector contents = new Vector();
		Vector<Object> header = new Vector<Object>();
		int rowCount = -1;
		for (Iterator rit = sheet.rowIterator(); rit.hasNext();) {
			rowCount++;
			Vector<Object> rowData = new Vector<Object>();
			HSSFRow row = (HSSFRow) rit.next();
			for (Iterator cit = row.cellIterator(); cit.hasNext();) {
				HSSFCell cell = (HSSFCell) cit.next();
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_BOOLEAN:
					if (rowCount == 0)
						header.add(new Boolean(cell.getBooleanCellValue()));
					else
						rowData.add(new Boolean(cell.getBooleanCellValue()));
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if (rowCount == 0)
						header.add(new Double(cell.getNumericCellValue()));
					else
						rowData.add(new Double(cell.getNumericCellValue()));
					break;
				case HSSFCell.CELL_TYPE_STRING:
					HSSFRichTextString string = cell.getRichStringCellValue();
					String tempString = (string == null ? new String("") : string.getString());
					if (rowCount == 0)
						header.add(tempString);
					else
						rowData.add(tempString);
					break;
				default:
					break;
				}
			}
			if (rowCount != 0)
				contents.add(rowData);
		}
		BoardTableModel model = new BoardTableModel(contents, header);
		return model;

	}

}
