package com.jonas.agile.devleadtool.component.table;

import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.ComboTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.ComboTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class MyTable extends JTable {

   private Logger log = MyLogger.getLogger(MyTable.class);
   private TableRowSorter<TableModel> sorter;

   private Object sorterLock = new Object();

   public MyTable() {
      this(new DefaultTableModel());
   }

   public MyTable(MyTableModel modelModel) {
      this((AbstractTableModel) modelModel);
   }

   MyTable(AbstractTableModel defaultTableModel) {
      super(defaultTableModel);
      setDefaultRenderer(String.class, new StringTableCellRenderer());
      setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());
      setDefaultEditor(Boolean.class, new CheckBoxTableCellEditor());

      // setAutoCreateRowSorter(true);
      sorter = new TableRowSorter(defaultTableModel);
      setRowSorter(sorter);

      setDragEnabled(true);
      // FIXME make this dynamic
      if (getModel() instanceof MyTableModel) {
      int boardStatus = getColumnIndex(Column.BoardStatus);
      if (boardStatus > -1) {
         TableColumn tc = getTableColumn(boardStatus);
            tc.setCellRenderer(new ComboTableCellRenderer());
         JComboBox combo = new JComboBox(BoardStatusValue.values());
         tc.setCellEditor(new DefaultCellEditor(combo));
      }
   }
   }

   private TableColumn getTableColumn(int boardStatus) {
      TableColumnModel tcm = getColumnModel();
      return tcm.getColumn(boardStatus);
   }

   public void addEmptyRow() {
      ((MyTableModel) this.getModel()).addEmptyRow();
   }

   public void addJira(JiraIssue jiraIssue) {
      MyTableModel model = getMyModel();
      model.addJira(jiraIssue);
   }

   public void addJira(String jira) {
      MyTableModel model = getMyModel();
      model.addJira(jira);
   }

   public Column getColumnEnum(int itsColumn) {
      MyTableModel model = getMyModel();
      return model.getColumn(convertColumnIndexToModel(itsColumn));
  }

   public int getColumnIndex(Column column) {
       return convertColumnIndexToView((getMyModel()).getColumnIndex(column));
   }

   private MyTableModel getMyModel() {
      return (MyTableModel) getModel();
  }

   public Object getValueAt(Column column, int rowInView) {
      int colInView = getColumnIndex(column);
      MyTableModel model = getMyModel();
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
         (getMyModel()).removeRow(convertRowIndexToModel);
      }
   }

   public void setColumnEditor(int i, ComboTableCellEditor editor) {
      TableColumn tc = getTableColumn(i);
      tc.setCellEditor(editor);

   }

   public void setColumnRenderer(int i, TableCellRenderer renderer) {
      TableColumn tc = getTableColumn(i);
      tc.setCellRenderer(renderer);
   }

   public void setRow(JiraIssue jira, int i) {
      log.debug("setRow for jira: " + jira + "i: " + i);
      setValueAt(jira.getKey(), i, 0);
      // TODO only works with one fix-version below!!
      List<JiraVersion> fixVersions = jira.getFixVersions();
      JiraVersion jiraVersion = fixVersions != null && fixVersions.size() > 0 ? fixVersions.get(0) : null;
      setValueAt(jiraVersion, i, 1);
      setValueAt(jira.getStatus(), i, 2);
      setValueAt(jira.getResolution(), i, 3);
   }

   public void setValueAt(Object value, int row, Column column) {
      setValueAt(value, row, getColumnIndex(column));
   }

   public void setValueAt(Object value, int row, int column) {
      super.setValueAt(value, row, column);
   }


   public TableRowSorter<TableModel> getSorter() {
      return sorter;
   }


   public void addJira(String jira, Map<Column, Object> map) {
      ((MyTableModel)this.getModel()).addJira(jira, map);
   }
}
