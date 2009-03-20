package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AddBoardReconcileDialog;
import com.jonas.agile.devleadtool.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.listener.JiraParseListenerImpl;
import com.jonas.agile.devleadtool.component.listener.TableListener;
import com.jonas.agile.devleadtool.component.listener.TableModelListenerAlerter;
import com.jonas.agile.devleadtool.component.menu.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.menu.SprintTreePopupMenu;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.MyEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.tree.SprintTree;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.component.tree.xml.JiraSaxHandler;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParser;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParserImpl;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

public class MyInternalFrameInnerPanel extends MyComponentPanel {

   private static final int MAX_RESULT = 400;

   private Logger log = MyLogger.getLogger(MyInternalFrameInnerPanel.class);

   private BoardPanel boardPanel;
   private JiraPanel jiraPanel;
   private JCheckBox editableCheckBox;
   private DnDTreePanel sprintPanel;

   private MyTable boardTable;
   private MyTable jiraTable;

   private JiraParseListenerImpl jiraParseListener;

   public MyInternalFrameInnerPanel(PlannerHelper client) throws SAXException {
      this(client, null, null);
   }

   public MyInternalFrameInnerPanel(PlannerHelper helper, BoardTableModel boardModel, JiraTableModel jiraModel) throws SAXException {
      super(new BorderLayout());
      boardModel = (boardModel == null) ? new BoardTableModel() : boardModel;
      jiraModel = (jiraModel == null) ? new JiraTableModel() : jiraModel;

      jiraModel.setBoardModel(boardModel);

      DnDTreeModel model = new DnDTreeModel("LLU");
      SprintTree tree = new SprintTree(model);

      DnDTreeBuilder dnDTreeBuilder = createDnDTreeBuilder(helper.getParentFrame());
      makeContent(boardModel, tree, helper, jiraModel, dnDTreeBuilder);

      setBorder(BorderFactory.createEmptyBorder(0, 2, 1, 0));
      wireUpListeners(boardModel, jiraModel, tree);
   }

   private DnDTreeBuilder createDnDTreeBuilder(JFrame parentFrame) throws SAXException {
      JiraSaxHandler saxHandler = new JiraSaxHandler();
      jiraParseListener = new JiraParseListenerImpl(MAX_RESULT, parentFrame);
      saxHandler.addJiraParseListener(jiraParseListener);

      XmlParser parser = new XmlParserImpl(saxHandler, MAX_RESULT);
      return new DnDTreeBuilder(parser);
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
      tabPane.setResizeWeight(0.5f);
      tabPane2.setResizeWeight(0.5f);
      return tabPane;
   }

   private Component getAddPanel(final PlannerHelper helper, final MyTable boardTable, final MyTable jiraTable) {
      JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
      final List<MyTable> tables = new ArrayList<MyTable>();
      tables.add(boardTable);
      tables.add(jiraTable);

      final MyTable[] array = tables.toArray(new MyTable[tables.size()]);
      addButton(panel, "Reconcile", new BasicActionListener(helper.getParentFrame()) {
         @Override
         public void doActionPerformed(ActionEvent e) {
            new AddBoardReconcileDialog(helper.getParentFrame(), jiraTable);
         }
      });
      addButton(panel, "Add", new BasicActionListener(helper.getParentFrame()) {
         @Override
         public void doActionPerformed(ActionEvent e) {
            new AddManualDialog(helper.getParentFrame(), array);
         }
      });
      addButton(panel, "Filters", new BasicActionListener(helper.getParentFrame()) {
         @Override
         public void doActionPerformed(ActionEvent e) {
            new AddFilterDialog(helper.getParentFrame(), array);
         }
      });
      addButton(panel, "Versions", new BasicActionListener(helper.getParentFrame()) {
         @Override
         public void doActionPerformed(ActionEvent e) {
            new AddVersionDialog(helper.getParentFrame(), array);
         }
      });
      JButton addButton = addButton(panel, new BasicAbstractAction("Duplicates?", "Higlight Duplicates in the Board Panel", helper.getParentFrame()){
         @Override
         public void doActionPerformed(ActionEvent e) {
            Set<String> duplicateJiras = findAnyDuplicateJiras(boardTable);
            presentTheDuplicateJiras(helper, duplicateJiras);
         }

         private Set<String> findAnyDuplicateJiras(final MyTable boardTable) {
            int rows = boardTable.getRowCount();
            Set<String> duplicateJiras = new HashSet<String>(); 
            for (int row = 0; row < rows; row++) {
               String jira = (String) boardTable.getValueAt(Column.Jira, row);
               for (int compareRow = row+1; compareRow < rows; compareRow++) {
                  String compareJira = (String) boardTable.getValueAt(Column.Jira, compareRow);
                  if(jira.equalsIgnoreCase(compareJira)){
                     duplicateJiras.add(jira);
                  }
               }
            }
            return duplicateJiras;
         }

         private void presentTheDuplicateJiras(final PlannerHelper helper, Set<String> duplicateJiras) {
            StringBuffer sb = new StringBuffer();
            for (String string : duplicateJiras) {
               sb.append(string).append(" ");
            }
            
            AlertDialog.alertMessage(helper.getParentFrame(), sb.toString());
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

   public void makeContent(MyTableModel boardModel, SprintTree tree, PlannerHelper helper, MyTableModel jiraModel, DnDTreeBuilder dndTreeBuilder) {
      boardPanel = new BoardPanel(boardModel);
      sprintPanel = new DnDTreePanel(tree, helper.getParentFrame());
      jiraPanel = new JiraPanel(jiraModel);
      JPanel jiraButtonPanel = getJiraPanelButtonRow();

      JPanel jiraMainPanel = new JPanel(new BorderLayout());
      jiraMainPanel.add(jiraPanel, BorderLayout.CENTER);
      jiraMainPanel.add(jiraButtonPanel, BorderLayout.SOUTH);

      boardTable = boardPanel.getTable();
      jiraTable = jiraPanel.getTable();

      TableModelListenerAlerter listener = new TableModelListenerAlerter();
      boardTable.setTableModelListenerAlerter(listener);
      jiraTable.setTableModelListenerAlerter(listener);

      new MyTablePopupMenu(boardTable, helper, boardTable, jiraTable);
      new MyTablePopupMenu(jiraTable, helper, boardTable, jiraTable);
      new SprintTreePopupMenu(helper.getParentFrame(), tree, dndTreeBuilder, jiraTable, boardTable);

      addNorth(createAndGetTopPanel(helper));
      addCenter(combineIntoSplitPane(boardPanel, jiraMainPanel, sprintPanel));
   }

   private JPanel getJiraPanelButtonRow() {
      JPanel jiraButtonPanel = new JPanel(new GridLayout(1, 1, 3, 3));
      HighlightIssuesAction highlightAction = new HighlightIssuesAction("Higlight Issues", (MyTableModel) jiraPanel.getTable().getModel(), jiraPanel.getTable());
      jiraButtonPanel.add(new JCheckBox(highlightAction));
      return jiraButtonPanel;
   }

   private JPanel createAndGetTopPanel(PlannerHelper helper) {
      JPanel topPanel = new JPanel();
      editableCheckBox = new JCheckBox("Editable?", true);
      topPanel.add(editableCheckBox);
      topPanel.add(getAddPanel(helper, boardTable, jiraTable));
      return topPanel;
   }

   private void setBoardDataListeners(final BoardTableModel boardModel, final MyTable boardTable, final MyTable jiraTable, SprintTree sprintTree) {
      boardTable.addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(sprintTree, boardTable, jiraTable));
      boardTable.addListener(new MyBoardTableListener());
      boardTable.addCheckBoxEditorListener(new MyBoardTableCheckboxEditorListener());
      boardModel.addTableModelListener(new FireUpdateOnOtherTableWhenUpdatedListener(jiraTable));
   }

   private void setSprintDataListener(final SprintTree sprintTree, final MyTable boardTable, final MyTable jiraTable) {
      sprintTree.addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(sprintTree, boardTable, jiraTable));
   }

   private void setJiraDataListener(MyTableModel jiraModel, final BoardTableModel boardModel, SprintTree sprintTree, MyTable boardTable) {
      jiraPanel.getTable().addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(sprintTree, jiraPanel.getTable(), boardTable));
      jiraModel.addTableModelListener(new FireUpdateOnOtherTableWhenUpdatedListener(boardTable));
   }

   public void wireUpListeners(BoardTableModel boardModel, MyTableModel jiraModel, SprintTree tree) {
      MyTable boardTable = boardPanel.getTable();
      MyTable jiraTable = jiraPanel.getTable();
      SprintTree sprintTree = sprintPanel.getTree();

      setBoardDataListeners(boardModel, boardTable, jiraTable, sprintTree);
      setJiraDataListener(jiraModel, boardModel, sprintTree, boardTable);
      setSprintDataListener(sprintTree, boardTable, jiraTable);

      editableCheckBox.addActionListener(new EditableListener());

      jiraParseListener.setTree(tree);
   }

   private final class FireUpdateOnOtherTableWhenUpdatedListener implements TableModelListener {
      private final MyTable targetTable;

      private FireUpdateOnOtherTableWhenUpdatedListener(MyTable targetTable) {
         this.targetTable = targetTable;
      }

      public void tableChanged(final TableModelEvent e) {
         log.debug("source: " + e.getSource() + " target: " + targetTable);
         if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != TableModelEvent.ALL_COLUMNS) {
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
                     final MyTableModel sourceAsModel = (MyTableModel) e.getSource();
                     String jira = (String) sourceAsModel.getValueAt(Column.Jira, row);
                     log.debug("updated jira: " + jira);
                     targetTable.fireTableDataChangedForJira(jira);
                  }
               }
            });
         }
      }
   }

   private final class HighlightIssuesAction extends AbstractAction {
      private final MyTableModel jiraModel;
      private final MyTable jiraTable;

      private HighlightIssuesAction(String name, MyTableModel jiramodel, MyTable jiraTable) {
         super(name);
         this.jiraModel = jiramodel;
         this.jiraTable = jiraTable;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         JCheckBox button = (JCheckBox) e.getSource();

         int[] selectedRows = saveSelection();

         if (button.isSelected()) {
            jiraModel.setRenderColors(true);
            jiraModel.fireTableDataChanged();
            log.debug("Setting rendering colors to TRUE");
         } else {
            jiraModel.setRenderColors(false);
            jiraModel.fireTableDataChanged();
            log.debug("Setting rendering colors to FALSE");
         }

         resetSelection(selectedRows);
      }

      private void resetSelection(int[] selectedRows) {
         for (int i = 0; i < selectedRows.length; i++) {
            jiraTable.setRowSelectionInterval(i, i);
         }
      }

      private int[] saveSelection() {
         int[] selectedRows = jiraTable.getSelectedRows();
         return selectedRows;
      }
   }

   private final class KeyListenerToHighlightSprintSelectionElsewhere extends KeyAdapter {
      private final SprintTree sprintTree;
      private final MyTable boardTable;
      private final MyTable jiraTable;
      private boolean pressed = false;
      private ListSelectionModel lsm;

      private KeyListenerToHighlightSprintSelectionElsewhere(SprintTree sprintTree, MyTable boardTable, MyTable jiraTable) {
         this.sprintTree = sprintTree;
         this.boardTable = boardTable;
         this.jiraTable = jiraTable;
         lsm = boardTable.getSelectionModel();
      }

      @Override
      public void keyPressed(KeyEvent e) {
         // ctrl-f for finding in the other tables!
         if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_F && !pressed) {
            pressed = true;
            ListSelectionModel boardSelectionModel = boardTable.getSelectionModel();
            ListSelectionModel jiraSelectionModel = jiraTable.getSelectionModel();
            boardSelectionModel.setValueIsAdjusting(true);
            jiraSelectionModel.setValueIsAdjusting(true);
            log.debug("KeyPressed Source: " + e.getSource());
            jiraTable.clearSelection();
            if (e.getSource() instanceof SprintTree)
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

   private final class MyBoardTableCheckboxEditorListener implements CellEditorListener {
      public void editingCanceled(ChangeEvent e) {
      }

      public void editingStopped(ChangeEvent e) {
         MyEditor editor = (MyEditor) e.getSource();
         MyTable table = boardPanel.getTable();
         MyTableModel model = (MyTableModel) table.getModel();
         model.fireTableCellUpdatedExceptThisOne(table.convertRowIndexToModel(editor.getRowEdited()), table.convertColumnIndexToModel(editor
               .getColEdited()));
      }
   }

   private final class MyBoardTableListener implements TableListener {
      @Override
      public void jiraRemoved(String jira) {
         MyTable jiraTable = jiraPanel.getTable();
         jiraTable.fireTableDataChangedForJira(jira);
      }
   }
}


abstract class BasicActionListener implements ActionListener {

   private Frame parentFrame;

   public BasicActionListener(Frame parentFrame) {
      super();
      this.parentFrame = parentFrame;
   }

   @Override
   public final void actionPerformed(ActionEvent e) {
      try {
         doActionPerformed(e);
      } catch (Throwable ex) {
         AlertDialog.alertException(parentFrame, ex);
      }
   }

   public abstract void doActionPerformed(ActionEvent e);

}

abstract class BasicAbstractAction extends AbstractAction{
   
   private Frame parentFrame;

   public BasicAbstractAction(String name, String description, Frame parentFrame){
      super(name);
      putValue(Action.SHORT_DESCRIPTION, name);
      this.parentFrame = parentFrame;
   }
   
   @Override 
   public final void actionPerformed(ActionEvent e) {
      try {
         doActionPerformed(e);
      } catch (Throwable ex) {
         AlertDialog.alertException(parentFrame, ex);
      }
   }

   public abstract void doActionPerformed(ActionEvent e);
   
   
}