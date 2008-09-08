package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.jira.JiraIssue;

public class PlanTableModelTest extends JonasTestCase {

   PlanTableModel model = new PlanTableModel();

   public void testDoesJiraExist() {
      assertFalse(model.doesJiraExist("llu-1"));
      model.addRow(new JiraIssue("llu-1", "summary 1", "status", "resolution"));
      assertTrue(model.doesJiraExist("llu-1"));
      assertFalse(model.doesJiraExist("llu-2"));
      model.addRow(new JiraIssue("llu-2", "summary 2", "status", "resolution"));
      model.removeRow(0);
      assertFalse(model.doesJiraExist("llu-1"));
      assertFalse(model.doesJiraExist("lLu-1"));
      assertTrue(model.doesJiraExist("llu-2"));
      assertTrue(model.doesJiraExist("lLu-2"));
   }

   public void testAddAndSetRowWorks() {
      assertEquals(0, model.getRowCount());
      assertTrue(model.addRow(new JiraIssue("test1", "summary 1", "status1", "resolution1")));
      assertEquals(1, model.getRowCount());
      assertEquals("test1", model.getValueAt(0, 0));
      assertEquals("status1", model.getValueAt(0, 2));
      assertEquals("resolution1", model.getValueAt(0, 3));
      assertTrue(model.addRow(new JiraIssue("test3", "summary 3", "status3", "resolution3")));
      // assertTrue(model.setRow(new JiraIssue("test2", "status2", "resolution2"), 0));
      assertEquals(2, model.getRowCount());
      assertEquals("test1", model.getValueAt(0, 0));
      assertEquals("status1", model.getValueAt(0, 2));
      assertEquals("resolution1", model.getValueAt(0, 3));
      assertEquals("test3", model.getValueAt(1, 0));
      assertEquals("status3", model.getValueAt(1, 2));
      assertEquals("resolution3", model.getValueAt(1, 3));
   }

   public void testShouldCreateFromConstructorOk() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Jira);
      contents.add(getContentRow(1, "row0-", 0));
      contents.add(getContentRow(1, "row1-", 0));
      PlanTableModel model = new PlanTableModel(contents, header);
      
      assertEquals(2, model.getRowCount());
      assertEquals(9, model.getColumnCount());
      assertEquals("row0-0", model.getValueAt(0,0));
      assertEquals(null, model.getValueAt(0,1));
      assertEquals(null, model.getValueAt(0,2));
      assertEquals(null, model.getValueAt(0,3));
      assertEquals(null, model.getValueAt(0,4));
      assertEquals(null, model.getValueAt(0,5));
      assertEquals(null, model.getValueAt(0,6));
      assertEquals(null, model.getValueAt(0,7));
      assertEquals(null, model.getValueAt(0,8));
   }

   private Vector<Object> getContentRow(int noOfColumns, String data, int identifier) {
      Vector<Object> vector = new Vector<Object>();
      while (noOfColumns >0 ) {
         vector.add(data+identifier);
         noOfColumns--;
         
      }
      return vector;
   }
   
   public void testShouldHaveEmptyRowAndColumnNamesOfSameSize(){
      assertEquals(model.getColumnNames().size(), model.getEmptyRow().length);
   }
}
