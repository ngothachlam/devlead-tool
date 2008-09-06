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
import com.jonas.agile.devleadtool.component.listener.HyperLinkOpenerAdapter;
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

   public MyTable table;
   private final PlannerHelper helper;

   private Logger log = MyLogger.getLogger(BoardPanel.class);

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

   public void setEditable(boolean selected) {
      ((MyTableModel) table.getModel()).setEditable(selected);
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

      table.addMouseListener(new HyperLinkOpenerAdapter(helper, Column.URL, Column.Jira));
      table.addKeyListener(new KeyStrokeForJiraChangeTableListener(table));
      table.setAutoCreateRowSorter(true);

      JScrollPane scrollPane = new MyScrollPane(table);

      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
      addCenter(scrollPane);
   }

   protected void setButtons() {
      // MyPanel buttonPanel = SwingUtil.getGridPanel(0, 2, 5, 5).bordered();
      JPanel buttonPanel = new JPanel();

      addPanelWithAddAndRemoveOptions(table, buttonPanel, new AddNewRowActionListenerListener() {
         public void addedNewRow(String jiraString, int itsRow, int itsColumn) {
            ((MyTableModel) table.getModel()).setValueAt(jiraString, itsRow, itsColumn);
         }
      });

      addButton(buttonPanel, "Copy to Plan", new ActionListener() {
         private Logger log = MyLogger.getLogger(this.getClass());

         public void actionPerformed(ActionEvent e) {
            // AlertDialog messageDialog = AlertDialog.message(helper, "Copying... ");

            final int[] selectedRows = table.getSelectedRows();
            final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Copying...", "Copying selected messages from Board to Plan...",
                  selectedRows.length);
            SwingWorker worker = new SwingWorker() {

               public void done() {
                  dialog.setCompleteWithDelay(300);
               }

               @Override
               protected Object doInBackground() {
                  try {
                     for (int i = 0; i < selectedRows.length; i++) {
                        Object valueAt = table.getValueAt(selectedRows[i], 6);
                        try {
                           helper.addToPlan((String) valueAt, false);
                        } catch (JiraIssueNotFoundException e) {
                           AlertDialog.alertException(helper, e);
                           e.printStackTrace();
                        }
                        dialog.increseProgress();
                     }
                  } catch (Exception e) {
                     AlertDialog.alertException(helper, e);
                     e.printStackTrace();
                  }
                  return null;
               }

            };
            worker.execute();
            // messageDialog.addText("Done!");
         }
      });

      addSouth(buttonPanel);
   }
}
