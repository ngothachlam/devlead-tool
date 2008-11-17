package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.model.PlanTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

public class PlanPanel extends MyComponentPanel {

   private final PlannerHelper helper;
   private Logger log = MyLogger.getLogger(PlanPanel.class);
   private MyTable table;

   public PlanPanel(PlannerHelper helper, MyTableModel planModel) {
      super(new BorderLayout());
      this.helper = helper;
      table = new MyTable("Plan", planModel, true);

      JScrollPane scrollpane = new JScrollPane(table);

      addCenter(scrollpane);
      setBorder(BorderFactory.createTitledBorder("Plan"));

   }

   public boolean doesJiraExist(String jira) {
      return ((PlanTableModel) table.getModel()).doesJiraExist(jira);
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
}
