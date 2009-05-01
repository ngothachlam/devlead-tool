package com.jonas.agile.devleadtool.gui.component.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.table.TableColumnExt;
import com.jonas.agile.devleadtool.gui.component.table.editor.BoardStatusCellEditor;
import com.jonas.agile.devleadtool.gui.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.gui.component.table.editor.JiraCellEditor;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.listener.TableListener;
import com.jonas.agile.devleadtool.gui.listener.TableModelListenerAlerter;
import com.jonas.agile.devleadtool.sprint.Sprint;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.agile.devleadtool.sprint.table.SprintComboBoxModel;
import com.jonas.common.ColorUtil;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraIssue;

public class MyTable extends JXTable {

   private CheckBoxTableCellEditor checkBoxEditor;

   private ColumnRearranger columnRearranger;

   private DuplicateHighlighter duplicateHighlighter;

   private JiraCellEditor jiraEditor;

   private List<TableListener> listeners = new ArrayList<TableListener>();

   private Logger log = MyLogger.getLogger(MyTable.class);

   private MarkDelegator marker = new MarkDelegator();
   private MyTableModel model;
   private Map<Column, TableColumn> tableColumns;
   private String title;

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

      // TODO add tooltip for the contents of the table as well by owerriding
      // the getToolTipText method in MyTable (or create JiraTable...)
      // setTableHeader(new JTableHeader(columnModel) {
      // @Override
      // public String getToolTipText(MouseEvent e) {
      // Point p = e.getPoint();
      // int colIndex = columnAtPoint(p);
      // return getColumnName(colIndex);
      // }
      // });
      // getTableHeader().setToolTipText("blah");
      this.setColumnControlVisible(true);
      columnRearranger = new ColumnRearranger(this);
      this.addKeyListener(new MarkKeyListener(allowMarking));
      addHighlighter(new MyTableHighlighter(this));
      duplicateHighlighter = new DuplicateHighlighter(this);
      addHighlighter(duplicateHighlighter);
      // setRolloverEnabled(true);
   }

   public void addCheckBoxEditorListener(CellEditorListener cellEditorListener) {
      checkBoxEditor.addCellEditorListener(cellEditorListener);
   }

   public void addColumn(Column column) {
      TableColumn tableColumn = tableColumns.get(column);
      if (tableColumn != null)
         addColumn(tableColumn);
   }

   public void addEmptyRow() {
      model.addEmptyRow();
   }

   private void addJira(JiraIssue jiraIssue, boolean isToMarkJiraIfNew) {
      boolean isNewJiraInTable = model.addJira(jiraIssue);
      if (isToMarkJiraIfNew && isNewJiraInTable)
         markJira(jiraIssue.getKey());
   }

   public void addJira(String jira) {
      addJira(jira, true);
   }

   private void addJira(String jira, boolean isToMarkJira) {
      model.addJira(jira);
      if (isToMarkJira)
         markJira(jira);
   }

   private void addJira(String jira, Map<Column, Object> map, boolean isToMarkJiraIfNew) {
      boolean isNewJiraInTable = model.addJira(jira, map);
      if (isNewJiraInTable && isToMarkJiraIfNew)
         markJira(jira);
   }

   public void addJiraAndMarkIfNew(JiraIssue jiraIssue) {
      addJira(jiraIssue, true);
   }

   public void addJiraAndMarkIfNew(String key) {
      addJira(key, true);
   }

   public void addJiraAndMarkIfNew(String jiraString, Map<Column, Object> map) {
      addJira(jiraString, map, true);
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

   public void changeMarkOfSelected() {
      marker.changeMarkOfSelected();
   }

   public void fireTableDataChanged() {
      getMyModel().fireTableDataChanged();
   }

   public void fireTableDataChangedForJira(String jira) {
      int row = getRowWithJira(jira);
      if (row > -1)
         model.fireTableRowsUpdated(convertRowIndexToModel(row), convertRowIndexToModel(row));
   }

   protected void fireTableRowsUpdated(int rowEdited, int rowEdited2) {
      model.fireTableRowsUpdated(convertRowIndexToModel(rowEdited), convertRowIndexToModel(rowEdited2));
   }

   public Column[] getCols() {
      Map<Column, Integer> columnNames = model.getColumnNames();
      return columnNames.keySet().toArray(new Column[columnNames.size()]);
   }

   public Column getColumnEnum(int itsColumn) {
      return model.getColumn(convertColumnIndexToModel(itsColumn));
   }

   public int getColumnIndex(Column column) {
      return convertColumnIndexToView(model.getColumnIndex(column));
   }

   @Override
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

   public boolean isJiraPresentAsDupe(String jira) {
      int rowCount = getRowCount();
      Set<String> jiras = new HashSet<String>();
      for (int row = 0; row < rowCount; row++) {
         String testJira = (String) getValueAt(Column.Jira, row);
         if (jiras.contains(testJira)) {
            return true;
         }
         if (jira.equalsIgnoreCase(testJira)) {
            jiras.add(testJira);
         }
      }
      return false;
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
   }

   public void markSelected() {
      marker.markSelected();
   }

   private void notifyAllListenersThatJiraWasRemoved(String jira) {
      for (TableListener listener : listeners) {
         listener.jiraRemoved(jira);
      }
   }

   public void removeColumn(Column column) {
      int colIndex = getColumnIndex(column);
      if (colIndex < 0)
         return;
      TableColumn tableColumn = getTableColumn(colIndex);
      tableColumns.put(column, tableColumn);
      removeColumn(tableColumn);
   }

   public void removeSelectedRows() {
      int[] selectedRows = getSelectedRows(); // Need to store this before hand
      // or the last selected row seems
      // to disappear!
      for (int count = selectedRows.length - 1; count > -1; count--) {
         int tableSelectedRow = selectedRows[count];
         String jira = (String) getValueAt(Column.Jira, tableSelectedRow);
         int convertRowIndexToModel = convertRowIndexToModel(tableSelectedRow);
         if (log.isDebugEnabled())
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

   private void setDefaultEditors() {
      JComboBox combo = new JComboBox(BoardStatusValue.values());
      setDefaultEditor(BoardStatusValue.class, new BoardStatusCellEditor(combo, this));

      SprintCache sprintCache = getMyModel().getSprintCache();
      if (sprintCache != null) {
         ComboBoxModel model = new SprintComboBoxModel(sprintCache);
         JComboBox sprintCombo = new JComboBox(model);
         setDefaultEditor(Sprint.class, new BoardStatusCellEditor(sprintCombo, this));
      }
   }

   public void setDupelicateHighlighterEnableQuery(EnabledQuery checkForDuplicatesEnabledQueryAction) {
      duplicateHighlighter.setEnabledQuery(checkForDuplicatesEnabledQueryAction);
   }

   public void setJiraBasedOnJiraColumns(JiraIssue jira) {
      model.setJiraBasedOnJiraColumns(jira);
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

   public void showAndRearrangeColumns(String... colsToShowAndRearrange) {
      columnRearranger.showAndRearrangeColumns(colsToShowAndRearrange);
   }

   public void unMarkSelection() {
      marker.unMarkSelected();
   }

   public void unSort() {
      setAutoCreateRowSorter(true);
   }

   private class ColumnRearranger {
      private MyTable table;

      private ColumnRearranger(MyTable table) {
         this.table = table;
      }

      private int getNewColumn(String string) {
         for (int colIndex = 0; colIndex < table.getColumnCount(); colIndex++) {
            if (table.getColumnName(colIndex).equals(string))
               return colIndex;
         }
         return -1;
      }

      private void hideAllColumns() {
         for (int col = table.getColumnCount() - 1; col >= 0; col--) {
            TableColumnExt columnExt = table.getColumnExt(col);
            columnExt.setVisible(false);
         }
      }

      private void showAndRearrangeColumns(String... colsToShowAndRearrange) {
         hideAllColumns();
         for (String columnName : colsToShowAndRearrange) {
            showAndRearrangeColumnToCurrentLastColumn(columnName);
         }
      }

      private void showAndRearrangeColumnToCurrentLastColumn(String string) {
         TableColumnModel columnModel = table.getColumnModel();
         TableColumnExt columnExt;
         columnExt = table.getColumnExt(string);
         if (columnExt == null) {
            log.warn(string + " does not exist in table (maybe hidden already? or it just doesn't exist even in the model?");
            return;
         }
         columnExt.setVisible(true);
         int addedColIndex = getNewColumn(string);
         int newIndex = table.getColumnCount() - 1;
         if (newIndex != addedColIndex)
            columnModel.moveColumn(addedColIndex, newIndex);
      }

      public void resetColumns() {
         hideAllColumns();
         MyTableModel model = table.getMyModel();
         for (int col = 0; col < model.getColumnCount(); col++) {
            String colName = model.getColumnName(col);
            showAndRearrangeColumnToCurrentLastColumn(colName);
         }
      }

   }

   class DuplicateHighlighter extends AbstractHighlighter {

      private EnabledQuery enabledQuery;

      MyTableModel model;
      private final MyTable myTable;

      public DuplicateHighlighter(MyTable myTable) {
         this.myTable = myTable;
         this.model = myTable.getMyModel();
      }

      @Override
      protected Component doHighlight(Component cellComponent, ComponentAdapter componentAdapter) {
         if (enabledQuery != null && enabledQuery.isEnabled()) {
            if (cellComponent instanceof JComponent) {
               TableAdapter adapter = (TableAdapter) componentAdapter;

               if (adapter.column == 0) {
                  JComponent textField = (JComponent) cellComponent;
                  String jira = (String) myTable.getValueAt(Column.Jira, adapter.row);
                  if (myTable.isJiraPresentAsDupe(jira)) {
                     textField.setForeground(Color.RED);
                  }
               }
            }
         }
         return cellComponent;
      }

      public void setEnabledQuery(EnabledQuery enabledQuery) {
         this.enabledQuery = enabledQuery;
      }

   }

   private class MarkDelegator {
      private boolean allowMarking;

      private MarkDelegator() {
      }

      public void changeMarkOfSelected() {
         int[] selectedRows = getSelectedRows();
         boolean atLeastOneMarked = false;
         boolean atLeastOneUnMarked = false;
         for (int row : selectedRows) {
            if (!atLeastOneMarked && isMarked(row)) {
               atLeastOneMarked = true;
            } else if (!atLeastOneUnMarked && !isMarked(row)) {
               atLeastOneUnMarked = true;
            }

            if (atLeastOneMarked && atLeastOneUnMarked) {
               break;
            }
         }
         if (atLeastOneMarked && !atLeastOneUnMarked) {
            unMarkSelected();
         } else if (atLeastOneMarked || atLeastOneUnMarked) {
            markSelected();
         }
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
         }
      }

   }

   private final class MarkKeyListener extends KeyAdapter {
      private final boolean allowMarking;

      private MarkKeyListener(boolean allowMarking) {
         this.allowMarking = allowMarking;
      }

      @Override
      public void keyPressed(KeyEvent e) {
         switch (e.getKeyCode()) {
            case KeyEvent.VK_M:
               if (allowMarking && e.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
                  log.debug("backspace and mark");
                  changeMarkOfSelected();
               }
               break;
            case KeyEvent.VK_ESCAPE:
               clearSelection();
               break;
         }
      }
   }

   class MyTableHighlighter extends AbstractHighlighter {

      private final MyTable table;

      public MyTableHighlighter(MyTable table) {
         this.table = table;
      }

      @Override
      protected Component doHighlight(Component component, ComponentAdapter adapter) {
         TableAdapter adapter2 = (TableAdapter) adapter;

         int row = adapter2.row;
         int column = adapter2.column;

         if (log.isDebugEnabled())
            log.debug("doHighlight of row: " + row + " and col: " + column);

         boolean hasFocus = adapter.hasFocus();
         boolean isSelected = adapter.isSelected();
         boolean isEditable = adapter.isEditable();

         JComponent jComponent = null;
         if (component instanceof JComponent) {
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
         if (!isEditable && !hasFocus) {
            component.setBackground(ColorUtil.darkenColor(component.getBackground(), -75));
         }

         if (model != null) {
            setColor(table, isSelected, hasFocus, row, column, component, model, adapter.getValue(), table);
         }

         return component;
      }

      private void setColor(final JTable table, final boolean isSelected, final boolean hasFocus, final int row, final int column, final Component cell, final MyTableModel model, final Object value, final MyTable myTable) {
         ColorDTO colorDTO = model.getColor(value, table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column));

         Color color = colorDTO.getColor();
         if (color != null) {
            if (isSelected) {
               color = ColorUtil.darkenColor(color, -55);
            }
            cell.setBackground(color);
         } else if (myTable != null && !isSelected && !hasFocus && myTable.isMarked(row)) {
            cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -35));
         }

         if (colorDTO.isMarked()) {
            cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), -25));
         } else {
            cell.setBackground(ColorUtil.darkenColor(cell.getBackground(), +35));
         }
      }
   }

   public void resetColumns() {
      columnRearranger.resetColumns();
   }
}
