package com.jonas.agile.devleadtool.funtionaltest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.jonas.agile.devleadtool.burndown.HistoricalBoardDao;
import com.jonas.agile.devleadtool.data.PersistanceException;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.agile.devleadtool.gui.component.dialog.CombinedModelDTO;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.common.DateHelper;

public class LoadingBurnUpDataFunctionalTest {

   HistoricalBoardDao dao;
   DateHelper dateHelper = new DateHelper();

   @Before
   public void setUp() {
      dao = new HistoricalBoardDao(dateHelper);
   }

   @Test
   public void shouldLoadModelOk() throws IOException, PersistanceException {
      ExcelSprintDao excelSprintDao = new ExcelSprintDao();
      PlannerDAOExcelImpl plannerDao = new PlannerDAOExcelImpl(excelSprintDao);

      File sprintTrackerFile = new File("test-data//Sprint Tracker - llu.xls");
      assertTrue(sprintTrackerFile.exists());
      CombinedModelDTO loadedData = plannerDao.loadAllData(sprintTrackerFile);

      BoardTableModel boardModel = loadedData.getBoardModel();

      File file = new File("bin//Sprint Tracker - llu_historical.csv");
      if (file.exists())
         file.delete();
      assertFalse("File cannot exist as we are trying to save to it from afresh!", file.exists());
      dao.save(file, boardModel);
      assertNoOflines("The file should contain 18 lines", 18, file);
      dao.save(file, boardModel);
      assertNoOflines("The file should contain 18 lines", 18, file);
   }

   private void assertNoOflines(String string, int i, File file) throws IOException {
      FileReader reader = new FileReader(file);
      BufferedReader br = new BufferedReader(reader);
      int rows = 0;
      while (br.readLine() != null) {
         rows++;
      }
      assertEquals(string, i, rows);
   }
}
