package com.jonas.agile.devleadtool.component.table.model;

import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class PlanTableModel extends MyTableModel {

   public static final String COLUMNNAME_HYPERLINK = "URL";
   private static Vector<String> columnNames = new Vector<String>();

   static {
      columnNames.add("Jira");
      columnNames.add("FixVersion");
      columnNames.add("Status");
      columnNames.add("Resolution");
      columnNames.add(COLUMNNAME_HYPERLINK);
   }

   @Override
   protected Object[] getEmptyRow() {
      return new Object[] { "", "", "", "", "" };
   }

   public void setValueAt(Object value, int row, int column) {
      super.setValueAt(value, row, column);
      fireTableRowsUpdated(0, this.getRowCount() - 1);
   }

   private Logger log = MyLogger.getLogger(PlanTableModel.class);

   public PlanTableModel() {
      super(columnNames, 0);
   }

   public PlanTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      super(contents, header);
   }

   @Override
   public boolean isCellEditable(int row, int column) {
      return isEditable() ? true : false;
   }

   public boolean isRed(Object value, int row, int column) {
      log.debug("isRed: " + value + " row: " + row + " col: " + column);
      switch (column) {
      case 0:
    	  return countOfSameValueInColumn(value, column) > 1;
      case 1:
         return value == null || value.toString().length() == 0;
      default:
         break;
      }
      return false;
   }
   

   public boolean addRow(JiraIssue jiraIssue) {
      List<JiraVersion> fixVersions = jiraIssue.getFixVersions();
      if (fixVersions.size() > 1) {
         throw new RuntimeException(jiraIssue + " has more than one fixversion! Model can't handle it at the moment!!");
      }
      JiraVersion jiraVersion = null;
      if (fixVersions != null && fixVersions.size() > 0) {
         jiraVersion = fixVersions.get(0);
      }
      Object[] objects = new Object[] { jiraIssue.getKey(), jiraVersion, jiraIssue.getStatus(), jiraIssue.getResolution() };
      super.addRow(objects);
      log.debug("adding jira to plan: " + jiraIssue.getKey());
      fireTableRowsUpdated(0, this.getRowCount() - 1);
      return true;
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

   public boolean doesJiraExist(String jira) {
      for (int row = 0; row < getRowCount(); row++) {
         log.debug("checking row " + row);
         if (jira.equalsIgnoreCase((String) getValueAt(row, 0)))
            return true;
      }
      return false;
   }

}
