package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.MyScrollPane;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyComponentPanel;

public class JiraPanel extends MyComponentPanel {

   private Logger log = MyLogger.getLogger(JiraPanel.class);
   private MyTable table;

   public JiraPanel(MyTableModel jiraModel) {
      super(new BorderLayout());

      table = new MyTable("Jira", jiraModel, false);

      JScrollPane scrollpane = new MyScrollPane(table);

      addCenter(scrollpane);
      setBorder(BorderFactory.createTitledBorder("Jira"));
   }

   private Component getButtonPanel() {
      JPanel panel = new JPanel(new GridLayout(1,1,5,5));
      panel.add(new JButton("Higlight Issues"));
      return panel;
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
