package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;

public class JiraPanel extends MyDataPanel {

   private Logger log = MyLogger.getLogger(JiraPanel.class);

   public JiraPanel(MyTableModel jiraModel) {
      super(new BorderLayout());

      table = new MyTable("Jira", jiraModel, false);
      JScrollPane scrollpane = new JScrollPane(table);
      setBorder(BorderFactory.createTitledBorder("Jira"));
      addCenter(scrollpane);

      addSouth(getFilterPanel());
   }

}
