package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

public abstract class MyTableModel extends DefaultTableModel {
   // public abstract class MyTableModel extends TableSorter {

   protected Counter counter = new Counter();
   private Logger log = MyLogger.getLogger(MyTableModel.class);
   protected Map<ColumnDataType, Integer> columnNames = new LinkedHashMap<ColumnDataType, Integer>();
   protected boolean editable = true;

   public MyTableModel(ColumnDataType[] columns) {
      super(columns, 0);
      initiateColumns(columns);
   }

   MyTableModel(ColumnDataType[] columns, Vector<Vector<Object>> contents, Vector<ColumnDataType> header) {
      initiateColumns(columns);
      List<Integer> convertInputHeaderToOriginal = getConvertionNumbers(header, getColumnNames());

      Vector<Vector<Object>> newDataVector = new Vector<Vector<Object>>();
      for (Vector<Object> vector : contents) {
         newDataVector.add(sortVectorBasedOnList(convertInputHeaderToOriginal, vector));
      }
      Vector<ColumnDataType> newHeaderVector = sortHeaderBasedOnList(convertInputHeaderToOriginal, header, getColumnNames());
      this.setDataVector(newDataVector, newHeaderVector);
      log.debug("Initiated from existing contents and header!");
   }

   final public void addEmptyRow() {
      this.addRow(getEmptyRow());
   }

   final public void addJira(String jira) {
      if (!doesJiraExist(jira)) {
         Object[] row = getEmptyRow();
         row[0] = jira.toUpperCase();
         log.debug("addJira of row size: " + row.length);
         addRow(row);
      }
   }

   final public Class<?> getColumnClass(int columnIndex) {
      Object valueAt = getValueAt(0, columnIndex);
      return valueAt != null ? getClass(valueAt) : getClassFromColumn(columnIndex);
   }

   final public ColumnDataType getColumnEnum(int columnNo) {
      return ColumnDataType.getEnum(getColumnName(columnNo));
   }

   final public Map<ColumnDataType, Integer> getColumnNames() {
      return columnNames;
   }

   final public int getColumnNo(ColumnDataType column) {
      for (int col = 0; col < getColumnCount(); col++) {
         if (getColumnName(col).equals(column.toString())) {
            return col;
         }
      }
      return -1;
   }

   // final public Object[] getEmptyRow() {
   // Map<Column, Integer> colnams = getColumnNames();
   // Object[] objects = new Object[colnams.size()];
   // int i = 0;
   // log.debug("getting Empty Row");
   // for (Column column : colnams.keySet()) {
   // ColumnDTO dto = ColumnDTO.getColumnDTO(column);
   // objects[i++] = dto.getDefaultValue();
   // log.debug("column: " + column + " containing: " + objects[i - 1]);
   // }
   // return objects;
   // }

   // final public Class[] getEmptyRowClass() {
   // Map<Column, Integer> colnams = getColumnNames();
   // Class[] objects = new Object[colnams.size()];
   // int i = 0;
   // log.debug("getting Empty Row");
   // for (Column column : colnams.keySet()) {
   // ColumnDTO dto = ColumnDTO.getColumnDTO(column);
   // objects[i++] = dto.getClass();
   // log.debug("column: " + column + " containing: " + objects[i - 1]);
   // }
   // return objects;
   // }
   final public Object[] getEmptyRow() {
      Map<ColumnDataType, Integer> colnams = getColumnNames();
      Object[] objects = new Object[colnams.size()];
      int i = 0;
      log.debug("getting Empty Row");
      for (ColumnDataType column : colnams.keySet()) {
         objects[i++] = column.getCellContentDefault();
         log.debug("column: " + column + " containing: " + objects[i - 1]);
      }
      return objects;
   }

   /**
    * Returns -1 if cannot be found otherwise first instance of occurence.
    * 
    * @param value
    * @param columnNo
    * @param column
    * @return
    */
   final public int getRowOfSameValueInColumn(Object value, ColumnDataType column) {
      int columnNo = getColumnNo(column);
      for (int i = 0; i < this.getRowCount(); i++) {
         Object valueAt = this.getValueAt(i, columnNo);
         if (value instanceof String) {
            String stringValue = (String) value;
            String stringRowValue = (String) valueAt;
            if ((stringValue).length() > 0 && stringValue.equalsIgnoreCase(stringRowValue)) {
               return i;
            }
         } else if (valueAt.equals(value)) {
            return i;
         }
      }
      return -1;
   }

   final public Object getValueAt(ColumnDataType column, int row) {
      return getValueAt(row, getColumnNo(column));
   }

   final public boolean isCellEditable(int row, int column) {
      return isEditable() ? true : false;
   }

   final public boolean isEditable() {
      return editable;
   }

   final public void setEditable(boolean selected) {
      editable = selected;
      if (getRowCount() > 0) {
         fireTableStructureChanged();
      }
      // TODO need to fireUpdate on Table?
   }

   // Only required if the table is updated by the app so that it becomes visible to the user.
   final public void setValueAt(Object value, int rowIndex, int columnIndex) {
      super.setValueAt(value, rowIndex, columnIndex);
      fireTableRowsUpdated(rowIndex, rowIndex);
   }

   final private Class<?> getClass(Object valueAt) {
      return valueAt.getClass();
   }

   final private Class<?> getClassFromColumn(int columnIndex) {
      return getEmptyRow()[columnIndex].getClass();
   }

   final public boolean doesJiraExist(String name) {
      for (int row = 0; row < getRowCount(); row++) {
         if (name.equalsIgnoreCase((String) getValueAt(ColumnDataType.Jira, row))) {
            return true;
         }
      }
      return false;
   }

   final public int getJiraRow(String name) {
      for (int row = 0; row < getRowCount(); row++) {
         if (name.equalsIgnoreCase((String) getValueAt(ColumnDataType.Jira, row))) {
            return row;
         }
      }
      return -1;
   }

   final protected void initiateColumns(ColumnDataType[] columns) {
      counter.reset();
      for (ColumnDataType column : columns) {
         putIntoColumnNames(column);
      }
   }

   final protected void putIntoColumnNames(ColumnDataType column) {
      int valueAndIncrease = counter.getValueAndIncrease();
      log.debug("putIntoColumnNames: " + column + " of default type: " + column.getCellContentDefault() + " in position " + valueAndIncrease);
      columnNames.put(column, valueAndIncrease);
   }

   final ColumnDataType findIndexThatDoesNotExist(Map<ColumnDataType, Integer> columnNames, Vector<ColumnDataType> realVector, int i) {
      Set<ColumnDataType> keySet = columnNames.keySet();
      ColumnDataType[] columns = keySet.toArray(new ColumnDataType[columnNames.size()]);
      for (int temp = 0; temp < columns.length; temp++) {
         ColumnDataType column = columns[temp];
         log.debug("Column: " + column + " temp: " + temp + " of " + i);
         if (!realVector.contains(column)) {
            log.debug("RealVector contains the column!");
            if (temp == i) {
               return column;
            }
         } else {
            i++;
         }
      }
      return null;
   }

   final List<Integer> getConvertionNumbers(Vector<ColumnDataType> mixedUpVector, Map<ColumnDataType, Integer> originalVector) {
      List<Integer> list = new ArrayList();
      log.debug("mixedUpVectorSize: " + mixedUpVector.size());
      log.debug("originalVector: " + originalVector.size());
      if (mixedUpVector.size() == originalVector.size()) {
         for (ColumnDataType column : mixedUpVector) {
            Integer integer = originalVector.get(column);
            log.debug("for " + column + " we are getting " + integer);
            list.add(integer);
         }
      } else {
         for (ColumnDataType originalColumn : originalVector.keySet()) {
            Integer integer = -1;
            if (mixedUpVector.contains(originalColumn))
               integer = mixedUpVector.indexOf(originalColumn);
            list.add(integer);
         }
      }
      return list;
   }

   final Vector<ColumnDataType> sortHeaderBasedOnList(List<Integer> convertedList, Vector<ColumnDataType> realVector, Map<ColumnDataType, Integer> columnNames) {
      Vector<ColumnDataType> result = sortVectorBasedOnList(convertedList, realVector);
      int i = 0;
      List<ColumnDataType> newColumn = new ArrayList<ColumnDataType>();
      for (ColumnDataType column : result) {
         if (column == null) {
            newColumn.add(findIndexThatDoesNotExist(columnNames, realVector, i++));
            log.debug("newColumn: " + newColumn);
         }
      }
      for (ColumnDataType column : newColumn) {
         for (int j = 0; j < result.size(); j++) {
            if (result.get(j) == null) {
               result.remove(j);
               result.insertElementAt(column, j);
               break;
            }
         }
      }
      return result;
   }

   final <T> Vector<T> sortVectorBasedOnList(List<Integer> convertedList, Vector<T> realVector) {
      Vector<T> result = new Vector<T>();
      for (Integer integer : convertedList) {
         T t = null;
         try {
            t = realVector.get(integer);
         } catch (ArrayIndexOutOfBoundsException e) {
            log.warn("ArrayIndexOutOfBoundsException -> ");
         } catch (NullPointerException e) {
            log.warn("NullPointerException -> ");
         }
         log.debug("adding " + t + " from " + integer);
         result.add(t);
      }
      return result;
   }

   public void addJira(JiraIssue jiraIssue) {
      if (!doesJiraExist(jiraIssue.getKey())) {
         Set<ColumnDataType> columnSet = columnNames.keySet();
         Vector<Object> contents = new Vector<Object>();
         for (ColumnDataType column : columnSet) {
            switch (column) {
            case Jira:
               contents.add(jiraIssue.getKey());
               break;
            case Description:
               contents.add(jiraIssue.getSummary());
               break;
            case J_FixVersion:
               contents.add(jiraIssue.getFixVersions());
               break;
            case J_Status:
               contents.add(jiraIssue.getStatus());
               break;
            case J_Resolution:
               contents.add(jiraIssue.getResolution());
               break;
            case J_BuildNo:
               contents.add(jiraIssue.getBuildNo());
               break;
            case ListPrio:
               contents.add(jiraIssue.getLLUListPriority());
               break;
            case J_Dev_Estimate:
               contents.add(jiraIssue.getEstimate());
               break;
            case J_Dev_Spent:
               contents.add(jiraIssue.getSpent());
               break;
            case J_Type:
               contents.add(jiraIssue.getType());
               break;
            default:
               contents.add(column.getCellContentDefault());
               break;
            }
         }
         // Object[] objects = new Object[] { jiraIssue.getKey(), jiraIssue.getSummary(), jiraIssue.getFixVersions(), jiraIssue.getStatus(),
         // jiraIssue.getResolution(), jiraIssue.getBuildNo(), jiraIssue.getEstimate() };
         // super.addRow(objects);
         super.addRow(contents);
      }
   }

}
