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
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DnDTreePanel;
import com.jonas.agile.devleadtool.component.JiraParseListenerImpl;
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
import com.jonas.agile.devleadtool.component.tree.model.DnDTree;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.model.DnDTreeModel;
import com.jonas.testing.tree.fromScratch.tree.xml.DnDTreeBuilder;
import com.jonas.testing.tree.fromScratch.tree.xml.JiraSaxHandler;
import com.jonas.testing.tree.fromScratch.tree.xml.XmlParser;
import com.jonas.testing.tree.fromScratch.tree.xml.XmlParserImpl;

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
//          XmlParser parser = new XmlParserLargeMock(saxHandler);
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
      MyTable jiraTable = jiraPanel.getTable();
      popups.add(new MyTablePopupMenu(boardTable, helper, boardTable, jiraTable));
      popups.add(new MyTablePopupMenu(jiraTable, helper, boardTable, jiraTable));

      JPanel panel = new JPanel();
      editableCheckBox = new JCheckBox("Editable?", true);
      panel.add(editableCheckBox);
      panel.add(getAddPanel(helper, boardTable, jiraTable));
      addNorth(panel);

      addCenter(combineIntoSplitPane(boardPanel, jiraPanel, sprintPanel));
   }

   private void setBoardDataListeners(final BoardTableModel boardModel, MyTable boardTable, MyTable jiraTable, DnDTree sprintTree) {
      boardModel.addTableModelListener(new BoardAndJiraSyncListener(boardTable, jiraTable, boardModel));
      boardTable.getSelectionModel().addListSelectionListener(new MyBoardSelectionListener(boardTable, jiraTable, sprintTree));
      boardTable.addListener(new MyTableListener());
      boardTable.addJiraEditorListener(new MyJiraCellEditorListener());
      boardTable.addCheckBoxEditorListener(new MyCheckboxCellEditorListener());
   }

   private void setJiraDataListener(JiraTableModel jiraModel, final BoardTableModel boardModel) {
      jiraModel.addTableModelListener(new BoardAndJiraSyncListener(boardPanel.getTable(), jiraPanel.getTable(), boardModel));
      jiraPanel.getTable().addJiraEditorListener(new MyJiraCellEditorListenerForPanel(boardModel));
   }

   public void wireUpListeners(BoardTableModel boardModel, JiraTableModel jiraModel) {
      MyTable boardTable = boardPanel.getTable();
      MyTable jiraTable = jiraPanel.getTable();
      DnDTree sprintTree = sprintPanel.getTree();
      
      setBoardDataListeners(boardModel, boardTable, jiraTable, sprintTree);
      setJiraDataListener(jiraModel, boardModel);

      editableCheckBox.addActionListener(new EditableListener());
   }

   private final class MyBoardSelectionListener implements ListSelectionListener {
      private final MyTable boardTable;
      private final MyTable jiraTable;
      private ListSelectionModel lsm;
      private final DnDTree sprintTree;

      public MyBoardSelectionListener(MyTable boardTable, MyTable jiraTable, DnDTree sprintTree) {
         this.boardTable = boardTable;
         this.jiraTable = jiraTable;
         this.sprintTree = sprintTree;
         lsm = boardTable.getSelectionModel();
      }

      @Override
      public void valueChanged(final ListSelectionEvent e) {
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               highlightInOtherPanels(e);
            }
         });
      }

      private void highlightInOtherPanels(ListSelectionEvent e) {
         if (e.getValueIsAdjusting()) {
            return;
         }
         jiraTable.clearSelection();
         sprintTree.clearSelection();
         if (!lsm.isSelectionEmpty()) {
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
               if (lsm.isSelectedIndex(i)) {
                  String jira = (String) boardTable.getValueAt(Column.Jira, i);
                  selectInJiraTable(jira);
                  selectInSprintTree(jira);
               }
            }
         }
      }

      private void selectInSprintTree(String jira) {
         List<TreePath> jiraPathList = sprintTree.getJiraPath(jira);
         TreePath[] jiraTreePath = jiraPathList.toArray(new TreePath[jiraPathList.size()]);
         sprintTree.addSelectionPaths(jiraTreePath);
         if (jiraTreePath.length > 0) {
            for (TreePath treePath : jiraTreePath) {
               sprintTree.scrollPathToVisible(treePath);
            }
         }
      }

      private void selectInJiraTable(String jira) {
         int jiraRow = jiraTable.getRowWithJira(jira);
         if (jiraRow != -1) {
            jiraTable.addRowSelectionInterval(jiraRow, jiraRow);
            jiraTable.scrollRectToVisible(jiraTable.getCellRect(jiraRow, 0, true));
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
}
