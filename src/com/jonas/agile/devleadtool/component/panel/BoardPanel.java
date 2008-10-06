package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.JTableHeader;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;

public class BoardPanel extends MyComponentPanel {

   private PlannerHelper helper;
   private Logger log = MyLogger.getLogger(BoardPanel.class);
   public MyTable table;

   BoardPanel() {
      super(null);
   }

   public BoardPanel(PlannerHelper client) {
      this(client, new BoardTableModel());
   }

   public BoardPanel(PlannerHelper helper, MyTableModel boardModel) {
      super(new BorderLayout());
      this.helper = helper;
      makeContent(boardModel);
      initialiseTableHeader();
   }

   private JPanel getButtonPanelNorth() {
      JPanel buttonPanel = new JPanel();
      addFilter(buttonPanel, table, Column.Jira, Column.Description);
      return buttonPanel;
   }

   public MyTableModel getModel() {
      return ((MyTableModel) table.getModel());
   }

   public MyTable getTable() {
      return table;
   }

   private void initialiseTableHeader() {
      JTableHeader header = table.getTableHeader();
      header.setReorderingAllowed(true);
   }

   protected void makeContent(MyTableModel boardTableModel) {
      table = new MyTable(boardTableModel);

      table.setDefaultRenderer(String.class, new StringTableCellRenderer());
      table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());
      table.setDefaultEditor(Boolean.class, new CheckBoxTableCellEditor(new JCheckBox()));

      JScrollPane scrollPane = new MyScrollPane(table);

      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
      addCenter(scrollPane);
   }

   public void setEditable(boolean selected) {
      ((MyTableModel) table.getModel()).setEditable(selected);
   }
}
