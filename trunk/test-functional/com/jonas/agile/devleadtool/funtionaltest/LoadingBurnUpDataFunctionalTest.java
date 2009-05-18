package com.jonas.agile.devleadtool.funtionaltest;

import static org.junit.Assert.assertTrue;

import java.io.File;
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
   public void setUp(){
      dao = new HistoricalBoardDao(dateHelper);
   }
   
   @Test
   public void shouldLoadModelOk() throws IOException, PersistanceException{
      File sprintTrackerFile = new File("test-data//Sprint Tracker - llu.xls");
      HistoricalBoardDao dao = new HistoricalBoardDao(new DateHelper());
      
      assertTrue(sprintTrackerFile.exists());
      
      ExcelSprintDao excelSprintDao = new ExcelSprintDao();
      PlannerDAOExcelImpl plannerDao = new PlannerDAOExcelImpl(excelSprintDao);
      CombinedModelDTO loadedData = plannerDao.loadAllData(sprintTrackerFile);
      
      BoardTableModel boardModel = loadedData.getBoardModel();
      dao.save(boardModel);
   }
}
