package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.easymock.classextension.EasyMock;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.TestObjects;

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
      PlannerDAOExcelImpl dao = new PlannerDAOExcelImpl();
      HSSFWorkbook wb = dao.getWorkBook(xlsFile);
      HSSFSheet sheet = dao.getSheet("test", wb);
      assertTrue(sheet != null);
      HSSFSheet sheet2 = dao.getSheet("test", wb);
      assertEquals(sheet, sheet2);
   }

   public void testSouldGetCorrectWorkBook() {
      PlannerDAOExcelImpl dao = new PlannerDAOExcelImpl();
      HSSFWorkbook wb = dao.getWorkBook(xlsFile);
      assertTrue(wb != null);
      HSSFWorkbook wb2 = dao.getWorkBook(xlsFile);
      HSSFWorkbook wb3 = dao.getWorkBook(new File("bin\\test.xls"));
      HSSFWorkbook wb4 = dao.getWorkBook(new File("bin\\test2.xls"));
      assertEquals(wb, wb2);
      assertEquals(wb, wb3);
      assertNotSame(wb, wb4);
   }

   public void testSouldSaveAndLoadBoardCorrectly() throws IOException {
      PlannerDAO dao = new PlannerDAOExcelImpl();
      MyTableModel model_original = new BoardTableModel();

      // Save and Load on new file
      dao.saveBoardModel(xlsFile, model_original);
      MyTableModel model_loaded = dao.loadBoardModel(xlsFile);

      assertEquals(1, model_loaded.getRowCount());
      assertEquals(7, model_loaded.getColumnCount());
      assertHeaderInModel(model_loaded, new Object[] { "Jira", "Open", "Bugs", "InProgress", "Resolved", "Complete", "URL" });
      assertRowInModel(0, model_loaded, new Object[] { "", "", "", "", "", "", "" });

      // Modify,
      model_loaded.addRow(new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
      model_loaded.addRow(new Object[] { "1234", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "LLU-1234" });

      // Save and Load on the existing file
      dao.saveBoardModel(xlsFile, model_loaded);
      model_loaded = dao.loadBoardModel(xlsFile);

      assertEquals(3, model_loaded.getRowCount());
      assertEquals(7, model_loaded.getColumnCount());
      assertHeaderInModel(model_loaded, new Object[] { "Jira", "Open", "Bugs", "InProgress", "Resolved", "Complete", "URL" });
      assertRowInModel(0, model_loaded, new Object[] { "", "", "", "", "", "", "" });
      // assertRowInModel(0, model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" });
      assertRowInModel(1, model_loaded, new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
      assertRowInModel(2, model_loaded, new Object[] { "1234", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "LLU-1234" });
   }

   public void testSouldSaveAndLoadPlanCorrectly() throws IOException {
      PlannerDAO dao = new PlannerDAOExcelImpl();
      MyTableModel model_original = createClassMock(PlanTableModel.class);
      EasyMock.expect(model_original.getColumnCount()).andReturn(2).anyTimes();
      EasyMock.expect(model_original.getColumnName(0)).andReturn("Header0").anyTimes();
      EasyMock.expect(model_original.getColumnName(1)).andReturn("Header1").anyTimes();
      EasyMock.expect(model_original.getRowCount()).andReturn(0).anyTimes();
      
      replay();
      
      // Save and Load on new file
      dao.savePlanModel(xlsFile, model_original);
      MyTableModel model_loaded = dao.loadPlanModel(xlsFile);

      verify();
      
      
      assertEquals(0, model_loaded.getRowCount());
      assertHeaderInModel(model_loaded, new Object[] { "Jira", "Description", "FixVersion", "Status", "Resolution", "BuildNo", "URL", "BoardStatus" });
      assertEquals("Header0", model_loaded.getColumnName(0));
      assertEquals("Header1", model_loaded.getColumnName(1));
      assertEquals(2, model_loaded.getColumnCount());
      assertEquals(0, model_loaded.getRowCount());

      reset();
      EasyMock.expect(model_original.getColumnCount()).andReturn(0).anyTimes();
      replay();
      
      // Modify,
      model_loaded.addRow(new Object[] { "123", "desc1", TestObjects.Version_10, "1", "2", "link1" });
      model_loaded.addRow(new Object[] { "1234", "desc2", TestObjects.Version_11, "3", "4", "link2" });

      // Save and Load on the existing file
      dao.savePlanModel(xlsFile, model_loaded);
      model_loaded = dao.loadPlanModel(xlsFile);

      assertEquals(2, model_loaded.getRowCount());
      assertEquals(8, model_loaded.getColumnCount());
      assertHeaderInModel(model_loaded, new Object[] { "Jira", "Description", "FixVersion", "Status", "Resolution", "BuildNo", "URL", "BoardStatus" });
      assertRowInModel(0, model_loaded, new Object[] { "123", "desc1", "Version 10", "1", "2", "link1" });
      assertRowInModel(1, model_loaded, new Object[] { "1234", "desc2", "Version 11", "3", "4", "link2" });
   }

   private void assertHeaderInModel(TableModel model_loaded, Object[] expected) {
      for (int j = 0; j < expected.length; j++) {
         assertEquals(expected[j], model_loaded.getColumnName(j));
      }
   }

   private void assertRowInModel(int row, TableModel model, Object[] expected) {
      for (int j = 0; j < expected.length; j++) {
         assertEquals("error on " + j + " out of " + expected.length, expected[j], model.getValueAt(row, j));
      }
   }
}
