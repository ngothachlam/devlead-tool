package com.jonas.agile.devleadtool.component.table.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import com.jonas.agile.devleadtool.component.table.Column;
import junit.framework.TestCase;

public class MyTableModelTest extends TestCase {

   public void testShouldConvertionNumbersWithStandardColumnsOk() {
      MyTableModel model = new JiraTableModel();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);

      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Description, 1);

      List convert = model.getConvertionNumbers(header, columnNames);

      assertEquals(2, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(0, convert.get(1));
   }

   public void testShouldConvertionNumbersWithFewerColumnsOk() {
      MyTableModel model = new JiraTableModel();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);

      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Note, 1);
      columnNames.put(Column.BuildNo, 2);
      columnNames.put(Column.Description, 3);

      List convert = model.getConvertionNumbers(header, columnNames);

      assertEquals(4, convert.size());
      assertEquals(1, convert.get(0));
      assertEquals(-1, convert.get(1));
      assertEquals(-1, convert.get(2));
      assertEquals(0, convert.get(3));
   }

   public void testShouldConvertionNumbersWithMoreColumnsOk() {
      MyTableModel model = new JiraTableModel();
      Vector<Column> header = new Vector<Column>();
      header.add(Column.Description);
      header.add(Column.Jira);
      header.add(Column.isBug);

      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Description, 1);

      List convert = model.getConvertionNumbers(header, columnNames);

      assertEquals(1, convert.get(0));
      assertEquals(0, convert.get(1));
      assertEquals(2, convert.size());
   }

   public void testShouldSortVectorBasedOnListWithStandardColumnsOk() {
      MyTableModel model = new JiraTableModel();

      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(0);

      Vector<Object> mixedRowVector = new Vector<Object>();
      mixedRowVector.add("Description");
      mixedRowVector.add("Jira");

      Vector result = model.sortVectorBasedOnList(originalList, mixedRowVector);
      assertEquals("Jira", result.get(0));
      assertEquals("Description", result.get(1));
      assertEquals(2, result.size());
   }

   public void testShouldSortVectorBasedOnListWithFewerColumnsOk() {
      MyTableModel model = new JiraTableModel();

      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(-1);
      originalList.add(0);
      originalList.add(-1);

      Vector<Object> mixedRowVector = new Vector<Object>();
      mixedRowVector.add("Description");
      mixedRowVector.add("Jira");

      Vector result = model.sortVectorBasedOnList(originalList, mixedRowVector);
      assertEquals("Jira", result.get(0));
      assertEquals(null, result.get(1));
      assertEquals("Description", result.get(2));
      assertEquals(null, result.get(3));
      assertEquals(4, result.size());
   }

   public void testShouldReturnFirstOccurenceInSetThatDoesNotExistInVector() {
      MyTableModel model = new JiraTableModel();
      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Actual, 0);
      columnNames.put(Column.BuildNo, 1);
      columnNames.put(Column.Closed_Sprint, 2);

      Vector<Column> vector = new Vector<Column>();
      vector.add(Column.BuildNo);
      assertEquals(Column.Actual, model.findIndexThatDoesNotExist(columnNames, vector, 0));
      assertEquals(Column.Closed_Sprint, model.findIndexThatDoesNotExist(columnNames, vector, 1));
      assertEquals(null, model.findIndexThatDoesNotExist(columnNames, vector, 2));
   }

   public void testShouldSortHeaderBasedOnListWithFewerColumnsOk() {
      MyTableModel model = new JiraTableModel();

      List<Integer> originalList = new ArrayList<Integer>();
      originalList.add(1);
      originalList.add(-1);
      originalList.add(0);
      originalList.add(-1);

      Vector<Column> mixedRowVector = new Vector<Column>();
      mixedRowVector.add(Column.Description);
      mixedRowVector.add(Column.Jira);

      Map<Column, Integer> columnNames = new LinkedHashMap<Column, Integer>();
      columnNames.put(Column.Jira, 0);
      columnNames.put(Column.Description, 1);
      columnNames.put(Column.BuildNo, 2);
      columnNames.put(Column.FixVersion, 3);

      Vector<Column> result = model.sortHeaderBasedOnList(originalList, mixedRowVector, columnNames);
      assertEquals(Column.Jira, result.get(0));
      assertEquals(Column.BuildNo, result.get(1));
      assertEquals(Column.Description, result.get(2));
      assertEquals(Column.FixVersion, result.get(3));
      assertEquals(4, result.size());

   }

   public void testShouldAddRowsWhilstEditingCorrectly() {
      MyTableModel model = new JiraTableModel();
      assertEquals(0, model.getRowCount());
      model.addEmptyRow();
      model.setValueAt("123", 0, 0);
      assertEquals(1, model.getRowCount());
      assertEquals("123", model.getValueAt(0, 0));

      model.setValueAt("1234", 0, 0);
      assertEquals(1, model.getRowCount());
      assertEquals("1234", model.getValueAt(0, 0));

      model.addEmptyRow();
      model.setValueAt("1235", 1, 0);
      assertEquals(2, model.getRowCount());
      assertEquals("1234", model.getValueAt(0, 0));
      assertEquals("1235", model.getValueAt(1, 0));
   }
}
