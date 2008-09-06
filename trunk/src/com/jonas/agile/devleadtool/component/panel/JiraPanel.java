package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.listener.DownloadJirasListener;
import com.jonas.agile.devleadtool.component.listener.RemoveJTableSelectedRowsListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.HyperLinker;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraPanel extends MyComponentPanel {

   final PlannerHelper helper;
   Logger log = MyLogger.getLogger(JiraPanel.class);
   MyTable table;

   public JiraPanel(PlannerHelper helper) {
      super(new BorderLayout());
      this.helper = helper;

      table = new MyTable();
      table.setModel(new JiraTableModel());
      table.setAutoCreateRowSorter(true);
      table.setDragEnabled(true);

      JScrollPane scrollpane = new MyScrollPane(table);

      this.addCenter(scrollpane);
      this.addSouth(getButtonPanel());
      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
   }

   private Component getButtonPanel() {
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();

      Vector<JiraProject> projects = JiraProject.getProjects();
      final JComboBox jiraProjectsCombo = new JComboBox(projects);
      final JComboBox jiraProjectFixVersionCombo = new JComboBox();

      jiraProjectsCombo.addActionListener(new AlteringProjectListener(jiraProjectFixVersionCombo));

      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(2, 2, 2, 2);
      gbc.gridx = 0;
      gbc.gridy = 0;
      panel.add(jiraProjectsCombo, gbc);
      gbc.gridx = 1;
      gbc.gridy = 0;
      addButton(panel, "Refresh", new RefreshingFixVersionListener(jiraProjectFixVersionCombo, jiraProjectsCombo), gbc);
      gbc.gridx = 2;
      gbc.gridy = 0;
      panel.add(jiraProjectFixVersionCombo, gbc);
      gbc.gridx = 3;
      gbc.gridy = 0;
      addButton(panel, "Get Jiras", new DownloadJirasListener(jiraProjectFixVersionCombo, table, helper), gbc);
      gbc.gridx = 0;
      gbc.gridy = 1;
      addButton(panel, "Remove Selected", new RemoveJTableSelectedRowsListener(table), gbc);
      gbc.gridx = 1;
      gbc.gridy = 1;
      addButton(panel, "Open Jiras", new OpenJirasListener(), gbc);
      // addButton(buttons, "BoardStatus", new ActionListener() {
      // public void actionPerformed(ActionEvent e) {
      // MyTableModel model = (MyTableModel) table.getModel();
      // for (int i = 0; i < model.getRowCount(); i++) {
      // int boardStatusNo = model.getColumnNo(Column.BoardStatus);
      // int jiraNameNo = model.getColumnNo(Column.Jira);
      // String jira = (String) model.getValueAt(i, jiraNameNo);
      // List<BoardStatus> jirasBoardStatusList = helper.getPlannerCommunicator().getJiraStatusFromBoard(jira);
      // model.setValueAt(jirasBoardStatusList, i, boardStatusNo);
      // }
      // }
      // });

      return panel;
   }

   public MyTableModel getJiraModel() {
      return ((MyTableModel) table.getModel());
   }

   public void setEditable(boolean selected) {
      ((MyTableModel) table.getModel()).setEditable(selected);
   }

   private final class AlteringProjectListener implements ActionListener {
      private final JComboBox jiraProjectFixVersionCombo;

      private AlteringProjectListener(JComboBox jiraProjectFixVersionCombo) {
         this.jiraProjectFixVersionCombo = jiraProjectFixVersionCombo;
      }

      public void actionPerformed(ActionEvent e) {
         jiraProjectFixVersionCombo.removeAllItems();
         jiraProjectFixVersionCombo.setEditable(false);
      }
   }

   private final class ClearJirasListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         MyTableModel model = ((MyTableModel) table.getModel());
         while (model.getRowCount() > 0) {
            model.removeRow(0);
         }
      }
   }

   private final class OpenJirasListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         int[] rows = table.getSelectedRows();
         StringBuffer sb = new StringBuffer();
         for (int j = 0; j < rows.length; j++) {
            String jira = (String) table.getModel().getValueAt(table.convertRowIndexToModel(rows[j]), 0);
            String jira_url = null;
            boolean error = false;
            try {
               jira_url = helper.getJiraUrl(jira);
            } catch (NotJiraException e1) {
               if (sb.length() > 0) {
                  sb.append(", ");
               }
               sb.append(jira);
               error = true;
            }
            if (!error) {
               HyperLinker.displayURL(jira_url + "/browse/" + jira);
            }
         }
         if (sb.length() > 0) {
            sb.append(" are incorrect!");
            AlertDialog.alertException(helper, e.toString());
         }
      }
   }

   private final class RefreshingFixVersionListener implements ActionListener {
      private final JComboBox jiraProjectFixVersionCombo;
      private final JComboBox jiraProjectsCombo;

      private RefreshingFixVersionListener(JComboBox jiraProjectFixVersionCombo, JComboBox jiraProjectsCombo) {
         this.jiraProjectFixVersionCombo = jiraProjectFixVersionCombo;
         this.jiraProjectsCombo = jiraProjectsCombo;
      }

      public void actionPerformed(ActionEvent e) {
         log.debug("getting fixVersion : " + jiraProjectsCombo.getSelectedItem());
         jiraProjectFixVersionCombo.removeAllItems();
         final Object[] selectedObjects = jiraProjectsCombo.getSelectedObjects();
         final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Refreshing Fix Versions...", "Refreshing Fix Versions...",
               selectedObjects.length);
         SwingWorker worker = new SwingWorker() {
            public Object doInBackground() {
               dialog.setIndeterminate(false);
               for (int i = 0; i < selectedObjects.length; i++) {
                  JiraProject selectedProject = (JiraProject) selectedObjects[i];
                  dialog.setNote("Refreshing Fix Versions for " + selectedProject.getJiraKey() + "...");
                  try {
                     JiraVersion[] fixVersions = selectedProject.getJiraClient().getFixVersionsFromProject(selectedProject, false);
                     for (JiraVersion jiraVersion : fixVersions) {
                        jiraProjectFixVersionCombo.addItem(jiraVersion);
                     }
                  } catch (RemoteException e1) {
                     AlertDialog.alertException(helper, e1);
                  }
               }
               return null;
            }

            @Override
            public void done() {
               dialog.setCompleteWithDelay(300);
            }
         };
         worker.execute();
      }
   }

}
