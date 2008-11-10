package com.jonas.agile.devleadtool.component;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AddManualDialog;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.listener.OpenJirasListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.jira.JiraIssue;

public class MyTablePopupMenu extends MyPopupMenu {

   private final MyTable sourceTable;

   public MyTablePopupMenu(MyTable source, PlannerHelper helper, MyTable... tables) {
      super(source);
      this.sourceTable = source;
      JFrame parentFrame = helper.getParentFrame();

      add(new MenuItem_Add("Add Jiras", source, parentFrame, tables));
      add(new MenuItem_Default("Open in Browser", new OpenJirasListener(sourceTable, helper)));
      add(new MenuItem_Sync("Dowload Jira Info", sourceTable, helper));
      addSeparator();
      addMenuItem_Copys(source, parentFrame, tables);
      addSeparator();
      add(new MenuItem_Remove("Remove Jiras", sourceTable, helper.getParentFrame()));
   }

   private void addMenuItem_Copys(MyTable source, JFrame parentFrame, MyTable... tables) {
      for (MyTable tableDTO : tables) {
         if (!tableDTO.equals(source)) {
            String title = "Copy to other Table: " + tableDTO.getTitle();
            add(new MenuItem_Copy(parentFrame, title, sourceTable, tableDTO));
         }
      }
   }
   

   private class MenuItem_Add extends JMenuItem {

      public MenuItem_Add(String string, final MyTable source, final JFrame frame, final MyTable... dtos) {
         super(string);
         addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               AddManualDialog addManualDialog = new AddManualDialog(frame, dtos);
               addManualDialog.setSourceTable(source);
            }
         });
      }
   }

   private class MenuItem_Copy extends JMenuItem implements ActionListener {

      private final MyTable destinationTable;
      private JFrame frame;
      private MyTable sourceTable;

      public MenuItem_Copy(JFrame frame, String string, MyTable source, MyTable table) {
         super(string);
         this.frame = frame;
         this.sourceTable = source;
         this.destinationTable = table;
         addActionListener(this);
      }

      public void actionPerformed(ActionEvent e) {
         final int[] selectedRows = sourceTable.getSelectedRows();
         if (selectedRows.length == 0 ){
            AlertDialog.alertMessage(frame, "No rows selected!");
         }
         final ProgressDialog dialog = new ProgressDialog(frame, "Copying...", "Copying selected messages from Board to Plan...",
               selectedRows.length);
         try {
            for (int i = 0; i < selectedRows.length; i++) {
               String jiraString = (String) sourceTable.getValueAt(Column.Jira, selectedRows[i]);
               addJira(jiraString, destinationTable);
               dialog.increseProgress();
            }
            dialog.setCompleteWithDelay(300);
         } catch (Exception ex) {
            AlertDialog.alertException(null, ex);
            ex.printStackTrace();
         }
      }

      void addJira(String jiraString, MyTable table) {
         Map<Column, Object> map = new HashMap<Column, Object>();
         Column[] columns = sourceTable.getColumns();
         for (Column column : columns) {
            MyTableModel model = (MyTableModel) sourceTable.getModel();
            int row = model.getRowWithJira(jiraString);
            map.put(column, model.getValueAt(column, row));
         }
         table.addJira(jiraString, map);
      }
   }

   private class MenuItem_Default extends JMenuItem {

      public MenuItem_Default(String string, ActionListener actionListener) {
         super(string);
         addActionListener(actionListener);
      }
   }

   private class MenuItem_Remove extends JMenuItem implements ActionListener {
      private MyTable sourceTable;
      private final Frame parent;

      public MenuItem_Remove(String string, MyTable sourceTable, Frame parent) {
         super(string);
         this.sourceTable = sourceTable;
         this.parent = parent;
         addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         final ProgressDialog dialog = new ProgressDialog(parent, "Removing...", "Removing selected Jiras...",0);
         if (sourceTable.getSelectedRowCount() <= 0){
            dialog.setNote("Nothing selected!!");
            dialog.setCompleteWithDelay(2000);
         }
         sourceTable.removeSelectedRows();
         dialog.setCompleteWithDelay(300);
      }
   }

   private class MenuItem_Sync extends JMenuItem {
      public MenuItem_Sync(String string, final MyTable sourceTable, PlannerHelper helper) {
         super(string);
         addActionListener(getActionListener(sourceTable, helper));
      }

      private SyncWithJiraListener getActionListener(final MyTable sourceTable, PlannerHelper helper) {
         SyncWithJiraListener actionListener = new SyncWithJiraListener(sourceTable, helper);
         SyncWithJiraActionListenerListener syncWithJiraActionListenerListener = new SyncWithJiraActionListenerListener() {
            public void jiraAdded(JiraIssue jiraIssue) {
               sourceTable.addJira(jiraIssue);
            }

            public void jiraSynced(JiraIssue jiraIssue, int tableRowSynced) {
               sourceTable.syncJira(jiraIssue, tableRowSynced);
            }
         };
         actionListener.addListener(syncWithJiraActionListenerListener);
         return actionListener;
      }
   }
}
