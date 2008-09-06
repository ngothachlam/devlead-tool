package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.listener.PlanFixVersionListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.data.PlanFixVersion;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;

public class FixVersionsPanel extends MyComponentPanel {

   private Logger log = MyLogger.getLogger(FixVersionsPanel.class);
   private MyTable table;

   public FixVersionsPanel() {
      super(new BorderLayout());

      addCenter(getTablePanel());
      addSouth(getBottomPanel());

      addListeners();
   }

   private void addListeners() {
      PlanFixVersion.addListener(new PlanFixVersionListener() {
         public void planFixVersionAdded(PlanFixVersion fixVersion, JiraProject project) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Vector<Object> rowData = getRowData(fixVersion);
            model.addRow(rowData);
         }

         public void planFixVersionRemoved() {
         }
      });
   }

   private Component getTablePanel() {
      JPanel panel = new JPanel(new BorderLayout());
      table = new MyTable();
      Vector header = new Vector();
      header.add("FixVersion");
      table.setModel(new DefaultTableModel(null, header) {
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      });
      JScrollPane scrollpane = new JScrollPane(table);
      panel.add(scrollpane);
      return panel;
   }

   private Vector<Object> getRowData(PlanFixVersion fixVersion) {
      Vector<Object> rowData = new Vector<Object>();
      rowData.add(fixVersion);
      return rowData;
   }

   private Component getBottomPanel() {
      JPanel panel = new JPanel();

      addLabel(panel, "FixVersion:");
      final JTextField newVersionField = addTextField(panel, 10);
      final JComboBox projectCombo = new JComboBox(JiraProject.getProjects());
      panel.add(projectCombo);
      addButton(panel, "Add", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JiraProject project = (JiraProject) projectCombo.getSelectedItem();
            new PlanFixVersion(newVersionField.getText(), project);
         }

      });
      addButton(panel, "Remove Selected", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            while (table.getSelectedRowCount() > 0) {
               int tableSelectedRow = table.getSelectedRow();
               int modelRowNo = table.convertRowIndexToModel(tableSelectedRow);
               PlanFixVersion versionToRemove = (PlanFixVersion) model.getValueAt(modelRowNo, 0);
               PlanFixVersion.remove(versionToRemove);
               model.removeRow(modelRowNo);
            }
         }
      });
      addButton(panel, "Update Selected", new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            if (table.getSelectedRowCount() > 1) {
               throw new RuntimeException("Can only update one row!!");
            }
            while (table.getSelectedRowCount() > 0) {
               int tableSelectedRow = table.getSelectedRow();
               int modelRowNo = table.convertRowIndexToModel(tableSelectedRow);
               PlanFixVersion versionToUpdate = (PlanFixVersion) model.getValueAt(modelRowNo, 0);
               versionToUpdate.setText(newVersionField.getText());
               versionToUpdate.setProject((JiraProject) projectCombo.getSelectedItem());
               model.fireTableDataChanged();
            }
         }
      });
      return panel;
   }
}
