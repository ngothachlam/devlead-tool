package com.jonas.agile.devleadtool.component.table.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

public class JiraTableModel extends MyTableModel {

   public static final String COLUMNNAME_HYPERLINK = "URL";
   protected static Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();

   private Logger log = MyLogger.getLogger(JiraTableModel.class);

   static {
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Description, 1);
      columnNames.put(Column.FixVersion, 2);
      columnNames.put(Column.Status, 3);
      columnNames.put(Column.Resolution, 4);
      columnNames.put(Column.BuildNo, 5);
      columnNames.put(Column.Estimate, 6);
   }

   public JiraTableModel() {
      super(columnNames);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(contents, header);
   }

   public boolean addRow(JiraIssue jiraIssue) {
      if (!exists(jiraIssue.getKey())) {
         Object[] objects = new Object[] { jiraIssue.getKey(), jiraIssue.getSummary(), jiraIssue.getFixVersions(), jiraIssue.getStatus(),
               jiraIssue.getResolution(), jiraIssue.getBuildNo(), jiraIssue.getEstimate()};
         super.addRow(objects);
         return true;
      }
      log.debug("jira already in model: " + jiraIssue.getKey());
      return false;
   }

   @Override
   public boolean isCellEditable(int row, int column) {
      return column >= 6 ? false : (isEditable() ? false : false);
   }

   protected boolean exists(String name) {
      for (int row = 0; row < getRowCount(); row++) {
         if (getValueAt(row, 0).equals(name)) {
            return true;
         }
      }
      return false;
   }

   @Override
   protected Object[] getEmptyRow() {
      return new Object[] { "", "", "", "", "", "", null};
   }

   @Override
   Map<Column, Integer> getColumnNames() {
      return columnNames;
   }

}
