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
import com.google.inject.Inject;
import com.jonas.agile.devleadtool.gui.component.dialog.CombinedModelDTO;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.TableModelDTO;
import com.jonas.agile.devleadtool.gui.listener.DaoListener;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;

public class PlannerDAOExcelImpl implements PlannerDAO {

   private static Map<File, HSSFWorkbook> fileOrganiser = new HashMap<File, HSSFWorkbook>();
   private List<DaoListener> listeners = new ArrayList<DaoListener>();
   private int loadingNow = 0;
   private Logger log = MyLogger.getLogger(PlannerDAOExcelImpl.class);
   private int savesNow = 0;
   private final ExcelSprintDao sprintDao;
   private SprintCache sprintCache;

   @Inject
   public PlannerDAOExcelImpl(ExcelSprintDao sprintDao) {
      super();
      // FIXME 1 who calls this constructor? Guice?
      this.sprintDao = sprintDao;
   }

   private void addCellValue(Map<Integer, Column> columns, Vector<Object> rowData, int colCount, Object cellContents) {
      Column column = columns.get(colCount);
      log.debug("\tColumn " + column + " (from col " + colCount + ") should" + (!column.isToLoad() ? " not " : " ") + "be loaded with \""
            + cellContents + "\"!");
      Object parsed = null;
      if (column.isToLoad()) {
         if (column.isUsingCache()) {
            parsed = column.parseFromPersistanceStore(cellContents, sprintCache);
         } else {
            parsed = column.parseFromPersistanceStore(cellContents);
         }
         rowData.add(parsed);
      }
   }

   @Override
   public void addListener(DaoListener daoListener) {
      log.debug("adding Listener");
      listeners.add(daoListener);
   }

   public static HSSFSheet getSheet(String sheetName, HSSFWorkbook wb, boolean deleteRows) {
      HSSFSheet sheet = wb.getSheet(sheetName);
      
      if(deleteRows && sheet != null){
         int index = wb.getSheetIndex(sheetName);
         wb.removeSheetAt(index);
         sheet = null;
      }
      
      return sheet == null ? wb.createSheet(sheetName) : sheet;
   }

   TableModelDTO loadModel(File xlsFile, String sheetName) throws IOException, PersistanceException {
      log.debug("Loading Model from " + xlsFile.getAbsolutePath() + " and sheet: " + sheetName);
      notifyListeners(DaoListenerEvent.LoadingModelStarted, "Loading " + sheetName + " Started");

      InputStream inp = null;

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();
      TableModelDTO dataModelDTO = new TableModelDTO(header, contents);
      try {
         int rowCount = -1;

         inp = new FileInputStream(xlsFile);
         POIFSFileSystem fileSystem = new POIFSFileSystem(inp);
         HSSFWorkbook wb = new HSSFWorkbook(fileSystem);

         HSSFSheet sheet = getSheet(sheetName, wb, false);
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
               HSSFCell cell = cit.next();
               int cellType = cell.getCellType();
               log.debug("Read cell value \"" + cell + "\" of type " + cellType + " at row " + rowCount + ", column " + (++colCount));
               Object cellContents = null;
               switch (cellType) {
               case HSSFCell.CELL_TYPE_BLANK:
                  cellContents = "";
               case HSSFCell.CELL_TYPE_BOOLEAN:
                  cellContents = cell.getBooleanCellValue();
                  break;
               case HSSFCell.CELL_TYPE_NUMERIC:
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
               addRowData(dataModelDTO, rowData);
         }
      } finally {
         if (inp != null)
            inp.close();
      }
      return dataModelDTO;
   }

   private void addRowData(TableModelDTO dataModelDTO, Vector<Object> rowData) {
      Vector<Vector<Object>> contents = dataModelDTO.getContents();
      contents.add(rowData);
   }

   @Override
   public CombinedModelDTO loadAllData(File file) throws IOException, PersistanceException {
      notifyLoadingStarted();
      BoardTableModel boardModel = null;
      JiraTableModel jiraModel = null;
      sprintCache = null;
      try {
         sprintCache = sprintDao.load(file);
         TableModelDTO dto = loadModel(file, "board");
         boardModel = new BoardTableModel(dto.getContents(), dto.getHeader(), sprintCache);
         dto = loadModel(file, "jira");
         jiraModel = new JiraTableModel(dto.getContents(), dto.getHeader());
      } catch (IOException e) {
         notifyListeners(DaoListenerEvent.LoadingErrored, "Exception occured! " + e.getMessage());
         e.printStackTrace();
         throw e;
      } catch (PersistanceException e) {
         notifyListeners(DaoListenerEvent.LoadingErrored, "Exception occured! " + e.getMessage());
         e.printStackTrace();
         throw e;
      } catch (RuntimeException e) {
         notifyListeners(DaoListenerEvent.LoadingErrored, "Exception occured! " + e.getMessage());
         e.printStackTrace();
         throw e;
      }
      notifyLoadingFinished();
      return new CombinedModelDTO(boardModel, jiraModel, sprintCache);
   }

   public void notifyListeners(DaoListenerEvent event, String message) {
      for (DaoListener listener : listeners) {
         listener.notify(event, message);
      }
   }

   private void notifyLoadingFinished() {
      notifyLoadingFinished("Loading Finished!");
   }

   private void notifyLoadingFinished(String message) {
      if (--loadingNow == 00) {
         notifyListeners(DaoListenerEvent.LoadingFinished, message);
      }
   }

   private void notifyLoadingStarted() {
      if (loadingNow++ == 0) {
         notifyListeners(DaoListenerEvent.LoadingStarted, "Loading Started!");
      }
   }

   private void notifySavingFinished(final String message) {
      if (--savesNow == 0) {
         notifyListeners(DaoListenerEvent.SavingFinished, message);
      }
   }

   private void notifySavingStarted() {
      if (savesNow++ == 0) {
         notifyListeners(DaoListenerEvent.SavingStarted, "Saving Started!");
      }
   }

   @Override
   public void removeListener(DaoListener daoListener) {
      listeners.remove(daoListener);
   }

   private void saveModel(File xlsFile, MyTableModel model, String sheetName) throws IOException {
      notifyListeners(DaoListenerEvent.SavingModelStarted, "Saving " + sheetName + " Started");
      FileOutputStream fileOut = null;
      FileInputStream fileIn = null;
      try {
         log.debug("Saving to " + xlsFile.getAbsolutePath());
         fileIn = new FileInputStream(xlsFile);
         HSSFWorkbook wb = new HSSFWorkbook(fileIn);
         if (fileIn != null)
            fileIn.close();

         HSSFSheet sheet = getSheet(sheetName, wb, true);

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

               Column column = model.getColumn(colCount);
               valueAt = column.parseToPersistanceStore(valueAt);

               if (valueAt == null)
                  cell.setCellValue(new HSSFRichTextString(""));
               else if (valueAt instanceof Boolean) {
                  cell.setCellValue(((Boolean) valueAt).booleanValue());
               } else if (valueAt instanceof Double) {
                  cell.setCellValue(((Double) valueAt).doubleValue());
               } else if (valueAt instanceof Float) {
                  cell.setCellValue(((Float) valueAt).floatValue());
               } else {
                  cell.setCellValue(new HSSFRichTextString(valueAt.toString()));
               }
            }
         }

         fileOut = new FileOutputStream(xlsFile);
         // FIXME if the excel sheet is already open - this throws FileNotFoundException and thus fails
         wb.write(fileOut);
      } finally {
         if (fileOut != null)
            fileOut.close();
      }
   }

   @Override
   public void saveAllData(File file, MyTableModel boardModel, MyTableModel jiraModel, SprintCache sprintCache) throws IOException {
      notifySavingStarted();
      fileOrganiser.clear();
      saveModel(file, boardModel, "board");
      saveModel(file, jiraModel, "jira");
      sprintDao.save(file, sprintCache);
      // FIXME 1 save sprint cache here to sprint sheet!
      notifySavingFinished("Saving Finished!");
   }

   private void setValue(TableModelDTO dataModelDTO, int rowCount, Map<Integer, Column> columns, Vector<Object> rowData, int colCount,
         Object cellContents) throws PersistanceException {
      if (rowCount == 0) {
         log.debug("\tHeader!");
         Column column = Column.getEnum(cellContents);
         if (column == null) {
            throw new PersistanceException("Found column " + cellContents
                  + " in file, but there is no such column representation. Update it to one of " + getStringOfColumns());
         }
         columns.put(colCount, column);
         if (column.isToLoad())
            dataModelDTO.getHeader().add(column);
      } else {
         addCellValue(columns, rowData, colCount, cellContents);
      }
   }

   private String getStringOfColumns() {
      Column[] values = Column.values();
      StringBuffer sb = new StringBuffer("(");
      if (values.length > 0) {
         sb.append(values[0]);
      }
      for (int i = 1; i < values.length; i++) {
         Column column = values[i];
         sb.append(",").append(column);
      }
      sb.append(")");
      return sb.toString();
   }
}
