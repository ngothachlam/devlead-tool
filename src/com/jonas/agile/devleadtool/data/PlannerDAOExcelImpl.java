package com.jonas.agile.devleadtool.data;

import java.awt.Color;
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
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.google.inject.Inject;
import com.jonas.agile.devleadtool.gui.component.dialog.CombinedModelDTO;
import com.jonas.agile.devleadtool.gui.component.table.ColorDTO;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.ColumnWrapper;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.TableModelDTO;
import com.jonas.agile.devleadtool.gui.listener.DaoListener;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;

public class PlannerDAOExcelImpl implements PlannerDAO {

   private List<DaoListener> listeners = new ArrayList<DaoListener>();
   private Logger log = MyLogger.getLogger(PlannerDAOExcelImpl.class);
   private final ExcelSprintDao sprintDao;
   private SprintCache sprintCache;

   @Inject
   public PlannerDAOExcelImpl(ExcelSprintDao sprintDao) {
      super();
      this.sprintDao = sprintDao;
   }

   private void addCellValue(Map<Integer, ColumnWrapper> columns, Vector<Object> rowData, int colCount, Object cellContents) {
      ColumnWrapper column = columns.get(colCount);
      if (log.isDebugEnabled())
         log.debug("\t\tColumn " + column.getType() + " (from col " + colCount + ") should" + (!column.isToLoad() ? " not " : " ") + "be loaded with \"" + cellContents + "\"!");
      Object parsed = null;
      if (column.isToLoad()) {
         if (column.useCacheMethod()) {
            parsed = column.parseFromPersistanceStore(cellContents, sprintCache);
         } else {
            parsed = column.parseFromPersistanceStore(cellContents);
         }
         if (parsed == null && log.isDebugEnabled()) {
            log.warn("\tWhen trying to parse column " + column + " from store using value \"" + cellContents + "\" - we got null! (Cache was " + (column.useCacheMethod() ? "" : "not") + " used)");
         }
         rowData.add(parsed);
      } else {
         rowData.add(column.getDefaultValue());
      }
   }

   @Override
   public void addListener(DaoListener daoListener) {
      log.debug("adding Listener");
      listeners.add(daoListener);
   }

   public static HSSFSheet getSheet(String sheetName, HSSFWorkbook wb, boolean deleteRows) {
      HSSFSheet sheet = wb.getSheet(sheetName);

      if (deleteRows && sheet != null) {
         int index = wb.getSheetIndex(sheetName);
         wb.removeSheetAt(index);
         sheet = null;
      }

      return sheet == null ? wb.createSheet(sheetName) : sheet;
   }

   public TableModelDTO loadModel(File xlsFile, String sheetName) throws IOException, PersistanceException {
      if (log.isDebugEnabled())
         log.debug("Loading Model from " + xlsFile.getAbsolutePath() + " and sheet: " + sheetName);
      InputStream inp = null;

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<ColumnType> header = new Vector<ColumnType>();
      TableModelDTO dataModelDTO = new TableModelDTO(header, contents);
      try {
         int rowCount = -1;

         inp = new FileInputStream(xlsFile);
         POIFSFileSystem fileSystem = new POIFSFileSystem(inp);
         HSSFWorkbook wb = new HSSFWorkbook(fileSystem);

         HSSFSheet sheet = getSheet(sheetName, wb, false);
         Map<Integer, ColumnWrapper> columns = new HashMap<Integer, ColumnWrapper>();
         // for each row in the sheet...
         for (Iterator<HSSFRow> rit = sheet.rowIterator(); rit.hasNext();) {
            Vector<Object> rowData = new Vector<Object>();
            rowCount++;
            HSSFRow row = rit.next();
            if (log.isDebugEnabled())
               log.debug("Loading row " + rowCount);
            // for each column in the row...
            for (Iterator<HSSFCell> cit = row.cellIterator(); cit.hasNext();) {
               HSSFCell cell = cit.next();
               int cellType = cell.getCellType();
               if (log.isDebugEnabled())
                  log.debug("Read cell value \"" + cell + "\" of type " + cellType + " at row " + rowCount + ", column " + cell.getColumnIndex());
               Object cellContents = null;
               switch (cellType) {
                  case HSSFCell.CELL_TYPE_BLANK:
                     ColumnWrapper wrapper = columns.get(cell.getColumnIndex());
                     cellContents = wrapper.getDefaultValue();
                     if (log.isDebugEnabled()) {
                        log.debug("\tcell is blank! I would like to read in " + wrapper.getType() + "'s default value (\"" + cellContents + "\")!");
                     }
                     break;
                  case HSSFCell.CELL_TYPE_BOOLEAN:
                     cellContents = cell.getBooleanCellValue();
                     break;
                  case HSSFCell.CELL_TYPE_NUMERIC:
                     String valueOf = String.valueOf(cell.getNumericCellValue());
                     cellContents = valueOf == null ? "" : valueOf;
                     break;
                  case HSSFCell.CELL_TYPE_STRING:
                     HSSFRichTextString cellHeader = cell.getRichStringCellValue();
                     cellContents = cellHeader == null ? "" : cellHeader.getString();
                     break;
               }
               setValue(dataModelDTO, rowCount, columns, rowData, cell.getColumnIndex(), cellContents);
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

      for (int remainingCol = rowData.size(); remainingCol < dataModelDTO.getHeader().size(); remainingCol++) {
         ColumnType header = dataModelDTO.getHeader().get(remainingCol);
         Object defaultValue = ColumnWrapper.get(header).getDefaultValue();
         if (log.isDebugEnabled())
            log.debug(" header " + header + " at position " + remainingCol + " was found emtpy in rowdata so we need to add default value of \"" + defaultValue + "\"");
         rowData.add(defaultValue);
      }

      contents.add(rowData);
   }

   @Override
   public CombinedModelDTO loadAllData(File file) throws IOException, PersistanceException {
      notifyListeners(DaoListenerEvent.LoadingStarted);
      BoardTableModel boardModel = null;
      JiraTableModel jiraModel = null;
      sprintCache = null;
      try {
         sprintCache = sprintDao.load(file);
         TableModelDTO dto = loadModel(file, "board");
         boardModel = new BoardTableModel(dto.getContents(), dto.getHeader(), sprintCache);
         dto = loadModel(file, "jira");
         jiraModel = new JiraTableModel(dto.getContents(), dto.getHeader());
         notifyListeners(DaoListenerEvent.LoadingFinished);
         return new CombinedModelDTO(boardModel, jiraModel, sprintCache);
      } catch (IOException e) {
         notifyListeners(DaoListenerEvent.LoadingErrored);
         e.printStackTrace();
         throw e;
      } catch (PersistanceException e) {
         notifyListeners(DaoListenerEvent.LoadingErrored);
         e.printStackTrace();
         throw e;
      } catch (RuntimeException e) {
         notifyListeners(DaoListenerEvent.LoadingErrored);
         e.printStackTrace();
         throw e;
      }
   }

   public void notifyListeners(DaoListenerEvent event) {
      for (DaoListener listener : listeners) {
         listener.notify(event);
      }
   }

   @Override
   public void removeListener(DaoListener daoListener) {
      listeners.remove(daoListener);
   }

   private void saveModel(File xlsFile, MyTableModel model, String sheetName) throws IOException {
      FileOutputStream fileOut = null;
      FileInputStream fileIn = null;
      try {
         if (log.isDebugEnabled())
            log.debug("Saving to " + xlsFile.getAbsolutePath());
         HSSFWorkbook wb = null;
         if (xlsFile.exists()) {
            fileIn = new FileInputStream(xlsFile);
            try {
               wb = new HSSFWorkbook(fileIn);
            } catch (IOException e) {
               wb = new HSSFWorkbook();
            }
         } else {
            wb = new HSSFWorkbook();
         }

         if (fileIn != null)
            fileIn.close();

         HSSFSheet sheet = getSheet(sheetName, wb, true);

         // save column Headers
         HSSFRow row = sheet.createRow((short) 0);
         for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
            HSSFCell cell = row.createCell(colCount);
            Object valueAt = model.getColumnName(colCount);
            cell.setCellValue(new HSSFRichTextString((String) valueAt));
         }

         // save all data rows...
         for (int rowCount = 0; rowCount < model.getRowCount(); rowCount++) {
            row = sheet.createRow(rowCount + 1);
            for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
               HSSFCell cell = row.createCell(colCount);
               Object valueAt = model.getValueAt(rowCount, colCount);
               if (log.isDebugEnabled())
                  log.debug(" saving value \"" + valueAt + "\" at row " + rowCount + " and column " + colCount);

               ColumnWrapper column = model.getColumnWrapper(colCount);

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
               setCellStyle(wb, model, rowCount, colCount, valueAt, cell);
               valueAt = column.parseToPersistanceStore(valueAt);
            }
         }

         for (short colCount = 0; colCount < model.getColumnCount(); colCount++) {
            ColumnWrapper wrapper = model.getColumnWrapper(colCount);
            if (log.isDebugEnabled())
               log.debug("Column " + wrapper + " is " + (!wrapper.isToAutoResize() ? "not" : "") + " to be autoresized");
            if (wrapper.isToAutoResize()) {
               sheet.autoSizeColumn(colCount);
            }
         }

         fileOut = new FileOutputStream(xlsFile);
         // FIXME if the excel sheet is already open - this throws
         // FileNotFoundException and thus fails
         // wb.pack();
         wb.write(fileOut);
      } finally {
         if (fileOut != null)
            fileOut.close();
      }
   }

   private void setCellStyle(HSSFWorkbook wb, MyTableModel model, int rowCount, int colCount, Object valueAt, HSSFCell cell) {
      ColorDTO modelColor = model.getColor(valueAt, rowCount, colCount, true);

      Color acolor = modelColor.getColor();

      if (acolor != null) {
         HSSFPalette palette = wb.getCustomPalette();
         byte red = (byte) acolor.getRed();
         byte green = (byte) acolor.getGreen();
         byte blue = (byte) acolor.getBlue();

         if (log.isDebugEnabled())
            log.debug("is getting from palette" + palette + " red: " + red + " green: " + green + " blue: " + blue);

         HSSFColor color = palette.findColor(red, green, blue);
         if (color == null) {

            Short hssfColor = SwingUtil.getFreeHSSFColor();

            palette.setColorAtIndex(hssfColor, red, green, blue);
            color = palette.getColor(hssfColor);
         }

         HSSFCellStyle style = null;

         for (short counter = 0; counter < wb.getNumCellStyles(); counter++) {
            HSSFCellStyle tempStyle = wb.getCellStyleAt(counter);
            if (tempStyle.getFillForegroundColor() == color.getIndex()) {
               style = tempStyle;
            }
         }
         if (style == null) {
            style = wb.createCellStyle();
         }

         style.setFillForegroundColor(color.getIndex());
         style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         cell.setCellStyle(style);
      }
   }

   @Override
   public void saveAllData(File file, MyTableModel boardModel, MyTableModel jiraModel, SprintCache sprintCache) throws IOException {
      notifyListeners(DaoListenerEvent.SavingStarted);
      try {
         saveModel(file, boardModel, "board");
         saveModel(file, jiraModel, "jira");
         sprintDao.save(file, sprintCache);
         notifyListeners(DaoListenerEvent.SavingFinished);
      } catch (IOException e) {
         notifyListeners(DaoListenerEvent.SavingErrored);
         throw e;
      } catch (RuntimeException e) {
         notifyListeners(DaoListenerEvent.LoadingErrored);
         e.printStackTrace();
         throw e;
      } finally {
         SwingUtil.resetFreeHSSFColors();
      }
   }

   private void setValue(TableModelDTO dataModelDTO, int rowCount, Map<Integer, ColumnWrapper> columns, Vector<Object> rowData, int colCount, Object cellContents) throws PersistanceException {
      if (rowCount == 0) {
         setValueOnHeader(dataModelDTO, columns, colCount, cellContents);
      } else {
         setValueOnBody(dataModelDTO, columns, rowData, colCount, cellContents);
      }
   }

   private void setValueOnBody(TableModelDTO dataModelDTO, Map<Integer, ColumnWrapper> columns, Vector<Object> rowData, int colCount, Object cellContents) {
      if (log.isDebugEnabled())
         log.debug("\tsize of rowData is " + rowData.size() + " and the current colCount is " + colCount);
      for (int missingCol = rowData.size(); missingCol < colCount; missingCol++) {
         Vector<ColumnType> header = dataModelDTO.getHeader();
         ColumnType column = header.get(missingCol);
         Object defaultValue = ColumnWrapper.get(column).getDefaultValue();
         addCellValue(columns, rowData, missingCol, defaultValue);
         if (log.isDebugEnabled())
            log.debug("\tadding " + column + " with default value \"" + defaultValue + "\"");
      }
      addCellValue(columns, rowData, colCount, cellContents);
   }

   private void setValueOnHeader(TableModelDTO dataModelDTO, Map<Integer, ColumnWrapper> columns, int colCount, Object cellContents) throws PersistanceException {
      if (log.isDebugEnabled())
         log.debug("\tHeader! Trying to find the header for " + cellContents.toString());
      ColumnWrapper wrapper = ColumnWrapper.get(cellContents.toString());
      if (wrapper == null) {
         throw new PersistanceException("Found column " + cellContents + " in file, but there is no such columnWrapper representation. Update it to one of " + getStringOfColumns());
      }
      ColumnType columnType = wrapper.getType();
      if (columnType == null) {
         throw new PersistanceException(cellContents.toString() + " has a wrapper, but the wrapper does not have a columnType relating to it!");
      }
      columns.put(colCount, wrapper);
      Vector<ColumnType> header = dataModelDTO.getHeader();
      header.add(columnType);
   }

   private String getStringOfColumns() {
      ColumnType[] values = ColumnType.values();
      StringBuffer sb = new StringBuffer("(");
      if (values.length > 0) {
         sb.append(values[0]);
      }
      for (int i = 1; i < values.length; i++) {
         ColumnType column = values[i];
         sb.append(",").append(column);
      }
      sb.append(")");
      return sb.toString();
   }
}
