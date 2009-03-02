package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.common.SwingUtil;
import com.jonas.jira.JiraIssue;

public class JiraTableModelTest extends JonasTestCase {

   private JiraTableModel jiraModel;

   private void assertRow(String[] strings, MyTableModel model, int row) {
      int cols = 0;
      for (; cols < strings.length; cols++) {
         Object valueAt = model.getValueAt(row, cols);
         assertEquals("Value at column " + cols + " is incorrect", strings[cols], valueAt);
      }
      assertEquals("Amount of cols are not as expected! (Expected : " + cols + " but is: " + model.getColumnCount() + ")", cols, model.getColumnCount());
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
      header.add(Column.J_Type);
      header.add(Column.J_Sprint);
      header.add(Column.J_FixVersion);
      header.add(Column.J_Delivery);
      header.add(Column.J_Resolution);
      header.add(Column.J_BuildNo);
      header.add(Column.J_Dev_Estimate);
      header.add(Column.J_Dev_Spent);

      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      String[] content = new String[] { "Jira", "Description", "J_Type", "J_Sprint", "J_FixVersion", "J_Delivery", "J_Resolution",
            "J_BuildNo", "J_Dev_Estimate", "J_Dev_Spent" };
      contents.add(getTestRowVector(content, 0));
      contents.add(getTestRowVector(content, 1));

      jiraModel = new JiraTableModel(contents, header);

      assertEquals(2, jiraModel.getRowCount());
      assertEquals(10, jiraModel.getColumnCount());
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
      jiraModel = new JiraTableModel();
      JiraIssue jiraIssue = new JiraIssue("Jira-1", "Summary 1", "Open", "Resolved", "Type");
      JiraIssue jiraIssue2 = new JiraIssue("Jira-2", "Summary 2", "Open", "Resolved", "Type");
      jiraModel.addJira("Jira-1");
   }

   public void testShouldGetColorOk() {
      jiraModel = new JiraTableModel();
      jiraModel.setRenderColors(true);
      assertEquals(null, jiraModel.getColor("llu-1", 0, Column.Jira));

      // add llu-1 and llu-2 to jiraModel 
      jiraModel.addJira("llu-1");
      jiraModel.addJira("llu-2");
      
      assertEquals(null, jiraModel.getColor("llu-1", 0, Column.Jira));
      assertEquals(null, jiraModel.getColor("llu-2", 0, Column.Jira));
      
      //add jira llu-2 and llu-3 to boardModel with llu-2 having board's dev_estimate set to '1'
      testBoardModel.addJira("llu-2");
      testBoardModel.addJira("llu-3");
      testBoardModel.setValueAt("1", 0, Column.Dev_Estimate);
      jiraModel.setBoardModel(testBoardModel);

      assertEquals(null, jiraModel.getColor("llu-1", 0, Column.Jira));
      assertEquals(null, jiraModel.getColor("est1", 0, Column.J_Dev_Estimate));
      assertEquals(null, jiraModel.getColor("est1", 0, Column.J_Dev_Spent));
      
      assertEquals(SwingUtil.cellGreen, jiraModel.getColor("llu-2", 1, Column.Jira));
      assertEquals(SwingUtil.cellRed, jiraModel.getColor("est2", 1, Column.J_Dev_Estimate));
      assertEquals(null, jiraModel.getColor("est2", 1, Column.J_Dev_Spent));
   }
   
   
   public void testShouldHiglightIncorrectSprintOk(){
      jiraModel = new JiraTableModel();
      assertTrue(jiraModel.isSprintOk(BoardStatusValue.UnKnown, null));
      assertTrue(jiraModel.isSprintOk(BoardStatusValue.NA, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.Open, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.Bug, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.Parked, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.InDevProgress, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.Resolved, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.InQAProgress, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.Complete, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.ForShowCase, null));
      assertFalse(jiraModel.isSprintOk(BoardStatusValue.Approved, null));
   }
   
   public void testShouldCompareFixversionsOk(){
      jiraModel = new JiraTableModel();
      ArrayList<String> jiraFixVersion = new ArrayList<String>();
      assertFalse(jiraModel.isFixVersionOk("LLU 12", jiraFixVersion));
      
      jiraFixVersion.add("LLU 13");
      assertFalse(jiraModel.isFixVersionOk("LLU 12", jiraFixVersion));
      assertTrue(jiraModel.isFixVersionOk("LLU 13", jiraFixVersion));
      
      jiraFixVersion.add("LLU 12");
      assertFalse(jiraModel.isFixVersionOk("LLU 12", jiraFixVersion));
      assertFalse(jiraModel.isFixVersionOk("LLU 13", jiraFixVersion));
      assertTrue(jiraModel.isFixVersionOk("LLU 12, LLU 13", jiraFixVersion));
      assertTrue(jiraModel.isFixVersionOk("LLU 13, LLU 12", jiraFixVersion));
   }

   public void testShouldCompareEstimatesOk() {
      jiraModel = new JiraTableModel();
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

   private Column[] columns = { Column.Jira, Column.Description, Column.Dev_Actual, Column.Dev_Estimate };
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
