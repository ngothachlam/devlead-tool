package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
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
import com.jonas.agile.devleadtool.component.table.Column;
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
   private MyTable table;

   public JiraPanel(PlannerHelper helper) {
      this(helper, new JiraTableModel());
   }

   public JiraPanel(PlannerHelper helper, JiraTableModel jiraModel) {
      super(new BorderLayout());
      this.helper = helper;

      table = new MyTable(jiraModel);

      JScrollPane scrollpane = new MyScrollPane(table);

      this.addCenter(scrollpane);
      this.addSouth(getBottomPanel());
      this.setBorder(BorderFactory.createEmptyBorder(1, 2, 2, 3));
   }

   protected JPanel getBottomPanel() {
      MyPanel buttonPanel = new MyPanel(new BorderLayout());
      // JPanel buttonPanelOne = getButtonPanelNorth();
      // buttonPanel.addNorth(buttonPanelOne);
      JPanel buttonPanelTwo = getButtonPanelSouth();
      buttonPanel.addSouth(buttonPanelTwo);
      return buttonPanel;
   }

   private JPanel getButtonPanelNorth() {
      JPanel buttonPanel = new JPanel();
      addFilter(buttonPanel, table, Column.Jira, Column.Description);
      return buttonPanel;
   }

   private JPanel getButtonPanelSouth() {
      JPanel mainPanel = new JPanel(new BorderLayout());

      JPanel topPanel = new JPanel(new FlowLayout());

      Vector<JiraProject> projects = JiraProject.getProjects();

      addPanelWithAddAndRemoveOptions(table, topPanel);
      SyncWithJiraActionListener listener = new SyncWithJiraActionListener(table, helper);
      SyncWithJiraActionListenerListener syncWithJiraListener = new SyncWithJiraActionListenerListener() {
         public void jiraAdded(JiraIssue jiraIssue) {
            table.addJira(jiraIssue);
         }

         public void jiraSynced(JiraIssue jira, int tableRowSynced) {
            // new JiraIssue(jira.getKey(), jira.getSummary(), jira.getStatus(), jira.getResolution(), jira.getStatus(),
            // jira.getBuildNo(),jira.getEstimate(), jira.getLLUListPriority());
            log.debug("jira synced " + jira.getKey() + " on table row " + tableRowSynced);
            table.setValueAt(jira.getKey(), tableRowSynced, Column.Jira);
            table.setValueAt(jira.getSummary(), tableRowSynced, Column.Description);
            table.setValueAt(jira.getType(), tableRowSynced, Column.J_Type);
            table.setValueAt(jira.getFixVersions(), tableRowSynced, Column.J_FixVersion);
            table.setValueAt(jira.getStatus(), tableRowSynced, Column.J_Status);
            table.setValueAt(jira.getResolution(), tableRowSynced, Column.J_Resolution);
            table.setValueAt(jira.getBuildNo(), tableRowSynced, Column.J_BuildNo);
            table.setValueAt(jira.getEstimate(), tableRowSynced, Column.J_Dev_Estimate);
            table.setValueAt(jira.getSpent(), tableRowSynced, Column.J_Dev_Spent);
         }
      };
      listener.addListener(syncWithJiraListener);
      addButton(topPanel, "Sync", listener);
      addButton(topPanel, "Open", new OpenJirasListener(table, helper));
      addButton(topPanel, "Copy to Board", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getBoardTable();
         }
      }, helper), Column.Description);
      addButton(topPanel, "Copy to Plan", new CopyToTableListener(table, new DestinationRetriever() {
         public MyTable getDestinationTable() {
            return helper.getActiveInternalFrame().getPlanTable();
         }
      }, helper, Column.Description, Column.Release, Column.Planned_Sprint, Column.Resolved_Sprint, Column.Closed_Sprint, Column.Dev_Estimate,
            Column.Dev_Actual, Column.Note));

      JPanel bottomPanel = getFixversionSyncPanel(projects, syncWithJiraListener);

      mainPanel.add(topPanel, BorderLayout.NORTH);
      mainPanel.add(bottomPanel, BorderLayout.SOUTH);
      return mainPanel;
   }

   private JPanel getFixversionSyncPanel(Vector<JiraProject> projects, SyncWithJiraActionListenerListener syncWithJiraListener) {
      JPanel panel = new JPanel(new FlowLayout());
      final JComboBox jiraProjectsCombo = new JComboBox(projects);
      final JComboBox jiraProjectFixVersionCombo = new JComboBox();

      AlteringProjectListener alteringProjectListener = new AlteringProjectListener(jiraProjectFixVersionCombo);
      RefreshingFixVersionListener refreshFixVersionListener = new RefreshingFixVersionListener(jiraProjectsCombo, jiraProjectFixVersionCombo);
      DownloadJirasListener downloadJirasListener = new DownloadJirasListener(jiraProjectFixVersionCombo, table, helper);

      downloadJirasListener.addListener(syncWithJiraListener);
      jiraProjectsCombo.addActionListener(alteringProjectListener);

      panel.add(jiraProjectsCombo);
      addButton(panel, "Refresh", refreshFixVersionListener);
      panel.add(jiraProjectFixVersionCombo);
      addButton(panel, "Get Jiras", downloadJirasListener);
      return panel;
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

   private final Map<JiraProject, JiraVersion[]> state = new HashMap<JiraProject, JiraVersion[]>();

   private class AlteringProjectListener implements ActionListener {
      private final JComboBox fixVersionCombo;

      private AlteringProjectListener(JComboBox jiraProjectFixVersionCombo) {
         this.fixVersionCombo = jiraProjectFixVersionCombo;
      }

      public void actionPerformed(ActionEvent e) {
         fixVersionCombo.setEditable(false);
         fixVersionCombo.removeAllItems();

         JComboBox projectsCombo = ((JComboBox) e.getSource());
         JiraVersion[] fixVersions = state.get(projectsCombo.getSelectedItem());
         if (fixVersions != null) {
            for (JiraVersion jiraVersion : fixVersions) {
               fixVersionCombo.addItem(jiraVersion);
            }
         }
      }
   }

   private final class RefreshingFixVersionListener implements ActionListener {
      private final class VersionComparator implements Comparator<JiraVersion> {
         @Override
         public int compare(JiraVersion o1, JiraVersion o2) {
            String name1 = o1.getName();
            String name2 = o2.getName();
            return name1.compareToIgnoreCase(name2);
         }
      }

      private final JComboBox projectCombo;
      private final JComboBox fixVersionCombo;

      private RefreshingFixVersionListener(JComboBox jiraProjectsCombo, JComboBox jiraFixVersionCombo) {
         this.projectCombo = jiraProjectsCombo;
         this.fixVersionCombo = jiraFixVersionCombo;
      }

      public void actionPerformed(ActionEvent e) {
         log.debug("getting fixVersion : " + projectCombo.getSelectedItem());

         // store the fix versions
         state.put((JiraProject) projectCombo.getSelectedItem(), getFixVersionsInCombo());

         fixVersionCombo.removeAllItems();
         final Object[] selectedObjects = projectCombo.getSelectedObjects();
         final ProgressDialog dialog = new ProgressDialog(helper.getParentFrame(), "Refreshing Fix Versions...", "Refreshing Fix Versions...",
               selectedObjects.length);
         SwingWorker worker = new SwingWorker() {
            public Object doInBackground() {
               dialog.setIndeterminate(false);
               JiraProject selectedProject = (JiraProject) selectedObjects[0];
               dialog.setNote("Refreshing Fix Versions for " + selectedProject.getJiraKey() + "...");
               try {
                  JiraVersion[] fixVersions = selectedProject.getJiraClient().getFixVersionsFromProject(selectedProject, false);
                  Arrays.sort(fixVersions, new VersionComparator());
                  for (JiraVersion jiraVersion : fixVersions) {
                     fixVersionCombo.addItem(jiraVersion);
                  }
               } catch (RemoteException e1) {
                  AlertDialog.alertException(helper, e1);
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

      private JiraVersion[] getFixVersionsInCombo() {
         JiraVersion[] items = new JiraVersion[fixVersionCombo.getItemCount()];
         for (int i = 0; i < items.length; i++) {
            JiraVersion item = (JiraVersion) fixVersionCombo.getItemAt(i);
            items[i] = item;
         }
         return items;
      }
   }

}
