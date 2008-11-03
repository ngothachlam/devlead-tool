package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import com.jonas.agile.devleadtool.MyStatusBar;
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
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.component.tree.xml.JiraSaxHandler;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParser;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParserImpl;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

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

   private void setBoardDataListeners(final BoardTableModel boardModel, final MyTable boardTable, MyTable jiraTable, DnDTree sprintTree) {
      boardModel.addTableModelListener(new BoardAndJiraSyncListener(boardTable, jiraTable, boardModel));
      boardTable.getSelectionModel().addListSelectionListener(new MyBoardSelectionListener(boardTable, jiraTable, sprintTree));
      boardTable.addKeyListener(new KeyMaskValueAdjusterListener(boardTable.getSelectionModel()));
      boardTable.addListener(new MyTableListener());
      boardTable.addJiraEditorListener(new MyJiraCellEditorListener());
      boardTable.addCheckBoxEditorListener(new MyCheckboxCellEditorListener());
   }

   private void setSprintDataListener(final DnDTree sprintTree, final MyTable boardTable) {
      sprintTree.addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(boardTable, sprintTree));
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
      setSprintDataListener(sprintTree, boardTable);

      editableCheckBox.addActionListener(new EditableListener());
   }

   private final class KeyListenerToHighlightSprintSelectionElsewhere extends KeyAdapter {
      private final MyTable boardTable;
      private final DnDTree sprintTree;
      private boolean pressed = false;

      private KeyListenerToHighlightSprintSelectionElsewhere(MyTable boardTable, DnDTree sprintTree) {
         this.boardTable = boardTable;
         this.sprintTree = sprintTree;
      }

      @Override
      public void keyPressed(KeyEvent e) {
         if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
            if (e.getKeyCode() == KeyEvent.VK_F) {
               if (!pressed) {
                  boardTable.clearSelection();
                  pressed = true;
                  TreePath[] jiraTreePaths = sprintTree.getSelectionPaths();
                  ListSelectionModel selectionModel = boardTable.getSelectionModel();
                  selectionModel.setValueIsAdjusting(true);
                  for (int i = 0; i < jiraTreePaths.length; i++) {
                     TreePath jiraTreePath = jiraTreePaths[i];
                     Object lastPathComponent = jiraTreePath.getLastPathComponent();
                     if (lastPathComponent instanceof JiraNode) {
                        JiraNode jiraNode = (JiraNode) lastPathComponent;
                        int row = boardTable.getRowWithJira(jiraNode.getKey());
                        if (row > -1)
                           boardTable.addRowSelectionInterval(row, row);
                        else
                           MyStatusBar.getInstance().setMessage("Jira " + jiraNode.getKey() + " not found on the board", true);
                     }
                  }
                  selectionModel.setValueIsAdjusting(true);
                  scrollToSelection(boardTable);
               }
            }
         }
      }

      private void scrollToSelection(final MyTable table) {
         int[] selectedRows = table.getSelectedRows();
         if (selectedRows.length > 0)
            table.scrollRectToVisible(table.getCellRect(selectedRows[0], 0, true));
         if (selectedRows.length > 1)
            table.scrollRectToVisible(table.getCellRect(selectedRows[selectedRows.length - 1], 0, true));
      }

      @Override
      public void keyReleased(KeyEvent e) {
         if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK || e.getKeyCode() == KeyEvent.VK_F) {
            pressed = false;
         }
      }

      @Override
      public void keyTyped(KeyEvent e) {
      }
   }

   private final class KeyMaskValueAdjusterListener extends KeyAdapter {
      private ListSelectionModel selectionModel;

      private KeyMaskValueAdjusterListener(ListSelectionModel selectionModel) {
         this.selectionModel = selectionModel;
      }

      @Override
      public void keyPressed(KeyEvent e) {
         selectionModel.setValueIsAdjusting(true);
      }

      @Override
      public void keyReleased(KeyEvent e) {
         if (e.getModifiersEx() == 0) {
            selectionModel.setValueIsAdjusting(false);
         }
      }
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
                  selectInSprintTree(sprintTree, jira);
               }
            }
            scrollToSelection(jiraTable);
            scrollToSelection(sprintTree);
         }
      }

      private void scrollToSelection(final MyTable table) {
         int[] selectedRows = table.getSelectedRows();
         if (selectedRows.length > 0)
            table.scrollRectToVisible(table.getCellRect(selectedRows[0], 0, true));
         if (selectedRows.length > 1)
            table.scrollRectToVisible(table.getCellRect(selectedRows[selectedRows.length - 1], 0, true));
      }

      private void scrollToSelection(DnDTree tree) {
      //FIXME - merge into one abstract class for scrollToSelection... methods
      //USABILITY - add an inidcator that there is a selected row (both in tree and table) outside of the scrollpane
         TreePath[] paths = tree.getSelectionPaths();
         if (paths != null && paths.length > -1)
            sprintTree.scrollPathToVisible(paths[0]);
         if (paths != null && paths.length > 0)
            sprintTree.scrollPathToVisible(paths[paths.length - 1]);
      }

      private void selectInSprintTree(DnDTree sprintTree, String jira) {
         List<TreePath> jiraPathList = sprintTree.getJiraPath(jira);
         TreePath[] jiraTreePath = jiraPathList.toArray(new TreePath[jiraPathList.size()]);
         sprintTree.addSelectionPaths(jiraTreePath);
      }

      private void selectInJiraTable(String jira) {
         int jiraRow = jiraTable.getRowWithJira(jira);
         if (jiraRow != -1) {
            jiraTable.addRowSelectionInterval(jiraRow, jiraRow);
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
