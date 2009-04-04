package com.jonas.agile.devleadtool.gui.component.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;
import com.jonas.agile.devleadtool.gui.component.table.editor.BoardStatusCellEditor;
import com.jonas.agile.devleadtool.gui.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.gui.component.table.editor.JiraCellEditor;
import com.jonas.agile.devleadtool.gui.component.table.editor.MyDefaultCellEditor;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.gui.component.table.renderer.StringTableCellRenderer;
import com.jonas.agile.devleadtool.gui.listener.TableListener;
import com.jonas.agile.devleadtool.gui.listener.TableModelListenerAlerter;
import com.jonas.common.ColorUtil;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraIssue;

public class MyTable extends JXTable {

   private class MarkDelegator {
      private boolean allowMarking;

      private MarkDelegator() {
      }

      public void clearMarked() {
         for (int i = 0; i < getRowCount(); i++) {
            model.clearMarked();
         }
      }

      public boolean isAllowMarking() {
         return allowMarking;
      }

      private boolean isMarked(int row) {
         if (!allowMarking)
            return false;
         if (row >= getRowCount() || row == -1)
            return false;
         return model.isMarked(convertRowIndexToModel(row));
      }

      protected void mark(int row) {
         if (allowMarking) {
            model.mark(convertRowIndexToModel(row));
         }
      }

      public void markSelected() {
         if (!allowMarking)
            return;
         int[] rows = getSelectedRows();
         for (int row : rows) {
            mark(row);
         }
      }

      public void setAllowMarking(boolean allowMarking) {
         this.allowMarking = allowMarking;
      }

      private void unMark(int row) {
         model.unMark(convertRowIndexToModel(row));
      }

      private void unMarkSelected() {
         int[] selectedRows = getSelectedRows();
         for (int row : selectedRows) {
            unMark(row);
            fireTableRowsUpdated(row, row);
         }
      }

   }

   private final class MarkKeyListener extends KeyAdapter {
      private final boolean allowMarking;

      private MarkKeyListener(boolean allowMarking) {
         this.allowMarking = allowMarking;
      }

      public void keyPressed(KeyEvent e) {
         switch (e.getKeyCode()) {
         case KeyEvent.VK_M:
            if (allowMarking && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
               log.debug("backspace and mark");
               markSelected();
            }
            break;
         case KeyEvent.VK_ESCAPE:
            clearSelection();
            break;
         }
      }
   }

   private CheckBoxTableCellEditor checkBoxEditor;
   private JiraCellEditor jiraEditor;
   private List<TableListener> listeners = new ArrayList<TableListener>();
   private Logger log = MyLogger.getLogger(MyTable.class);
   private MarkDelegator marker = new MarkDelegator();

   private MyTableModel model;

   private String title;
   private Map<Column, TableColumn> tableColumns;

   public MyTable(String title, MyTableModel model, final boolean allowMarking) {
      super(model);
      this.title = title;
      this.model = model;

      tableColumns = new HashMap<Column, TableColumn>();

      marker.setAllowMarking(allowMarking);
      setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
      setColumnSelectionAllowed(false);
      setRowSelectionAllowed(true);

      setDefaultEditors();

      setFillsViewportHeight(true);

      // TODO add tooltip for the contents of the table as well by owerriding the getToolTipText method in MyTable (or create JiraTable...)
      // setTableHeader(new JTableHeader(columnModel) {
      // @Override
      // public String getToolTipText(MouseEvent e) {
      // Point p = e.getPoint();
      // int colIndex = columnAtPoint(p);
      // return getColumnName(colIndex);
      // }
      // });
      getTableHeader().setToolTipText("blah");
      this.setColumnControlVisible(true);
      this.addKeyListener(new MarkKeyListener(allowMarking));

      Highlighter higlighter = new MyTableHighlighter(this);
      addHighlighter(higlighter);
      // setRolloverEnabled(true);
   }

   public void addCheckBoxEditorListener(CellEditorListener cellEditorListener) {
      checkBoxEditor.addCellEditorListener(cellEditorListener);
   }

   public void addEmptyRow() {
      model.addEmptyRow();
   }

   public void addJira(JiraIssue jiraIssue, boolean markIt) {
      model.addJira(jiraIssue);
      if (markIt)
         markJira(jiraIssue.getKey());
   }

   public void addJira(String jira, boolean markIt) {
      model.addJira(jira);
      if (markIt)
         markJira(jira);
   }

   public void addJira(String jira, Map<Column, Object> map, boolean markIt) {
      model.addJira(jira, map);
      if (markIt)
         markJira(jira);
   }

   public void addJiraEditorListener(CellEditorListener cellEditorListener) {
      jiraEditor.addCellEditorListener(cellEditorListener);
   }

   public void addListener(TableListener myTableListener) {
      listeners.add(myTableListener);
   }

   public void addRow(Vector<Object> rowData) {
      model.addRow(rowData);
   }

   public boolean addSelection(String jira) {
      int jiraRow = getRowWithJira(jira);
      if (jiraRow != -1) {
         addRowSelectionInterval(jiraRow, jiraRow);
         return true;
      }
      return false;
   }

   public void fireTableDataChangedForJira(String jira) {
      int row = getRowWithJira(jira);
      if (row > -1)
         model.fireTableRowsUpdated(convertRowIndexToModel(row), convertRowIndexToModel(row));
   }

   protected void fireTableRowsUpdated(int rowEdited, int rowEdited2) {
      model.fireTableRowsUpdated(convertRowIndexToModel(rowEdited), convertRowIndexToModel(rowEdited2));
   }

   public Column getColumnEnum(int itsColumn) {
      return model.getColumn(convertColumnIndexToModel(itsColumn));
   }

   public int getColumnIndex(Column column) {
      return convertColumnIndexToView(model.getColumnIndex(column));
   }

   public Column[] getCols() {
      Map<Column, Integer> columnNames = model.getColumnNames();
      return columnNames.keySet().toArray(new Column[columnNames.size()]);
   }

   public TableModel getModel() {
      return super.getModel();
   }

   public MyTableModel getMyModel() {
      return model;
   }

   public int getRowWithJira(String jira) {
      int modelRow = model.getRowWithJira(jira);
      if (modelRow < 0) {
         return -1;
      }
      return convertRowIndexToView(modelRow);
   }

   private TableColumn getTableColumn(int colIndex) {
      TableColumnModel tcm = getColumnModel();
      return tcm.getColumn(colIndex);
   }

   public TableModelListenerAlerter getTableModelListenerAlerter() {
      if (model != null)
         return model.getTableModelListenerAlerter();
      return null;
   }

   public String getTitle() {
      return title;
   }

   @Override
   public String getToolTipText(MouseEvent event) {
      Point p = event.getPoint();
      int colIndex = columnAtPoint(p);
      int rowIndex = rowAtPoint(p);
      if (rowIndex == -1 || colIndex == -1) {
         return super.getToolTipText(event);
      }
      rowIndex = convertRowIndexToModel(rowIndex);
      colIndex = convertColumnIndexToModel(colIndex);

      Object valueAt = getModel().getValueAt(rowIndex, colIndex);
      valueAt = (valueAt == null || valueAt.toString().length() == 0) ? " " : valueAt.toString().trim();
      String header = getModel().getColumnName(colIndex).trim();
      String extra = model.getExtraToolTipText(rowIndex, colIndex);

      return getTTText(valueAt, header, extra);
   }

   private String getTTText(Object valueAt, String header, String extra) {
      extra = extra != null && extra.trim().length() > 0 ? " - " + extra : "";
      StringBuffer sb = new StringBuffer(header);
      sb.append(": ").append(valueAt).append(extra);
      return sb.toString();
   }

   public Object getValueAt(Column column, int rowInView) {
      int colInView = getColumnIndex(column);
      return model.getValueAt(convertRowIndexToModel(rowInView), convertColumnIndexToModel(colInView));
   }

   public Object getValueAt(Column release, String jira) {
      return model.getValueAt(release, jira);
   }

   public void insertRow(int index, Vector<Object> rowData) {
      int convertRowIndexToModel = convertRowIndexToModel(index);
      model.insertRow(convertRowIndexToModel, rowData);
   }

   public boolean isColumn(Column column, int colNoToCompare) {
      return column.equals(getColumnEnum(colNoToCompare));
   }

   public boolean isJiraPresent(String jira) {
      return model.isJiraPresent(jira);
   }

   public boolean isMarked(int row) {
      return marker.isMarked(row);
   }

   public boolean isMarkingAllowed() {
      return marker.isAllowMarking();
   }

   public void markJira(String jira) {
      int jiraRow = getRowWithJira(jira);
      marker.mark(jiraRow);
      fireTableDataChangedForJira(jira);
   }

   public void markSelected() {
      marker.markSelected();
   }

   private void notifyAllListenersThatJiraWasRemoved(String jira) {
      for (TableListener listener : listeners) {
         listener.jiraRemoved(jira);
      }
   }

   public void removeSelectedRows() {
      int[] selectedRows = getSelectedRows(); // Need to store this before hand or the last selected row seems to disappear!
      for (int count = selectedRows.length - 1; count > -1; count--) {
         int tableSelectedRow = selectedRows[count];
         String jira = (String) getValueAt(Column.Jira, tableSelectedRow);
         int convertRowIndexToModel = convertRowIndexToModel(tableSelectedRow);
         log.debug("Removing selected row: " + convertRowIndexToModel);
         model.removeRow(convertRowIndexToModel);
         notifyAllListenersThatJiraWasRemoved(jira);
      }
   }

   public void scrollToSelection() {
      int[] selectedRows = getSelectedRows();
      if (selectedRows.length > 0) {
         scrollRectToVisible(getCellRect(selectedRows[0], 0, true));
      }
      if (selectedRows.length > 1) {
         scrollRectToVisible(getCellRect(selectedRows[selectedRows.length - 1], 0, true));
      }

   }

   public void setColumnRenderer(int i, TableCellRenderer renderer) {
      TableColumn tc = getTableColumn(i);
      tc.setCellRenderer(renderer);
   }

   private void setDefaultEditors() {
      JComboBox combo = new JComboBox(BoardStatusValue.values());
      setDefaultEditor(BoardStatusValue.class, new BoardStatusCellEditor(combo, this));

//      int colIndex = getColumnIndex(Column.Release);
//      if (colIndex > -1) {
//         TableColumn tc = getTableColumn(colIndex);
//         tc.setCellEditor(new MyDefaultCellEditor(new JTextField()));
//      }
//      colIndex = getColumnIndex(Column.Jira);
//      if (colIndex > -1) {
//         jiraEditor = new JiraCellEditor(new JTextField());
//         TableColumn tc = getTableColumn(colIndex);
//         tc.setCellEditor(jiraEditor);
//      }
   }

   public void setModel(MyTableModel model) {
      this.model = model;
      super.setModel(model);
   }

   public void setTableModelListenerAlerter(TableModelListenerAlerter listener) {
      model.setTableModelListenerAlerter(listener);
   }

   public void setValueAt(Object value, int row, Column column) {
      getMyModel().setValueAt(value, convertRowIndexToModel(row), column);
   }

   public void setValueAt(Object value, String jira, Column column) {
      getMyModel().setValueAt(value, jira, column);
   }

   public void syncJira(JiraIssue jiraIssue, int tableRowSynced) {
      model.syncJira(jiraIssue, convertRowIndexToModel(tableRowSynced));
   }

   public void unMarkSelection() {
      marker.unMarkSelected();
   }

   public void unSort() {
      setAutoCreateRowSorter(true);
   }

   public void removeColumn(Column column) {
      int colIndex = getColumnIndex(column);
      if (colIndex < 0)
         return;
      TableColumn tableColumn = getTableColumn(colIndex);
      tableColumns.put(column, tableColumn);
      removeColumn(tableColumn);
   }

   public void addColumn(Column column) {
      TableColumn tableColumn = tableColumns.get(column);
      if (tableColumn != null)
         addColumn(tableColumn);
   }

   class MyTableHighlighter extends AbstractHighlighter {

      private final MyTable table;

      public MyTableHighlighter(MyTable table) {
         this.table = table;
      }

      @Override
      protected Component doHighlight(Component component, ComponentAdapter adapter) {
         TableAdapter adapter2 = (TableAdapter) adapter;
         
         boolean hasFocus = adapter.hasFocus();
         boolean isSelected = adapter.isSelected();
         int row = adapter2.row;
         int column = adapter2.column;
         
         JComponent jComponent = null;
         if (component instanceof JComponent){
            jComponent = (JComponent) component;
         } 
         
         if (hasFocus) {
            component.setBackground(SwingUtil.getTableCellFocusBackground());
            if (jComponent != null) {
               jComponent.setBorder(SwingUtil.focusCellBorder);
            }
         } else if (isSelected) {
            component.setBackground(table.getSelectionBackground());
            if (jComponent != null) {
               jComponent.setBorder(SwingUtil.defaultCellBorder);
            }
         } else {
            component.setBackground(table.getBackground());
            if (jComponent != null) {
               jComponent.setBorder(SwingUtil.defaultCellBorder);
            }
         }
         if (!adapter.isEditable() && !hasFocus) {
            component.setBackground(ColorUtil.darkenColor(component.getBackground(), -75));
         }

         if (model != null) {
            setColor(table, isSelected, hasFocus, row, column, component, model, adapter.getValue(), table);
         }
         
         return component;
      }

      private void setColor(final JTable table, final boolean isSelected, final boolean hasFocus, final int row, final int column,
            final Component cell, final MyTableModel model, final Object value, final MyTable myTable) {
         Color color = model.getColor(value, table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column));
         if (color != null) {
            if (isSelected) {
               color = ColorUtil.darkenColor(color, -75);
            }
            cell.setBackground(color);
         } else if (myTable != null && !isSelected && !hasFocus && myTable.isMarked(row)) {
            cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -35));
         }
      }
   }
}
