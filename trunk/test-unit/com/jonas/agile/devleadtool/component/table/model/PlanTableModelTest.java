package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class PlanTableModelTest extends JonasTestCase {

   PlanTableModel model = new PlanTableModel();

   public void testShouldAddJiraAndCalculateIfJiraExistsCorrectly() {
      assertFalse(model.doesJiraExist("llu-1"));
      model.addJira("llu-1");
      assertTrue(model.doesJiraExist("llu-1"));
      assertFalse(model.doesJiraExist("llu-2"));
      assertEquals(1, model.getRowCount());
      model.addJira("llu-2");
      assertTrue(model.doesJiraExist("llu-1"));
      assertTrue(model.doesJiraExist("lLu-1"));
      assertTrue(model.doesJiraExist("llu-2"));
      assertTrue(model.doesJiraExist("lLu-2"));
      assertEquals(2, model.getRowCount());
      model.removeRow(0);
      assertFalse(model.doesJiraExist("llu-1"));
      assertFalse(model.doesJiraExist("lLu-1"));
      assertTrue(model.doesJiraExist("llu-2"));
      assertTrue(model.doesJiraExist("lLu-2"));
      assertEquals(1, model.getRowCount());
   }

   public void testShouldCreateFromConstructorOk() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Jira);
      contents.add(getTestContentRow(1, "row0-", 0));
      contents.add(getTestContentRow(1, "row1-", 0));
      PlanTableModel model = new PlanTableModel(contents, header);

      assertEquals(2, model.getRowCount());
      assertEquals(9, model.getColumnCount());
      assertEquals("row0-0", model.getValueAt(0, 0));
      assertEquals(null, model.getValueAt(0, 1));
      assertEquals(null, model.getValueAt(0, 2));
      assertEquals(null, model.getValueAt(0, 3));
      assertEquals(null, model.getValueAt(0, 4));
      assertEquals(null, model.getValueAt(0, 5));
      assertEquals(null, model.getValueAt(0, 6));
      assertEquals(null, model.getValueAt(0, 7));
      assertEquals(null, model.getValueAt(0, 8));
      assertEquals("row1-0", model.getValueAt(1, 0));
      assertEquals(null, model.getValueAt(1, 1));
      assertEquals(null, model.getValueAt(1, 2));
      assertEquals(null, model.getValueAt(1, 3));
      assertEquals(null, model.getValueAt(1, 4));
      assertEquals(null, model.getValueAt(1, 5));
      assertEquals(null, model.getValueAt(1, 6));
      assertEquals(null, model.getValueAt(1, 7));
      assertEquals(null, model.getValueAt(1, 7));
   }

   public void testShouldHaveEmptyRowAndColumnNamesOfSameSize() {
      assertEquals(model.getColumnNames().size(), model.getEmptyRow().length);
   }
}
