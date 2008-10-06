package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;

public class PlanPanel extends MyComponentPanel {

   private final PlannerHelper helper;
   private Logger log = MyLogger.getLogger(PlanPanel.class);
   private JFrame planVersionsFrame;
   private MyTable table;

   public PlanPanel(PlannerHelper client) {
      this(client, new PlanTableModel());
   }

   public PlanPanel(PlannerHelper helper, PlanTableModel planModel) {
      super(new BorderLayout());
      this.helper = helper;
      table = new MyTable(planModel);

      JScrollPane scrollpane = new JScrollPane(table);

      // table.addMouseListener(new HyperLinkOpenerAdapter(helper, ColumnDataType.URL, ColumnDataType.Jira));
      this.addCenter(scrollpane);
      this.addSouth(getBottomPanel());
      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
   }

   public boolean doesJiraExist(String jira) {
      return ((PlanTableModel) table.getModel()).doesJiraExist(jira);
   }

   protected JPanel getBottomPanel() {
      MyPanel buttonPanel = new MyPanel(new BorderLayout());
      // JPanel buttonPanelOne = getButtonPanelNorth();
      // buttonPanel.addNorth(buttonPanelOne);
      JPanel buttonPanelTwo = getButtonPanelSouth();
      buttonPanel.addSouth(buttonPanelTwo);
      return buttonPanel;
   }

   private JPanel getButtonPanelNorth() {
      JPanel buttonPanel = new JPanel();
      addFilter(buttonPanel, table, Column.Jira, Column.Description);
      return buttonPanel;
   }

   private JPanel getButtonPanelSouth() {
      JPanel buttons = new JPanel();

      setupPlanVersionsFrame();
      addButton(buttons, "PlanVersions", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            SwingUtil.centreWindowWithinWindow(planVersionsFrame, helper.getParentFrame());
            planVersionsFrame.setVisible(true);
         }
      });
      return buttons;
   }

   public PlanTableModel getPlanModel() {
      return ((PlanTableModel) table.getModel());
   }

   public MyTable getTable() {
      return table;
   }

   public void setEditable(boolean selected) {
      ((PlanTableModel) table.getModel()).setEditable(selected);
   }

   private void setupPlanVersionsFrame() {
      planVersionsFrame = new JFrame();
      JPanel contentPanel = new FixVersionsPanel();
      planVersionsFrame.setContentPane(contentPanel);
      planVersionsFrame.setSize(450, 210);
   }

}
