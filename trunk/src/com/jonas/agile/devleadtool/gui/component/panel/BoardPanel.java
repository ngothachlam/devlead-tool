package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.sprint.SprintCache;
import com.jonas.common.logging.MyLogger;

public class BoardPanel extends MyDataPanel {

   private Logger log = MyLogger.getLogger(BoardPanel.class);
   public BoardPanel(SprintCache sprintCache) {
      this(new BoardTableModel(sprintCache));
   }

   public BoardPanel(MyTableModel boardModel) {
      super(new BorderLayout());
      
      table = new MyTable("Board", boardModel, true);
      JScrollPane scrollPane = new JScrollPane(table);
      setBorder(BorderFactory.createTitledBorder("Board"));
      addCenter(scrollPane);
      
      addSouth(getFilterPanel());
   }
}
