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
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.DnDTreePanel;
import com.jonas.agile.devleadtool.component.JiraParseListenerImpl;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.MyTreePopupMenu;
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
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.component.tree.xml.JiraSaxHandler;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParser;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParserImpl;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

public class MyInternalFrameInnerComponent extends MyComponentPanel {

   private static final int MAX_RESULT = 1000;

   private Logger log = MyLogger.getLogger(MyInternalFrameInnerComponent.class);

   private BoardPanel boardPanel;
   private JiraPanel jiraPanel;
   private JCheckBox editableCheckBox;
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
         saxHandler.addJiraParseListener(new JiraParseListenerImpl(tree, MAX_RESULT, helper.getParentFrame()));

         XmlParser parser = new XmlParserImpl(saxHandler, MAX_RESULT);
         // XmlParser parser = new XmlParserLargeMock(saxHandler);

         DnDTreeBuilder dndTreeBuilder = new DnDTreeBuilder(parser);
         log.trace("MyInternalFrameInnerComponent 1.5");
         makeContent(boardModel, tree, dndTreeBuilder, helper, jiraModel);
         log.trace("MyInternalFrameInnerComponent 1.6");
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

      MyTable boardTable = boardPanel.getTable();
      MyTable jiraTable = jiraPanel.getTable();
      
      new MyTablePopupMenu(boardTable, helper, boardTable, jiraTable);
      new MyTablePopupMenu(jiraTable, helper, boardTable, jiraTable);
      new MyTreePopupMenu(helper.getParentFrame(), tree, dndTreeBuilder, helper);

      JPanel panel = new JPanel();
      editableCheckBox = new JCheckBox("Editable?", true);
      panel.add(editableCheckBox);
      panel.add(getAddPanel(helper, boardTable, jiraTable));
      addNorth(panel);

      addCenter(combineIntoSplitPane(boardPanel, jiraPanel, sprintPanel));
   }

   private void setBoardDataListeners(final BoardTableModel boardModel, final MyTable boardTable, MyTable jiraTable, DnDTree sprintTree) {
      boardModel.addTableModelListener(new BoardAndJiraSyncListener(boardTable, jiraTable, boardModel));
      boardTable.addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(sprintTree, boardTable, jiraTable));
      boardTable.addListener(new MyTableListener());
      boardTable.addJiraEditorListener(new MyJiraCellEditorListener());
      boardTable.addCheckBoxEditorListener(new MyCheckboxCellEditorListener());
   }

   private void setSprintDataListener(final DnDTree sprintTree, final MyTable boardTable, final MyTable jiraTable) {
      sprintTree.addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(sprintTree, boardTable, jiraTable));
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
      setSprintDataListener(sprintTree, boardTable, jiraTable);

      editableCheckBox.addActionListener(new EditableListener());
   }

   private final class KeyListenerToHighlightSprintSelectionElsewhere extends KeyAdapter {
      private final DnDTree sprintTree;
      private final MyTable boardTable;
      private final MyTable jiraTable;
      private boolean pressed = false;
      private ListSelectionModel lsm;

      private KeyListenerToHighlightSprintSelectionElsewhere(DnDTree sprintTree, MyTable boardTable, MyTable jiraTable) {
         this.sprintTree = sprintTree;
         this.boardTable = boardTable;
         this.jiraTable = jiraTable;
         lsm = boardTable.getSelectionModel();
      }

      @Override
      public void keyPressed(KeyEvent e) {
         if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_F && !pressed) {
            pressed = true;
            ListSelectionModel boardSelectionModel = boardTable.getSelectionModel();
            ListSelectionModel jiraSelectionModel = jiraTable.getSelectionModel();
            boardSelectionModel.setValueIsAdjusting(true);
            jiraSelectionModel.setValueIsAdjusting(true);
            log.debug("KeyPressed Source: " + e.getSource());
            jiraTable.clearSelection();
            if (e.getSource() instanceof DnDTree)
               setSelectionToOthersIfSourceisTree();
            else if (e.getSource() instanceof MyTable)
               setSelectionToOthersIfSourceisTable();
            jiraTable.scrollToSelection();
            boardSelectionModel.setValueIsAdjusting(false);
            jiraSelectionModel.setValueIsAdjusting(false);
         }
      }

      private void setSelectionToOthersIfSourceisTable() {
         sprintTree.clearSelection();
         if (!lsm.isSelectionEmpty()) {
            for (int i = lsm.getMinSelectionIndex(); i <= lsm.getMaxSelectionIndex(); i++) {
               if (lsm.isSelectedIndex(i)) {
                  String jira = (String) boardTable.getValueAt(Column.Jira, i);
                  log.debug("Jira: " + jira);
                  jiraTable.addSelection(jira);
                  sprintTree.addSelection(jira);
               }
            }
            sprintTree.scrollToSelection();
         }
      }

      private void setSelectionToOthersIfSourceisTree() {
         boardTable.clearSelection();
         TreePath[] jiraTreePaths = sprintTree.getSelectionPaths();
         StringBuffer boardSb = new StringBuffer("");
         StringBuffer jiraSb = new StringBuffer("");
         for (int i = 0; i < jiraTreePaths.length; i++) {
            TreePath jiraTreePath = jiraTreePaths[i];
            Object lastPathComponent = jiraTreePath.getLastPathComponent();
            if (lastPathComponent instanceof JiraNode) {
               JiraNode jiraNode = (JiraNode) lastPathComponent;
               sprintTree.addSelection(jiraNode.getKey());
               if (!boardTable.addSelection(jiraNode.getKey()))
                  boardSb.append(jiraNode.getKey()).append(", ");
               if (!jiraTable.addSelection(jiraNode.getKey()))
                  jiraSb.append(jiraNode.getKey()).append(", ");
            }
         }
         if (jiraTreePaths.length == 1)
            sprintTree.setLeadSelectionPath(jiraTreePaths[0]);
         boardTable.scrollToSelection();
         sprintTree.scrollToSelection();
         boardSb.append(" not found on 'Board' and ").append(jiraSb).append(" not found on 'Jira'");
         MyStatusBar.getInstance().setMessage(boardSb.toString(), true);
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
