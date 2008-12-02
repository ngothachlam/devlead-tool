package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.jonas.agile.devleadtool.component.dialog.CombinedModelDTO;
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.TableModelDTO;
import com.jonas.common.logging.MyLogger;

public class PlannerDAOExcelImpl implements PlannerDAO {

   private static Map<File, HSSFWorkbook> fileOrganiser = new HashMap<File, HSSFWorkbook>();
   private Logger log = MyLogger.getLogger(PlannerDAOExcelImpl.class);
   private File xlsFile;
   private List<DaoListener> listeners = new ArrayList<DaoListener>();
   private int savesNow = 0;
   private int loadingNow = 0;

   public PlannerDAOExcelImpl() {
      super();
   }

   public File getXlsFile() {
      return xlsFile;
   }

   @Override
   public CombinedModelDTO loadModels() throws IOException {
      notifyLoadingStarted();
      TableModelDTO dto = loadModel(xlsFile, "board");
      BoardTableModel boardModel = new BoardTableModel(dto.getContents(), dto.getHeader());
      dto = loadModel(xlsFile, "jira");
      JiraTableModel jiraModel = new JiraTableModel(dto.getContents(), dto.getHeader());
      notifyLoadingFinished();
      return new CombinedModelDTO(boardModel, jiraModel);
   }

   @Override
   public void saveModels(MyTableModel boardModel, MyTableModel jiraModel) throws IOException {
      notifySavingStarted();
      saveModel(xlsFile, boardModel, "board");
      saveModel(xlsFile, jiraModel, "jira");
      notifySavingFinished();
   }

   @Override
   public void setXlsFile(File xlsFile) {
      this.xlsFile = xlsFile;
   }

   private void saveModel(File xlsFile, MyTableModel model, String sheetName) throws IOException {
      notifyListeners(DaoListenerEvent.SavingModelStarted, "Saving " + sheetName + " Started");
      FileOutputStream fileOut = null;
      try {
         log.debug("Saving to " + xlsFile.getAbsolutePath());
         HSSFWorkbook wb = getWorkBook(xlsFile);
         HSSFSheet sheet = getSheet(sheetName, wb);

         // HSSFCellStyle style_red_background = wb.createCellStyle();
         // style_red_background.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         // style_red_background.setFillForegroundColor(new HSSFColor.RED().getIndex());

         // save column Headers
         HSSFRow row = sheet.createRow((short) 0);
         for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
            HSSFCell cell = row.createCell((short) colCount);
            Object valueAt = model.getColumnName(colCount);
            cell.setCellValue(new HSSFRichTextString((String) valueAt));
         }

         // save all data rows...
         for (int rowCount = 0; rowCount < model.getRowCount(); rowCount++) {
            row = sheet.createRow((short) rowCount + 1);
            for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
               HSSFCell cell = row.createCell((short) colCount);
               Object valueAt = model.getValueAt(rowCount, colCount);
               log.debug(" saving value \"" + valueAt + "\" at row " + rowCount + " and column " + colCount);
               if (valueAt == null)
                  cell.setCellValue(new HSSFRichTextString(""));
               else if (valueAt instanceof Boolean) {
                  cell.setCellValue(((Boolean) valueAt).booleanValue());
               } else if (valueAt instanceof Double) {
                  cell.setCellValue(((Double) valueAt).doubleValue());
               } else {
                  cell.setCellValue(new HSSFRichTextString(valueAt.toString()));
               }
            }
         }

         fileOut = new FileOutputStream(xlsFile);
         wb.write(fileOut);
      } finally {
         fileOut.close();
      }
   }

   protected HSSFSheet getSheet(String sheetName, HSSFWorkbook wb) {
      HSSFSheet sheet = wb.getSheet(sheetName);
      sheet = sheet == null ? wb.createSheet(sheetName) : sheet;
      return sheet;
   }

   protected HSSFWorkbook getWorkBook(File xlsFile) {
      HSSFWorkbook workbook = fileOrganiser.get(xlsFile);
      if (workbook == null) {
         workbook = new HSSFWorkbook();
         fileOrganiser.put(xlsFile, workbook);
      }
      return workbook;
   }

   TableModelDTO loadModel(File xlsFile, String sheetName) throws IOException {
      log.debug("Loading Model from " + xlsFile.getAbsolutePath() + " and sheet: " + sheetName);
      notifyListeners(DaoListenerEvent.LoadingModelStarted, "Loading " + sheetName + " Started");

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();
      TableModelDTO dataModelDTO = new TableModelDTO(header, contents);
      InputStream inp = new FileInputStream(xlsFile);
      HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
      int rowCount = -1;

      HSSFSheet sheet = wb.getSheet(sheetName);
      Map<Integer, Column> columns = new HashMap<Integer, Column>();
      // for each row in the sheet...
      for (Iterator<HSSFRow> rit = sheet.rowIterator(); rit.hasNext();) {
         Vector<Object> rowData = new Vector<Object>();
         rowCount++;
         HSSFRow row = rit.next();
         log.debug("Loading row " + rowCount);
         int colCount = -1;
         // for each column in the row...
         for (Iterator<HSSFCell> cit = row.cellIterator(); cit.hasNext();) {
            colCount++;
            HSSFCell cell = cit.next();
            int cellType = cell.getCellType();
            log.debug("Read cell value \"" + cell + "\" of type " + cellType + " at row " + rowCount + ", column " + colCount);
            String cellContents = null;
            switch (cellType) {
            case HSSFCell.CELL_TYPE_BLANK:
               cellContents = "";
            case HSSFCell.CELL_TYPE_BOOLEAN:
               // rowData.add(Boolean.valueOf(cell.getBooleanCellValue()));
               break;
            case HSSFCell.CELL_TYPE_NUMERIC:
//               // rowData.add(Double.valueOf(cell.getNumericCellValue()));
               String valueOf = String.valueOf(cell.getNumericCellValue());
               cellContents = (valueOf == null ? "" : valueOf);
               break;
            case HSSFCell.CELL_TYPE_STRING:
               HSSFRichTextString cellHeader = cell.getRichStringCellValue();
               cellContents = (cellHeader == null ? "" : cellHeader.getString());
               break;
            }
            setValue(dataModelDTO, rowCount, columns, rowData, colCount, cellContents);
         }
         if (rowCount != 0)
            dataModelDTO.getContents().add(rowData);
      }
      return dataModelDTO;
   }

   private void setValue(TableModelDTO dataModelDTO, int rowCount, Map<Integer, Column> columns, Vector<Object> rowData,
         int colCount, String cellContents) {
      if (rowCount == 0) {
         log.debug("\tHeader!");
         Column column = Column.getEnum(cellContents);
         if (column == null) {
            throw new NullPointerException("Failed to add column (" + cellContents + ") to " + colCount + " of type " + column + " (size is " + columns.size()
                  + ")");
         }
         columns.put(colCount, column);
         if (column.isToLoad())
            dataModelDTO.getHeader().add(column);
      } else {
         addCellValue(columns, rowData, colCount, cellContents);
      }
   }

   private void addCellValue(Map<Integer, Column> columns, Vector<Object> rowData, int colCount, String cellContents) {
      Column column = columns.get(colCount);
      // log.debug("\tColumn " + column + " (from col " + colCount + ") should " + (!column.isToLoad() ? " not " : "") + " be loaded with " + cellContents + "!");
      Object parsed = null;
      if (column.isToLoad()) {
         parsed = column.parse(cellContents);
         rowData.add(parsed);
      }
      log.debug("\tColumn " + column + " (from col " + colCount + ") should " + (!column.isToLoad() ? " not " : "") + " be loaded with " + cellContents
            + " (parsed: " + parsed + ")!");
   }

   public void notifyListeners(DaoListenerEvent event, String message) {
      for (DaoListener listener : listeners) {
         listener.notify(event, message);
      }
   }

   @Override
   public void addListener(DaoListener daoListener) {
      log.debug("adding Listener");
      listeners.add(daoListener);
   }

   @Override
   public void removeListener(DaoListener daoListener) {
      log.debug("removing Listener");
      listeners.remove(daoListener);
   }

   private void notifyLoadingFinished() {
      if (--loadingNow == 00) {
         log.debug("notifyLoadingFinished");
         notifyListeners(DaoListenerEvent.LoadingFinished, "Loading Finished!");
      }
   }

   private void notifyLoadingStarted() {
      if (loadingNow++ == 0) {
         log.debug("notifyLoadingStarted");
         notifyListeners(DaoListenerEvent.LoadingStarted, "Loading Started!");
      }
   }

   private void notifySavingFinished() {
      if (--savesNow == 0) {
         log.debug("notifySavingFinished");
         notifyListeners(DaoListenerEvent.SavingFinished, "Saving Finished!");
      }
   }

   private void notifySavingStarted() {
      if (savesNow++ == 0) {
         log.debug("notifySavingStarted");
         notifyListeners(DaoListenerEvent.SavingStarted, "Saving Started!");
      }
   }
}
