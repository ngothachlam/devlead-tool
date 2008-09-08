package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.table.JTableHeader;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.listener.AddNewRowActionListenerListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.editor.CheckBoxTableCellEditor;
import com.jonas.agile.devleadtool.component.table.model.BoardTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.component.table.renderer.CheckBoxTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.HyperlinkTableCellRenderer;
import com.jonas.agile.devleadtool.component.table.renderer.StringTableCellRenderer;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.JiraIssueNotFoundException;

public class BoardPanel extends MyComponentPanel {

   private final PlannerHelper helper;

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

      table.setDefaultRenderer(String.class, new StringTableCellRenderer());
      table.setDefaultRenderer(Boolean.class, new CheckBoxTableCellRenderer());
      table.setColumnRenderer(6, new HyperlinkTableCellRenderer());
      table.setDefaultEditor(Boolean.class, new CheckBoxTableCellEditor());

      table.setAutoCreateRowSorter(true);

      JScrollPane scrollPane = new MyScrollPane(table);

      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
      addCenter(scrollPane);
   }

   protected void setButtons() {
      JPanel buttonPanel = new JPanel();

      addPanelWithAddAndRemoveOptions(table, buttonPanel);
      addButton(buttonPanel, "Open Jiras", new OpenJirasListener(table, helper));
      addButton(buttonPanel, "Copy to Plan", new CopyToPlanListener(table, helper));

      addSouth(buttonPanel);
   }

   public void setEditable(boolean selected) {
      ((MyTableModel) table.getModel()).setEditable(selected);
   }

   private final class CopyToPlanListener implements ActionListener {
      private Logger log = MyLogger.getLogger(this.getClass());
      private final MyTable table2;
      private final PlannerHelper helper2;

      public CopyToPlanListener(MyTable table, PlannerHelper helper) {
         table2 = table;
         helper2 = helper;
      }

      public void actionPerformed(ActionEvent e) {

         final int[] selectedRows = table2.getSelectedRows();
         final ProgressDialog dialog = new ProgressDialog(helper2.getParentFrame(), "Copying...", "Copying selected messages from Board to Plan...",
               selectedRows.length);
         SwingWorker worker = new SwingWorker() {

            @Override
            protected Object doInBackground() {
               try {
                  for (int i = 0; i < selectedRows.length; i++) {
                     String valueAt = (String) table2.getValueAt(Column.Jira, selectedRows[i]);
                     try {
                        helper2.addToPlan(valueAt, false);
                     } catch (JiraIssueNotFoundException e) {
                        AlertDialog.alertException(helper2, e);
                        e.printStackTrace();
                     }
                     dialog.increseProgress();
                  }
               } catch (Exception e) {
                  AlertDialog.alertException(helper2, e);
                  e.printStackTrace();
               }
               return null;
            }

            public void done() {
               dialog.setCompleteWithDelay(300);
            }

         };
         worker.execute();
         // messageDialog.addText("Done!");
      }
   }
}
