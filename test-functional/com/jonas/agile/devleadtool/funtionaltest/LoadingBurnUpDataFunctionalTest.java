package com.jonas.agile.devleadtool.funtionaltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.jonas.agile.devleadtool.burndown.HistoricalBoardDao;
import com.jonas.agile.devleadtool.burndown.HistoricalDataDTO;
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
      // Load All Data
      ExcelSprintDao excelSprintDao = new ExcelSprintDao();
      PlannerDAOExcelImpl plannerDao = new PlannerDAOExcelImpl(excelSprintDao);

      File sprintTrackerFile = new File("test-data//Sprint Tracker - llu.xls");
      assertTrue(sprintTrackerFile.exists());
      CombinedModelDTO loadedData = plannerDao.loadAllData(sprintTrackerFile);

      BoardTableModel boardModel = loadedData.getBoardModel();
      assertEquals("We expect to have loaded 17 rows", 17, boardModel.getRowCount());

      // Assert historical data saving
      File file = new File("bin//Sprint Tracker - llu_historical.csv");
      if (file.exists())
         file.delete();
      assertFalse("File cannot exist as we are trying to save to it from afresh!", file.exists());
      dao.save(file, boardModel);
      assertTrue("File exist as we are trying to save to it from when already exists!", file.exists());
      assertNoOflines("The file should contain 18 lines (includes the header)", 18, file);
      dao.save(file, boardModel);
      assertNoOflines("The file should contain 18 lines (includes the header)", 18, file);

      // Assert historical data loaded
      HistoricalDataDTO historicalDataDto = dao.load(file);
      assertTrue("Should load at least more than one header", historicalDataDto.getHeaders().size()>1);
      assertEquals("Should load 17 rows of historical data", 17, historicalDataDto.getBody().size());
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
