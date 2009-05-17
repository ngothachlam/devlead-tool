package com.jonas.agile.devleadtool.funtionaltest;

import java.io.File;
import java.io.IOException;

import com.jonas.agile.devleadtool.data.PersistanceException;
import com.jonas.agile.devleadtool.data.PlannerDAOExcelImpl;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
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

   public void testShouldLoadTableModelDTOOkWithNullType() throws IOException, PersistanceException {
      TableModelDTO dtoLoaded = dao.loadModel(new File("test-data\\shouldLoadFineIntoPlannerWithEmptyType.xls"), "board");

      assertEquals(11, dtoLoaded.getHeader().size());

      assertEquals(4, dtoLoaded.getContents().size());

      int i = 0;

      assertEquals(ColumnType.Jira, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.Description, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.Resolution, dtoLoaded.getHeader().get(i++));
      assertEquals(ColumnType.Type, dtoLoaded.getHeader().get(i++));
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
      assertEquals(IssueType.TBD, dtoLoaded.getContents().get(0).get(i++));
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
      assertEquals(IssueType.TBD, dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
      assertEquals(BoardStatusValue.Failed, dtoLoaded.getContents().get(1).get(i++));
      assertEquals("merge", dtoLoaded.getContents().get(1).get(i++));
      assertEquals("merge", dtoLoaded.getContents().get(1).get(i++));
      assertEquals(10000, dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
   }

   public void testShouldLoadJiraModelWithBuildNumbersOk() throws IOException, PersistanceException {
      TableModelDTO dtoLoaded = dao.loadModel(new File("test-data\\Copy of Copy of Sprint Tracker - llu.xls"), "jira");

//      assertEquals(14, dtoLoaded.getHeader().size());
      assertEquals(1, dtoLoaded.getContents().size());

      assertEquals(ColumnType.Jira, dtoLoaded.getHeader().get(0));
      assertEquals(ColumnType.Description, dtoLoaded.getHeader().get(1));
      assertEquals(ColumnType.Type, dtoLoaded.getHeader().get(2));
      assertEquals(ColumnType.J_Sprint, dtoLoaded.getHeader().get(3));
      assertEquals(ColumnType.Project, dtoLoaded.getHeader().get(4));
      assertEquals(ColumnType.FixVersion, dtoLoaded.getHeader().get(5));
      assertEquals(ColumnType.Owner, dtoLoaded.getHeader().get(6));
      assertEquals(ColumnType.Environment, dtoLoaded.getHeader().get(7));
      assertEquals(ColumnType.Delivery, dtoLoaded.getHeader().get(8));
      assertEquals(ColumnType.Resolution, dtoLoaded.getHeader().get(9));
      assertEquals(ColumnType.BuildNo, dtoLoaded.getHeader().get(10));
      assertEquals(ColumnType.J_DevEst, dtoLoaded.getHeader().get(11));
      assertEquals(ColumnType.J_DevAct, dtoLoaded.getHeader().get(12));
      assertEquals(ColumnType.prio, dtoLoaded.getHeader().get(13));
      
      assertEquals("LLU-4626", dtoLoaded.getContents().get(0).get(0));
      assertEquals("Handle EMP code 1526", dtoLoaded.getContents().get(0).get(1));
      assertEquals(IssueType.TBD, dtoLoaded.getContents().get(0).get(2));
      assertEquals("15-3", dtoLoaded.getContents().get(0).get(3));
      assertEquals("Small Business Change", dtoLoaded.getContents().get(0).get(4));
      assertEquals("LLU 15", dtoLoaded.getContents().get(0).get(5));
      assertEquals("Business", dtoLoaded.getContents().get(0).get(6));
      assertEquals("Production", dtoLoaded.getContents().get(0).get(7));
      assertEquals("", dtoLoaded.getContents().get(0).get(8));
      assertEquals("Resolved (DEV Complete)", dtoLoaded.getContents().get(0).get(9));
      assertEquals("llu-service-management-build-3075, llu-master-build-4406", dtoLoaded.getContents().get(0).get(10));
      assertEquals("1.0", dtoLoaded.getContents().get(0).get(11));
      assertEquals("0.5", dtoLoaded.getContents().get(0).get(12));
      assertEquals(-1, dtoLoaded.getContents().get(0).get(13));
   }

   public void testShouldLoadModelOk() throws IOException, PersistanceException {
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
