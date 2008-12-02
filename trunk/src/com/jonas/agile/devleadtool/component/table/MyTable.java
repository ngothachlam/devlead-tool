package com.jonas.agile.devleadtool.component.table;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.CellEditorListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.listener.TableListener;
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

   private CheckBoxTableCellEditor checkBoxEditor;
   private JiraCellEditor jiraEditor;
   private List<TableListener> listeners = new ArrayList<TableListener>();
   private Logger log = MyLogger.getLogger(MyTable.class);
   private MarkDelegator marker = new MarkDelegator();
   private MyTableModel model;
   private String title;

   private MyTable(String title, AbstractTableModel defaultTableModel, final boolean allowMarking) {
      super(defaultTableModel);
      this.title = title;
      marker.setAllowMarking(allowMarking);
      getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      setColumnSelectionAllowed(false);
      setRowSelectionAllowed(true);

      CheckBoxTableCellRenderer checkBoxRenderer = new CheckBoxTableCellRenderer(defaultTableModel);
      checkBoxEditor = new CheckBoxTableCellEditor(new JCheckBox());

      setDefaultRenderer(Object.class, new MyDefaultCellRenderer());
      setDefaultRenderer(Integer.class, new MyDefaultCellRenderer());
      setDefaultRenderer(String.class, new StringTableCellRenderer(defaultTableModel));
      setDefaultRenderer(Boolean.class, checkBoxRenderer);
      setComboEditors();

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
            int colIndex = columnAtPoint(p);
            return getColumnName(colIndex);
         }
      });

      getModel().addTableModelListener(marker);
      this.addKeyListener(new MarkKeyListener(allowMarking));
   }

//   List<Class> comboClasses = new ArrayList<Class>();

   private void setComboEditors() {
      JComboBox combo = new JComboBox(BoardStatusValue.values());
//      comboClasses.add(BoardStatusValue.class);
      setDefaultEditor(BoardStatusValue.class, new BoardStatusCellEditor(combo, this));
   }

   MyTable(String title, boolean allowMarking) {
      this(title, new DefaultTableModel(), allowMarking);
   }

   public void markSelected() {
      marker.markSelected();
   }

   public MyTable(String title, MyTableModel modelModel, boolean allowMarking) {
      this(title, (AbstractTableModel) modelModel, allowMarking);
   }

   public void addCheckBoxEditorListener(CellEditorListener cellEditorListener) {
      checkBoxEditor.addCellEditorListener(cellEditorListener);
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

   public void addJira(String jira, Map<Column, Object> map) {
      model.addJira(jira, map);
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

   public boolean doesJiraExist(String jira) {
      return model.doesJiraExist(jira);
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

   public Column[] getColumns() {
      Map<Column, Integer> columnNames = model.getColumnNames();
      return columnNames.keySet().toArray(new Column[columnNames.size()]);
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

   // public TableCellEditor getTableColumnEditor(int colIndex) {
   // return getTableColumn(colIndex).getCellEditor();
   // }

   // public TableColumn getTableEditor(int boardStatus) {
   // TableColumnModel tcm = getColumnModel();
   // return tcm.getColumn(boardStatus);
   // }

   public String getTitle() {
      return title;
   }

   public Object getValueAt(Column column, int rowInView) {
      int colInView = getColumnIndex(column);
      return model.getValueAt(convertRowIndexToModel(rowInView), convertColumnIndexToModel(colInView));
   }

   public Object getValueAt(Column release, String jira) {
      return model.getValueAt(release, jira);
   }

   // public TableRowSorter<TableModel> getSorter() {
   // return sorter;
   // }

   public void insertRow(int index, Vector<Object> rowData) {
      int convertRowIndexToModel = convertRowIndexToModel(index);
      System.out.println("insertRow for model row: " + convertRowIndexToModel);
      model.insertRow(convertRowIndexToModel, rowData);
   }

   public boolean isColumn(Column column, int colNoToCompare) {
      return column.equals(getColumnEnum(colNoToCompare));
   }

   public boolean isMarked(int row) {
      return marker.isMarked(row);
   }

   public boolean isMarkingAllowed() {
      return marker.isAllowMarking();
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

   public void setModel(MyTableModel model) {
      this.model = model;
      super.setModel(model);
   }

   public void setValueAt(Object value, int row, Column column) {
      setValueAt(value, row, getColumnIndex(column));
   }

   public void setValueAt(Object value, int row, int column) {
      super.setValueAt(value, row, column);
   }

   public void setValueAt(Object value, String jira, Column column) {
      int row = getRowWithJira(jira.toUpperCase());
      if (row < 0) {
         log.warn("Jira " + jira + " doesn't exist in table " + title + " so I cannot setValue(" + value + "," + jira + "," + column + ")");
         return;
      }
      log.debug("Updating " + jira + "'s " + column + " to \"" + value + "\" in table " + getTitle());
      setValueAt(value, row, column);
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
//         case KeyEvent.VK_SPACE:
//            int selectedRow = getSelectedRow();
//            int selectedColumn = getSelectedColumn();
//            if (getSelectedColumnCount() != 1 && getSelectedRowCount() != 1)
//               return;
//            if (comboClasses.contains(getValueAt(selectedRow, selectedColumn).getClass())) {
//               boolean ok = editCellAt(selectedRow, selectedColumn);
//               Component comp = getEditorComponent();
//               log.debug("editing? " + ok + " - " + comp.getClass());
//               if (ok && comp instanceof JComboBox){
//                  ((JComboBox) comp).setVisible(true);
//                  log.debug("Should set visible!");
//               }
//
//            }
//            break;
         }
      }
   }

   private class MarkDelegator implements TableModelListener {
      private boolean allowMarking;
      private Map<Integer, Boolean> marked = new HashMap<Integer, Boolean>();

      private MarkDelegator() {
         int rowCount = getRowCount();
         for (int i = 0; i < rowCount; i++) {
            marked.put(convertRowIndexToModel(i), Boolean.FALSE);
         }
      }

      public void clearMarked() {
         for (int i = 0; i < getRowCount(); i++) {
            marked.put(convertRowIndexToModel(i), Boolean.FALSE);
         }
      }

      public boolean isAllowMarking() {
         return allowMarking;
      }

      private boolean isMarked(int row) {
         log.debug("isMarked for row " + row + " whilst rowCount is " + getRowCount() + " and marked list is " + marked.size());
         if (row >= getRowCount())
            return false;
         Integer convertRowIndexToModel = convertRowIndexToModel(row);
         if (marked.containsKey(convertRowIndexToModel))
            return marked.get(convertRowIndexToModel);
         return false;
      }

      private void mark(int row) {
         marked.put(convertRowIndexToModel(row), Boolean.TRUE);
      }

      public void markSelected() {
         int[] rows = getSelectedRows();
         for (int row : rows) {
            mark(row);
         }
      }

      public void setAllowMarking(boolean allowMarking) {
         this.allowMarking = allowMarking;
      }

      @Override
      public void tableChanged(TableModelEvent e) {
         int firstRow = e.getFirstRow();
         int lastRow = e.getLastRow();
         switch (e.getType()) {
         case TableModelEvent.DELETE:
            log.debug("table changed by deleting " + firstRow + " to " + lastRow);
            for (int i = firstRow; i <= lastRow; i++) {
               log.debug("\ti = " + i);
               for (int j = i; j < getRowCount(); j++) {
                  log.debug("\t\tj = " + j);
                  int newKey = j;
                  int oldKey = newKey + 1;
                  Boolean valueToMoveUp = marked.get(oldKey);
                  log.debug("\t\tnewKey = " + newKey + " oldKey = " + oldKey + " valueToMoveUp = " + valueToMoveUp);
                  marked.put(newKey, valueToMoveUp);
               }
               marked.remove(convertRowIndexToModel(getRowCount() - 1) + 1);
            }
            break;
         case TableModelEvent.INSERT:
            log.debug("table changed by inserting " + firstRow + " to " + lastRow);
            for (int i = firstRow; i <= lastRow; i++) {
               marked.put(i, Boolean.FALSE);
            }
            break;
         default:
            break;
         }
      }

      private void unMark(int row) {
         marked.put(convertRowIndexToModel(row), Boolean.FALSE);
      }

      private void unMarkSelected() {
         int[] selectedRows = getSelectedRows();
         for (int row : selectedRows) {
            unMark(row);
            fireTableRowsUpdated(row, row);
         }
      }

   }

}
