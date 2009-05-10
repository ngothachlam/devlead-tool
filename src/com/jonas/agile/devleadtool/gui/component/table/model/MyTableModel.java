package com.jonas.agile.devleadtool.gui.component.table.model;

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
import org.apache.poi.hssf.util.HSSFColor;

import com.jonas.agile.devleadtool.gui.component.table.ColorDTO;
import com.jonas.agile.devleadtool.gui.component.table.ColumnType;
import com.jonas.agile.devleadtool.gui.component.table.ColumnWrapper;
import com.jonas.agile.devleadtool.gui.listener.TableModelListenerAlerter;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;
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

   protected Map<ColumnType, Integer> columnNames = new LinkedHashMap<ColumnType, Integer>();

   protected Counter counter = new Counter();

   protected boolean editable = true;
   private Map<String, String> extraToolTipText = new HashMap<String, String>();
   private Logger log = MyLogger.getLogger(MyTableModel.class);
   private ModelMarker modelMarkerDelegator;
   private boolean renderColors = false;

   private SprintCache sprintCache;

   private TableModelListenerAlerter tableModelListenerAlerter;

   protected MyTableModel(ColumnType[] columns) {
      super(columns, 0);
      initiateColumns(columns);

      modelMarkerDelegator = new ModelMarker();
      addTableModelListener(modelMarkerDelegator);
   }

   MyTableModel(ColumnType[] columns, Vector<Vector<Object>> contents, Vector<ColumnType> header) {
      log.trace("MyTableModel");
      initiateColumns(columns);
      List<Integer> convertInputHeaderToOriginal = getConvertionNumbers(header, getColumnNames());

      Vector<Vector<Object>> newDataVector = new Vector<Vector<Object>>();
      for (Vector<Object> vector : contents) {
         newDataVector.add(sortVectorBasedOnList(convertInputHeaderToOriginal, vector));
      }
      Vector<ColumnType> newHeaderVector = sortHeaderBasedOnList(convertInputHeaderToOriginal, header, getColumnNames());
      this.setDataVector(newDataVector, newHeaderVector);
      log.debug("Initiated from existing contents and header!");

      modelMarkerDelegator = new ModelMarker();
      addTableModelListener(modelMarkerDelegator);
   }

   /**
    * 
    * @param jiraIssue
    * @return returns true if the jira is new to the table, otherwise false.
    */
   public boolean addJira(JiraIssue jiraIssue) {
      boolean isJiraPresent = isJiraPresent(jiraIssue.getKey());
      if (!isJiraPresent) {
         Set<ColumnType> columnSet = columnNames.keySet();
         Vector<Object> contents = new Vector<Object>();
         for (ColumnType column : columnSet) {
            contents.add(getValueFromIssue(jiraIssue, column));
         }
         super.addRow(contents);
      }
      return !isJiraPresent;
   }

   /**
    * 
    * @param jira
    * @return returns true if the jire is new to the table, otherwise false.
    */
   final public boolean addJira(String jira) {
      boolean isJiraPresent = isJiraPresent(jira);
      if (!isJiraPresent) {
         Object[] row = getEmptyRow();
         row[0] = jira;
         if (log.isDebugEnabled())
            log.debug("adding Jira " + jira + " of row size: " + row.length);
         addRow(row);
      }
      return !isJiraPresent;
   }

   public boolean addJira(String jira, Map<ColumnType, Object> map) {
      boolean isJiraPresent = addJira(jira);
      setValues(jira, map);
      return !isJiraPresent;
   }

   public void addJira(String jira, Map<ColumnType, Object> map, int row) {
      // fixme - when not already in table model - raise a dialog and compare
      // the results.
      addJira(jira);
      for (ColumnType column : map.keySet()) {
         Object value = map.get(column);
         int columnIndex = getColumnIndex(column);
         if (columnIndex != -1) {
            if (log.isDebugEnabled())
               log.debug("\tSetting value \"" + value + "\" to Column " + column + " (" + jira + ", at " + row + ", col " + columnIndex + ")");
            setValueAt(value, row, columnIndex);
         }
      }
   }

   public void clearMarked() {
      getMarker().clearMarked();
   }

   final ColumnType findIndexThatDoesNotExist(Map<ColumnType, Integer> columnNames, Vector<ColumnType> realVector, int i) {
      Set<ColumnType> keySet = columnNames.keySet();
      ColumnType[] columns = keySet.toArray(new ColumnType[columnNames.size()]);
      for (int temp = 0; temp < columns.length; temp++) {
         ColumnType column = columns[temp];
         if (log.isDebugEnabled())
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

   public abstract Color getColor(Object value, int row, ColumnType column);

   public ColorDTO getColor(Object value, int row, int column, boolean includeHSSFColor) {
      ColorDTO colorDto = getColor(value, row, column);
      if (includeHSSFColor)
         colorDto.setHSSFColor(getHSSFColor(colorDto.getColor()));
      return colorDto;
   }

   private HSSFColor getHSSFColor(Color color) {
      if (color == null)
         return null;
      if (SwingUtil.cellBlue.equals(color))
         return new HSSFColor.BLUE();
      if (SwingUtil.cellGreen.equals(color))
         return new HSSFColor.GREEN();
      if (SwingUtil.cellRed.equals(color))
         return new HSSFColor.RED();
      if (SwingUtil.cellLightBlue.equals(color))
         return new HSSFColor.LIGHT_BLUE();
      if (SwingUtil.cellLightGreen.equals(color))
         return new HSSFColor.LIGHT_GREEN();
      if (SwingUtil.cellLightRed.equals(color))
         return new HSSFColor.ORANGE();
      if (SwingUtil.cellLightYellow.equals(color))
         return new HSSFColor.LIGHT_YELLOW();
      return null;
   }

   public ColorDTO getColor(Object value, int row, int column) {
      Color color = getColor(value, row, getColumnType(column));
      return new ColorDTO(color, isMarked(row));
   }

   final public ColumnWrapper<?> getColumnWrapper(int columnIndex) {
      String columnName = getColumnName(columnIndex);
      return ColumnWrapper.get(columnName);
   }

   final public ColumnType getColumnType(int columnIndex) {
      return getColumnWrapper(columnIndex).getType();
   }

   @Override
   final public Class<?> getColumnClass(int columnIndex) {
      return getColumnWrapper(columnIndex).getDefaultClass();
   }

   final public int getColumnIndex(ColumnType column) {
      Integer index = columnNames.get(column);
      return index == null ? -1 : index;
   }

   final public Map<ColumnType, Integer> getColumnNames() {
      return columnNames;
   }

   final List<Integer> getConvertionNumbers(Vector<ColumnType> mixedUpVector, Map<ColumnType, Integer> originalVector) {
      List<Integer> list = new ArrayList();
      if (log.isDebugEnabled())
         log.debug("mixedUpVector size: " + mixedUpVector.size() + " and originalVector size: " + originalVector.size());
      if (mixedUpVector.size() == originalVector.size()) {
         for (ColumnType column : mixedUpVector) {
            Integer integer = originalVector.get(column);
            log.debug("for " + column + " we are getting " + integer);
            list.add(integer);
         }
      } else {
         for (ColumnType originalColumn : originalVector.keySet()) {
            Integer integer = -1;
            if (mixedUpVector.contains(originalColumn))
               integer = mixedUpVector.indexOf(originalColumn);
            list.add(integer);
         }
      }
      return list;
   }

   final public Object[] getEmptyRow() {
      Map<ColumnType, Integer> colnams = getColumnNames();
      Object[] objects = new Object[colnams.size()];
      int i = 0;
      log.debug("getting Empty Row");
      for (ColumnType column : colnams.keySet()) {
         ColumnWrapper columnWrapper = ColumnWrapper.get(column);
         objects[i++] = columnWrapper.getDefaultValue();
         log.debug("column: " + column + " containing: " + objects[i - 1]);
      }
      return objects;
   }

   public String getExtraToolTipText(int row, int col) {
      return extraToolTipText.get(row + "-" + col);
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
   final public int getRowOfSameValueInColumn(Object value, ColumnType column) {
      int columnNo = getColumnIndex(column);
      for (int i = 0; i < this.getRowCount(); i++) {
         Object valueAt = this.getValueAt(i, columnNo);
         if (value instanceof String) {
            String stringValue = value.toString();
            String stringRowValue = valueAt.toString();
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
         Object jira = getValueAt(ColumnType.Jira, row);
         String jiraAsString = jira.toString();
         if (name.equalsIgnoreCase(jiraAsString)) {
            return row;
         }
      }
      return -1;
   }

   public SprintCache getSprintCache() {
      return sprintCache;
   }

   public TableModelListenerAlerter getTableModelListenerAlerter() {
      return tableModelListenerAlerter;
   }

   final public Object getValueAt(ColumnType column, int row) {
      int columnIndex = getColumnIndex(column);
      return row > -1 && row < getRowCount() ? getValueAt(row, columnIndex) : null;
   }

   final public Object getValueAt(ColumnType column, String jira) {
      return getValueAt(column, getRowWithJira(jira));
   }

   Object getValueFromIssue(JiraIssue jiraIssue, ColumnType column) {
      Object value;
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
         case FixVersion:
            value = jiraIssue.getFixVersionsAsStrings();
            break;
         case Status:
            value = jiraIssue.getStatus();
            break;
         case Resolution:
            value = jiraIssue.getStatus() + " (" + jiraIssue.getResolution() + ")";
            break;
         case BuildNo:
            value = jiraIssue.getBuildNo();
            break;
         case prio:
            value = jiraIssue.getLLUListPriority();
            break;
         case J_DevEst:
            value = jiraIssue.getEstimate();
            break;
         case J_Sprint:
            value = jiraIssue.getSprint();
            break;
         case Project:
            value = jiraIssue.getProjectAsString();
            break;
         case Environment:
            value = jiraIssue.getEnvironment();
            break;
         case Owner:
            value = jiraIssue.getOwner();
            break;
         case Delivery:
            value = jiraIssue.getDeliveryDate();
            break;
         case J_DevAct:
            value = jiraIssue.getSpent();
            break;
         case Release:
            log.debug("Found Release!" + jiraIssue.getRelease());
            value = jiraIssue.getRelease();
            break;
         default:
            value = ColumnWrapper.get(column).getDefaultValue();
            break;
      }
      return value;
   }

   final protected void initiateColumns(ColumnType[] columns) {
      counter.reset();
      for (ColumnType column : columns) {
         putIntoColumnNames(column);
      }
   }

   @Override
   public void insertRow(int row, Vector rowData) {
      if (!isJiraPresent(rowData.get(0).toString())) {
         super.insertRow(row, rowData);
      }
   }

   @Override
   final public boolean isCellEditable(int row, int column) {
      return isEditable() ? getColumnWrapper(column).isEditable() : false;
   }

   final public boolean isEditable() {
      return editable;
   }

   final public boolean isJiraPresent(String name) {
      if (log.isDebugEnabled())
         log.debug("does " + name + " exist in " + getClass());
      return getRowWithJira(name) == -1 ? false : true;
   }

   public boolean isMarked(int row) {
      return getMarker().isMarked(row);
   }

   public void mark(int row) {
      getMarker().mark(row);
   }

   final protected void putIntoColumnNames(ColumnType column) {
      int valueAndIncrease = counter.getValueAndIncrease();
      if (log.isDebugEnabled())
         log.debug("putIntoColumnNames: " + column + " of default type: " + ColumnWrapper.get(column).getDefaultValue() + " in position " + valueAndIncrease);
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

   public void setJiraBasedOnJiraColumns(JiraIssue jiraIssue) {
      int row = getRowWithJira(jiraIssue.getKey());
      Set<ColumnType> columnSet = columnNames.keySet();
      for (ColumnType column : columnSet) {
         if (ColumnWrapper.get(column).isJiraColumn())
            setValueAt(getValueFromIssue(jiraIssue, column), row, column);
      }
   }

   public void setRenderColors(boolean renderColors) {
      this.renderColors = renderColors;
   }

   public void setSprintCache(SprintCache sprintCache) {
      this.sprintCache = sprintCache;
   }

   public void setTableModelListenerAlerter(TableModelListenerAlerter listener) {
      this.tableModelListenerAlerter = listener;
      this.addTableModelListener(listener);
   }

   protected void setToolTipText(int row, int col, String string) {
      extraToolTipText.put(row + "-" + col, string);
   }

   final public void setValueAt(Object value, int rowIndex, ColumnType column) {
      setValueAt(value, rowIndex, getColumnIndex(column));
   }

   @Override
   final public void setValueAt(Object value, int rowIndex, int columnIndex) {
      super.setValueAt(value, rowIndex, columnIndex);
   }

   public void setValueAt(Object value, String jira, ColumnType column) {
      int row = getRowWithJira(jira.toUpperCase());
      if (row < 0) {
         if (log.isDebugEnabled())
            log.warn("Jira " + jira + " isn't in model (" + this.getClass() + ") for setValue(" + value + "," + jira + "," + column + ")");
         return;
      }
      if (log.isDebugEnabled())
         log.debug("Updating " + jira + "'s " + column + " to \"" + value + "\" in model " + this.getClass());
      setValueAt(value, row, column);
   }

   private void setValues(String jira, Map<ColumnType, Object> map) {
      int row = getRowWithJira(jira);
      for (ColumnType column : map.keySet()) {
         Object value = map.get(column);
         int columnIndex = getColumnIndex(column);
         if (columnIndex != -1)
            setValueAt(value, row, columnIndex);
      }
   }

   protected boolean shouldNotRenderColors() {
      return renderColors != true;
   }

   final Vector<ColumnType> sortHeaderBasedOnList(List<Integer> convertedList, Vector<ColumnType> realVector, Map<ColumnType, Integer> columnNames) {
      Vector<ColumnType> result = sortVectorBasedOnList(convertedList, realVector);
      int i = 0;
      List<ColumnType> newColumn = new ArrayList<ColumnType>();
      for (ColumnType column : result) {
         if (column == null) {
            newColumn.add(findIndexThatDoesNotExist(columnNames, realVector, i++));
            if (log.isDebugEnabled())
               log.debug("newColumn: " + newColumn);
         }
      }
      for (ColumnType column : newColumn) {
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
         if (log.isDebugEnabled())
            log.debug("adding " + t + " from " + integer);
         result.add(t);
      }
      return result;
   }

   public void unMark(int row) {
      getMarker().unMark(row);
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
         fireTableRowsUpdated(row, row);
      }

      @Override
      public void tableChanged(TableModelEvent e) {
         int firstRow = e.getFirstRow();
         int lastRow = e.getLastRow();
         switch (e.getType()) {
            case TableModelEvent.DELETE:
               if (log.isDebugEnabled())
                  log.debug("table changed by deleting " + firstRow + " to " + lastRow);
               for (int i = firstRow; i <= lastRow; i++) {
                  if (log.isDebugEnabled())
                     log.debug("\ti = " + i);
                  for (int j = i; j < getRowCount(); j++) {
                     log.debug("\t\tj = " + j);
                     int newKey = j;
                     int oldKey = newKey + 1;
                     Boolean valueToMoveUp = marked.get(oldKey);
                     if (log.isDebugEnabled())
                        log.debug("\t\tnewKey = " + newKey + " oldKey = " + oldKey + " valueToMoveUp = " + valueToMoveUp);
                     marked.put(newKey, valueToMoveUp);
                  }
                  marked.remove((getRowCount() - 1) + 1);
               }
               break;
            case TableModelEvent.INSERT:
               if (log.isDebugEnabled())
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
         fireTableRowsUpdated(row, row);
      }
   }
}