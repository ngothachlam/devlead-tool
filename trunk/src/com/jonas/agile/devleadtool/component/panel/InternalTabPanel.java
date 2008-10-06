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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyTablePopupMenu;
import com.jonas.agile.devleadtool.component.dialog.AddDialog;
import com.jonas.agile.devleadtool.component.dialog.AddFilterDialog;
import com.jonas.agile.devleadtool.component.dnd.TableAndTitleDTO;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.MyComponentPanel;

public class InternalTabPanel extends MyComponentPanel {

   private BoardPanel boardPanel;
   private PlanPanel planPanel;
   private JiraPanel jiraPanel;

   private JCheckBox checkBox;

   public InternalTabPanel(PlannerHelper client) {
      this(client, null, null, null);
   }

   public InternalTabPanel(PlannerHelper helper, MyTableModel boardModel, MyTableModel planModel, MyTableModel jiraModel) {
      super(new BorderLayout());
      boardModel = (boardModel == null) ? new BoardTableModel() : boardModel;
      planModel = (planModel == null) ? new PlanTableModel() : planModel;
      jiraModel = (jiraModel == null) ? new JiraTableModel() : jiraModel;

      boardPanel = new BoardPanel(boardModel);
      planPanel = new PlanPanel(helper, planModel);
      jiraPanel = new JiraPanel(helper, jiraModel);

      TableAndTitleDTO tableAndTitleDTO1 = new TableAndTitleDTO("Board", boardPanel.getTable());
      TableAndTitleDTO tableAndTitleDTO2 = new TableAndTitleDTO("Plan", planPanel.getTable());
      TableAndTitleDTO tableAndTitleDTO3 = new TableAndTitleDTO("Jira", jiraPanel.getTable());

      //FIXME I want to put this into the MyTable instead!! - need to register the other tables with each other!
      new MyTablePopupMenu(boardPanel.getTable(), helper, tableAndTitleDTO1, tableAndTitleDTO2, tableAndTitleDTO3);
      new MyTablePopupMenu(planPanel.getTable(), helper, tableAndTitleDTO1, tableAndTitleDTO2, tableAndTitleDTO3);
      new MyTablePopupMenu(jiraPanel.getTable(), helper, tableAndTitleDTO1, tableAndTitleDTO2, tableAndTitleDTO3);

      JPanel panel = new JPanel();
      checkBox = new JCheckBox("Editable?", true);
      panel.add(checkBox);
      panel.add(getAddPanel(helper.getParentFrame(), boardPanel.getTable(), jiraPanel.getTable(), planPanel.getTable()));
      addNorth(panel);

      makeContent(boardModel);
      wireUpListeners();
      this.setBorder(BorderFactory.createEmptyBorder(0, 2, 1, 0));
   }

   private Component getAddPanel(final JFrame frame, MyTable boardTable, MyTable jiraTable, MyTable planTable) {
      JPanel panel = new JPanel(new GridLayout(1,2,5,5));
      final List<TableAndTitleDTO> tables = new ArrayList<TableAndTitleDTO>();
      tables.add(new TableAndTitleDTO("Board", boardTable));
      tables.add(new TableAndTitleDTO("Jira", jiraTable));
      tables.add(new TableAndTitleDTO("Plan", planTable));

      final TableAndTitleDTO[] array = tables.toArray(new TableAndTitleDTO[tables.size()]);
      addButton(panel, "Add", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            new AddDialog(frame, array);
         }
      });
      addButton(panel, "Filters", new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            new AddFilterDialog(frame, array);
         }
      });

      return panel;
   }

   public void makeContent(MyTableModel boardTableModel) {
      addCenter( combineIntoSplitPane(boardPanel, jiraPanel, planPanel) );
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
      // senderPanel.addComponentListener(this);
      // receiverPanel.addComponentListener(this);
      checkBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boardPanel.setEditable(checkBox.isSelected());
            planPanel.setEditable(checkBox.isSelected());
            jiraPanel.setEditable(checkBox.isSelected());
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
