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
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.component.listener.BoardAndJiraSyncListener;
import com.jonas.agile.devleadtool.component.listener.MyTableListener;
import com.jonas.agile.devleadtool.component.table.BoardStatusValue;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.editor.JiraCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

public class InternalTabPanel extends MyComponentPanel {

   Logger log = MyLogger.getLogger(InternalTabPanel.class);

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

      boardPanel = new BoardPanel(boardModel);
      planPanel = new PlanPanel(helper, planModel);
      jiraPanel = new JiraPanel(helper, jiraModel);

      setBoardDataListeners(boardModel);
      setJiraDataListener(jiraModel, boardModel);

      // FIXME I want to put this into the MyTable instead!! - need to register the other tables with each other!
      MyTable boardTable = boardPanel.getTable();
      popups.add(new MyTablePopupMenu(boardTable, helper, boardTable, planPanel.getTable(), jiraPanel.getTable()));
      popups.add(new MyTablePopupMenu(planPanel.getTable(), helper, boardTable, planPanel.getTable(), jiraPanel.getTable()));
      popups.add(new MyTablePopupMenu(jiraPanel.getTable(), helper, boardTable, planPanel.getTable(), jiraPanel.getTable()));

      JPanel panel = new JPanel();
      editableCheckBox = new JCheckBox("Editable?", true);
      panel.add(editableCheckBox);
      panel.add(getAddPanel(helper, boardTable, jiraPanel.getTable(), planPanel.getTable()));
      addNorth(panel);

      makeContent(boardModel);
      wireUpListeners();
      this.setBorder(BorderFactory.createEmptyBorder(0, 2, 1, 0));
   }

   private void setJiraDataListener(JiraTableModel jiraModel, final BoardTableModel boardModel) {
      // add listener when added or updated rows from addDialog;
      jiraModel.addTableModelListener(new BoardAndJiraSyncListener(boardPanel.getTable(), jiraPanel.getTable(), boardModel));
      // add listener when updating jira;
      jiraPanel.getTable().addJiraEditorListener(new CellEditorListener() {
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
      });
   }

   private void setBoardDataListeners(final BoardTableModel boardModel) {
      // add listener when added or updated rows from addDialog;
      boardModel.addTableModelListener(new BoardAndJiraSyncListener(boardPanel.getTable(), jiraPanel.getTable(), boardModel));
      // add listener when removed from popup menu;
      MyTableListener myTableListener = new MyTableListener() {
         @Override
         public void jiraRemoved(String jira) {
            MyTable jiraTable = jiraPanel.getTable();
            jiraTable.setValueAt(BoardStatusValue.NA, jira, Column.B_BoardStatus);
            jiraTable.setValueAt("", jira, Column.B_Release);
         }

      };
      boardModel.addTableModelListener(new TableModelListener(){
         @Override
         public void tableChanged(TableModelEvent e) {
            MyStatusBar.getInstance().setMessage("Board Data Changed!");
         }
      });
      boardPanel.getTable().addListener(myTableListener);
      // add listener when updating jira;
      boardPanel.getTable().addJiraEditorListener(new CellEditorListener() {
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
      });
      // add listener to update all colors when checkbox is ticked.
      boardPanel.getTable().addCheckBoxEditorListener(new CellEditorListener() {
         public void editingCanceled(ChangeEvent e) {
         }

         public void editingStopped(ChangeEvent e) {
            CheckBoxTableCellEditor editor = (CheckBoxTableCellEditor) e.getSource();
            MyTable table = boardPanel.getTable();
            MyTableModel model = (MyTableModel) table.getModel();
            model.fireTableCellUpdatedExceptThisOne(table.convertRowIndexToModel(editor.getRowEdited()), table.convertColumnIndexToModel(editor.getColEdited()));
         }
      });
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
