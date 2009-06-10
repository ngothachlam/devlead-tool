package com.jonas.agile.devleadtool.gui.component.table.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.junit.Test;

import com.jonas.agile.devleadtool.data.BoardStatusValueToJiraStatusMap;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.column.BoardStatusValue;
import com.jonas.agile.devleadtool.gui.component.table.column.IssueType;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;
import com.jonas.common.swing.SwingUtil;

public class BoardTableModelTest extends JonasTestCase {

   String[] tableHeader;
   Object[][] tableContents;
   MyTableModel model;

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      model = new BoardTableModel(null);
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
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
   }

   public void testGetEmptyRowFromBodard() {
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(null, null));
      assertTrue(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Approved, "Closed (Fixed)"));
      assertFalse(BoardStatusValueToJiraStatusMap.isMappedOk(BoardStatusValue.Failed, "Closed (Fixed)"));
   }

   public void testShouldGetColors() {
      assertEquals(SwingUtil.cellLightRed, model.getColor(BoardStatusValue.Failed, 0, ColumnType.BoardStatus));
   }

   @Test
   public void shouldColorCodeDEst() {
      assertEquals(0, model.getRowCount());
      assertEquals(SwingUtil.cellLightRed, model.getColor(null, 0, ColumnType.DEst));
      String jira = "llu-1";
      model.addJira(jira);
      model.setValueAt(null, jira, ColumnType.DEst);
      model.setValueAt(IssueType.TBD, jira, ColumnType.Type);
      assertEquals(SwingUtil.cellLightRed, model.getColor(null, 0, ColumnType.DEst));
      assertEquals(SwingUtil.cellLightRed, model.getColor("1.0", 0, ColumnType.DEst));
   }

}
