package com.jonas.agile.devleadtool.gui.component.table.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import com.jonas.agile.devleadtool.data.BoardStatusValueToJiraStatusMap;
import com.jonas.agile.devleadtool.gui.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.Column;
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

   public void testShouldGetStatusOk() {
      assertEquals(BoardStatusValue.NA, model.getStatus("llu-1"));

      model.addJira("llu-1");
      assertEquals(BoardStatusValue.UnKnown, model.getStatus("llu-1"));

      model.setValueAt(BoardStatusValue.Open, 0, Column.BoardStatus);
      assertEquals(BoardStatusValue.Open, model.getStatus("llu-1"));

      model.setValueAt(BoardStatusValue.Bug, 0, Column.BoardStatus);
      assertEquals(BoardStatusValue.Bug, model.getStatus("llu-1"));
   }

   public void testShouldHaveEmptyRowAndColumnNamesOfSameSize() {
      assertEquals(model.getColumnNames().size(), model.getEmptyRow().length);
   }

   public void testShouldGetAnyBoolColumnBackOk() {
      model.addJira(new JiraIssue("LLU-1", "llu-1 summary", "llu-1 status", "llu-1 resolution", "llu-1 type", "created"));

      assertEquals(1, model.getRowCount());

      assertEquals(BoardStatusValue.UnKnown, model.getValueAt(Column.BoardStatus, "LLU-1"));
      Set<BoardStatusValue> list = getSet(BoardStatusValue.Bug, BoardStatusValue.Complete, BoardStatusValue.InProgress);
      assertEquals(false, model.isBoardValueEither(0, list));

      // set To Open
      model.setValueAt(BoardStatusValue.Open, 0, Column.BoardStatus);
      assertEquals(BoardStatusValue.Open, model.getValueAt(Column.BoardStatus, "LLU-1"));
      list = getSet(BoardStatusValue.Open);
      assertEquals(true, model.isBoardValueEither(0, list));
      list = getSet(BoardStatusValue.Bug, BoardStatusValue.Complete, BoardStatusValue.InProgress, BoardStatusValue.Resolved);
      assertEquals(false, model.isBoardValueEither(0, list));
      list = getSet(BoardStatusValue.Bug, BoardStatusValue.Complete, BoardStatusValue.InProgress, BoardStatusValue.Resolved,
            BoardStatusValue.Open);
      assertEquals(true, model.isBoardValueEither(0, list));
      // set To Complete
      model.setValueAt(BoardStatusValue.Complete, 0, Column.BoardStatus);
      assertEquals(BoardStatusValue.Complete, model.getValueAt(Column.BoardStatus, "LLU-1"));
      list = getSet(BoardStatusValue.Bug, BoardStatusValue.Complete, BoardStatusValue.InProgress, BoardStatusValue.Resolved,
            BoardStatusValue.Open);
      assertEquals(true, model.isBoardValueEither(0, list));
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

      Vector<Column> header = new Vector<Column>();
      header.add(Column.Jira);
      header.add(Column.Description);
      header.add(Column.Resolution);
      header.add(Column.Release);
      header.add(Column.Merge);
      header.add(Column.BoardStatus);
      header.add(Column.Old);
      header.add(Column.DEst);
      header.add(Column.DAct);
      header.add(Column.QEst);
      header.add(Column.prio);
      header.add(Column.Note);
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
      assertEquals(SwingUtil.cellLightRed, model.getColor(BoardStatusValue.Bug, 0, Column.BoardStatus));
   }
}
