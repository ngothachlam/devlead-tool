package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

   MyTableModel(Map<Column, Integer> columnNames) {
      super(new Vector(columnNames.keySet()), 0);
   }

   MyTableModel(Map<Column, Integer> columnNames, int i) {
      super(new Vector(columnNames.keySet()), i);
   }

   MyTableModel(Vector<Vector<Object>> contents, Vector<Column> header) {
      this();
      List<Integer> convertInputHeaderToOriginal = getConvertionNumbers(header, getColumnNames());

      Vector<Vector> newDataVector = new Vector<Vector>();
      for (Vector<Object> vector : contents) {
         newDataVector.add(sortVectorBasedOnList(convertInputHeaderToOriginal, vector));
      }
      Vector newHeaderVector = sortVectorBasedOnList(convertInputHeaderToOriginal, header);
      this.setDataVector(newDataVector, newHeaderVector);
      log.debug("Initiated from existing contents and header!");
   }

   public final void addEmptyRow() {
      this.addRow(getEmptyRow());
   }

   public Column getColumn(int columnNo) {
      return Column.getEnum(getColumnName(columnNo));
   }

   public Class<?> getColumnClass(int columnIndex) {
      Object valueAt = getValueAt(0, columnIndex);
      return valueAt != null ? getClass(valueAt) : getClassFromColumn(columnIndex);
   }

   private Class<?> getClassFromColumn(int columnIndex) {
      return getEmptyRow()[columnIndex].getClass();
   }

   private Class<?> getClass(Object valueAt) {
      return valueAt.getClass();
   }

   public int getColumnNo(Column column) {
      for (int col = 0; col < getColumnCount(); col++) {
         if (getColumnName(col).equals(column.toString())) {
            return col;
         }
      }
      return -1;
   }

   public int getCountOfSameValueInColumn(Object value, int column) {
      int countOfSimilar = 0;
      for (int i = 0; i < this.getRowCount(); i++) {
         Object valueAt = this.getValueAt(i, column);
         if (value instanceof String) {
            String stringValue = (String) value;
            String stringRowValue = (String) valueAt;
            if ((stringValue).length() > 0 && stringValue.equalsIgnoreCase(stringRowValue)) {
               countOfSimilar++;
            }
         } else if (valueAt != null && valueAt.equals(value)) {
            countOfSimilar++;
         }
      }
      return countOfSimilar;
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

   public abstract boolean isRed(Object value, int row, int column);

   public void setEditable(boolean selected) {
      editable = selected;
      fireTableStructureChanged();
      // TODO need to fireUpdate on Table?
   }

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

   protected abstract Object[] getEmptyRow();

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

   abstract Map<Column, Integer> getColumnNames();

}
