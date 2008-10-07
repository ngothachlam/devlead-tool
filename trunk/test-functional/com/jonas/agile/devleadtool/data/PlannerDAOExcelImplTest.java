package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.easymock.classextension.EasyMock;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.component.table.model.TableModelDTO;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class PlannerDAOExcelImplTest extends JonasTestCase {
   File xlsFile = new File("bin\\test.xls");
   
   PlannerDAOExcelImpl dao = new PlannerDAOExcelImpl(null);

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

   public void testSouldSaveAndLoadPlanCorrectly() throws IOException {
      MyTableModel model_original = createClassMock(PlanTableModel.class);

       // setup expectations for header
       EasyMock.expect(model_original.getColumnCount()).andReturn(4).anyTimes();
       EasyMock.expect(model_original.getColumnName(0)).andReturn("Jira").anyTimes();
       EasyMock.expect(model_original.getColumnName(1)).andReturn("Description").anyTimes();
       EasyMock.expect(model_original.getColumnName(2)).andReturn("prio").anyTimes();
       EasyMock.expect(model_original.getColumnName(3)).andReturn("isInProgress").anyTimes();
      
       // setup expectations for number of data rows
       EasyMock.expect(model_original.getRowCount()).andReturn(2).anyTimes();
       // setup expectations for first data row
       EasyMock.expect(model_original.getValueAt(0, 0)).andReturn("Row0-Col0").anyTimes();
       EasyMock.expect(model_original.getValueAt(0, 1)).andReturn("Row0-Col1").anyTimes();
       EasyMock.expect(model_original.getValueAt(0, 2)).andReturn("1").anyTimes();
       EasyMock.expect(model_original.getValueAt(0, 3)).andReturn("TRUE").anyTimes();
       // setup expectations for first data row when checking color.
       // setup expectations for first data row
       EasyMock.expect(model_original.getValueAt(1, 0)).andReturn("Row1-Col0").anyTimes();
       EasyMock.expect(model_original.getValueAt(1, 1)).andReturn("Row1-Col1").anyTimes();
       EasyMock.expect(model_original.getValueAt(1, 2)).andReturn("2").anyTimes();
       EasyMock.expect(model_original.getValueAt(1, 3)).andReturn("FALSE").anyTimes();
       // setup expectations for first data row when checking color.

      replay();

      // Save and Load on new file
      dao.setXlsFile(xlsFile);
      dao.savePlanModel(model_original);
      TableModelDTO dtoLoaded = dao.loadModel(xlsFile, "plan");

      verify();

      assertEquals(4, dtoLoaded.getHeader().size());
      
      assertEquals(2, dtoLoaded.getContents().size());
      
      assertEquals(4, dtoLoaded.getContents().get(0).size());
      assertEquals("Row0-Col0", dtoLoaded.getContents().get(0).get(0));
      assertEquals("Row0-Col1", dtoLoaded.getContents().get(0).get(1));
      assertEquals(1, dtoLoaded.getContents().get(0).get(2));
      assertEquals(Boolean.TRUE, dtoLoaded.getContents().get(0).get(3));
      
      assertEquals(4, dtoLoaded.getContents().get(1).size());
      assertEquals("Row1-Col0", dtoLoaded.getContents().get(1).get(0));
      assertEquals("Row1-Col1", dtoLoaded.getContents().get(1).get(1));
      assertEquals(2, dtoLoaded.getContents().get(1).get(2));
      assertEquals(Boolean.FALSE, dtoLoaded.getContents().get(1).get(3));
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
