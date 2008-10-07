package com.jonas.agile.devleadtool.component.table;

import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DropMode;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.jira.JiraIssue;

public class MyTable extends JTable {

   private MyTableModel model;

   // private TableRowSorter<TableModel> sorter;

   public MyTable() {
      this(new DefaultTableModel());
   }

   public MyTable(MyTableModel modelModel) {
      this((AbstractTableModel) modelModel);
   }

   MyTable(AbstractTableModel defaultTableModel) {
      super(defaultTableModel);
      setDefaultRenderer(String.class, new StringTableCellRenderer());
      setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer(defaultTableModel));
      setDefaultEditor(Boolean.class, new CheckBoxTableCellEditor(new JCheckBox()));
      // setDefaultEditor(Boolean.class, new DefaultCellEditor(new JCheckBox()));

      setDragEnabled(true);
      setDropMode(DropMode.INSERT);
      setFillsViewportHeight(true);

      setAutoCreateRowSorter(true);
      // sorter = new TableRowSorter<TableModel>(defaultTableModel);
      // setRowSorter(sorter);

      // FIXME make this dynamic
      if (getModel() instanceof MyTableModel) {
         model = (MyTableModel) getModel();
         int boardStatus = getColumnIndex(Column.BoardStatus);
         if (boardStatus > -1) {
            TableColumn tc = getTableColumn(boardStatus);
            tc.setCellRenderer(new StringTableCellRenderer());
            JComboBox combo = new JComboBox(BoardStatusValue.values());
            tc.setCellEditor(new DefaultCellEditor(combo));
         }
      }
   }

   public void unSort() {
      setAutoCreateRowSorter(true);
      // sorter.setSortKeys(null);
   }

   private TableColumn getTableColumn(int boardStatus) {
      TableColumnModel tcm = getColumnModel();
      return tcm.getColumn(boardStatus);
   }

   public void addEmptyRow() {
      model.addEmptyRow();
   }

   public void addJira(JiraIssue jiraIssue) {
      model.addJira(jiraIssue);
   }

   public void addJira(String jira) {
      model.addJira(jira);
   }

   public Column getColumnEnum(int itsColumn) {
      return model.getColumn(convertColumnIndexToModel(itsColumn));
   }

   public int getColumnIndex(Column column) {
      return convertColumnIndexToView(model.getColumnIndex(column));
   }

   public Object getValueAt(Column column, int rowInView) {
      int colInView = getColumnIndex(column);
      return model.getValueAt(convertRowIndexToModel(rowInView), convertColumnIndexToModel(colInView));
   }

   public boolean isColumn(Column column, int colNoToCompare) {
      return column.equals(getColumnEnum(colNoToCompare));
   }

   public void removeSelectedRows() {
      int[] selectedRows = getSelectedRows(); // Need to store this before hand or the last selected row seems to disappear!
      for (int count = selectedRows.length - 1; count > -1; count--) {
         // we can't remove upwards the table and down (from lower to greater index) as the table changes on each delete.
         int tableSelectedRow = selectedRows[count];
         int convertRowIndexToModel = convertRowIndexToModel(tableSelectedRow);
         model.removeRow(convertRowIndexToModel);
      }
   }

   public void setColumnRenderer(int i, TableCellRenderer renderer) {
      TableColumn tc = getTableColumn(i);
      tc.setCellRenderer(renderer);
   }

   public void setValueAt(Object value, int row, Column column) {
      setValueAt(value, row, getColumnIndex(column));
   }

   public void setValueAt(Object value, int row, int column) {
      super.setValueAt(value, row, column);
   }

   // public TableRowSorter<TableModel> getSorter() {
   // return sorter;
   // }

   public void addJira(String jira, Map<Column, Object> map) {
      model.addJira(jira, map);
   }

   public void insertRow(int index, Vector<Object> rowData) {
      System.out.println("insertRow");
      // int convertRowIndexToModel = index;
      int convertRowIndexToModel = convertRowIndexToModel(index);
      System.out.println(convertRowIndexToModel);
      model.insertRow(convertRowIndexToModel, rowData);
   }

   public void addRow(Vector<Object> rowData) {
      model.addRow(rowData);
   }

   public Column[] getColumns() {
      Map<Column, Integer> columnNames = model.getColumnNames();
      return columnNames.keySet().toArray(new Column[columnNames.size()]);
   }

   public void syncJira(JiraIssue jiraIssue, int tableRowSynced) {
      model.syncJira(jiraIssue, convertRowIndexToModel(tableRowSynced));
   }

}
