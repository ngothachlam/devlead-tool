package com.jonas.stats.charts.common.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.jonas.agile.devleadtool.burndown.ContentsDto;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class ExcelDao implements Dao {

   HSSFFormulaEvaluator formulaEvaluator;

   private File xlsFile;
   public ExcelDao(File xlsFile, String excelSheet) {
      super();
      this.xlsFile = xlsFile;
      this.excelSheet = excelSheet;
   }

   private String excelSheet;
   
   public ContentsDto loadContents() throws IOException {
      InputStream inp = null;
      try {
         Vector<String> readInHeader = new Vector<String>();
         Vector<Vector<Object>> readInData = new Vector<Vector<Object>>();

         inp = new FileInputStream(xlsFile);
         POIFSFileSystem fileSystem = new POIFSFileSystem(inp);
         HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
         HSSFSheet sheet = PlannerDAOExcelImpl.getSheet(excelSheet, wb, false);

         formulaEvaluator = new HSSFFormulaEvaluator(wb);
         
         // for each row in the sheet...
         int rowCount = 0;
         for (Iterator<HSSFRow> rowIterator = sheet.rowIterator(); rowIterator.hasNext();) {
            HSSFRow row = rowIterator.next();

            if (isHeaderRow(rowCount)) {
               readInHeader = getHeaderRow(row);
            } else {
               Vector<Object> readInRow = getRow(row);
               readInData.add(readInRow);
            }
            rowCount++;
         }
         return new ContentsDto(readInHeader, readInData);

      } finally {
         if (inp != null) {
            inp.close();
         }
      }
   }

   Vector<String> getHeaderRow(HSSFRow row) {
      String cellContents = null;
      Vector<String> readInRow = new Vector<String>();
      for (Iterator<HSSFCell> cellIterator = row.cellIterator(); cellIterator.hasNext();) {
         HSSFCell cell = cellIterator.next();
         int cellType = cell.getCellType();
         switch (cellType) {
            case HSSFCell.CELL_TYPE_STRING:
               HSSFRichTextString cellValue = cell.getRichStringCellValue();
               cellContents = (cellValue == null ? "" : cellValue.getString());
               break;
            case HSSFCell.CELL_TYPE_BLANK:
               cellContents = "";
               break;
            default:
               throw new RuntimeException("This is not handled! it is of type: " + cellType);
         }
         readInRow.add(cellContents);
      }
      return readInRow;
   }

   Vector<Object> getRow(HSSFRow row) {
      Object cellContents = null;
      Vector<Object> readInRow = new Vector<Object>();
      for (Iterator<HSSFCell> cellIterator = row.cellIterator(); cellIterator.hasNext();) {
         HSSFCell cell = cellIterator.next();
         int cellType = cell.getCellType();
         switch (cellType) {
            case HSSFCell.CELL_TYPE_NUMERIC:
               cellContents = String.valueOf(cell.getNumericCellValue());
               break;
            case HSSFCell.CELL_TYPE_FORMULA:
               HSSFFormulaEvaluator.CellValue cellFormValue = formulaEvaluator.evaluate(cell);
               cellContents = cellFormValue.formatAsString();
//               cellContents = "";
               break;
            case HSSFCell.CELL_TYPE_STRING:
               HSSFRichTextString cellValue = cell.getRichStringCellValue();
               cellContents = (cellValue == null ? "" : cellValue.getString());
               break;
            case HSSFCell.CELL_TYPE_BLANK:
               cellContents = "";
               break;
            default:
               throw new RuntimeException("This is not handled! it is of type: " + cellType);
         }
         readInRow.add(cellContents);
      }
      System.out.println(" read in " + readInRow);
      System.out.println();
      return readInRow;
   }

   boolean isHeaderRow(int rowCount) {
      return rowCount == 0;
   }

}
