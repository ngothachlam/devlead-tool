package com.jonas.agile.devleadtool.component.table.model;

import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class PlanTableModel extends MyTableModel {

   private Logger log = MyLogger.getLogger(PlanTableModel.class);
   private static final ColumnDataType[] columns = { ColumnDataType.Jira, ColumnDataType.Description, ColumnDataType.J_Type, ColumnDataType.Planned_Sprint, ColumnDataType.Resolved_Sprint, ColumnDataType.Closed_Sprint, ColumnDataType.Dev_Estimate, ColumnDataType.QA_Estimate, ColumnDataType.Dev_Actual, ColumnDataType.Note };

   public PlanTableModel() {
      super(columns);
   }

   public PlanTableModel(Vector<Vector<Object>> contents, Vector<ColumnDataType> header) {
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

   public Object getValueAt(ColumnDataType column, String jira) {
      int columnNo = this.getColumnNo(column);
      int rowNo = this.getJiraRow(jira);
      return this.getValueAt(rowNo, columnNo);
   }

}
