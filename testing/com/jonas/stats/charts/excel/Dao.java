package com.jonas.stats.charts.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.jonas.agile.devleadtool.burndown.ContentsDto;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;

public interface Dao {

   public ContentsDto loadContents(File xlsFile, String excelSheet) throws IOException;

}
