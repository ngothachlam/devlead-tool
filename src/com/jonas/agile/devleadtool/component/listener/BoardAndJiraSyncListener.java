package com.jonas.agile.devleadtool.component.listener;

import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.JiraCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

/**
 * When the Board Table values are updated, we need to update the JiraTable's Release and BoardStatus values.
 */
public class BoardAndJiraSyncListener implements TableModelListener {

   private final Logger log = MyLogger.getLogger(BoardAndJiraSyncListener.class);
   private final MyTable boardTable;
   private final MyTable jiraTable;
   private final BoardTableModel boardModel;

   public BoardAndJiraSyncListener(MyTable boardTable, MyTable jiraTable, BoardTableModel boardModel) {
      this.boardTable = boardTable;
      this.jiraTable = jiraTable;
      this.boardModel = boardModel;
   }

   public void tableChanged(TableModelEvent e) {
      log.debug("First row: " + e.getFirstRow() + " Last row: " + e.getLastRow() + " Column: " + e.getColumn() + "(all Columns:" + e.ALL_COLUMNS + ") EventType: "
            + e.getType() + "(insert: " + TableModelEvent.INSERT + ", update: " + TableModelEvent.UPDATE + ") Source: " + e.getSource());
      MyTableModel source = (MyTableModel) e.getSource();
      if (e.getColumn() == e.ALL_COLUMNS && e.getType() == TableModelEvent.INSERT) {
         log.debug("All columns and insert!");
         for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
            String jira = (String) source.getValueAt(Column.Jira, row);
            if (boardTable.doesJiraExist(jira)) {
               BoardStatusValue status = boardModel.getStatus(jira);
               String release = boardModel.getRelease(jira);
               log.debug("Trying to update Jira Table with Jira: " + jira + ", Status: " + status + " and Release: " + release);
               updateJiraTable(jira, status, release);
            }
         }
      } else if (notSameSourceAsLast(e) && isColumnsUpdatedEither(e, source, Column.Jira, Column.BoardStatus, Column.Release)
            && e.getType() == TableModelEvent.UPDATE) {
         log.debug("Jira and update!" + e.getFirstRow() + " to " + e.getLastRow());
         for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
            String jira = (String) source.getValueAt(Column.Jira, row);
            if (boardTable.doesJiraExist(jira)) {
               BoardStatusValue status = boardModel.getStatus(jira);
               String release = (String) boardTable.getValueAt(Column.Release, jira);
               log.debug("Trying to update Jira Table with Jira: " + jira + ", Status: " + status + " and Release: " + release);
               updateJiraTable(jira, status, release);
            }
         }
      } else if (notSameSourceAsLast(e) && isColumnsUpdatedEither(e, source, Column.Dev_Actual, Column.Dev_Estimate) && e.getType() == TableModelEvent.UPDATE) {
         log.debug("Jira and update!" + e.getFirstRow() + " to " + e.getLastRow());
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               jiraTable.fireTableDataChanged();
            }
         });
      }
   }

   private boolean notSameSourceAsLast(TableModelEvent e) {
      return e.getSource() != jiraTable.getModel();
   }

   private boolean isColumnsUpdatedEither(TableModelEvent e, MyTableModel source, Column... columns) {
      for (int i = 0; i < columns.length; i++) {
         Column column = columns[i];
         if (source.getColumnIndex(column) == e.getColumn())
            return true;
      }
      return false;
   }

   private void updateJiraTable(final String jira, final BoardStatusValue status, final String release) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            jiraTable.setValueAt(status, jira, Column.B_BoardStatus);
            jiraTable.setValueAt(release, jira, Column.B_Release);
         }
      });
   }
}