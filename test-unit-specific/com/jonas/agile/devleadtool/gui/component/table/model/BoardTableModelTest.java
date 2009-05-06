package com.jonas.agile.devleadtool.gui.component.table.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import com.jonas.agile.devleadtool.data.BoardStatusValueToJiraStatusMap;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraIssue;

public class BoardTableModelTest extends JonasTestCase {

   String[] tableHeader;
   Object[][] tableContents;
   BoardTableModel model;

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      model = new BoardTableModel(null);
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldSetEditableCorrectly() {
      assertTrue(model.isCellEditable(0, 0));
      assertFalse(model.isCellEditable(0, 1));
      assertFalse(model.isCellEditable(0, 2));
      assertTrue(model.isCellEditable(0, 3));
      assertTrue(model.isCellEditable(0, 4));
      assertTrue(model.isCellEditable(0, 5));
      assertTrue(model.isCellEditable(0, 6));
      assertTrue(model.isCellEditable(0, 7));
   }

   public void testShouldHaveEmptyRowAndColumnNamesOfSameSize() {
      assertEquals(model.getColumnNames().size(), model.getEmptyRow().length);
   }

   private Set<BoardStatusValue> getSet(BoardStatusValue... values) {
      Set<BoardStatusValue> list = new HashSet<BoardStatusValue>();
      for (BoardStatusValue boardStatusValue : values) {
         list.add(boardStatusValue);
      }
      return list;
   }

   public void testShouldCreateFromConstructorOk() {
      Vector<Vector<Object>> contents = new Vector<Vector<Object>>();

      Vector<ColumnType> header = new Vector<ColumnType>();
      header.add(ColumnType.Jira);
      header.add(ColumnType.Description);
      header.add(ColumnType.Resolution);
      header.add(ColumnType.Release);
      header.add(ColumnType.Merge);
      header.add(ColumnType.BoardStatus);
      header.add(ColumnType.Old);
      header.add(ColumnType.DEst);
      header.add(ColumnType.DAct);
      header.add(ColumnType.QEst);
      header.add(ColumnType.prio);
      header.add(ColumnType.Note);
      contents.add(getTestContentRow(header.size(), "0."));
      contents.add(getTestContentRow(header.size(), "1."));

      MyTableModel model = new BoardTableModel(contents, header, null);

      assertEquals(2, model.getRowCount());
      assertEquals("0.0", model.getValueAt(0, 0));
      assertEquals("0.1", model.getValueAt(0, 1));
      assertEquals("0.2", model.getValueAt(0, 2));
      assertEquals("0.3", model.getValueAt(0, 3));
      assertEquals("0.4", model.getValueAt(0, 4));
      assertEquals("0.5", model.getValueAt(0, 5));
      assertEquals("0.6", model.getValueAt(0, 6));
      assertEquals("0.7", model.getValueAt(0, 7));
      assertEquals("1.0", model.getValueAt(1, 0));
      assertEquals("1.1", model.getValueAt(1, 1));
      assertEquals("1.2", model.getValueAt(1, 2));
      assertEquals("1.3", model.getValueAt(1, 3));
      assertEquals("1.4", model.getValueAt(1, 4));
      assertEquals("1.5", model.getValueAt(1, 5));
      assertEquals("1.6", model.getValueAt(1, 6));
      assertEquals("1.7", model.getValueAt(1, 7));
   }

   public void testGetEmptyRowFromBoard() {
      Object[] emptyRow = model.getEmptyRow();
      assertEquals("", emptyRow[0]); // Column.Jira
      assertEquals("", emptyRow[1]); // Column.Description
      assertEquals("", emptyRow[2]); // J_Resolution
      assertEquals("", emptyRow[3]); // Release
      assertEquals("", emptyRow[4]); // Merge
      assertEquals(BoardStatusValue.UnKnown, emptyRow[5]); // BoardStatus
      assertEquals(false, emptyRow[6]); // Old
      assertEquals("", emptyRow[7]); // Dev_Estimate
      assertEquals("", emptyRow[8]); // Dev_Remain
      assertEquals("", emptyRow[9]); // Dev Actual
      assertEquals("", emptyRow[10]); // QA Estimate
      assertEquals(null, emptyRow[11]); // Prio
      assertEquals("", emptyRow[12]); // Note
   }

   public void testGetEmptyRowFromBodard() {
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(null, null));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Approved, "Closed (Fixed)"));
      assertFalse(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Bug, "Closed (Fixed)"));
   }
   
   public void testShouldGetColors(){
      assertEquals(SwingUtil.cellLightRed, model.getColor(BoardStatusValue.Bug, 0, ColumnType.BoardStatus));
   }
}
