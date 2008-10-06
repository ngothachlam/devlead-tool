package com.jonas.agile.devleadtool.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AddDialog;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.dnd.TableAndTitleDTO;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraListener;
import com.jonas.agile.devleadtool.component.panel.OpenJirasListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.jira.JiraIssue;

public class MyTablePopupMenu extends JPopupMenu {

   private final MyTable sourceTable;

   public MyTablePopupMenu(MyTable source, PlannerHelper helper, TableAndTitleDTO... tables) {
      super();
      this.sourceTable = source;

      sourceTable.addMouseListener(new PopupListener(this));

      add(new MenuItem_Add("Add Jiras to Table", sourceTable, helper.getParentFrame(), tables));
      add(new MenuItem_Open("Open in Browser", sourceTable, helper));
      add(new MenuItem_Sync("Sync with Jira", sourceTable, helper));
      addSeparator();

      for (TableAndTitleDTO tableDTO : tables) {
         if (!tableDTO.getTable().equals(source)) {
            String title = "Copy to Table " + tableDTO.getTitle();
            add(new MenuItem_Copy((title), sourceTable, tableDTO.getTable()));
         }
      }

      addSeparator();
      add(new MenuItem_Remove("Remove from Table", sourceTable));
   }

   @Override
   public void show(Component invoker, int x, int y) {
      if (sourceTable.getSelectedRowCount() > 0)
         super.show(invoker, x, y);
   }
}


class PopupListener extends MouseAdapter {
   private MyTablePopupMenu popup;

   public PopupListener(MyTablePopupMenu popup) {
      this.popup = popup;
   }

   private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
         popup.show(e.getComponent(), e.getX(), e.getY());
      }
   }

   public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
   }

   public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
   }
}


class MenuItem_Copy extends JMenuItem implements ActionListener {

   private final MyTable destinationTable;
   private MyTable sourceTable;

   public MenuItem_Copy(String string, MyTable source, MyTable table) {
      super(string);
      this.sourceTable = source;
      this.destinationTable = table;
      addActionListener(this);
   }

   public void actionPerformed(ActionEvent e) {
      final int[] selectedRows = sourceTable.getSelectedRows();
      final ProgressDialog dialog = new ProgressDialog(null, "Copying...", "Copying selected messages from Board to Plan...", selectedRows.length);
      try {
         for (int i = 0; i < selectedRows.length; i++) {
            String jiraString = (String) sourceTable.getValueAt(Column.Jira, selectedRows[i]);
            addJira(jiraString, destinationTable);
            dialog.increseProgress();
         }
      } catch (Exception ex) {
         AlertDialog.alertException(null, ex);
         ex.printStackTrace();
      }
      dialog.setCompleteWithDelay(300);
   }

   void addJira(String jiraString, MyTable table) {
      Map<Column, Object> map = new HashMap<Column, Object>();
      Column[] columns = sourceTable.getColumns();
      for (Column column : columns) {
         MyTableModel model = (MyTableModel) sourceTable.getModel();
         int row = model.getRowWithJira(jiraString, Column.Jira);
         map.put(column, model.getValueAt(column, row));
      }
      table.addJira(jiraString, map);
   }
}


class MenuItem_Open extends JMenuItem {
   private MyTable sourceTable;

   public MenuItem_Open(String string, MyTable sourceTable, PlannerHelper helper) {
      super(string);
      this.sourceTable = sourceTable;
      addActionListener(new OpenJirasListener(sourceTable, helper));
   }
}


class MenuItem_Add extends JMenuItem {
   private MyTable sourceTable;

   public MenuItem_Add(String string, MyTable sourceTable, final JFrame frame, final TableAndTitleDTO... dtos) {
      super(string);
      this.sourceTable = sourceTable;
      addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            new AddDialog(frame, dtos);
         }
      });

   }
}


class MenuItem_Remove extends JMenuItem implements ActionListener {
   private MyTable sourceTable;

   public MenuItem_Remove(String string, MyTable sourceTable) {
      super(string);
      this.sourceTable = sourceTable;
      addActionListener(this);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      sourceTable.removeSelectedRows();
   }
}


class MenuItem_Sync extends JMenuItem {
   private MyTable sourceTable;

   public MenuItem_Sync(String string, final MyTable sourceTable, PlannerHelper helper) {
      super(string);
      this.sourceTable = sourceTable;
      SyncWithJiraActionListenerListener syncWithJiraActionListenerListener = new SyncWithJiraActionListenerListener() {
         public void jiraAdded(JiraIssue jiraIssue) {
            sourceTable.addJira(jiraIssue);
         }

         public void jiraSynced(JiraIssue jiraIssue, int tableRowSynced) {
            sourceTable.addJira(jiraIssue);
         }
      };

      SyncWithJiraListener syncWithJiraListener = new SyncWithJiraListener(sourceTable, helper);
      syncWithJiraListener.addListener(syncWithJiraActionListenerListener);
      addActionListener(syncWithJiraListener);
   }
}