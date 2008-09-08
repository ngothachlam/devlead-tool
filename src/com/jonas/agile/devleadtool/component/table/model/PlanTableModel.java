package com.jonas.agile.devleadtool.component.table.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class PlanTableModel extends MyTableModel {

   protected static Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
//   private static Vector<Column> columnNames = new Vector<Column>();

   private Logger log = MyLogger.getLogger(PlanTableModel.class);

   static {
      columnNames.put(Column.Jira,0);
      columnNames.put(Column.Description,1);
      columnNames.put(Column.Type,2);
      columnNames.put(Column.Planned_Sprint,3);
      columnNames.put(Column.Resolved_Sprint,4);
      columnNames.put(Column.Closed_Sprint,5);
      columnNames.put(Column.Estimate,6);
      columnNames.put(Column.Actual,7);
      columnNames.put(Column.Note,8);
   }

   public PlanTableModel() {
      super(columnNames);
   }

   public PlanTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(contents, header);
   }

   public void addJira(String jira){
      Object[] objects = new Object[] { jira, null, null, null, null, null, null, null, null };
      super.addRow(objects);
   }
   
   public boolean doesJiraExist(String jira) {
      for (int row = 0; row < getRowCount(); row++) {
         log.debug("checking row " + row);
         if (jira.equalsIgnoreCase((String) getValueAt(row, 0)))
            return true;
      }
      return false;
   }

   @Override
   public boolean isCellEditable(int row, int column) {
      return isEditable() ? true : false;
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

   @Override
   protected Object[] getEmptyRow() {
      return new Object[] { "", "", "", "", "", "", "", "", "" };
   }

   @Override
   Map<Column, Integer> getColumnNames() {
      return columnNames;
   }

}
