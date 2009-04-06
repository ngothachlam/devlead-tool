package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.model.TableModelDTO;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class PlannerDAOExcelImplTest extends JonasTestCase {
   File xlsFile = new File("bin\\test.xls");
   
   PlannerDAOExcelImpl dao = new PlannerDAOExcelImpl();

   // TODO assert on saving cell background colors.

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testSouldGetCorrectSheet() {
      HSSFWorkbook wb = dao.getWorkBook(xlsFile);
      HSSFSheet sheet = dao.getSheet("test", wb);
      assertTrue(sheet != null);
      HSSFSheet sheet2 = dao.getSheet("test", wb);
      assertEquals(sheet, sheet2);
   }

   public void testSouldGetCorrectWorkBook() {
      HSSFWorkbook wb = dao.getWorkBook(xlsFile);
      assertTrue(wb != null);
      HSSFWorkbook wb2 = dao.getWorkBook(xlsFile);
      HSSFWorkbook wb3 = dao.getWorkBook(new File("bin\\test.xls"));
      HSSFWorkbook wb4 = dao.getWorkBook(new File("bin\\test2.xls"));
      assertEquals(wb, wb2);
      assertEquals(wb, wb3);
      assertNotSame(wb, wb4);
   }
   
   public void testShouldBlah() throws IOException{
      TableModelDTO dtoLoaded = dao.loadModel(new File("test-data\\test.xls"), "board");

      assertEquals(10, dtoLoaded.getHeader().size());
      
      assertEquals(4, dtoLoaded.getContents().size());
      
      int i = 0;
      
      assertEquals(Column.Jira, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.Description, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.Resolution, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.Release, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.Merge, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.BoardStatus, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.DevEst, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.DevAct, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.prio, dtoLoaded.getHeader().get(i++));
      assertEquals(Column.Note, dtoLoaded.getHeader().get(i++));
      
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
      assertEquals(BoardStatusValue.Bug, dtoLoaded.getContents().get(1).get(i++));
      assertEquals("merge", dtoLoaded.getContents().get(1).get(i++));
      assertEquals("merge", dtoLoaded.getContents().get(1).get(i++));
      assertEquals(10000, dtoLoaded.getContents().get(1).get(i++));
      assertEquals("", dtoLoaded.getContents().get(1).get(i++));
   }
}
