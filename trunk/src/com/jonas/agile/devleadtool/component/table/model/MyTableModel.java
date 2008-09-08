package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;

public abstract class MyTableModel extends DefaultTableModel {

   private Logger log = MyLogger.getLogger(MyTableModel.class);
   protected boolean editable = true;

   private MyTableModel() {
      super();
   }

   private MyTableModel(Vector<Column> vector, int i) {
      super(vector, i);
   }

   MyTableModel(Map<Column, Integer> columnNames) {
      this(new Vector<Column>(columnNames.keySet()), 0);
   }

   MyTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      this();
      List<Integer> convertInputHeaderToOriginal = getConvertionNumbers(header, getColumnNames());

      Vector<Vector<Object>> newDataVector = new Vector<Vector<Object>>();
      for (Vector<Object> vector : contents) {
         newDataVector.add(sortVectorBasedOnList(convertInputHeaderToOriginal, vector));
      }
      Vector<Column> newHeaderVector = sortHeaderBasedOnList(convertInputHeaderToOriginal, header, getColumnNames());
      this.setDataVector(newDataVector, newHeaderVector);
      log.debug("Initiated from existing contents and header!");
   }

   public final void addEmptyRow() {
      this.addRow(getEmptyRow());
   }

   public Class<?> getColumnClass(int columnIndex) {
      Object valueAt = getValueAt(0, columnIndex);
      return valueAt != null ? getClass(valueAt) : getClassFromColumn(columnIndex);
   }

   public Column getColumnEnum(int columnNo) {
      return Column.getEnum(getColumnName(columnNo));
   }

   public int getColumnNo(Column column) {
      for (int col = 0; col < getColumnCount(); col++) {
         if (getColumnName(col).equals(column.toString())) {
            return col;
         }
      }
      return -1;
   }

   public JiraProject getProjectForRow(int row) {
      String jira = (String) getValueAt(row, 0);
      return JiraProject.getProjectByKey(PlannerHelper.getProjectKey(jira));
   }

   /**
    * Returns -1 if cannot be found otherwise first instance of occurence.
    * 
    * @param value
    * @param column
    * @return
    */
   public int getRowOfSameValueInColumn(Object value, int column) {
      for (int i = 0; i < this.getRowCount(); i++) {
         Object valueAt = this.getValueAt(i, column);
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

   public abstract boolean isCellEditable(int row, int column);

   public boolean isEditable() {
      return editable;
   }

   // public int getCountOfSameValueInColumn(Object value, int column) {
   // int countOfSimilar = 0;
   // for (int i = 0; i < this.getRowCount(); i++) {
   // Object valueAt = this.getValueAt(i, column);
   // if (value instanceof String) {
   // String stringValue = (String) value;
   // String stringRowValue = (String) valueAt;
   // if ((stringValue).length() > 0 && stringValue.equalsIgnoreCase(stringRowValue)) {
   // countOfSimilar++;
   // }
   // } else if (valueAt != null && valueAt.equals(value)) {
   // countOfSimilar++;
   // }
   // }
   // return countOfSimilar;
   // }

   public void setEditable(boolean selected) {
      editable = selected;
      fireTableStructureChanged();
      // TODO need to fireUpdate on Table?
   }

   // Only required if the table is updated by the app so that it becomes visible to the user.
   public void setValueAt(Object value, int rowIndex, int columnIndex) {
      super.setValueAt(value, rowIndex, columnIndex);
      fireTableRowsUpdated(rowIndex, rowIndex);
   }

   private Class<?> getClass(Object valueAt) {
      return valueAt.getClass();
   }

   private Class<?> getClassFromColumn(int columnIndex) {
      return getEmptyRow()[columnIndex].getClass();
   }

   protected abstract Object[] getEmptyRow();

   abstract Map<Column, Integer> getColumnNames();

   List<Integer> getConvertionNumbers(Vector<Column> mixedUpVector, Map<Column, Integer> originalVector) {
      List<Integer> list = new ArrayList();
      log.debug("mixedUpVectorSize: " + mixedUpVector.size());
      log.debug("originalVector: " + originalVector.size());
      if (mixedUpVector.size() == originalVector.size()) {
         for (Column column : mixedUpVector) {
            Integer integer = originalVector.get(column);
            System.out.println("for " + column + " we are getting " + integer);
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

   Vector<Column> sortHeaderBasedOnList(List<Integer> convertedList, Vector<Column> realVector, Map<Column, Integer> columnNames) {
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
            if(result.get(j) == null){
               result.remove(j);
               result.insertElementAt(column, j);
               break;
            }
         }
      }
      return result;
   }

   Column findIndexThatDoesNotExist(Map<Column, Integer> columnNames, Vector<Column> realVector, int i) {
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

   <T> Vector<T> sortVectorBasedOnList(List<Integer> convertedList, Vector<T> realVector) {
      Vector<T> result = new Vector<T>();
      for (Integer integer : convertedList) {
         T t = null;
         try {
            t = realVector.get(integer);
         } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("ArrayIndexOutOfBoundsException -> ");
         } catch (NullPointerException e) {
            System.out.print("NullPointerException -> ");
         }
         System.out.println("adding " + t + " from " + integer);
         result.add(t);
      }
      return result;
   }

}
