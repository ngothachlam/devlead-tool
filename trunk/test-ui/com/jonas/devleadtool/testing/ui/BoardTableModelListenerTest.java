package com.jonas.devleadtool.testing.ui;

import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import com.jonas.agile.devleadtool.component.listener.TableSyncerFromBoardToJiraListener;
import com.jonas.agile.devleadtool.component.listener.TableListener;
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

      boardTable = new MyTable("board", boardModel, true);
      jiraTable = new MyTable("jira", jiraModel, false);
      boardTable.addListener(new TableListener() {
         @Override
         public void jiraRemoved(String jira) {
         }
      });

      TableSyncerFromBoardToJiraListener boardTableModelListener = new TableSyncerFromBoardToJiraListener(boardTable, jiraTable, boardModel);
      boardModel.addTableModelListener(boardTableModelListener);

      super.setUp();
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }

   private void simulateEditToTable(MyTable table, String value, int row, Column column) {
      int columnIndex = table.getColumnIndex(column);
      JiraCellEditor editor = (JiraCellEditor) table.getCellEditor(row, columnIndex);
      boardTable.editCellAt(0, 0);
      editor.getTableCellEditorComponent(boardTable, value, true, row, columnIndex);
      boardTable.editingStopped(new ChangeEvent(editor));
   }
}
