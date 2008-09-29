package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.MyScrollPane;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.listener.CopyToTableListener;
import com.jonas.agile.devleadtool.component.listener.DestinationRetriever;
import com.jonas.agile.devleadtool.component.listener.DownloadJirasListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.component.table.ColumnDataType;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.table.model.JiraTableModel;
import com.jonas.agile.devleadtool.component.table.model.MyTableModel;
import com.jonas.common.MyComponentPanel;
import com.jonas.common.MyPanel;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class JiraPanel extends MyComponentPanel {

   private final PlannerHelper helper;
   private Logger log = MyLogger.getLogger(JiraPanel.class);
   private TableRowSorter<TableModel> sorter;
   private MyTable table;

   public JiraPanel(PlannerHelper helper) {
      super(new BorderLayout());
      this.helper = helper;

      table = new MyTable(new JiraTableModel());
      // table.setModel(new JiraTableModel());

      // FIXME - the filterer doesn't work
      // sorter = new TableRowSorter<TableModel>(table.getModel());
      // table.setRowSorter(sorter);

      JScrollPane scrollpane = new MyScrollPane(table);

      this.addCenter(scrollpane);
      this.addSouth(getBottomPanel());
      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
   }

   protected JPanel getBottomPanel() {
      MyPanel buttonPanel = new MyPanel(new BorderLayout());
      // JPanel buttonPanelOne = getButtonPanelNorth();
      JPanel buttonPanelTwo = getButtonPanelSouth();
      // buttonPanel.addNorth(buttonPanelOne);
      buttonPanel.addSouth(buttonPanelTwo);
      return buttonPanel;
   }

   private JPanel getButtonPanelNorth() {
      JPanel buttonPanel = new JPanel();
      addFilter(buttonPanel, table, sorter, ColumnDataType.Jira, ColumnDataType.Description);
      return buttonPanel;
   }

   private JPanel getButtonPanelSouth() {
      JPanel mainPanel = new JPanel(new BorderLayout());

      JPanel topPanel = new JPanel(new FlowLayout());
      JPanel bottomPanel = new JPanel(new FlowLayout());

      Vector<JiraProject> projects = JiraProject.getProjects();
      final JComboBox jiraProjectsCombo = new JComboBox(projects);
      final JComboBox jiraProjectFixVersionCombo = new JComboBox();

      jiraProjectsCombo.addActionListener(new AlteringProjectListener(jiraProjectFixVersionCombo));

      addPanelWithAddAndRemoveOptions(table, topPanel);
      SyncWithJiraActionListener listener = new SyncWithJiraActionListener(table, helper);
      SyncWithJiraActionListenerListener syncWithJiraListener = new SyncWithJiraActionListenerListener() {
         public void jiraAdded(JiraIssue jiraIssue) {
            table.addJira(jiraIssue);
         }

         public void jiraSynced(JiraIssue jira, int tableRowSynced) {
            table.setValueAt(jira.getSummary(), tableRowSynced, ColumnDataType.Description);
            table.setValueAt(jira.getType(), tableRowSynced, ColumnDataType.J_Type);
            table.setValueAt(jira.getFixVersions(), tableRowSynced, ColumnDataType.J_FixVersion);
            table.setValueAt(jira.getStatus(), tableRowSynced, ColumnDataType.J_Status);
            table.setValueAt(jira.getResolution(), tableRowSynced, ColumnDataType.J_Resolution);
            table.setValueAt(jira.getBuildNo(), tableRowSynced, ColumnDataType.J_BuildNo);
            table.setValueAt(jira.getEstimate(), tableRowSynced, ColumnDataType.J_Dev_Estimate);
            table.setValueAt(jira.getSpent(), tableRowSynced, ColumnDataType.J_Dev_Spent);
         }

         public void jiraSyncedCompleted() {
         }
      };
      listener.addListener(syncWithJiraListener);
      addButton(topPanel, "Sync", listener);
      addButton(topPanel, "Open Jiras", new OpenJirasListener(table, helper));
      addButton(topPanel, "Copy to Board", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getBoardTable();
         }
      }, helper));
      addButton(topPanel, "Copy to Plan", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getPlanTable();
         }
      }, helper));

      bottomPanel.add(jiraProjectsCombo);
      addButton(bottomPanel, "Refresh", new RefreshingFixVersionListener(jiraProjectFixVersionCombo, jiraProjectsCombo));
      bottomPanel.add(jiraProjectFixVersionCombo);
      DownloadJirasListener listener2 = new DownloadJirasListener(jiraProjectFixVersionCombo, table, helper);
      listener2.addListener(syncWithJiraListener);
      addButton(bottomPanel, "Get Jiras", listener2);
      // addButton(panel, "Remove Selected", new RemoveJTableSelectedRowsListener(table));

      mainPanel.add(topPanel, BorderLayout.NORTH);
      mainPanel.add(bottomPanel, BorderLayout.SOUTH);
      return mainPanel;
   }

   public MyTableModel getJiraModel() {
      return ((MyTableModel) table.getModel());
   }

   public MyTable getTable() {
      return table;
   }

   public void setEditable(boolean selected) {
      ((MyTableModel) table.getModel()).setEditable(selected);
   }

   private class AlteringProjectListener implements ActionListener {
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
