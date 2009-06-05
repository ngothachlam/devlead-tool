package com.jonas.stats.charts.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.jonas.agile.devleadtool.burndown.ContentsDto;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public class ExcelDao {

   public ContentsDto loadContents(File xlsFile, String excelSheet) throws IOException {
      InputStream inp = null;
      try {
         Vector<String> readInHeader = new Vector<String>();
         Vector<Vector<Object>> readInData = new Vector<Vector<Object>>();

         inp = new FileInputStream(xlsFile);
         POIFSFileSystem fileSystem = new POIFSFileSystem(inp);
         HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
         HSSFSheet sheet = PlannerDAOExcelImpl.getSheet(excelSheet, wb, false);

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

   private Vector<String> getHeaderRow(HSSFRow row) {
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
            default:
               throw new RuntimeException("This is not handled! it is of type: " + cellType);
         }
         readInRow.add(cellContents);
      }
      return readInRow;
   }

   private Vector<Object> getRow(HSSFRow row) {
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
               cellContents = String.valueOf(cell.getRichStringCellValue());
               break;
            case HSSFCell.CELL_TYPE_STRING:
               HSSFRichTextString cellValue = cell.getRichStringCellValue();
               cellContents = (cellValue == null ? "" : cellValue.getString());
               break;
            default:
               throw new RuntimeException("This is not handled! it is of type: " + cellType);
         }
         System.out.print("\""+cellContents+"\" ");
         readInRow.add(cellContents);
      }
      System.out.println();
      return readInRow;
   }

   private boolean isHeaderRow(int rowCount) {
      return rowCount == 0;
   }

}
