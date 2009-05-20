package com.jonas.agile.devleadtool.funtionaltest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

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
import com.jonas.jira.TestObjects;

public class LoadingBurnUpDataFunctionalTest {

   private HistoricalBoardDao dao;
   private DateHelper dateHelper = new DateHelper();
   private File sprintTrackerHistorical_TestFile = new File("bin//Sprint Tracker - llu_historical.csv");
   private File sprintTrackerHistorical_STATIC_TestFile = new File("bin//Sprint Tracker - llu_historical_static.csv");
   private String today = dateHelper.getTodaysDateAsString();

   private Object[][] sprintTrackerHistorical_Expectation = {
         { "HistoricalDate", "DayInSprint", "Jira", "Description", "Type", "Resolution", "Release", "Merge", "BoardStatus", "Old", "DEst", "QEst", "DRem", "QRem", "DAct", "QAct", "prio", "Note", "Sprint" },
         { "Tue 19-05-2009", "1", "LLU-4520", "Update the MLC and TPDS Certificates", "Story", "Open (Unresolved)", "LLU 15", "", "1. Open", "false", "0", "1", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4613", "Handle EMP code 7007", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4626", "Handle EMP code 1526", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4648", "Remove R500 templates from EMP simulator", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4674", "Remove R500 templates from all projects", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "3", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4675", "Improve inventory sonic build times", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4697", "Remove template duplication for IMS tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4691", "Standardise Health Check Scripts", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4696", "Technical approach on how this audit should be done", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4695", "Add logging around the port state validation for deallocation requests", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0.5", "", "", "", "", "", "-1", "",
               "testCurrentSprint" }, { "Tue 19-05-2009", "1", "LLU-4698", "Remove template duplication for SM tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4692", "Improve service sonic build times", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4699", "Design an approach on how this can be accomplished", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "1", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4685", "JMS Message sender should load production vm templates", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "2", "", "1", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4690", "Build stats need to parse multiple fitnesse files", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4677", "IMS Sonic RME logging", "Dev", "Resolved (Fixed)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4732", "Implement build statistics for service sonic", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "1.5", "", "1", "", "", "", "-1", "", "testCurrentSprint" } };
   private Object[][] sprintTrackerHistorical_ExpectationWithModification = {
         { "HistoricalDate", "DayInSprint", "Jira", "Description", "Type", "Resolution", "Release", "Merge", "BoardStatus", "Old", "DEst", "QEst", "DRem", "QRem", "DAct", "QAct", "prio", "Note", "Sprint" },
         { "Tue 19-05-2009", "1", "LLU-4520", "Update the MLC and TPDS Certificates", "Story", "Open (Unresolved)", "LLU 15", "", "1. Open", "false", "0", "1", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4613", "Handle EMP code 7007", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4626", "Handle EMP code 1526", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4648", "Remove R500 templates from EMP simulator", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4674", "Remove R500 templates from all projects", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "3", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4675", "Improve inventory sonic build times", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4697", "Remove template duplication for IMS tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4691", "Standardise Health Check Scripts", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4696", "Technical approach on how this audit should be done", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4695", "Add logging around the port state validation for deallocation requests", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0.5", "", "", "", "", "", "-1", "",
               "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4698", "Remove template duplication for SM tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4692", "Improve service sonic build times", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4699", "Design an approach on how this can be accomplished", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "1", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4685", "JMS Message sender should load production vm templates", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "2", "", "1", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4690", "Build stats need to parse multiple fitnesse files", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4677", "IMS Sonic RME logging", "Dev", "Resolved (Fixed)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { "Tue 19-05-2009", "1", "LLU-4732", "Implement build statistics for service sonic", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "1.5", "", "1", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4520", "Update the MLC and TPDS Certificates", "Story", "Open (Unresolved)", "LLU 15", "", "1. Open", "false", "0", "1", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4613", "Handle EMP code 7007", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4626", "Handle EMP code 1526", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4648", "Remove R500 templates from EMP simulator", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4674", "Remove R500 templates from all projects", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "3", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4675", "Improve inventory sonic build times", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4697", "Remove template duplication for IMS tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4691", "Standardise Health Check Scripts", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4696", "Technical approach on how this audit should be done", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4695", "Add logging around the port state validation for deallocation requests", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0.5", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4698", "Remove template duplication for SM tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4692", "Improve service sonic build times", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4699", "Design an approach on how this can be accomplished", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "1", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4685", "JMS Message sender should load production vm templates", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "2", "", "1", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4690", "Build stats need to parse multiple fitnesse files", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4677", "IMS Sonic RME logging", "Dev", "Resolved (Fixed)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4732", "Implement build statistics for service sonic", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "1.5", "", "1", "", "", "", "-1", "", "testCurrentSprint" } };
   private Object[][] sprintTrackerHistorical_ExpectationWithDualSaves = {
         { "HistoricalDate", "DayInSprint", "Jira", "Description", "Type", "Resolution", "Release", "Merge", "BoardStatus", "Old", "DEst", "QEst", "DRem", "QRem", "DAct", "QAct", "prio", "Note", "Sprint" },
         { today, "1", "LLU-4520", "Update the MLC and TPDS Certificates", "Story", "Open (Unresolved)", "LLU 15", "", "1. Open", "false", "0", "1", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4613", "Handle EMP code 7007", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4626", "Handle EMP code 1526", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4648", "Remove R500 templates from EMP simulator", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4674", "Remove R500 templates from all projects", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "3", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4675", "Improve inventory sonic build times", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4697", "Remove template duplication for IMS tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4691", "Standardise Health Check Scripts", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4696", "Technical approach on how this audit should be done", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4695", "Add logging around the port state validation for deallocation requests", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0.5", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4698", "Remove template duplication for SM tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4692", "Improve service sonic build times", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4699", "Design an approach on how this can be accomplished", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "1", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4685", "JMS Message sender should load production vm templates", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "2", "", "1", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4690", "Build stats need to parse multiple fitnesse files", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4677", "IMS Sonic RME logging", "Dev", "Resolved (Fixed)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "1", "LLU-4732", "Implement build statistics for service sonic", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "1.5", "", "1", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4520", "Update the MLC and TPDS Certificates", "Story", "Open (Unresolved)", "LLU 15", "", "1. Open", "false", "0", "1", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4613", "Handle EMP code 7007", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4626", "Handle EMP code 1526", "Story", "Resolved (Fixed)", "LLU 15", "", "4. Resolved", "false", "1.0", "1.0", "", "0.2", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4648", "Remove R500 templates from EMP simulator", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4674", "Remove R500 templates from all projects", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "3", "", "", "", "0.5", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4675", "Improve inventory sonic build times", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4697", "Remove template duplication for IMS tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4691", "Standardise Health Check Scripts", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4696", "Technical approach on how this audit should be done", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4695", "Add logging around the port state validation for deallocation requests", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0.5", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4698", "Remove template duplication for SM tests", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4692", "Improve service sonic build times", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "2", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4699", "Design an approach on how this can be accomplished", "Dev", "Open (Unresolved)", "LLU 16", "", "1. Open", "false", "1", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4685", "JMS Message sender should load production vm templates", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "2", "", "1", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4690", "Build stats need to parse multiple fitnesse files", "Dev", "Resolved (DEV Complete)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4677", "IMS Sonic RME logging", "Dev", "Resolved (Fixed)", "LLU 16", "", "4. Resolved", "false", "0", "", "", "", "", "", "-1", "", "testCurrentSprint" },
         { today, "2", "LLU-4732", "Implement build statistics for service sonic", "Dev", "Open (Unresolved)", "LLU 16", "", "3. InProgress", "false", "1.5", "", "1", "", "", "", "-1", "", "testCurrentSprint" } };

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

      Vector<Vector<Object>> expectation = createExpectation(sprintTrackerHistorical_Expectation);
      assertHistoricalData(expectation, historicalData);
   }

   private Vector<Vector<Object>> createExpectation(Object[][] array) {
      Vector<Vector<Object>> expectation = new Vector<Vector<Object>>();
      for (int i = 0; i < array.length; i++) {
         Object[] subArray = array[i];
         Vector vector = new Vector();
         for (int j = 0; j < subArray.length; j++) {
            vector.add(subArray[j]);
         }
         expectation.add(vector);
      }
      return expectation;
   }

   private void assertHistoricalData(Vector<Vector<Object>> expectation, HistoricalData historicalData) {
      Vector<String> header = historicalData.getHeaders();
      for (int counter = 0; counter < header.size(); counter++) {
         assertEquals("The header on col " + counter + " is not what is expected!", expectation.get(0).get(counter), header.get(counter));
      }
      assertEquals("Header column count is incorrect", expectation.get(0).size(), header.size());

      Vector<Vector<Object>> body = historicalData.getBody();
      int row = 1;
      for (Vector<Object> vector : body) {
         for (int counter = 0; counter < vector.size(); counter++) {
            assertEquals("Column  " + expectation.get(0).get(counter) + " (" + counter + ") in row " + row + " (including header ) is not what is expected!", expectation.get(row).get(counter), vector.get(counter).toString());
         }
         for (int counter = vector.size(); counter < expectation.get(row).size(); counter++) {
            System.out.println(expectation.get(row).get(counter));
         }
         assertEquals("Data column count is incorrect for row " + row, expectation.get(row).size(), vector.size());
         row++;
      }
      assertEquals("The amount of rows in the data is incorrect!", expectation.size() - 1, body.size());

   }

   @Test
   public void shouldSaveHistoricalDataFromFromScratch() throws IOException, PersistanceException {
      BoardTableModel boardModel = loadBoardModel(new File("test-data//Sprint Tracker - llu.xls"));

      // Assert historical data saving
      assertFalse("File cannot exist as we are trying to save to it from afresh!", sprintTrackerHistorical_TestFile.exists());
      dao.save(sprintTrackerHistorical_TestFile, boardModel, 1, TestObjects.TEST_CURRENT_SPRINT);
      assertTrue("File exist as we are trying to save to it from when already exists!", sprintTrackerHistorical_TestFile.exists());
      assertNoOflines("The file should contain 18 lines (includes the header)", 18, sprintTrackerHistorical_TestFile);
   }

   @Test
   public void shouldLoadBoardModelOkAndThenSaveAndLoadHistoricalOnDifferentDaysOkIfHistoricalFileAlreadyExists() throws IOException, PersistanceException {
      BoardTableModel boardModel = loadBoardModel(new File("test-data//Sprint Tracker - llu.xls"));

      // setup a file with old historical data...
      {
         sprintTrackerHistorical_STATIC_TestFile.delete();
         assertFalse("File cannot exist as we are setting it up now!", sprintTrackerHistorical_STATIC_TestFile.exists());
         dao.setDateHelper(new DateHelperForTesting());
         dao.save(sprintTrackerHistorical_STATIC_TestFile, boardModel, 1, TestObjects.TEST_CURRENT_SPRINT);
         assertNoOflines("The file should contain 18 lines (includes the header) as we are adding day 2 data", 18, sprintTrackerHistorical_STATIC_TestFile);
         assertTrue("File need to exist as we have just tried to set it up with test data", sprintTrackerHistorical_STATIC_TestFile.exists());
      }

      dao.setDateHelper(new DateHelper());
      dao.save(sprintTrackerHistorical_STATIC_TestFile, boardModel, 2, TestObjects.TEST_CURRENT_SPRINT);
      assertNoOflines("The file should contain 35 lines (includes the header) as we are adding day 2 data", 35, sprintTrackerHistorical_STATIC_TestFile);

      HistoricalData historicalData = dao.load(sprintTrackerHistorical_STATIC_TestFile);

      assertTrue("Should load at least more than two headers (Date and DayInSprint)", historicalData.getHeaders().size() > 2);
      assertEquals("Should load 34 rows of historical data", 34, historicalData.getBody().size());

      Vector<Vector<Object>> expectation = createExpectation(sprintTrackerHistorical_ExpectationWithModification);
      assertHistoricalData(expectation, historicalData);
   }

   @Test
   public void shouldLoadBoardModelOkAndThenSaveAndLoadHistoricalOnDifferentDaysOk() throws IOException, PersistanceException {
      BoardTableModel boardModel = loadBoardModel(new File("test-data//Sprint Tracker - llu.xls"));

      // Assert historical data saving
      assertFalse("File cannot exist as we are trying to save to it from afresh!", sprintTrackerHistorical_TestFile.exists());
      dao.save(sprintTrackerHistorical_TestFile, boardModel, 1, TestObjects.TEST_CURRENT_SPRINT);
      assertNoOflines("The file should contain 18 lines (includes the header) as we are adding day 2 data", 18, sprintTrackerHistorical_TestFile);

      assertTrue("File need to exist as we are trying to save to it by copying historical data!", sprintTrackerHistorical_TestFile.exists());

      dao.save(sprintTrackerHistorical_TestFile, boardModel, 1, TestObjects.TEST_CURRENT_SPRINT);
      assertNoOflines("The file should contain 18 lines (includes the header) as we are overwriting day 1 data", 18, sprintTrackerHistorical_TestFile);

      dao.save(sprintTrackerHistorical_TestFile, boardModel, 2, TestObjects.TEST_CURRENT_SPRINT);
      assertNoOflines("The file should contain 35 lines (includes the header) as we are adding day 2 data", 35, sprintTrackerHistorical_TestFile);

      HistoricalData historicalData = dao.load(sprintTrackerHistorical_TestFile);

      assertTrue("Should load at least more than two headers (Date and DayInSprint)", historicalData.getHeaders().size() > 2);
      assertEquals("Should load 34 rows of historical data", 34, historicalData.getBody().size());

      Vector<Vector<Object>> expectation = createExpectation(sprintTrackerHistorical_ExpectationWithDualSaves);
      assertHistoricalData(expectation, historicalData);
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

class DateHelperForTesting extends DateHelper {

   @Override
   public String getTodaysDateAsString() {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DATE, 19);
      calendar.set(Calendar.MONTH, 4);
      calendar.set(Calendar.YEAR, 2009);
      return getDateAsString(calendar.getTime());
   }
}