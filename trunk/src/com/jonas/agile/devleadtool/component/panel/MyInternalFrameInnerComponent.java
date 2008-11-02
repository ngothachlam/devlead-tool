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
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.component.listener.BoardAndJiraSyncListener;
import com.jonas.agile.devleadtool.component.listener.TableListener;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.JiraCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.DnDTreePanel;
import com.jonas.testing.tree.fromScratch.JiraParseListenerImpl;
import com.jonas.testing.tree.fromScratch.tree.DnDTree;
import com.jonas.testing.tree.fromScratch.tree.model.DnDTreeModel;
import com.jonas.testing.tree.fromScratch.xml.DnDTreeBuilder;
import com.jonas.testing.tree.fromScratch.xml.JiraSaxHandler;
import com.jonas.testing.tree.fromScratch.xml.XmlParser;
import com.jonas.testing.tree.fromScratch.xml.XmlParserImpl;

public class MyInternalFrameInnerComponent extends MyComponentPanel {

   private Logger log = MyLogger.getLogger(MyInternalFrameInnerComponent.class);

   private BoardPanel boardPanel;

   private JiraPanel jiraPanel;

   private JCheckBox editableCheckBox;

   private List<MyTablePopupMenu> popups = new ArrayList<MyTablePopupMenu>(3);

   private DnDTreePanel sprintPanel;

   public MyInternalFrameInnerComponent(PlannerHelper client) {
      this(client, null, null);
   }

   public MyInternalFrameInnerComponent(PlannerHelper helper, BoardTableModel boardModel, JiraTableModel jiraModel) {
      super(new BorderLayout());
      try {
         boardModel = (boardModel == null) ? new BoardTableModel() : boardModel;
         jiraModel = (jiraModel == null) ? new JiraTableModel() : jiraModel;

         DnDTreeModel model = new DnDTreeModel("LLU");
         DnDTree tree = new DnDTree(model);
         JiraSaxHandler saxHandler = new JiraSaxHandler();
         saxHandler.addJiraParseListener(new JiraParseListenerImpl(tree));

         XmlParser parser = new XmlParserImpl(saxHandler);
         // XmlParser parser = new XmlParserLargeMock(saxHandler);
         // XmlParser parser = new XmlParserAtlassain(saxHandler);

         DnDTreeBuilder dndTreeBuilder = new DnDTreeBuilder(parser);

         makeContent(boardModel, tree, dndTreeBuilder, helper, jiraModel);
         this.setBorder(BorderFactory.createEmptyBorder(0, 2, 1, 0));

         wireUpListeners(boardModel, jiraModel);
      } catch (SAXException e) {
         e.printStackTrace();
      }
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

   private Component getAddPanel(final PlannerHelper helper, MyTable boardTable, MyTable jiraTable) {
      JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
      final List<MyTable> tables = new ArrayList<MyTable>();
      tables.add(boardTable);
      tables.add(jiraTable);

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

   public BoardPanel getBoardPanel() {
      return boardPanel;
   }

   public JiraPanel getJiraPanel() {
      return jiraPanel;
   }

   public void makeContent(MyTableModel boardModel, DnDTree tree, DnDTreeBuilder dndTreeBuilder, PlannerHelper helper, MyTableModel jiraModel) {
      boardPanel = new BoardPanel(boardModel);
      sprintPanel = new DnDTreePanel(tree, dndTreeBuilder, helper.getParentFrame());
      jiraPanel = new JiraPanel(helper, jiraModel);

      // FIXME I want to put this into the MyTable instead!! - need to register the other tables with each other!
      MyTable boardTable = boardPanel.getTable();
      popups.add(new MyTablePopupMenu(boardTable, helper, boardTable, jiraPanel.getTable()));
      popups.add(new MyTablePopupMenu(jiraPanel.getTable(), helper, boardTable, jiraPanel.getTable()));

      JPanel panel = new JPanel();
      editableCheckBox = new JCheckBox("Editable?", true);
      panel.add(editableCheckBox);
      panel.add(getAddPanel(helper, boardTable, jiraPanel.getTable()));
      addNorth(panel);

      addCenter(combineIntoSplitPane(boardPanel, jiraPanel, sprintPanel));
   }

   private void setBoardDataListeners(final BoardTableModel boardModel) {
      boardModel.addTableModelListener(new BoardAndJiraSyncListener(boardPanel.getTable(), jiraPanel.getTable(), boardModel));
      boardModel.addTableModelListener(new MyTableModelListener());
      boardPanel.getTable().getSelectionModel().addListSelectionListener(new MyBoardSelectionListener(boardPanel.getTable(), jiraPanel.getTable()));
      boardPanel.getTable().addListener(new MyTableListener());
      boardPanel.getTable().addJiraEditorListener(new MyJiraCellEditorListener());
      boardPanel.getTable().addCheckBoxEditorListener(new MyCheckboxCellEditorListener());
   }

   private void setJiraDataListener(JiraTableModel jiraModel, final BoardTableModel boardModel) {
      jiraModel.addTableModelListener(new BoardAndJiraSyncListener(boardPanel.getTable(), jiraPanel.getTable(), boardModel));
      jiraPanel.getTable().addJiraEditorListener(new MyJiraCellEditorListenerForPanel(boardModel));
   }

   public void wireUpListeners(BoardTableModel boardModel, JiraTableModel jiraModel) {
      setBoardDataListeners(boardModel);
      setJiraDataListener(jiraModel, boardModel);

      editableCheckBox.addActionListener(new EditableListener());
   }

   private final class MyBoardSelectionListener implements ListSelectionListener {
      private final MyTable boardTable;
      private final MyTable jiraTable;

      public MyBoardSelectionListener(MyTable boardTable, MyTable jiraTable) {
         this.boardTable = boardTable;
         this.jiraTable = jiraTable;
      }

      @Override
      public void valueChanged(ListSelectionEvent e) {
         ListSelectionModel lsm = (ListSelectionModel) e.getSource();
         if (e.getValueIsAdjusting()) {
            return;
         }
         jiraTable.clearSelection();
         if (lsm.isSelectionEmpty()) {
         } else {
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
//            int oldJiraRow = jiraTable.getRowCount();
            for (int i = minIndex; i <= maxIndex; i++) {
               if (lsm.isSelectedIndex(i)) {
                  String jira = (String) boardTable.getValueAt(Column.Jira, i);
                  int jiraRow = jiraTable.getRowWithJira(jira);
                  if (jiraRow != -1) {
                     jiraTable.addRowSelectionInterval(jiraRow, jiraRow);
//                     if (jiraRow < oldJiraRow) {
                        jiraTable.scrollRectToVisible(jiraTable.getCellRect(jiraRow, 0, true));
                     // oldJiraRow = jiraRow;
                     // }
                  }
                  log.debug("Row " + i + " is selected. It is jira " + jira + " should select " + jiraRow + " in jira!");
               }
            }
         }

      }
   }

   private final class EditableListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         boardPanel.setEditable(editableCheckBox.isSelected());
         sprintPanel.setEditable(editableCheckBox.isSelected());
         jiraPanel.setEditable(editableCheckBox.isSelected());
      }
   }

   private final class MyCheckboxCellEditorListener implements CellEditorListener {
      public void editingCanceled(ChangeEvent e) {
      }

      public void editingStopped(ChangeEvent e) {
         CheckBoxTableCellEditor editor = (CheckBoxTableCellEditor) e.getSource();
         MyTable table = boardPanel.getTable();
         MyTableModel model = (MyTableModel) table.getModel();
         model.fireTableCellUpdatedExceptThisOne(table.convertRowIndexToModel(editor.getRowEdited()), table.convertColumnIndexToModel(editor.getColEdited()));
      }
   }

   private final class MyJiraCellEditorListener implements CellEditorListener {
      public void editingCanceled(ChangeEvent e) {
      }

      public void editingStopped(ChangeEvent e) {
         JiraCellEditor editor = (JiraCellEditor) e.getSource();
         log.debug("col edited: " + editor.getColEdited() + " row edited: " + editor.getRowEdited() + " which has new value \"" + editor.getValue()
               + "\" and old value: \"" + editor.getOldValue() + "\"");
         MyTable jiraTable = jiraPanel.getTable();
         jiraTable.setValueAt(BoardStatusValue.NA, (String) editor.getOldValue(), Column.B_BoardStatus);
         jiraTable.setValueAt("", (String) editor.getOldValue(), Column.B_Release);
      }
   }

   private final class MyJiraCellEditorListenerForPanel implements CellEditorListener {
      private final BoardTableModel boardModel;

      private MyJiraCellEditorListenerForPanel(BoardTableModel boardModel) {
         this.boardModel = boardModel;
      }

      public void editingCanceled(ChangeEvent e) {
      }

      public void editingStopped(ChangeEvent e) {
         JiraCellEditor editor = (JiraCellEditor) e.getSource();
         log.debug("col edited: " + editor.getColEdited() + " row edited: " + editor.getRowEdited() + " which has new value \"" + editor.getValue()
               + "\" and old value: \"" + editor.getOldValue() + "\"");
         MyTable jiraTable = jiraPanel.getTable();

         String jira = (String) editor.getValue();

         BoardStatusValue status = boardModel.getStatus(jira);
         String release = boardModel.getRelease(jira);

         jiraTable.setValueAt(status, (String) editor.getOldValue(), Column.B_BoardStatus);
         jiraTable.setValueAt(release, (String) editor.getOldValue(), Column.B_Release);
      }
   }

   private final class MyTableListener implements TableListener {
      @Override
      public void jiraRemoved(String jira) {
         MyTable jiraTable = jiraPanel.getTable();
         jiraTable.setValueAt(BoardStatusValue.NA, jira, Column.B_BoardStatus);
         jiraTable.setValueAt("", jira, Column.B_Release);
      }
   }

   private final class MyTableModelListener implements TableModelListener {
      @Override
      public void tableChanged(TableModelEvent e) {
         MyStatusBar.getInstance().setMessage("Board Data Changed!", true);
      }
   }
}
