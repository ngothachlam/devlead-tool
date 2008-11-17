package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

public class JiraPanel extends MyComponentPanel {

   private final PlannerHelper helper;
   private Logger log = MyLogger.getLogger(JiraPanel.class);
   private MyTable table;

   public JiraPanel(PlannerHelper helper) {
      this(helper, new JiraTableModel());
   }

   public JiraPanel(PlannerHelper helper, MyTableModel jiraModel) {
      super(new BorderLayout());
      this.helper = helper;

      table = new MyTable("Jira", jiraModel, false);

      JScrollPane scrollpane = new MyScrollPane(table);

      addCenter(scrollpane);
      setBorder(BorderFactory.createTitledBorder("Jira"));
   }

   public MyTableModel getJiraModel() {
      return ((MyTableModel) table.getModel());
   }

   public MyTable getTable() {
      return table;
   }

   public void setEditable(boolean selected) {
      ((MyTableModel) table.getModel()).setEditable(selected);
   }

}
