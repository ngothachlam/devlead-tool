package com.jonas.agile.devleadtool.burndown;


import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.jonas.agile.devleadtool.gui.action.SaveDataAction;

public class HistoricalBoardDaoTest {

   @Before
   public void setUp() throws Exception {
   }

   @Test
   public void testShouldCalculateTargetFileOk(){
      HistoricalBoardDao dao = new HistoricalBoardDao(null);
      
      File file = dao.getFileForHistoricalSave(new File("bin"), new File("bin//this is an originalfile.xls"));
      assertEquals("HISTORICAL - this is an originalfile.csv", file.getName());
      assertEquals("bin", file.getParentFile().getName());
   }
}
