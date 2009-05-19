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
import com.jonas.agile.devleadtool.burndown.HistoricalData;
import com.jonas.agile.devleadtool.data.PersistanceException;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.agile.devleadtool.gui.component.dialog.CombinedModelDTO;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.sprint.ExcelSprintDao;
import com.jonas.common.DateHelper;

public class LoadingBurnUpDataFunctionalTest {

   HistoricalBoardDao dao;
   DateHelper dateHelper = new DateHelper();
   private File sprintTrackerHistorical_TestFile = new File("bin//Sprint Tracker - llu_historical.csv");

   @Before
   public void setUp() {
      dao = new HistoricalBoardDao(dateHelper);
      deleteFile();
   }

   private void deleteFile() {
      if (sprintTrackerHistorical_TestFile.exists()) {
         boolean result = sprintTrackerHistorical_TestFile.delete();
         if (!result) {
            throw new RuntimeException("did not manage to delete file");
         }
      }
   }

   @Test
   public void shouldLoadHistoricalDataOk() throws IOException, PersistanceException {
      File file = new File("test-data//Sprint Tracker - llu_historical.csv");
      HistoricalData historicalData = dao.load(file);

      assertTrue("Should load at least more than two headers (Date and DayInSprint)", historicalData.getHeaders().size() > 2);
      assertEquals("Should load 17 rows of historical data", 17, historicalData.getBody().size());
   }

   @Test
   public void shouldSaveHistoricalDataFromFromScratch() throws IOException, PersistanceException {
      BoardTableModel boardModel = loadBoardModel(new File("test-data//Sprint Tracker - llu.xls"));

      // Assert historical data saving
      assertFalse("File cannot exist as we are trying to save to it from afresh!", sprintTrackerHistorical_TestFile.exists());
      dao.save(sprintTrackerHistorical_TestFile, boardModel, 1);
      assertTrue("File exist as we are trying to save to it from when already exists!", sprintTrackerHistorical_TestFile.exists());
      assertNoOflines("The file should contain 18 lines (includes the header)", 18, sprintTrackerHistorical_TestFile);
   }

   @Test
   public void shouldLoadBoardModelOkAndThenSaveAndLoadHistoricalOnDifferentDaysOk() throws IOException, PersistanceException {
      BoardTableModel boardModel = loadBoardModel(new File("test-data//Sprint Tracker - llu.xls"));

      // Assert historical data saving
      assertFalse("File cannot exist as we are trying to save to it from afresh!", sprintTrackerHistorical_TestFile.exists());
      dao.save(sprintTrackerHistorical_TestFile, boardModel, 1);
      assertTrue("File should exist as we have just tried to save to it!", sprintTrackerHistorical_TestFile.exists());

      // Assert historical data loaded
      HistoricalData historicalData = dao.load(sprintTrackerHistorical_TestFile);
      assertTrue("Should load at least more than two headers (Date and DayInSprint)", historicalData.getHeaders().size() > 2);
      assertEquals("Should load 17 rows of historical data", 17, historicalData.getBody().size());
      assertNoOflines("The file should contain 35 lines (includes the header) as we are adding day 2 data", 17, sprintTrackerHistorical_TestFile);

      assertTrue("Need to make the save smarter and save to one file only! if there is already saved for this day - don't add to historical data, otherwise add it", false);

      // Assert trying to save over old historical data works as well as adding new data
      dao.save(sprintTrackerHistorical_TestFile, boardModel, 1);
      assertNoOflines("The file should contain 18 lines (includes the header) as we are overwriting day 1 data", 18, sprintTrackerHistorical_TestFile);

      dao.save(sprintTrackerHistorical_TestFile, boardModel, 2);
      assertNoOflines("The file should contain 35 lines (includes the header) as we are adding day 2 data", 35, sprintTrackerHistorical_TestFile);

   }

   private BoardTableModel loadBoardModel(File sprinterXlsFile) throws IOException, PersistanceException {
      // Load BoardModel Data
      ExcelSprintDao excelSprintDao = new ExcelSprintDao();
      PlannerDAOExcelImpl plannerDao = new PlannerDAOExcelImpl(excelSprintDao);

      assertTrue("Cannot load the file if it doesn't exist!", sprinterXlsFile.exists());
      CombinedModelDTO loadedData = plannerDao.loadAllData(sprinterXlsFile);

      BoardTableModel boardModel = loadedData.getBoardModel();
      assertEquals("We expect to have loaded 17 rows", 17, boardModel.getRowCount());
      return boardModel;
   }

   private void assertNoOflines(String string, int i, File file) throws IOException {
      FileReader reader = null;
      BufferedReader br = null;
      try {
         reader = new FileReader(file);
         br = new BufferedReader(reader);
         int rows = 0;
         while (br.readLine() != null) {
            rows++;
         }
         assertEquals(string, i, rows);
      } finally {
         if (br != null)
            br.close();
         if (reader != null)
            reader.close();
      }
   }
}
