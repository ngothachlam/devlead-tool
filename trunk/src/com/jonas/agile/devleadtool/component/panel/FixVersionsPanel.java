package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.listener.PlanFixVersionListener;
import com.jonas.agile.devleadtool.component.listener.RemoveJTableSelectedRowsListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.agile.devleadtool.data.PlanFixVersion;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.testHelpers.TryoutTester;

public class FixVersionsPanel extends MyComponentPanel {

   final PlannerHelper helper;
   private Logger log = MyLogger.getLogger(FixVersionsPanel.class);
   private MyTable table;

   public FixVersionsPanel(PlannerHelper client) {
      super(new BorderLayout());
      this.helper = client;

      addCenter(getTablePanel());
      addSouth(getBottomPanel());

      addListeners();
   }

   private void addListeners() {
      PlanFixVersion.addListener(new PlanFixVersionListener() {
         public void planFixVersionAdded(PlanFixVersion fixVersion, JiraProject project) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Vector<Object> rowData = getRowData(project, fixVersion);
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
      header.add("Project");
      table.setModel(new DefaultTableModel(null, header));
      JScrollPane scrollpane = new JScrollPane(table);
      panel.add(scrollpane);
      return panel;
   }

   public static void main(String[] args) {
      JFrame frame = TryoutTester.getFrame();
      JPanel panel = new FixVersionsPanel(new PlannerHelper(frame, "test"));
      frame.setContentPane(panel);
      frame.setVisible(true);
   }

   private Vector<Object> getRowData(JiraProject project, PlanFixVersion fixVersion) {
      Vector<Object> rowData = new Vector<Object>();
      rowData.add(fixVersion);
      rowData.add(project);
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
      addButton(panel, "Remove", new ActionListener() {
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
      return panel;
   }
}
