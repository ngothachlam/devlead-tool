package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
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

   public PlannerDAOExcelImpl(TableModelBuilder modelBuilder) {
      super();
      this.modelBuilder = modelBuilder;
   }

   private Column getHeaderMappingToColumn(String tempString) {
      return Column.getEnum(tempString);
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

   public File getXlsFile() {
      return xlsFile;
   }

   public BoardTableModel loadBoardModel() throws IOException {
      TableModelDTO dto = loadModel(xlsFile, "board");
      return new BoardTableModel(dto.getContents(), dto.getHeader());
   }

   TableModelDTO loadModel(File xlsFile, String sheetName) throws IOException {
      log.debug("Loading Model from " + xlsFile.getAbsolutePath() + " and sheet: " + sheetName);
      InputStream inp = new FileInputStream(xlsFile);
      HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));

      HSSFSheet sheet = wb.getSheet(sheetName);
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();
      TableModelDTO dataModelDTO = new TableModelDTO(header, contents);
      int rowCount = -1;
      for (Iterator<HSSFRow> rit = sheet.rowIterator(); rit.hasNext();) {
         rowCount++;
         Vector<Object> rowData = new Vector<Object>();
         HSSFRow row = rit.next();
         log.debug("Going through rows to load!" + rowCount);
         for (Iterator<HSSFCell> cit = row.cellIterator(); cit.hasNext();) {
            HSSFCell cell = cit.next();
            int cellType = cell.getCellType();
            log.debug("Going through columns. Got column of type " + cellType);
            switch (cellType) {
            // FIXME - don't use the cell type - use the column type!!! Start by mapping sheets to TableModels to get the columns!
            case HSSFCell.CELL_TYPE_BLANK:
               rowData.add(null);
               break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
               rowData.add(Boolean.valueOf(cell.getBooleanCellValue()));
               break;
            case HSSFCell.CELL_TYPE_NUMERIC:
               rowData.add(new Double(cell.getNumericCellValue()));
               break;
            case HSSFCell.CELL_TYPE_STRING:
               HSSFRichTextString cellHeader = cell.getRichStringCellValue();
               String cellHeaderAsString = (cellHeader == null ? "" : cellHeader.getString());
               if (rowCount == 0)
                  dataModelDTO.getHeader().add(getHeaderMappingToColumn(cellHeaderAsString));
               else
                  rowData.add(cellHeaderAsString);
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

   public PlanTableModel loadPlanModel() throws IOException {
      TableModelDTO dto = loadModel(xlsFile, "plan");
      return modelBuilder.buildPlanTableModel(dto);
   }

   public void saveBoardModel(MyTableModel model) throws IOException {
      saveModel(xlsFile, model, "board");
   }

   private void saveModel(File xlsFile, MyTableModel model, String sheetName) throws IOException {
      HSSFWorkbook wb = getWorkBook(xlsFile);
      HSSFSheet sheet = getSheet(sheetName, wb);
      HSSFRow row = sheet.createRow((short) 0);

      HSSFCellStyle style_red_background = wb.createCellStyle();
      style_red_background.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      style_red_background.setFillForegroundColor(new HSSFColor.RED().getIndex());

      for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
         HSSFCell cell = row.createCell((short) colCount);
         Object valueAt = model.getColumnName(colCount);
         if (valueAt instanceof Boolean) {
            cell.setCellValue(((Boolean) valueAt).booleanValue());
         } else if (valueAt instanceof String) {
            cell.setCellValue(new HSSFRichTextString((String) valueAt));
         }
      }
      // Create a row and put some cells in it. Rows are 0 based.
      for (int rowCount = 0; rowCount < model.getRowCount(); rowCount++) {
         row = sheet.createRow((short) rowCount + 1);
         for (int colCount = 0; colCount < model.getColumnCount(); colCount++) {
            HSSFCell cell = row.createCell((short) colCount);
            Object valueAt = model.getValueAt(rowCount, colCount);
            log.debug("valueAt: " + valueAt + " row: " + rowCount + "col:" + colCount);
            if (valueAt == null)
               cell.setCellValue(new HSSFRichTextString(""));
            else if (valueAt instanceof Boolean) {
               cell.setCellValue(((Boolean) valueAt).booleanValue());
            } else {
               cell.setCellValue(new HSSFRichTextString(valueAt.toString()));
            }
         }
      }

      FileOutputStream fileOut = new FileOutputStream(xlsFile);
      wb.write(fileOut);
      fileOut.close();
   }

   public void savePlanModel(MyTableModel model) throws IOException {
      saveModel(xlsFile, model, "plan");
   }

   public void setXlsFile(File xlsFile) {
      this.xlsFile = xlsFile;
   }

}
