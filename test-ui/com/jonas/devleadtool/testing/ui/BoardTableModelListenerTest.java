package com.jonas.devleadtool.testing.ui;

import javax.swing.event.ChangeEvent;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.editor.JiraCellEditor;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.listener.TableListener;
import com.jonas.agile.devleadtool.junitutils.JonasTestCase;

public class BoardTableModelListenerTest extends JonasTestCase {

   private MyTable boardTable;
   private MyTable jiraTable;

   @Override
   protected void setUp() throws Exception {
      MyTableModel boardModel = new BoardTableModel(null);
      MyTableModel jiraModel = new JiraTableModel();

      boardTable = new MyTable("board", boardModel, true);
      jiraTable = new MyTable("jira", jiraModel, false);
      boardTable.addListener(new TableListener() {
         @Override
         public void jiraRemoved(String jira) {
         }
      });

      super.setUp();
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   private void simulateEditToTable(MyTable table, String value, int row, ColumnType column) {
      int columnIndex = table.getColumnIndex(column);
      JiraCellEditor editor = (JiraCellEditor) table.getCellEditor(row, columnIndex);
      boardTable.editCellAt(0, 0);
      editor.getTableCellEditorComponent(boardTable, value, true, row, columnIndex);
      boardTable.editingStopped(new ChangeEvent(editor));
   }
}
