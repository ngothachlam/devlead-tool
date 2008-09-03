package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.BoardStatus;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

public class JiraTableModel extends MyTableModel {

   public static final String COLUMNNAME_HYPERLINK = "URL";
   protected static Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();

   static {
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Description, 1);
      columnNames.put(Column.FixVersion, 2);
      columnNames.put(Column.Status, 3);
      columnNames.put(Column.Resolution, 4);
      columnNames.put(Column.BuildNo, 5);
      columnNames.put(Column.URL, 6);
      columnNames.put(Column.BoardStatus, 7);
   }

   @Override
   protected Object[] getEmptyRow() {
      return new Object[] { "", "", "", "", "", "", "", BoardStatus.UnKnown };
   }

   private Logger log = MyLogger.getLogger(JiraTableModel.class);

   public JiraTableModel() {
      super(new Vector(columnNames.keySet()), 0);
   }

   public JiraTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      // FIXME - this needs to work dynamically if columns are added and removed in the spreadsheet!! I.e. if more or less
      // columns added than intended!
      this();
      List<Integer> convertInputHeaderToOriginal = getConvertionNumbers(header, columnNames);
      addNullsWhereRequired(convertInputHeaderToOriginal, columnNames);

      Vector<Vector> newDataVector = new Vector<Vector>();
      for (Vector<Object> vector : contents) {
         newDataVector.add(sortVectorBasedOnList(convertInputHeaderToOriginal, vector));
      }
      Vector newHeaderVector = sortVectorBasedOnList(convertInputHeaderToOriginal, header);
      this.setDataVector(newDataVector, newHeaderVector);
      log.debug("Initiated from existing contents and header!");
   }

   void addNullsWhereRequired(List<Integer> convertInputHeaderToOriginal, Map<Column, Integer> columnNames2) {
      // TODO Auto-generated method stub
   }

   <T> Vector<T> sortVectorBasedOnList(List<Integer> originalList, Vector<T> vector) {
      Vector<T> result = new Vector<T>();
      for (Integer integer : originalList) {
         T t = null;
         try {
            t = vector.get(integer);
         } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("exception -> ");
         }
         System.out.println("adding " + t + " from " + integer);
         result.add(t);
      }
      return result;
   }

   List<Integer> getConvertionNumbers(Vector<Column> mixedUpVector, Map<Column, Integer> originalVector) {
      List<Integer> list = new ArrayList();
      for (Column column : mixedUpVector) {
         Integer integer = originalVector.get(column);
         System.out.println("for " + column + " we are getting " + integer);
         list.add(integer);
      }
      return list;
   }

   public boolean addRow(JiraIssue jiraIssue) {
      if (!exists(jiraIssue.getKey())) {
         Object[] objects = new Object[] { jiraIssue.getKey(), jiraIssue.getSummary(), jiraIssue.getFixVersions(), jiraIssue.getStatus(),
               jiraIssue.getResolution(), jiraIssue.getBuildNo(), jiraIssue.getKey(), BoardStatus.UnKnown };
         super.addRow(objects);
         return true;
      }
      log.debug("jira already in model: " + jiraIssue.getKey());
      return false;
   }

   protected boolean exists(String name) {
      for (int row = 0; row < getRowCount(); row++) {
         if (getValueAt(row, 0).equals(name)) {
            return true;
         }
      }
      return false;
   }

   public boolean isRed(Object value, int row, int column) {
      return false;
   }

   @Override
   public boolean isCellEditable(int row, int column) {
      return column >= 6 ? false : (isEditable() ? false : false);
   }

   // Only required if the table is updated by the app so that it becomes visible to the user.
   public void setValueAt(Object value, int rowIndex, int columnIndex) {
      log.debug("Setting value" + "[value: " + value + ", row:" + rowIndex + ", col:" + columnIndex + "]");
      super.setValueAt(value, rowIndex, columnIndex);
   }

}
