package com.jonas.agile.devleadtool.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.dnd.TableAndTitleDTO;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.guibuilding.basicdnd.PopupListener;

public class MyTablePopupMenu extends JPopupMenu {

   private final MyTable sourceTable;
   
   private static final List<MyTablePopupMenu> list = new ArrayList<MyTablePopupMenu>();

   public MyTablePopupMenu(MyTable source, TableAndTitleDTO... tables) {
      super();
      this.sourceTable = source;
      sourceTable.addMouseListener(new PopupListener(this));
      
      for (TableAndTitleDTO tableDTO : tables) {
         String menuItemTitle = "Copy to " + tableDTO.getTitle();
         CopyMenuItem menuItem = new CopyMenuItem(menuItemTitle, sourceTable, tableDTO.getTable());
         
         add(menuItem);
      }

      addSeparator();

      add(new RemoveMenuItem("Remove", sourceTable));
      
      list.add(this);
   }
}

class RemoveMenuItem extends JMenuItem implements ActionListener {
   private MyTable sourceTable;

   public RemoveMenuItem(String string, MyTable sourceTable) {
      super(string);
      this.sourceTable = sourceTable;
      addActionListener(this);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      sourceTable.removeSelectedRows();
   }
}


class CopyMenuItem extends JMenuItem implements ActionListener {

   private final MyTable destinationTable;
   private MyTable sourceTable;

   public CopyMenuItem(String string, MyTable source, MyTable table) {
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
