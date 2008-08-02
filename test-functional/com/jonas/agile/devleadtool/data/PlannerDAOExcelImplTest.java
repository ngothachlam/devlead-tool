package com.jonas.agile.devleadtool.data;

import java.io.File;
import java.io.IOException;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import junit.framework.TestCase;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;

public class PlannerDAOExcelImplTest extends TestCase {
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
      BoardTableModel model_original = new BoardTableModel();

      // Save and Load on new file
      dao.saveBoardModel(xlsFile, model_original);
      BoardTableModel model_loaded = dao.loadBoardModel(xlsFile);

      assertEquals(1, model_loaded.getRowCount());
      assertEquals(7, model_loaded.getColumnCount());
      assertHeaderInModel(model_loaded, new Object[] { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete", "URL" });
      assertRowInModel(0, model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" });

      // Modify,
      model_loaded.addRow(new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
      model_loaded.addRow(new Object[] { "1234", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "LLU-1234" });

      // Save and Load on the existing file
      dao.saveBoardModel(xlsFile, model_loaded);
      model_loaded = dao.loadBoardModel(xlsFile);

      assertEquals(3, model_loaded.getRowCount());
      assertEquals(7, model_loaded.getColumnCount());
      assertHeaderInModel(model_loaded, new Object[] { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete", "URL" });
      assertRowInModel(0, model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" });
      assertRowInModel(1, model_loaded, new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
      assertRowInModel(2, model_loaded, new Object[] { "1234", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "LLU-1234" });
   }

   public void testSouldSaveAndLoadPlanCorrectly() throws IOException {
      PlannerDAO dao = new PlannerDAOExcelImpl();
      assertTrue(false);
   }

   public void testSouldSaveAndLoadJiraCorrectly() throws IOException {
      PlannerDAO dao = new PlannerDAOExcelImpl();
      JiraTableModel model_original = new JiraTableModel();

      // Save and Load on new file
      dao.saveJiraModel(xlsFile, model_original);
      // JiraTableModel model_loaded = dao.loadJiraModel(xlsFile);
      //
      // assertEquals(1, model_loaded.getRowCount());
      // assertEquals(7, model_loaded.getColumnCount());
      // assertHeaderInModel(model_loaded, new Object[] { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete", "URL" });
      // assertRowInModel(0, model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" });
      //
      // // Modify,
      // model_loaded.addRow(new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
      // model_loaded.addRow(new Object[] { "1234", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "LLU-1234" });
      //      
      // //Save and Load on the existing file
      // dao.saveJiraModel(xlsFile, model_loaded);
      // model_loaded = dao.loadJiraModel(xlsFile);
      //
      // assertEquals(3, model_loaded.getRowCount());
      // assertEquals(7, model_loaded.getColumnCount());
      // assertHeaderInModel(model_loaded, new Object[] { "Jira", "Open", "Bugs", "In-Progress", "Resolved", "Complete", "URL" });
      // assertRowInModel(0, model_loaded, new Object[] { "", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "" });
      // assertRowInModel(1, model_loaded, new Object[] { "123", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "LLU-123" });
      // assertRowInModel(2, model_loaded, new Object[] { "1234", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "LLU-1234" });
   }

   private void assertHeaderInModel(TableModel model_loaded, Object[] expected) {
      for (int j = 0; j < expected.length; j++) {
         assertEquals(expected[j], model_loaded.getColumnName(j));
      }
   }

   private void assertRowInModel(int row, TableModel model_loaded, Object[] expected) {
      for (int j = 0; j < expected.length; j++) {
         assertEquals(expected[j], model_loaded.getValueAt(row, j));
      }
   }
}
