package com.jonas.agile.devleadtool.component.listener;

import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
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
      log.debug("First row: " + e.getFirstRow() + " Last row: " + e.getLastRow() + " Column: " + e.getColumn() + " EventType: " + e.getType() + " Source: "
            + e.getSource());
      updateJiraTable(e);
   }

   private void updateJiraTable(TableModelEvent e) {
      MyTableModel source = (MyTableModel) e.getSource();
      if (e.getColumn() == e.ALL_COLUMNS && e.getType() == TableModelEvent.INSERT) {
         for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
            String jira = (String) source.getValueAt(Column.Jira, row);
            if (boardTable.doesJiraExist(jira)) {
               BoardStatusValue status = boardModel.getStatus(jira);
               String release = boardModel.getRelease(jira);
               log.debug("Trying to update Jira Table with Jira: " + jira + ", Status: " + status + " and Release: " + release);
               updateJiraTable(jira, status, release);
            }
         }
      } else if (source.getColumnIndex(Column.Jira) == e.getColumn() && e.getType() == TableModelEvent.UPDATE) {
         for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
            String jira = (String) source.getValueAt(Column.Jira, row);
            if (boardTable.doesJiraExist(jira)) {
               BoardStatusValue status = boardModel.getStatus(jira);
               String release = (String) boardTable.getValueAt(Column.Release, jira);
               log.debug("Trying to update Jira Table with Jira: " + jira + ", Status: " + status + " and Release: " + release);
               updateJiraTable(jira, status, release);
            }
         }
      }
   }

   private void updateJiraTable(final String jira, final BoardStatusValue status, final String release) {
      SwingWorker worker = new SwingWorker() {
         @Override
         protected Object doInBackground() throws Exception {
            return null;
         }

         @Override
         protected void done() {
            jiraTable.setValueAt(status, jira, Column.B_BoardStatus);
            jiraTable.setValueAt(release, jira, Column.B_Release);
         }

         @Override
         protected void process(List chunks) {
         }
      };
      worker.execute();
   }
}