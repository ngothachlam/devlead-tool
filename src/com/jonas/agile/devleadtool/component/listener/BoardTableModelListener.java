package com.jonas.agile.devleadtool.component.listener;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.common.logging.MyLogger;

/**
 * When the Board Table values are updated, we need to update the JiraTable's Release and BoardStatus values.
 */
public class BoardTableModelListener implements TableModelListener {

   private final Logger log = MyLogger.getLogger(BoardTableModelListener.class);
   private final MyTable boardTable;
   private final MyTable jiraTable;
   private final BoardTableModel boardModel;

   public BoardTableModelListener(MyTable boardTable, MyTable jiraTable, BoardTableModel boardModel) {
      this.boardTable = boardTable;
      this.jiraTable = jiraTable;
      this.boardModel = boardModel;
   }

   public void tableChanged(TableModelEvent e) {
      log.debug("First row: " + e.getFirstRow() + " Last row: " + e.getLastRow() + " EventType: " + e.getType());
      updateJiraTable(e);
   }

   private void updateJiraTable(TableModelEvent e) {
      if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.UPDATE)
         for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
            String jira = (String) boardTable.getValueAt(Column.Jira, row);
            BoardStatusValue status = boardModel.getStatus(jira);
            jiraTable.setValueAt(status, jira, Column.B_BoardStatus);
            String release = (String) boardTable.getValueAt(Column.Release, jira);
            jiraTable.setValueAt(release, jira, Column.B_Release);
            log.debug("For row " + row + " in board (jira " + jira + ") I'm trying to set the release to \"" + release + "\" and boardStatus to " + status);
         }
   }
}