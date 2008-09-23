package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.listener.CopyToTableListener;
import com.jonas.agile.devleadtool.component.listener.DestinationRetriever;
import com.jonas.agile.devleadtool.component.listener.TabCheckButtonActionListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.HyperlinkTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

public class BoardPanel extends MyComponentPanel {

   final PlannerHelper helper;

   private Logger log = MyLogger.getLogger(BoardPanel.class);
   public MyTable table;

   public BoardPanel(PlannerHelper client) {
      this(client, new BoardTableModel());
   }

   public BoardPanel(PlannerHelper helper, MyTableModel boardModel) {
      super(new BorderLayout());
      this.helper = helper;
      makeContent(boardModel);
      setButtons();
      initialiseTableHeader();
   }

   public MyTableModel getModel() {
      return ((MyTableModel) table.getModel());
   }

   private void initialiseTableHeader() {
      JTableHeader header = table.getTableHeader();
      header.setReorderingAllowed(true);
   }

   protected void makeContent(MyTableModel boardTableModel) {
      table = new MyTable();
      table.setModel(boardTableModel);

      JScrollPane scrollPane = new MyScrollPane(table);

      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
      addCenter(scrollPane);
   }

   protected void setButtons() {
      JPanel buttonPanel = new JPanel();

      addPanelWithAddAndRemoveOptions(table, buttonPanel);
      addButton(buttonPanel, "Open Jiras", new OpenJirasListener(table, helper));
      addButton(buttonPanel, "Copy to Plan", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getPlanTable();
         }
      }, helper));
      addButton(buttonPanel, "Copy to Jira", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getJiraTable();
         }
      }, helper));
      addButton(buttonPanel, "TabCheck", new TabCheckButtonActionListener(table, helper.getPlannerCommunicator()));

      addSouth(buttonPanel);
   }

   public void setEditable(boolean selected) {
      ((MyTableModel) table.getModel()).setEditable(selected);
   }

   public MyTable getTable() {
      return table;
   }
}
