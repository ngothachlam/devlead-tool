package com.jonas.agile.devleadtool.component.table;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DropMode;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.listener.MyTableListener;
import com.jonas.agile.devleadtool.component.table.editor.BoardStatusCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.JiraCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.MyDefaultCellEditor;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;

public class MyTable extends JTable {

   private MyTableModel model;
   private String title;
   private Logger log = MyLogger.getLogger(MyTable.class);
   private List<MyTableListener> listeners = new ArrayList<MyTableListener>();
   private JiraCellEditor jiraEditor;
   private CheckBoxTableCellEditor checkBoxEditor;

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
      checkBoxEditor = new CheckBoxTableCellEditor(new JCheckBox());

      setDefaultRenderer(String.class, new StringTableCellRenderer(defaultTableModel));
      setDefaultRenderer(Boolean.class, checkBoxRenderer);
      setDefaultEditor(BoardStatusValue.class, new BoardStatusCellEditor(new JComboBox(BoardStatusValue.values())));
      setDefaultEditor(Boolean.class, checkBoxEditor);

      setDragEnabled(true);
      setDropMode(DropMode.INSERT);
      setFillsViewportHeight(true);

      setAutoCreateRowSorter(true);
      // sorter = new TableRowSorter<TableModel>(defaultTableModel);
      // setRowSorter(sorter);

      if (getModel() instanceof MyTableModel) {
         model = (MyTableModel) getModel();
         // FIXME make this dynamic
         int colIndex = getColumnIndex(Column.Release);
         if (colIndex > -1) {
            TableColumn tc = getTableColumn(colIndex);
            tc.setCellEditor(new MyDefaultCellEditor(new JTextField()));
         }
         colIndex = getColumnIndex(Column.Jira);
         if (colIndex > -1) {
            jiraEditor = new JiraCellEditor(new JTextField());
            TableColumn tc = getTableColumn(colIndex);
            tc.setCellEditor(jiraEditor);
         }
      }

      // TODO add tooltip for the contents of the table as well by owerriding the getToolTipText method in MyTable (or create JiraTable...)
      setTableHeader(new JTableHeader(columnModel) {
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
   
   public void addJiraEditorListener(CellEditorListener cellEditorListener){
      jiraEditor.addCellEditorListener(cellEditorListener);
   }
   public void addCheckBoxEditorListener(CellEditorListener cellEditorListener){
      checkBoxEditor.addCellEditorListener(cellEditorListener);
   }

   protected void fireTableRowsUpdated(int rowEdited, int rowEdited2) {
      model.fireTableRowsUpdated(convertRowIndexToModel(rowEdited), convertRowIndexToModel(rowEdited2));
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
         int tableSelectedRow = selectedRows[count];
         String jira = (String) getValueAt(Column.Jira, tableSelectedRow);
         int convertRowIndexToModel = convertRowIndexToModel(tableSelectedRow);
         model.removeRow(convertRowIndexToModel);
         notifyAllListenersThatJiraWasRemoved(jira);
      }
   }

   private void notifyAllListenersThatJiraWasRemoved(String jira) {
      for (MyTableListener listener : listeners) {
         listener.jiraRemoved(jira);
      }
   }

   public void setColumnRenderer(int i, TableCellRenderer renderer) {
      TableColumn tc = getTableColumn(i);
      tc.setCellRenderer(renderer);
   }

   public void setValueAt(Object value, int row, Column column) {
      setValueAt(value, row, getColumnIndex(column));
   }

   public void setValueAt(Object value, String jira, Column column) {
      int row = getRowWithJira(jira.toUpperCase());
      if (row < 0) {
         log.warn("Jira " + jira + " doesn't exist in table " + title + " so I cannot setValue(" + value + "," + jira + "," + column + ")");
         return;
      }
      log.debug("Updating " + jira + "'s " + column + " to \"" + value + "\"");
      setValueAt(value, row, column);
   }

   private int getRowWithJira(String jira) {
      int modelRow = model.getRowWithJira(jira);
      if (modelRow < 0) {
         return -1;
      }
      return convertRowIndexToView(modelRow);
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

   public Object getValueAt(Column release, String jira) {
      return model.getValueAt(release, jira);
   }

   public void addListener(MyTableListener myTableListener) {
      listeners.add(myTableListener);
   }

   public boolean doesJiraExist(String jira) {
      return model.doesJiraExist(jira);
   }
}
