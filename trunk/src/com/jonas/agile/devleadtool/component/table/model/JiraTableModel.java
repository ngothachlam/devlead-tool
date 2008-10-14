package com.jonas.agile.devleadtool.component.table.model;

import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;

public class JiraTableModel extends MyTableModel {

   // FIXME how do you run to get a special column of same object but with different object?

   private static final Column[] columns = { Column.Jira, Column.Description, Column.B_BoardStatus, Column.J_Type, Column.B_Release, Column.J_Sprint,
         Column.J_FixVersion, Column.J_Status, Column.J_Resolution, Column.J_BuildNo, Column.J_Dev_Estimate, Column.J_Dev_Spent, Column.Note };
   private BoardTableModel boardModel;
   private Logger log = MyLogger.getLogger(JiraTableModel.class);

   public BoardTableModel getBoardModel() {
      return boardModel;
   }

   public void setBoardModel(BoardTableModel boardModel) {
      this.boardModel = boardModel;
   }

   public JiraTableModel() {
      super(columns);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   @Override
   public boolean isRed(Object value, int row, int column) {
      return false;
   }

   @Override
   public Object getValueAt(int row, int column) {
      log.debug("getValueAt for row " + row + " column " + column);
      //FIXME make this prettier!
      if (boardModel != null) {
         Column column2 = getColumn(column);
         log.debug("for column " + column2);
         String jira = "";
         switch (column2) {
         case B_BoardStatus:
            jira = (String) getValueAt(Column.Jira, row);
            BoardStatusValue status = boardModel.getStatus(jira);
            log.debug("with Status " + status);
            return status;
         case B_Release:
            String release = "N/A";
            jira = (String) getValueAt(Column.Jira, row);
            int boardRowWithJira = boardModel.getRowWithJira(jira, Column.Jira);
            if (boardRowWithJira >= 0) {
               release = (String) boardModel.getValueAt(Column.Release, boardRowWithJira);
            }
            log.debug("Getting jira " + jira + " from jira Row " + row + ", contained on the boardModel row " + boardRowWithJira + " which has release " + release);
            return release;
         default:
            break;
         }
      }
      return super.getValueAt(row, column);
   }
}
