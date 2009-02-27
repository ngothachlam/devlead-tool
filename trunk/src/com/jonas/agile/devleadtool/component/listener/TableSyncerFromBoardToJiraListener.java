package com.jonas.agile.devleadtool.component.listener;

import javax.swing.SwingUtilities;
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
public class TableSyncerFromBoardToJiraListener implements TableModelListener {

   //TODO can we not delete this class now!!
   
   private final Logger log = MyLogger.getLogger(TableSyncerFromBoardToJiraListener.class);
   private final MyTable boardTable;
   private final MyTable jiraTable;
   private final BoardTableModel boardModel;

   public TableSyncerFromBoardToJiraListener(MyTable boardTable, MyTable jiraTable, BoardTableModel boardModel) {
      this.boardTable = boardTable;
      this.jiraTable = jiraTable;
      this.boardModel = boardModel;
   }

   public void tableChanged(final TableModelEvent e) {
      
      // TODO this need to only fireupdate on jira table.
      if (!isRightSource(e)) {
         log.error("Should never happen! The source of the event should be the boardTable model! It now is " + e.getSource().getClass());
         return;
      }
      log.debug("First row: " + e.getFirstRow() + " Last row: " + e.getLastRow() + " Column: " + e.getColumn() + "(all Columns:" + e.ALL_COLUMNS + ") EventType: "
            + e.getType() + "(insert: " + TableModelEvent.INSERT + ", update: " + TableModelEvent.UPDATE + ") Source: " + e.getSource());
      final MyTableModel sourceAsModel = (MyTableModel) e.getSource();
      if (e.getColumn() == e.ALL_COLUMNS && e.getType() == TableModelEvent.INSERT) {
         log.debug("All columns and insert!");
         for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
            String jira = (String) sourceAsModel.getValueAt(Column.Jira, row);
            BoardStatusValue status = boardModel.getStatus(jira);
            String release = boardModel.getRelease(jira);
            log.debug("Trying to update Jira Table with Jira: " + jira + ", Status: " + status + " and Release: " + release);
         }
      } else if (isColumnsUpdatedEither(e, sourceAsModel, Column.Jira, Column.BoardStatus, Column.Release) && e.getType() == TableModelEvent.UPDATE) {
         log.debug("Jira and update!" + e.getFirstRow() + " to " + e.getLastRow());
         for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
            String jira = (String) sourceAsModel.getValueAt(Column.Jira, row);
            BoardStatusValue status = boardModel.getStatus(jira);
            String release = (String) boardTable.getValueAt(Column.Release, jira);
            log.debug("Trying to update Jira Table with Jira: " + jira + ", Status: " + status + " and Release: " + release);
         }
      } else if (isColumnsUpdatedEither(e, sourceAsModel, Column.Dev_Actual, Column.Dev_Estimate) && e.getType() == TableModelEvent.UPDATE) {
         log.debug("Jira and update!" + e.getFirstRow() + " to " + e.getLastRow());
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
                  String jira = (String) sourceAsModel.getValueAt(Column.Jira, row);
                  jiraTable.fireTableDataChangedForJira(jira);
               }
            }
         });
      }
   }

   private boolean isRightSource(TableModelEvent e) {
      return e.getSource() == boardTable.getModel();
   }

   private boolean isColumnsUpdatedEither(TableModelEvent e, MyTableModel source, Column... columns) {
      for (int i = 0; i < columns.length; i++) {
         Column column = columns[i];
         if (source.getColumnIndex(column) == e.getColumn())
            return true;
      }
      return false;
   }

}