package com.jonas.agile.devleadtool.gui.component.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.table.MyTable;
import com.jonas.agile.devleadtool.gui.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.gui.component.table.model.MyTableModel;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyComponentPanel;

public class BoardPanel extends MyComponentPanel {

   private Logger log = MyLogger.getLogger(BoardPanel.class);
   public MyTable table;

   public BoardPanel() {
      this(new BoardTableModel());
   }

   public BoardPanel(MyTableModel boardModel) {
      super(new BorderLayout());
      makeContent(boardModel);
      initialiseTableHeader();
   }

   public MyTableModel getModel() {
      return table.getMyModel();
   }

   public MyTable getTable() {
      return table;
   }

   private void initialiseTableHeader() {
      JTableHeader header = table.getTableHeader();
      header.setReorderingAllowed(true);
   }

   protected void makeContent(MyTableModel boardTableModel) {
      table = new MyTable("Board", boardTableModel, true);
      JScrollPane scrollPane = new JScrollPane(table);
      setBorder(BorderFactory.createTitledBorder("Board"));
      addCenter(scrollPane);
   }

   public void setEditable(boolean selected) {
      table.getMyModel().setEditable(selected);
   }
}
