package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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

	public void saveModel(BoardTableModel model) {
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
			row = sheet.createRow((short) rowCount+1);
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
		try {
			FileOutputStream fileOut = new FileOutputStream(xlsFile);
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
