package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.MyEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardStatusToColumnMap;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

public class InternalTabPanel extends MyComponentPanel {

   private final class BoardEditorListener implements CellEditorListener {
      private final MyTable boardTable;
      private final MyTable jiraTable;
      private MyTableModel jiraModel;
      private MyTableModel boardModel;
      private Logger log = MyLogger.getLogger(BoardEditorListener.class);

      public BoardEditorListener(MyTable boardTable, MyTable jiraTable) {
         this.boardTable = boardTable;
         this.jiraTable = jiraTable;
         boardModel = (MyTableModel) boardTable.getModel();
         jiraModel = (MyTableModel) jiraTable.getModel();
      }

      @Override
      public void editingCanceled(ChangeEvent e) {
      }

      @Override
      public void editingStopped(ChangeEvent e) {
         log.debug(e);
         Object editorObject = e.getSource();
         MyEditor editor;
         if (editorObject instanceof MyEditor) {
            editor = (MyEditor) editorObject;
         } else {
            log.warn(editorObject + " needs to implemnet interface MyEditor to be used in this context!");
            return;
         }

         String jira = (String) boardModel.getValueAt(Column.Jira, editor.getRowEdited());
         Column boardColumn = boardTable.getColumnEnum(editor.getColEdited());
         log.debug("Edited column " + boardColumn + " and its jira is " + jira);

         int jiraModelRowWithJira = jiraModel.getRowWithJira(jira, Column.Jira);
         switch (boardColumn) {
         case isOpen:
         case isBug:
         case isComplete:
         case isInProgress:
         case isParked:
         case isResolved:
            jiraTable.setValueAt(getStatus(editor), jiraModelRowWithJira, Column.B_BoardStatus);
            jiraModel.fireTableRowsUpdated(editor.getRowEdited(), editor.getRowEdited());
            break;
         case Release:
            String release = (String) editor.getValue();
            jiraModel.setValueAt(release, jiraModelRowWithJira, Column.B_Release);
            jiraModel.fireTableRowsUpdated(jiraModelRowWithJira, jiraModelRowWithJira);
            log.debug("Setting Release to " + release + " on jiraModel row " + jiraModelRowWithJira);
            break;
         default:
            break;
         }
      }

      public BoardStatusValue getStatus(MyEditor editor) {
         int row = editor.getRowEdited();
         if (row >= 0) {
            for (Column column : boardTable.getColumns()) {
               switch (column) {
               case isOpen:
               case isBug:
               case isInProgress:
               case isResolved:
               case isComplete:
                  if (getBoardStatus(row, column)) {
                     return BoardStatusToColumnMap.getBoardStatus(column);
                  }
               default:
                  break;
               }
            }
         }
         return BoardStatusValue.UnKnown;
      }

      private boolean getBoardStatus(int row, Column column) {
         int columnTemp = boardTable.getColumnIndex(column);
         Boolean valueAt = (Boolean) boardTable.getValueAt(row, columnTemp);
         return valueAt == null ? false : valueAt.booleanValue();
      }
   }

   private final class BoardTableModelListener implements TableModelListener {
      private BoardTableModel boardModel;
      private JiraTableModel jiraModel;

      public BoardTableModelListener(BoardTableModel boardModel, JiraTableModel jiraModel) {
         this.boardModel = boardModel;
         this.jiraModel = jiraModel;
      }

      public void tableChanged(TableModelEvent e) {
         jiraModel.fireTableDataChanged();
      }
   }

   private BoardPanel boardPanel;
   private PlanPanel planPanel;
   private JiraPanel jiraPanel;

   private JCheckBox editableCheckBox;

   private List<MyTablePopupMenu> popups = new ArrayList<MyTablePopupMenu>(3);

   public InternalTabPanel(PlannerHelper client) {
      this(client, null, null, null);
   }

   public InternalTabPanel(PlannerHelper helper, BoardTableModel boardModel, MyTableModel planModel, JiraTableModel jiraModel) {
      super(new BorderLayout());
      boardModel = (boardModel == null) ? new BoardTableModel() : boardModel;
      planModel = (planModel == null) ? new PlanTableModel() : planModel;
      jiraModel = (jiraModel == null) ? new JiraTableModel() : jiraModel;
      jiraModel.setBoardModel(boardModel);
      boardModel.addTableModelListener(new BoardTableModelListener(boardModel, jiraModel));

      boardPanel = new BoardPanel(boardModel);
      planPanel = new PlanPanel(helper, planModel);
      jiraPanel = new JiraPanel(helper, jiraModel);

      // FIXME I want to put this into the MyTable instead!! - need to register the other tables with each other!
      MyTable boardTable = boardPanel.getTable();
      popups.add(new MyTablePopupMenu(boardTable, helper, boardTable, planPanel.getTable(), jiraPanel.getTable()));
      popups.add(new MyTablePopupMenu(planPanel.getTable(), helper, boardTable, planPanel.getTable(), jiraPanel.getTable()));
      popups.add(new MyTablePopupMenu(jiraPanel.getTable(), helper, boardTable, planPanel.getTable(), jiraPanel.getTable()));

      addEditorListenerToUpdateJiraTableWhenBoardTableChanges(boardTable);

      JPanel panel = new JPanel();
      editableCheckBox = new JCheckBox("Editable?", true);
      panel.add(editableCheckBox);
      panel.add(getAddPanel(helper, boardTable, jiraPanel.getTable(), planPanel.getTable()));
      addNorth(panel);

      makeContent(boardModel);
      wireUpListeners();
      this.setBorder(BorderFactory.createEmptyBorder(0, 2, 1, 0));
   }

   private void addEditorListenerToUpdateJiraTableWhenBoardTableChanges(MyTable boardTable) {
      BoardEditorListener boardTableEditorListener = new BoardEditorListener(boardTable, jiraPanel.getTable());
      for (int i = 0; i < boardTable.getColumnCount(); i++) {
         TableCellEditor cellEditor = boardTable.getCellEditor(0, i);
         cellEditor.addCellEditorListener(boardTableEditorListener);
      }
   }

   private Component getAddPanel(final PlannerHelper helper, MyTable boardTable, MyTable jiraTable, MyTable planTable) {
      JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
      final List<MyTable> tables = new ArrayList<MyTable>();
      tables.add(boardTable);
      tables.add(jiraTable);
      tables.add(planTable);

      final MyTable[] array = tables.toArray(new MyTable[tables.size()]);
      addButton(panel, "Add", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            new AddManualDialog(helper.getParentFrame(), array);
         }
      });
      addButton(panel, "Filters", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            new AddFilterDialog(helper.getParentFrame(), array);
         }
      });
      addButton(panel, "Versions", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            new AddVersionDialog(helper.getParentFrame(), array);
         }
      });

      return panel;
   }

   public void makeContent(MyTableModel boardTableModel) {
      addCenter(combineIntoSplitPane(boardPanel, jiraPanel, planPanel));
   }

   private Component combineIntoSplitPane(JPanel panel1, JPanel panel2, JPanel panel3) {
      JSplitPane tabPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      JSplitPane tabPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

      tabPane.add(panel1);
      tabPane.add(tabPane2);
      tabPane2.add(panel2);
      tabPane2.add(panel3);

      tabPane.setDividerLocation(550);
      tabPane2.setDividerLocation(350);
      return tabPane;
   }

   public void wireUpListeners() {
      editableCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boardPanel.setEditable(editableCheckBox.isSelected());
            planPanel.setEditable(editableCheckBox.isSelected());
            jiraPanel.setEditable(editableCheckBox.isSelected());
         }
      });
   }

   protected void closing() {
      // client.disconnect();
   }

   public BoardPanel getBoardPanel() {
      return boardPanel;
   }

   public JiraPanel getJiraPanel() {
      return jiraPanel;
   }

   public PlanPanel getPlanPanel() {
      return planPanel;
   }
}
