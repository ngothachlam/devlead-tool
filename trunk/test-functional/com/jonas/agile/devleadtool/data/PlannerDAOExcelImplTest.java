package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.jonas.agile.devleadtool.component.table.model.TableModelDTO;
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
      TableModelDTO dtoLoaded = dao.loadModel(new File("bin\\lludevsup.xls"), "board");

      assertEquals(10, dtoLoaded.getHeader().size());
      
      assertEquals(2, dtoLoaded.getContents().size());
      
      assertEquals("LLUDEVSUP-522", dtoLoaded.getContents().get(0).get(0));
      assertEquals("ebXML caching of services' files", dtoLoaded.getContents().get(0).get(1));
      assertEquals(false, dtoLoaded.getContents().get(0).get(2));
      assertEquals(false, dtoLoaded.getContents().get(0).get(3));
      assertEquals(true, dtoLoaded.getContents().get(0).get(4));
      assertEquals(false, dtoLoaded.getContents().get(0).get(5));
      assertEquals(false, dtoLoaded.getContents().get(0).get(6));
      assertEquals("", dtoLoaded.getContents().get(0).get(7));
      assertEquals("", dtoLoaded.getContents().get(0).get(8));
      assertEquals(-1, dtoLoaded.getContents().get(0).get(9));
      
      assertEquals("LLUDEVSUP-520", dtoLoaded.getContents().get(1).get(0));
      assertEquals("[INC14869] 822 MEMBERS NEED TO BE SET TO UTM IN IBMS", dtoLoaded.getContents().get(1).get(1));
      assertEquals(false, dtoLoaded.getContents().get(1).get(2));
      assertEquals(false, dtoLoaded.getContents().get(1).get(3));
      assertEquals(false, dtoLoaded.getContents().get(1).get(4));
      assertEquals(false, dtoLoaded.getContents().get(1).get(5));
      assertEquals(true, dtoLoaded.getContents().get(1).get(6));
      assertEquals("", dtoLoaded.getContents().get(1).get(7));
      assertEquals("", dtoLoaded.getContents().get(1).get(8));
      assertEquals(50001, dtoLoaded.getContents().get(1).get(9));
   }
}
