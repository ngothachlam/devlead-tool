package com.jonas.agile.devleadtool.component.table;

import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DropMode;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.jonas.agile.devleadtool.component.table.editor.BoardStatusCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.MyDefaultCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.MyEditor;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.jira.JiraIssue;

public class MyTable extends JTable {

   private MyTableModel model;
   private String title;

   public MyTable(String title) {
      this(title, new DefaultTableModel());
   }

   public MyTable(String title, MyTableModel modelModel) {
      this(title, (AbstractTableModel) modelModel);
   }

   MyTable(String title, AbstractTableModel defaultTableModel) {
      super(defaultTableModel);
      this.title = title;

      CheckBoxTableCellRenderer checkBoxRenderer = new CheckBoxTableCellRenderer(defaultTableModel);
      CheckBoxTableCellEditor checkBoxEditor = new CheckBoxTableCellEditor(new JCheckBox());

      setDefaultRenderer(String.class, new StringTableCellRenderer(defaultTableModel));
      setDefaultRenderer(Boolean.class, checkBoxRenderer);
      setDefaultEditor(Boolean.class, checkBoxEditor);

      checkBoxEditor.addCellEditorListener(getCheckBoxEditorListener());

      setDragEnabled(true);
      setDropMode(DropMode.INSERT);
      setFillsViewportHeight(true);

      setAutoCreateRowSorter(true);
      // sorter = new TableRowSorter<TableModel>(defaultTableModel);
      // setRowSorter(sorter);

      if (getModel() instanceof MyTableModel) {
         model = (MyTableModel) getModel();
         // FIXME make this dynamic
         int colIndex = getColumnIndex(Column.B_BoardStatus);
         if (colIndex > -1) {
            TableColumn tc = getTableColumn(colIndex);
            tc.setCellRenderer(new StringTableCellRenderer(model));
            JComboBox combo = new JComboBox(BoardStatusValue.values());
            tc.setCellEditor(new BoardStatusCellEditor(combo));
         }
         colIndex = getColumnIndex(Column.Release);
         if (colIndex > -1) {
            TableColumn tc = getTableColumn(colIndex);
            tc.setCellEditor(new MyDefaultCellEditor(new JTextField()));
         }
      }

      // TODO add tooltip for the contents of the table as well by owerriding the getToolTipText method in MyTable (or create JiraTable...)
      setTableHeader(new JTableHeader(columnModel){
         @Override
         public String getToolTipText(MouseEvent e) {
            java.awt.Point p = e.getPoint();
            int rowIndex = rowAtPoint(p);
            int colIndex = columnAtPoint(p);
            String tip = getColumnName(colIndex);
            return tip;
         }
      });
   }

   private CellEditorListener getCheckBoxEditorListener() {
      return new CellEditorListener() {
         @Override
         public void editingCanceled(ChangeEvent e) {
         }

         @Override
         public void editingStopped(ChangeEvent e) {
            MyEditor editor = (MyEditor) e.getSource();
            model.fireTableRowsUpdated(editor.getRowEdited(), editor.getRowEdited());
         }
      };
   }

   public void unSort() {
      setAutoCreateRowSorter(true);
      // sorter.setSortKeys(null);
   }

   private TableColumn getTableColumn(int colIndex) {
      TableColumnModel tcm = getColumnModel();
      return tcm.getColumn(colIndex);
   }
   
   public TableCellEditor getTableColumnEditor(int colIndex) {
      return getTableColumn(colIndex).getCellEditor();
   }
   
   public TableColumn getTableEditor(int boardStatus) {
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

   public String getTitle() {
      return title;
   }

   public void setModel(MyTableModel model) {
      this.model = model;
      super.setModel(model);
   }

}
