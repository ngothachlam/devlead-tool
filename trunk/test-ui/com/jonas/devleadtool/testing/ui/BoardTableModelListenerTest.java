package com.jonas.devleadtool.testing.ui;

import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import com.jonas.agile.devleadtool.component.listener.BoardAndJiraSyncListener;
import com.jonas.agile.devleadtool.component.listener.MyTableListener;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.JiraCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.MyDefaultCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class BoardTableModelListenerTest extends JonasTestCase {

   private MyTable boardTable;
   private MyTable jiraTable;

   protected void setUp() throws Exception {
      BoardTableModel boardModel = new BoardTableModel();
      JiraTableModel jiraModel = new JiraTableModel();

      boardTable = new MyTable("board", boardModel);
      jiraTable = new MyTable("jira", jiraModel);
      boardTable.addListener(new MyTableListener() {
         @Override
         public void jiraRemoved(String jira) {
            jiraTable.setValueAt(BoardStatusValue.NA, jira, Column.B_BoardStatus);
            jiraTable.setValueAt("", jira, Column.B_Release);
         }
      });

      BoardAndJiraSyncListener boardTableModelListener = new BoardAndJiraSyncListener(boardTable, jiraTable, boardModel);
      boardModel.addTableModelListener(boardTableModelListener);

      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   public void testShouldUpdateJiraWhenJiraAddedToBoard() {
      boardTable.addJira("llu-1");
      jiraTable.addJira("llu-1");

      assertEquals(BoardStatusValue.NA, jiraTable.getValueAt(Column.B_BoardStatus, "llu-1"));
      assertEquals("", jiraTable.getValueAt(Column.B_Release, "llu-1"));

      boardTable.setValueAt(Boolean.TRUE, "llu-1", Column.isInProgress);

      assertEquals(BoardStatusValue.InProgress, jiraTable.getValueAt(Column.B_BoardStatus, "llu-1"));
      assertEquals("", jiraTable.getValueAt(Column.B_Release, "llu-1"));

      boardTable.setValueAt("R1", "llu-1", Column.Release);

      assertEquals(BoardStatusValue.InProgress, jiraTable.getValueAt(Column.B_BoardStatus, "llu-1"));
      assertEquals("R1", jiraTable.getValueAt(Column.B_Release, "llu-1"));
   }

   public void testShouldUpdateJiraWhenJiraRemovedFromBoard() {
      boardTable.addJira("llu-1");
      jiraTable.addJira("llu-1");

      boardTable.setValueAt(Boolean.TRUE, "llu-1", Column.isInProgress);
      boardTable.setValueAt("R1", "llu-1", Column.Release);

      assertEquals(BoardStatusValue.InProgress, jiraTable.getValueAt(Column.B_BoardStatus, "llu-1"));
      assertEquals("R1", jiraTable.getValueAt(Column.B_Release, "llu-1"));

      boardTable.selectAll();
      boardTable.removeSelectedRows();

      assertEquals(0, boardTable.getRowCount());
      assertEquals(BoardStatusValue.NA, jiraTable.getValueAt(Column.B_BoardStatus, "llu-1"));
      assertEquals("", jiraTable.getValueAt(Column.B_Release, "llu-1"));
   }

   public void testShouldUpdateJiraWhenJiraUpdatedInBoard() {

      boardTable.addJira("llu-1");
      jiraTable.addJira("llu-1");
      jiraTable.addJira("llu-2");

      boardTable.setValueAt(Boolean.TRUE, "llu-1", Column.isInProgress);
      boardTable.setValueAt("R1", "llu-1", Column.Release);

      assertEquals(1, boardTable.getRowCount());
      assertEquals(BoardStatusValue.InProgress, jiraTable.getValueAt(Column.B_BoardStatus, "llu-1"));
      assertEquals("R1", jiraTable.getValueAt(Column.B_Release, "llu-1"));
      assertEquals(BoardStatusValue.NA, jiraTable.getValueAt(Column.B_BoardStatus, "llu-2"));
      assertEquals("", jiraTable.getValueAt(Column.B_Release, "llu-2"));

      simulateEditToTable(boardTable, "LLU-2", 0, Column.Jira);

      assertEquals(1, boardTable.getRowCount());
      assertEquals("LLU-2", boardTable.getValueAt(Column.Jira, "llu-2"));
      assertEquals(BoardStatusValue.InProgress, jiraTable.getValueAt(Column.B_BoardStatus, "llu-2"));
      assertEquals("R1", jiraTable.getValueAt(Column.B_Release, "llu-2"));
      assertEquals(BoardStatusValue.NA, jiraTable.getValueAt(Column.B_BoardStatus, "llu-1"));
      assertEquals("", jiraTable.getValueAt(Column.B_Release, "llu-1"));

   }

   private void simulateEditToTable(MyTable table, String value, int row, Column column) {
      int columnIndex = table.getColumnIndex(column);
      JiraCellEditor editor = (JiraCellEditor) table.getCellEditor(row, columnIndex);
      boardTable.editCellAt(0, 0);
      editor.getTableCellEditorComponent(boardTable, value, true, row, columnIndex);
      boardTable.editingStopped(new ChangeEvent(editor));
   }
}
