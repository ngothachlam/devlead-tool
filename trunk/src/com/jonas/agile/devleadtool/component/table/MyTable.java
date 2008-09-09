package com.jonas.agile.devleadtool.component.table;

import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.table.editor.ComboTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraVersion;

public class MyTable extends JTable {

   private Logger log = MyLogger.getLogger(MyTable.class);

   public MyTable() {
      super();
   }

   public void addEmptyRow() {
      ((MyTableModel)this.getModel()).addEmptyRow();
   }
   
   public void setColumnRenderer(int i, TableCellRenderer renderer) {
      TableColumnModel tcm = getColumnModel();
      TableColumn tc = tcm.getColumn(i);
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

   public void setColumnEditor(int i, ComboTableCellEditor editor) {
      TableColumnModel tcm = getColumnModel();
      TableColumn tc = tcm.getColumn(i);
      tc.setCellEditor(editor);

   }

//   public boolean isColumnEqual(int itsColumn, Column column) {
//      return getColumnName(itsColumn).equals(column.toString());
//   }

   public Column getColumnEnum(int itsColumn) {
      MyTableModel model = (MyTableModel) getModel();
      return model.getColumnEnum(convertColumnIndexToModel(itsColumn));
   }

   private int getColumnIndex(Column column) {
      return convertColumnIndexToView(((MyTableModel) getModel()).getColumnNo(column));
   }

   public void removeSelectedRows() {
      int[] selectedRows = getSelectedRows(); // Need to store this before hand or the last selected row seems to disappear!
      for (int count = selectedRows.length - 1; count > -1; count--) {
         // we can't remove upwards the table and down (from lower to greater index) as the table changes on each delete.
         int tableSelectedRow = selectedRows[count];
         int convertRowIndexToModel = convertRowIndexToModel(tableSelectedRow);
         ((MyTableModel) getModel()).removeRow(convertRowIndexToModel);
      }
   }

   public boolean isColumn(Column column, int colNoToCompare) {
      return column.equals(getColumnEnum(colNoToCompare));
   }
   
   public Object getValueAt(Column column, int rowInView) {
      int colInView = getColumnIndex(column);
      MyTableModel model = (MyTableModel)getModel();
      return model.getValueAt(convertRowIndexToModel(rowInView), convertColumnIndexToModel(colInView));
   }

   public void addJira(String jira) {
      MyTableModel model = (MyTableModel) getModel();
      model.addJira(jira);
   }

   public void setValueAt(Object value, int row, Column column) {
      setValueAt(value, row, getColumnIndex(column));
   }
}
