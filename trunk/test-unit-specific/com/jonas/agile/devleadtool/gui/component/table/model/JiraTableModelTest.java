package com.jonas.agile.devleadtool.gui.component.table.model;

import java.awt.Color;
import java.util.Vector;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraIssue;

public class JiraTableModelTest extends JonasTestCase {

   @Override
   protected void setUp() throws Exception {
      jiraModel = new JiraTableModel();
   }

   private JiraTableModel jiraModel;
   
   

   private void assertRow(String[] strings, MyTableModel model, int row) {
      int cols = 0;
      for (; cols < strings.length; cols++) {
         Object valueAt = model.getValueAt(row, cols);
         assertEquals("Value at column " + cols + " is incorrect", strings[cols], valueAt);
      }
      assertEquals("Amount of cols are not as expected! (Expected : " + cols + " but is: " + model.getColumnCount() + ")", cols, model
            .getColumnCount());
   }

   private Vector<Object> getTestRowVector(String[] strings, int i) {
      Vector<Object> vector = new Vector<Object>();
      for (String string : strings) {
         vector.add(string + "-" + i);
      }
      return vector;
   }

   public void testShouldBeConstructedFromDAOOk() {
      Vector<Column> header = new Vector<Column>();

      header.add(Column.Jira);
      header.add(Column.Description);
      header.add(Column.Type);
      header.add(Column.J_Sprint);
      header.add(Column.Project);
      header.add(Column.FixVersion);
      header.add(Column.Owner);
      header.add(Column.Environment);
      header.add(Column.Delivery);
      header.add(Column.Resolution);
      header.add(Column.BuildNo);
      header.add(Column.J_DevEst);
      header.add(Column.J_DevAct);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      String[] content = new String[] { "Jira", "Desc", "J_Type", "J_Sprint", "Proj", "Fix", "Own", "Env", "Del", "Res",
            "BuildNo", "J_DevEst", "J_DevAct" };
      contents.add(getTestRowVector(content, 0));
      contents.add(getTestRowVector(content, 1));

      jiraModel = new JiraTableModel(contents, header);

      assertEquals(2, jiraModel.getRowCount());
      assertRow(getTestRowArray(content, 0), jiraModel, 0);
      assertRow(getTestRowArray(content, 1), jiraModel, 1);
   }

   private String[] getTestRowArray(String[] content, int i) {
      String[] result = new String[content.length];
      for (int j = 0; j < content.length; j++) {
         result[j] = content[j] + "-" + i;
      }
      return result;
   }

   public void testShouldAddRowOk() {
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved", "Type");
      JiraIssue jiraIssue2 = new JiraIssue("Jira-2", "Summary 2", "Open", "Resolved", "Type");
      jiraModel.addJira("Jira-1");
   }

   public void testShouldGetColorOk() {
      jiraModel.setRenderColors(true);
      assertEquals(null, jiraModel.getColor("llu-1", 0, Column.Jira));

      // add llu-1 and llu-2 to jiraModel
      jiraModel.addJira("llu-1");
      jiraModel.addJira("llu-2");

      assertEquals(null, jiraModel.getColor("llu-1", 0, Column.Jira));
      assertEquals(null, jiraModel.getColor("llu-2", 0, Column.Jira));

      // add jira llu-2 and llu-3 to boardModel with llu-2 having board's dev_estimate set to '1'
      testBoardModel.addJira("llu-2");
      testBoardModel.addJira("llu-3");
      testBoardModel.setValueAt("1", 0, Column.DevEst);
      jiraModel.setBoardModel(testBoardModel);

      assertEquals(null, jiraModel.getColor("llu-1", 0, Column.Jira));
      assertEquals(null, jiraModel.getColor("est1", 0, Column.J_DevEst));
      assertEquals(null, jiraModel.getColor("est1", 0, Column.J_DevAct));

      assertEquals(SwingUtil.cellGreen, jiraModel.getColor("llu-2", 1, Column.Jira));
      assertEquals(SwingUtil.cellRed, jiraModel.getColor("est2", 1, Column.J_DevEst));
      assertEquals(null, jiraModel.getColor("est2", 1, Column.J_DevAct));
   }

   public void testShouldHiglightIncorrectSprints() {
      assertFalse(jiraModel.isSprintOk("", null));
      assertFalse(jiraModel.isSprintOk("", "1"));
      assertFalse(jiraModel.isSprintOk(null, ""));
      
      assertTrue(jiraModel.isSprintOk("", " "));
      assertTrue(jiraModel.isSprintOk("", ""));
      assertTrue(jiraModel.isSprintOk("13-1", "13-1"));
      assertTrue(jiraModel.isSprintOk("A", "A"));
   }

   public void testShouldHiglightIncorrectProjects() {
      assertFalse(jiraModel.isProjectOk(null));
      assertFalse(jiraModel.isProjectOk(""));
      assertFalse(jiraModel.isProjectOk("   "));
      assertTrue(jiraModel.isProjectOk("project"));
      assertFalse(jiraModel.isProjectOk("TBD"));
   }

   public void testShouldCompareFixversionsOk() {
      String jiraFixVersion = null;
      assertFalse(jiraModel.isFixVersionOk("LLU 12", jiraFixVersion));

      jiraFixVersion = "";
      assertTrue(jiraModel.isFixVersionOk("", jiraFixVersion));
      assertFalse(jiraModel.isFixVersionOk("TBD", jiraFixVersion));
      

      jiraFixVersion = "LLU 13";
      assertFalse(jiraModel.isFixVersionOk("LLU 12", jiraFixVersion));
      assertTrue(jiraModel.isFixVersionOk("LLU 13", jiraFixVersion));
      assertTrue(jiraModel.isFixVersionOk(new String("LLU 13"), jiraFixVersion));

      jiraFixVersion = "LLU 13, LLU 12";
      assertFalse(jiraModel.isFixVersionOk("LLU 12", jiraFixVersion));
      assertFalse(jiraModel.isFixVersionOk("LLU 13", jiraFixVersion));
      assertFalse(jiraModel.isFixVersionOk("LLU 12, LLU 13", jiraFixVersion));
      assertTrue(jiraModel.isFixVersionOk("LLU 13, LLU 12", jiraFixVersion));
      assertFalse(jiraModel.isFixVersionOk("LLU 13, LLU 12, LLU 14", jiraFixVersion));
      
      assertTrue(jiraModel.isFixVersionOk("TBD", "TBD"));
   }

   public void testShouldCompareEstimatesOk() {
      assertEquals(true, jiraModel.isJiraNumberOk("", null));
      assertEquals(true, jiraModel.isJiraNumberOk("", ""));
      assertEquals(true, jiraModel.isJiraNumberOk("", "0"));
      assertEquals(true, jiraModel.isJiraNumberOk("", "0.0"));
      assertEquals(false, jiraModel.isJiraNumberOk("", "0.1"));

      assertEquals(true, jiraModel.isJiraNumberOk(null, ""));
      assertEquals(true, jiraModel.isJiraNumberOk(null, "0"));
      assertEquals(true, jiraModel.isJiraNumberOk(null, "0.0"));
      assertEquals(false, jiraModel.isJiraNumberOk(null, "0.1"));

      assertEquals(true, jiraModel.isJiraNumberOk("0", ""));
      assertEquals(true, jiraModel.isJiraNumberOk("0", "0"));
      assertEquals(true, jiraModel.isJiraNumberOk("0", "0.0"));
      assertEquals(false, jiraModel.isJiraNumberOk("0", "0.1"));

      assertEquals(false, jiraModel.isJiraNumberOk("1.0", null));
      assertEquals(false, jiraModel.isJiraNumberOk("1.0", ""));
      assertEquals(false, jiraModel.isJiraNumberOk("1.0", "0"));
      assertEquals(false, jiraModel.isJiraNumberOk("1.0", "0.0"));

      assertEquals(false, jiraModel.isJiraNumberOk("0.9", "1.0"));
      assertEquals(false, jiraModel.isJiraNumberOk("1.0", "0.9"));
      assertEquals(true, jiraModel.isJiraNumberOk("1.0", "1.0"));
      assertEquals(false, jiraModel.isJiraNumberOk("1.1", "1.0"));
      assertEquals(false, jiraModel.isJiraNumberOk("1.0", "1.1"));

      assertEquals(true, jiraModel.isJiraNumberOk("merge", null));
      assertEquals(true, jiraModel.isJiraNumberOk("merge", ""));
      assertEquals(true, jiraModel.isJiraNumberOk("merge", "0"));
      assertEquals(false, jiraModel.isJiraNumberOk("merge", "0.1"));
      assertEquals(false, jiraModel.isJiraNumberOk("merge", "1.1"));
   }

   private Column[] columns = { Column.Jira, Column.Description, Column.DevAct, Column.DevEst };
   public TestTableModelTemp testBoardModel = new TestTableModelTemp(columns);

   class TestTableModelTemp extends MyTableModel {

      public TestTableModelTemp(Column[] columns) {
         super(columns);
      }

      @Override
      public Color getColor(Object value, int row, Column column) {
         return null;
      }

   }
}
