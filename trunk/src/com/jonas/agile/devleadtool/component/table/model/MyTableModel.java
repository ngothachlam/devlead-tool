package com.jonas.agile.devleadtool.component.table.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.listener.TableModelListenerAlerter;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

class Counter {
   private int i = 0;

   public int getValueAndIncrease() {
      return i++;
   }

   public void reset() {
      i = 0;
   }
}


public abstract class MyTableModel extends DefaultTableModel {

   private class JiraRowState implements TableModelListener {
      List<String> jiras = new ArrayList<String>();;

      private JiraRowState() {
         for (int row = 0; row < getRowCount(); row++) {
            jiras.add((String) getValueAt(row, 0));
         }
      }

      public boolean contains(String name) {
         return jiras.contains(name);
      }

      public int indexOf(String name) {
         return jiras.indexOf(name);
      }

      @Override
      public void tableChanged(TableModelEvent e) {
         int firstRow = e.getFirstRow();
         int lastRow = e.getLastRow();
         switch (e.getType()) {
         case TableModelEvent.INSERT:
            log.debug("insert firstRow: " + firstRow + " lastRow: " + lastRow);
            for (int row = firstRow; row <= lastRow; row++) {
               jiras.add((String) getValueAt(row, 0));
            }
            break;
         case TableModelEvent.DELETE:
            log.debug("delete firstRow: " + firstRow + " lastRow: " + lastRow);
            for (int row = firstRow; row <= lastRow; row++) {
               jiras.remove(row);
            }
            break;
         case TableModelEvent.UPDATE:
            log.debug("update firstRow: " + firstRow + " lastRow: " + lastRow);
            for (int row = firstRow; row <= lastRow; row++) {
               if (e.getColumn() == TableModelEvent.ALL_COLUMNS || e.getColumn() == 0)
                  jiras.remove(row);
               jiras.add(row, (String) getValueAt(row, 0));
            }
            break;
         }
      }

   }
   private class ModelMarker implements TableModelListener {

      private Map<Integer, Boolean> marked = new HashMap<Integer, Boolean>();

      private ModelMarker() {
         for (int row = 0; row < getRowCount(); row++) {
            marked.put(row, Boolean.FALSE);
         }
      }

      public void clearMarked() {
         for (int row = 0; row < getRowCount(); row++) {
            unMark(row);
         }
      }

      public boolean isMarked(int row) {
         if (marked == null || marked.size() == 0)
            return false;
         return marked.containsKey(row) ? marked.get(row) : false;
      }

      public void mark(int row) {
         marked.put(row, Boolean.TRUE);
      }

      @Override
      public void tableChanged(TableModelEvent e) {
         int firstRow = e.getFirstRow();
         int lastRow = e.getLastRow();
         switch (e.getType()) {
         case TableModelEvent.DELETE:
            log.debug("table changed by deleting " + firstRow + " to " + lastRow);
            for (int i = firstRow; i <= lastRow; i++) {
               log.debug("\ti = " + i);
               for (int j = i; j < getRowCount(); j++) {
                  log.debug("\t\tj = " + j);
                  int newKey = j;
                  int oldKey = newKey + 1;
                  Boolean valueToMoveUp = marked.get(oldKey);
                  log.debug("\t\tnewKey = " + newKey + " oldKey = " + oldKey + " valueToMoveUp = " + valueToMoveUp);
                  marked.put(newKey, valueToMoveUp);
               }
               marked.remove((getRowCount() - 1) + 1);
            }
            break;
         case TableModelEvent.INSERT:
            log.debug("table changed by inserting " + firstRow + " to " + lastRow);
            for (int i = firstRow; i <= lastRow; i++) {
               marked.put(i, Boolean.FALSE);
            }
            break;
         default:
            break;
         }
      }

      public void unMark(int row) {
         marked.put(row, Boolean.FALSE);
      }
   }
   protected Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
   protected Counter counter = new Counter();
   protected boolean editable = true;
   private Logger log = MyLogger.getLogger(MyTableModel.class);

   private ModelMarker modelMarkerDelegator;

   private boolean renderColors = false;

   private TableModelListenerAlerter tableModelListenerAlerter;

   protected MyTableModel(Column[] columns) {
      super(columns, 0);
      initiateColumns(columns);

      modelMarkerDelegator = new ModelMarker();
      addTableModelListener(modelMarkerDelegator);
   }

   MyTableModel(Column[] columns, Vector<Vector<Object>> contents, Vector<Column> header) {
      log.trace("MyTableModel");
      initiateColumns(columns);
      List<Integer> convertInputHeaderToOriginal = getConvertionNumbers(header, getColumnNames());

      Vector<Vector<Object>> newDataVector = new Vector<Vector<Object>>();
      for (Vector<Object> vector : contents) {
         newDataVector.add(sortVectorBasedOnList(convertInputHeaderToOriginal, vector));
      }
      Vector<Column> newHeaderVector = sortHeaderBasedOnList(convertInputHeaderToOriginal, header, getColumnNames());
      this.setDataVector(newDataVector, newHeaderVector);
      log.debug("Initiated from existing contents and header!");

      modelMarkerDelegator = new ModelMarker();
      addTableModelListener(modelMarkerDelegator);
   }

   final public void addEmptyRow() {
      this.addRow(getEmptyRow());
   }

   public void addJira(JiraIssue jiraIssue) {
      if (!doesJiraExist(jiraIssue.getKey())) {
         Set<Column> columnSet = columnNames.keySet();
         Vector<Object> contents = new Vector<Object>();
         for (Column column : columnSet) {
            contents.add(getValueFromIssue(jiraIssue, column));
         }
         super.addRow(contents);
      }
   }

   final public void addJira(String jira) {
      jira = jira.toUpperCase();
      if (!doesJiraExist(jira)) {
         Object[] row = getEmptyRow();
         row[0] = jira;
         log.debug("adding Jira " + jira + " of row size: " + row.length);
         addRow(row);
      }
   }

   public void addJira(String jira, Map<Column, Object> map) {
      // fixme - when not already in table model - raise a dialog and compare the results.
      addJira(jira);
      int row = getRowWithJira(jira);
      for (Column column : map.keySet()) {
         Object value = map.get(column);
         int columnIndex = getColumnIndex(column);
         if (columnIndex != -1)
            setValueAt(value, row, columnIndex);
      }
      fireTableRowsUpdated(row, row);
   }

   public void addJira(String jira, Map<Column, Object> map, int row) {
      // fixme - when not already in table model - raise a dialog and compare the results.
      addJira(jira);
      for (Column column : map.keySet()) {
         Object value = map.get(column);
         int columnIndex = getColumnIndex(column);
         if (columnIndex != -1) {
            log.debug("\tSetting value \"" + value + "\" to Column " + column + " (" + jira + ", at " + row + ", col " + columnIndex + ")");
            setValueAt(value, row, columnIndex);
         }
      }
   }

   public void clearMarked() {
      getMarker().clearMarked();
   }

   final public boolean doesJiraExist(String name) {
      log.debug("does " + name + " exist in " + getClass());
      for (int row = 0; row < getRowCount(); row++) {
         if (name.equalsIgnoreCase((String) getValueAt(Column.Jira, row))) {
            return true;
         }
      }
      return false;
   }

   final Column findIndexThatDoesNotExist(Map<Column, Integer> columnNames, Vector<Column> realVector, int i) {
      Set<Column> keySet = columnNames.keySet();
      Column[] columns = keySet.toArray(new Column[columnNames.size()]);
      for (int temp = 0; temp < columns.length; temp++) {
         Column column = columns[temp];
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

   final public void fireTableCellUpdatedExceptThisOne(int row, int col) {
      for (int i = 0; i < getColumnCount(); i++) {
         if (i != col)
            fireTableCellUpdated(row, i);
      }
   }

   final private Class<?> getClassFromColumn(int columnIndex) {
      return getColumn(columnIndex).getDefaultClass();
   }

   public abstract Color getColor(Object value, int row, Column column);

   public Color getColor(Object value, int row, int column) {
      return getColor(value, row, getColumn(column));
   }

   final public Column getColumn(int columnNo) {
      return Column.getEnum(getColumnName(columnNo));
   }

   final public Class<?> getColumnClass(int columnIndex) {
      return getClassFromColumn(columnIndex);
   }

   final public int getColumnIndex(Column column) {
      Integer index = columnNames.get(column);
      return index == null ? -1 : index;
   }

   final public Map<Column, Integer> getColumnNames() {
      return columnNames;
   }

   final List<Integer> getConvertionNumbers(Vector<Column> mixedUpVector, Map<Column, Integer> originalVector) {
      List<Integer> list = new ArrayList();
      log.debug("mixedUpVectorSize: " + mixedUpVector.size());
      log.debug("originalVector: " + originalVector.size());
      if (mixedUpVector.size() == originalVector.size()) {
         for (Column column : mixedUpVector) {
            Integer integer = originalVector.get(column);
            log.debug("for " + column + " we are getting " + integer);
            list.add(integer);
         }
      } else {
         for (Column originalColumn : originalVector.keySet()) {
            Integer integer = -1;
            if (mixedUpVector.contains(originalColumn))
               integer = mixedUpVector.indexOf(originalColumn);
            list.add(integer);
         }
      }
      return list;
   }

   final public Object[] getEmptyRow() {
      Map<Column, Integer> colnams = getColumnNames();
      Object[] objects = new Object[colnams.size()];
      int i = 0;
      log.debug("getting Empty Row");
      for (Column column : colnams.keySet()) {
         objects[i++] = column.getDefaultValue();
         log.debug("column: " + column + " containing: " + objects[i - 1]);
      }
      return objects;
   }

   private ModelMarker getMarker() {
      return modelMarkerDelegator;
   }

   /**
    * Returns -1 if cannot be found otherwise first instance of occurence.
    * 
    * @param value
    * @param columnNo
    * @param column
    * @return
    */
   final public int getRowOfSameValueInColumn(Object value, Column column) {
      int columnNo = getColumnIndex(column);
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

   final public int getRowWithJira(String name) {
      for (int row = 0; row < getRowCount(); row++) {
         if (name.equalsIgnoreCase((String) getValueAt(Column.Jira, row))) {
            return row;
         }
      }
      return -1;
   }

   public TableModelListenerAlerter getTableModelListenerAlerter() {
      return tableModelListenerAlerter;
   }

   final public Object getValueAt(Column column, int row) {
      int columnIndex = getColumnIndex(column);
      return row > -1 && row < getRowCount() ? getValueAt(row, columnIndex) : null;
   }

   final public Object getValueAt(Column column, String jira) {
      return getValueAt(column, getRowWithJira(jira));
   }

   Object getValueFromIssue(JiraIssue jiraIssue, Column column) {
      Object value;
      // FIXME add testcoverage to ensure this method returns the same type (int, boolean, etc) as the Column.parse method is!!
      switch (column) {
      case Jira:
         value = jiraIssue.getKey();
         break;
      case Description:
         value = jiraIssue.getSummary();
         break;
      case J_Type:
         value = jiraIssue.getType();
         break;
      case J_FixVersion:
         value = jiraIssue.getFixVersionsAsStrings();
         break;
      case J_Status:
         value = jiraIssue.getStatus();
         break;
      case J_Resolution:
         value = jiraIssue.getStatus() + " (" + jiraIssue.getResolution() + ")";
         break;
      case J_BuildNo:
         value = jiraIssue.getBuildNo();
         break;
      case prio:
         value = jiraIssue.getLLUListPriority();
         break;
      case J_Dev_Estimate:
         value = jiraIssue.getEstimate();
         break;
      case J_Sprint:
         value = jiraIssue.getSprint();
         break;
      case J_Project:
         value = jiraIssue.getProject();
         break;
      case J_Delivery:
         value = jiraIssue.getDeliveryDate();
         break;
      case J_Dev_Spent:
         value = jiraIssue.getSpent();
         break;
      case Release:
         log.debug("Found Release!" + jiraIssue.getRelease());
         value = jiraIssue.getRelease();
         break;
      default:
         value = column.getDefaultValue();
         break;
      }
      return value;
   }

   final protected void initiateColumns(Column[] columns) {
      counter.reset();
      for (Column column : columns) {
         putIntoColumnNames(column);
      }
   }

   @Override
   public void insertRow(int row, Vector rowData) {
      if (!doesJiraExist((String) rowData.get(0))) {
         super.insertRow(row, rowData);
      }
   }

   final public boolean isCellEditable(int row, int column) {
      return isEditable() ? getColumn(column).isEditable() : false;
   }

   final public boolean isEditable() {
      return editable;
   }

   public boolean isMarked(int row) {
      return getMarker().isMarked(row);
   }

   public void mark(int row) {
      getMarker().mark(row);
   }

   final protected void putIntoColumnNames(Column column) {
      int valueAndIncrease = counter.getValueAndIncrease();
      log.debug("putIntoColumnNames: " + column + " of default type: " + column.getDefaultValue() + " in position " + valueAndIncrease);
      columnNames.put(column, valueAndIncrease);
   }

   @Override
   public void removeRow(int row) {
      super.removeRow(row);
   }

   final public void setEditable(boolean selected) {
      editable = selected;
      if (getRowCount() > 0) {
         fireTableDataChanged();
      }
   }

   public void setRenderColors(boolean renderColors) {
      this.renderColors = renderColors;
   }

   public void setTableModelListenerAlerter(TableModelListenerAlerter listener) {
      this.tableModelListenerAlerter = listener;
      this.addTableModelListener(listener);
   }

   final public void setValueAt(Object value, int rowIndex, Column column) {
      setValueAt(value, rowIndex, getColumnIndex(column));
   }

   final public void setValueAt(Object value, int rowIndex, int columnIndex) {
      super.setValueAt(value, rowIndex, columnIndex);
   }

   protected boolean shouldRenderColors() {
      return renderColors != true;
   }

   final Vector<Column> sortHeaderBasedOnList(List<Integer> convertedList, Vector<Column> realVector, Map<Column, Integer> columnNames) {
      Vector<Column> result = sortVectorBasedOnList(convertedList, realVector);
      int i = 0;
      List<Column> newColumn = new ArrayList<Column>();
      for (Column column : result) {
         if (column == null) {
            newColumn.add(findIndexThatDoesNotExist(columnNames, realVector, i++));
            log.debug("newColumn: " + newColumn);
         }
      }
      for (Column column : newColumn) {
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

   public void syncJira(JiraIssue jiraIssue, int row) {
      log.debug("Syncing Jira: " + jiraIssue.getKey());
      Map<Column, Object> map = new HashMap<Column, Object>();
      Set<Column> columns = getColumnNames().keySet();
      for (Column column : columns) {
         if (column.isJiraColumn()) {
            Object valueAt = getValueFromIssue(jiraIssue, column);
            log.debug("\tPutting " + column + " to \"" + valueAt + "\" for row " + row);
            map.put(column, valueAt);
         }
      }
      addJira(jiraIssue.getKey(), map, row);
   }

   public void unMark(int row) {
      getMarker().unMark(row);
   }
}