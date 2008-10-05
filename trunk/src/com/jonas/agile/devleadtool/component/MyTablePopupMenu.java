package com.jonas.agile.devleadtool.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.dnd.TableAndTitleDTO;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;

public class MyTablePopupMenu extends JPopupMenu {

   private Map<MyTable, CopyMenuItem> menuTables = new HashMap<MyTable, CopyMenuItem>();
   private Map<String, MyTable> menuItems = new HashMap<String, MyTable>();

   public MyTablePopupMenu(TableAndTitleDTO... tables) {
      super();
      for (TableAndTitleDTO tableDTO : tables) {
         String menuItemTitle = "Copy to " + tableDTO.getTitle();
         MyTable table = tableDTO.getTable();
         CopyMenuItem menuItem = new CopyMenuItem(menuItemTitle, table);
         add(menuItem);
         menuTables.put(table, menuItem);
      }
   }

   public void setSourceAndShow(MyTable source, int x, int y) {
      for (MyTable table : menuTables.keySet()) {
         CopyMenuItem copyMenuItem = menuTables.get(table);
         if (source.equals(table)) {
            copyMenuItem.setEnabled(false);
         } else {
            copyMenuItem.setEnabled(true);
            copyMenuItem.setSource(source);
         }
      }
      show(source, x, y);
   }
}


class CopyMenuItem extends JMenuItem implements ActionListener {

   private final MyTable table;
   private MyTable source;

   public CopyMenuItem(String string, MyTable table) {
      super(string);
      this.table = table;
      addActionListener(this);
   }

   public void setSource(MyTable source) {
      this.source = source;
   }

   public void actionPerformed(ActionEvent e) {
      final int[] selectedRows = source.getSelectedRows();
      final ProgressDialog dialog = new ProgressDialog(null, "Copying...", "Copying selected messages from Board to Plan...",
            selectedRows.length);
      try {
         for (int i = 0; i < selectedRows.length; i++) {
            String jiraString = (String) source.getValueAt(Column.Jira, selectedRows[i]);
            addJira(jiraString, table);
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
      Column[] columns = source.getColumns();
      for (Column column : columns) {
         MyTableModel model = (MyTableModel) source.getModel();
         int row = model.getRowWithJira(jiraString, Column.Jira);
         map.put(column, model.getValueAt(column, row));
      }
      table.addJira(jiraString, map);
   }
}
