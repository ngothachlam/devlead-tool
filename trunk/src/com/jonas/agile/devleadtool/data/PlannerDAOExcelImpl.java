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
import com.jonas.agile.devleadtool.component.listener.DaoListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.component.table.model.TableModelBuilder;
import com.jonas.agile.devleadtool.component.table.model.TableModelDTO;
import com.jonas.common.logging.MyLogger;

public class PlannerDAOExcelImpl implements PlannerDAO {

   private static Map<File, HSSFWorkbook> fileOrganiser = new HashMap<File, HSSFWorkbook>();
   private Logger log = MyLogger.getLogger(PlannerDAOExcelImpl.class);
   private TableModelBuilder modelBuilder;
   private File xlsFile;
   private List<DaoListener> listeners = new ArrayList<DaoListener>();

   public PlannerDAOExcelImpl(TableModelBuilder modelBuilder) {
      super();
      this.modelBuilder = modelBuilder;
   }

   public File getXlsFile() {
      return xlsFile;
   }

   public BoardTableModel loadBoardModel() throws IOException {
      notifyListeners(DaoListenerEvent.LoadingModelStarted, "Loading Board Started");
      TableModelDTO dto = loadModel(xlsFile, "board");
      return new BoardTableModel(dto.getContents(), dto.getHeader());
   }

   public JiraTableModel loadJiraModel() throws IOException {
      notifyListeners(DaoListenerEvent.LoadingModelStarted, "Loading Jira Started");
      TableModelDTO dto = loadModel(xlsFile, "jira");
      return new JiraTableModel(dto.getContents(), dto.getHeader());
   }

   public PlanTableModel loadPlanModel() throws IOException {
      notifyListeners(DaoListenerEvent.LoadingModelStarted, "Loading Plan Started");
      TableModelDTO dto = loadModel(xlsFile, "plan");
      return modelBuilder.buildPlanTableModel(dto);
   }

   public void saveBoardModel(MyTableModel model) throws IOException {
      saveModel(xlsFile, model, "board");
   }

   public void saveJiraModel(MyTableModel model) throws IOException {
      saveModel(xlsFile, model, "jira");
   }

   public void savePlanModel(MyTableModel model) throws IOException {
      saveModel(xlsFile, model, "plan");
   }

   public void setXlsFile(File xlsFile) {
      this.xlsFile = xlsFile;
   }

   private Column getHeaderMappingToColumn(String tempString) {
      return Column.getEnum(tempString);
   }

   private void saveModel(File xlsFile, MyTableModel model, String sheetName) throws IOException {
      notifyListeners(DaoListenerEvent.SavingModelStarted, "Saving " + sheetName + " Started");
      FileOutputStream fileOut = null;
      try {
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
            switch (cellType) {
            case HSSFCell.CELL_TYPE_BLANK:
            case HSSFCell.CELL_TYPE_BOOLEAN:
               rowData.add(Boolean.valueOf(cell.getBooleanCellValue()));
               break;
            case HSSFCell.CELL_TYPE_NUMERIC:
               rowData.add(Double.valueOf(cell.getNumericCellValue()));
               break;
            case HSSFCell.CELL_TYPE_STRING:
               HSSFRichTextString cellHeader = cell.getRichStringCellValue();
               String cellContents = (cellHeader == null ? "" : cellHeader.getString());
               if (rowCount == 0) {
                  log.debug("\tHeader!");
                  Column column = Column.getEnum(cellContents);
                  if (column == null) {
                     throw new NullPointerException("Failed to add column (" + cellContents + ") to " + colCount + " of type " + column + " (size is "
                           + columns.size() + ")");
                  }
                  columns.put(colCount, column);
                  if (column.isToLoad())
                     dataModelDTO.getHeader().add(getHeaderMappingToColumn(cellContents));
               } else {
                  addCellValue(columns, rowData, colCount, cellContents);
               }
               break;
            default:
               break;
            }
         }
         if (rowCount != 0)
            dataModelDTO.getContents().add(rowData);
      }
      return dataModelDTO;
   }

   private void addCellValue(Map<Integer, Column> columns, Vector<Object> rowData, int colCount, String cellContents) {
      Column column = columns.get(colCount);
      log.debug("\tColumn " + column + " (from col " + colCount + ") should " + (!column.isToLoad() ? " not " : "") + " be loaded!");
      if (column.isToLoad()) {
         rowData.add(column.parse(cellContents));
      }
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

   public void notifyLoadingFinished() {
      log.debug("notifyLoadingFinished");
      notifyListeners(DaoListenerEvent.LoadingFinished, "Loading Finished!");
   }

   public void notifyLoadingStarted() {
      log.debug("notifyLoadingStarted");
      notifyListeners(DaoListenerEvent.LoadingStarted, "Loading Started!");
   }

   @Override
   public void notifySavingFinished() {
      log.debug("notifySavingFinished");
      notifyListeners(DaoListenerEvent.SavingFinished, "Saving Finished!");
   }

   @Override
   public void notifySavingStarted() {
      log.debug("notifySavingStarted");
      notifyListeners(DaoListenerEvent.SavingStarted, "Saving Started!");
   }
}
