package com.jonas.agile.devleadtool.component.table.model;

import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class PlanTableModel extends MyTableModel {

   private Logger log = MyLogger.getLogger(PlanTableModel.class);
   private static final Column[] columns = { Column.Jira, Column.Description, Column.J_Type, Column.Release, Column.Planned_Sprint, Column.Resolved_Sprint, Column.Closed_Sprint, Column.Dev_Estimate, Column.QA_Estimate, Column.Dev_Actual, Column.Note };

   public PlanTableModel() {
      super(columns);
   }

   public PlanTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(columns, contents, header);
   }

   public boolean setRow(JiraIssue jiraIssue, int row) {
      log.debug("overwriting jira: " + jiraIssue.getKey() + " to model on row: " + row);
      List<JiraVersion> fixVersions = jiraIssue.getFixVersions();
      if (fixVersions.size() > 1) {
         throw new RuntimeException(jiraIssue + " has more than one fixversion! Model can't handle it at the moment!!");
      }
      JiraVersion jiraVersion = fixVersions.size() > 0 ? fixVersions.get(0) : null;
      Object[] object = new Object[] { jiraIssue.getKey(), jiraVersion, jiraIssue.getStatus(), jiraIssue.getResolution() };
      for (int i = 0; i < object.length; i++) {
         setValueAt(object[i], row, i);
      }
      return true;
   }

   public Object getValueAt(Column column, String jira) {
      int columnNo = this.getColumnIndex(column);
      int rowNo = this.getRowWithJira(jira, Column.Jira);
      return this.getValueAt(rowNo, columnNo);
   }

}
