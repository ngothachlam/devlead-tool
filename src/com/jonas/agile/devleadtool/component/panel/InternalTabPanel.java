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
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.component.dialog.AddVersionDialog;
import com.jonas.agile.devleadtool.component.listener.BoardTableModelListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
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

      boardModel.addTableModelListener(new BoardTableModelListener(boardPanel.getTable(), jiraPanel.getTable(), boardModel));

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
