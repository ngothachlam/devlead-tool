package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.gui.component.InnerFrameToolbar;
import com.jonas.agile.devleadtool.gui.component.menu.MyTablePopupMenu;
import com.jonas.agile.devleadtool.gui.component.menu.SprintTreePopupMenu;
import com.jonas.agile.devleadtool.gui.component.table.Column;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.editor.MyEditor;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.gui.component.tree.SprintTree;
import com.jonas.agile.devleadtool.gui.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.gui.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.gui.component.tree.xml.JiraSaxHandler;
import com.jonas.agile.devleadtool.gui.component.tree.xml.XmlParser;
import com.jonas.agile.devleadtool.gui.component.tree.xml.XmlParserImpl;
import com.jonas.agile.devleadtool.gui.listener.JiraParseListenerImpl;
import com.jonas.agile.devleadtool.gui.listener.KeyListenerToHighlightSprintSelectionElsewhere;
import com.jonas.agile.devleadtool.gui.listener.TableListener;
import com.jonas.agile.devleadtool.gui.listener.TableModelListenerAlerter;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyComponentPanel;

public class MyInternalFrameInnerPanel extends MyComponentPanel {

   private static final int MAX_RESULT = 400;

   private Logger log = MyLogger.getLogger(MyInternalFrameInnerPanel.class);

   private BoardPanel boardPanel;
   private JiraPanel jiraPanel;
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

      addNorth(new InnerFrameToolbar(helper.getParentFrame(), boardPanel, jiraPanel, sprintPanel, boardTable, jiraTable));
      addCenter(combineIntoSplitPane(boardPanel, jiraMainPanel, sprintPanel));
   }

   private JPanel getJiraPanelButtonRow() {
      JPanel jiraButtonPanel = new JPanel(new GridLayout(1, 1, 3, 3));
      HighlightIssuesAction highlightAction = new HighlightIssuesAction("Higlight Issues", jiraPanel.getTable().getMyModel(), jiraPanel
            .getTable());
      jiraButtonPanel.add(new JCheckBox(highlightAction));
      return jiraButtonPanel;
   }

   private void setBoardDataListeners(final MyTableModel boardModel, final MyTable boardTable, final MyTable jiraTable, SprintTree sprintTree) {
      boardTable.addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(sprintTree, boardTable, jiraTable));
      boardTable.addListener(new MyBoardTableListener());
//      boardTable.addCheckBoxEditorListener(new MyBoardTableCheckboxEditorListener());
      boardModel.addTableModelListener(new FireUpdateOnOtherTableWhenUpdatedListener(jiraTable));
   }

   private void setSprintDataListener(final SprintTree sprintTree, final MyTable boardTable, final MyTable jiraTable) {
      sprintTree.addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(sprintTree, boardTable, jiraTable));
   }

   private void setJiraDataListener(MyTableModel jiraModel, final MyTableModel boardModel, SprintTree sprintTree, MyTable boardTable) {
      jiraPanel.getTable().addKeyListener(new KeyListenerToHighlightSprintSelectionElsewhere(sprintTree, jiraPanel.getTable(), boardTable));
      jiraModel.addTableModelListener(new FireUpdateOnOtherTableWhenUpdatedListener(boardTable));
   }

   public void wireUpListeners(MyTableModel boardModel, MyTableModel jiraModel, SprintTree tree) {
      MyTable boardTable = boardPanel.getTable();
      MyTable jiraTable = jiraPanel.getTable();
      SprintTree sprintTree = sprintPanel.getTree();

      setBoardDataListeners(boardModel, boardTable, jiraTable, sprintTree);
      setJiraDataListener(jiraModel, boardModel, sprintTree, boardTable);
      setSprintDataListener(sprintTree, boardTable, jiraTable);

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

         jiraModel.setRenderColors(button.isSelected());
         jiraModel.fireTableDataChanged();
         log.debug("Setting rendering colors to " + button.isSelected());

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

   private final class MyBoardTableCheckboxEditorListener implements CellEditorListener {
      public void editingCanceled(ChangeEvent e) {
      }

      public void editingStopped(ChangeEvent e) {
         MyEditor editor = (MyEditor) e.getSource();
         MyTable table = boardPanel.getTable();
         MyTableModel model = table.getMyModel();
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
