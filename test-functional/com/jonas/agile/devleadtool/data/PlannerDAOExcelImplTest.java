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

   // TODO assert on saving cell background colors.

   protected void setUp() throws Exception {
      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testSouldGetCorrectSheet() {
      PlannerDAOExcelImpl dao = new PlannerDAOExcelImpl(null);
      HSSFWorkbook wb = dao.getWorkBook(xlsFile);
      HSSFSheet sheet = dao.getSheet("test", wb);
      assertTrue(sheet != null);
      HSSFSheet sheet2 = dao.getSheet("test", wb);
      assertEquals(sheet, sheet2);
   }

   public void testSouldGetCorrectWorkBook() {
      PlannerDAOExcelImpl dao = new PlannerDAOExcelImpl(null);
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
      PlannerDAOExcelImpl dao = new PlannerDAOExcelImpl(null);

      MyTableModel model_original = createClassMock(PlanTableModel.class);

       // setup expectations for header
       EasyMock.expect(model_original.getColumnCount()).andReturn(2).anyTimes();
       EasyMock.expect(model_original.getColumnName(0)).andReturn("Jira").anyTimes();
       EasyMock.expect(model_original.getColumnName(1)).andReturn("inPanel").anyTimes();
      
       // setup expectations for number of data rows
       EasyMock.expect(model_original.getRowCount()).andReturn(2).anyTimes();
       
       // setup expectations for first data row
       EasyMock.expect(model_original.getValueAt(0, 0)).andReturn("Row0-Col0").anyTimes();
       EasyMock.expect(model_original.getValueAt(0, 1)).andReturn("Row0-Col1").anyTimes();
       // setup expectations for first data row when checking color.
       // setup expectations for first data row
       EasyMock.expect(model_original.getValueAt(1, 0)).andReturn("Row1-Col0").anyTimes();
       EasyMock.expect(model_original.getValueAt(1, 1)).andReturn("Row1-Col1").anyTimes();
       // setup expectations for first data row when checking color.

      replay();

      // Save and Load on new file
      dao.setXlsFile(xlsFile);
      dao.savePlanModel(model_original);
      TableModelDTO dtoLoaded = dao.loadModel(xlsFile, "plan");

      verify();

      assertEquals(2, dtoLoaded.getHeader().size());
      assertEquals(2, dtoLoaded.getContents().size());
      assertEquals(2, dtoLoaded.getContents().get(0).size());
      assertEquals("Row0-Col0", dtoLoaded.getContents().get(0).get(0));
      assertEquals("Row0-Col1", dtoLoaded.getContents().get(0).get(1));
   }
}
