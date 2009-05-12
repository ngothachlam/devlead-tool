package com.jonas.agile.devleadtool.funtionaltest;

import java.io.File;
import java.io.IOException;

import com.jonas.agile.devleadtool.data.PersistanceException;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.model.TableModelDTO;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class LoadingPlannerFunctionalTest extends JonasTestCase {
   PlannerDAOExcelImpl dao = new PlannerDAOExcelImpl(null);

   @Override
   protected void setUp() throws Exception {
      super.setUp();
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldLoadModelOk() throws IOException, PersistanceException{
      TableModelDTO dtoLoaded = dao.loadModel(new File("test-data\\shouldLoadFineIntoPlanner.xls"), "board");

      assertEquals(10, dtoLoaded.getHeader().size());
      
      assertEquals(4, dtoLoaded.getContents().size());
      
      int i = 0;
      
      assertEquals(ColumnType.Jira, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.Description, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.Resolution, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.Release, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.Merge, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.BoardStatus, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.DEst, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.DAct, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.prio, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.Note, dtoLoaded.getHeader().get(i++));
      
      i = 0;
      assertEquals("LLU-4198", dtoLoaded.getContents().get(0).get(i++));
      assertEquals("summary LLU-4198", dtoLoaded.getContents().get(0).get(i++));
      assertEquals("res 4198", dtoLoaded.getContents().get(0).get(i++));
      assertEquals("rel 4198", dtoLoaded.getContents().get(0).get(i++));
      assertEquals("mer 4198", dtoLoaded.getContents().get(0).get(i++));
      assertEquals(BoardStatusValue.Open, dtoLoaded.getContents().get(0).get(i++));
      assertEquals("1", dtoLoaded.getContents().get(0).get(i++));
      assertEquals("1.5", dtoLoaded.getContents().get(0).get(i++));
      assertEquals(-1, dtoLoaded.getContents().get(0).get(i++));
      assertEquals("not 4198", dtoLoaded.getContents().get(0).get(i++));
      
      i = 0;
      assertEquals("LLU-4211", dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
      assertEquals(BoardStatusValue.Failed, dtoLoaded.getContents().get(1).get(i++));
      assertEquals("merge", dtoLoaded.getContents().get(1).get(i++));
      assertEquals("merge", dtoLoaded.getContents().get(1).get(i++));
      assertEquals(10000, dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
   }
}
