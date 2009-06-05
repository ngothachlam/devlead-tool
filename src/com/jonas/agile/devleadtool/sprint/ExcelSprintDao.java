package com.jonas.agile.devleadtool.sprint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.common.logging.MyLogger;

public class ExcelSprintDao {
   private static final String SPRINT_SHEETNAME = "Sprints";
   private Logger log = MyLogger.getLogger(ExcelSprintDao.class);

   public SprintCache load(File xlsFile) throws IOException {
      SprintCache sprintCache = new SprintCache();
      InputStream inp = null;
      try {
         inp = new FileInputStream(xlsFile);
         POIFSFileSystem fileSystem = new POIFSFileSystem(inp);
         HSSFWorkbook wb = new HSSFWorkbook(fileSystem);
         HSSFSheet sheet = PlannerDAOExcelImpl.getSheet(SPRINT_SHEETNAME, wb, false);
      
         // for each row in the sheet...
         int rowCount = 0;
         for (Iterator<HSSFRow> rit = sheet.rowIterator(); rit.hasNext();) {
            HSSFRow row = rit.next();
            if (rowCount != 0) {
               short colCount = 0;
               Sprint newSprint = new Sprint();
               Object cellContents = null;
               for (Iterator<HSSFCell> cit = row.cellIterator(); cit.hasNext();) {
                  HSSFCell cell = cit.next();
                  int cellType = cell.getCellType();
                  log.debug("Read cell value \"" + cell + "\" of type " + cellType + " at row " + rowCount + ", column " + colCount);
                  switch (cellType) {
                  case HSSFCell.CELL_TYPE_BLANK:
                     cellContents = "";
                     break;
                  case HSSFCell.CELL_TYPE_STRING:
                     HSSFRichTextString cellHeader = cell.getRichStringCellValue();
                     cellContents = (cellHeader == null ? "" : cellHeader.getString());
                     break;
                  default:
                     throw new RuntimeException("This is not handled! it is of type: " + cellType);
                  }
                  sprintCache.setValueAt(cellContents, newSprint, colCount);
                  colCount++;
               }
               sprintCache.cache(newSprint, true);
            }
            rowCount++;
         }
      } finally {
         if (inp != null) {
            inp.close();
         }
      }
      return sprintCache;
   }

   public void save(File xlsFile, SprintCache sprintCache) throws IOException {
      FileOutputStream fileOut = null;
      FileInputStream fileIn = null;
      try {
         fileIn = new FileInputStream(xlsFile);
         HSSFWorkbook wb = new HSSFWorkbook(fileIn);
         if (fileIn != null)
            fileIn.close();
         HSSFSheet sheet = PlannerDAOExcelImpl.getSheet(SPRINT_SHEETNAME, wb, true);

         
         int columnMaxCount = createSprintHeaders(sheet, sprintCache);
         createSprintData(sheet, columnMaxCount, sprintCache);

         // resetTheSprintFileBeforeSaving(xlsFile);
         fileOut = new FileOutputStream(xlsFile);

         // FIXME if the excel sheet is already open - this throws FileNotFoundException and thus fails
         wb.write(fileOut);
      } finally {
         if (fileOut != null)
            fileOut.close();
      }
   }

   private void createSprintData(HSSFSheet sheet, int columnMaxCount, SprintCache sprintCache) {
      Vector<Sprint> sprints = sprintCache.getSprints();
      short rowCounter = 0;
      for (Sprint sprint : sprints) {
         HSSFRow row = sheet.createRow(++rowCounter);
         for (short colCounter = 0; colCounter < columnMaxCount; colCounter++) {
            HSSFCell cell = row.createCell(colCounter);
            Object cellValue = sprintCache.getValueAt(sprint, colCounter);
            log.debug(" saving value \"" + cellValue + "\" for sprint " + sprint.getName() + " and column " + colCounter);
            if (cellValue == null) {
               cell.setCellValue(new HSSFRichTextString(""));
            } else {
               cell.setCellValue(new HSSFRichTextString(cellValue.toString()));
            }
         }
      }
   }

   private int createSprintHeaders(HSSFSheet sheet, SprintCache sprintCache) {
      // save column Headers
      HSSFRow row = sheet.createRow((short) 0);
      int columnMaxCount = sprintCache.getColumnCount();
      for (int colCounter = 0; colCounter < columnMaxCount; colCounter++) {
         HSSFCell cell = row.createCell((short) colCounter);
         Object columnTitle = sprintCache.getColumnName(colCounter);
         HSSFRichTextString richTextString = new HSSFRichTextString(columnTitle.toString());
         cell.setCellValue(richTextString);
      }
      return columnMaxCount;
   }
}
