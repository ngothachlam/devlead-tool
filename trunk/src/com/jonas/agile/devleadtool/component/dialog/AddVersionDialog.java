package com.jonas.agile.devleadtool.component.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.TableRadioButton;
import com.jonas.agile.devleadtool.component.listener.DownloadJirasListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.MyPanel;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class AddVersionDialog extends JFrame {

   public AddVersionDialog(Window frame, PlannerHelper helper, MyTable... tables) {
      super();
      this.setContentPane(new AddVersionPanel(this, helper, tables));
      this.pack();

      SwingUtil.centreWindowWithinWindow(this, frame);
      setVisible(true);
   }
}

class AddVersionPanel extends MyPanel {
   private ButtonGroup group;

   public AddVersionPanel(final JFrame frame, PlannerHelper helper, MyTable... tables) {
      super(new BorderLayout());
      MyPanel panel = new MyPanel(new GridBagLayout());

      MyTable table = null;
      Enumeration elements = group.getElements();
      while (elements.hasMoreElements()) {
         TableRadioButton button = (TableRadioButton) elements.nextElement();
         if (button.isSelected()) {
            table = button.getTable();
         }
      }

      if (table == null)
         return;
      
      Vector<JiraProject> projects = JiraProject.getProjects();
      SyncWithJiraActionListenerListener syncWithJiraListener = new SyncListener(table);
      JPanel bottomPanel = getFixversionSyncPanel(projects, syncWithJiraListener, helper, tables);
      add(bottomPanel, BorderLayout.CENTER);
      
      
   }
   
   private JPanel getFixversionSyncPanel(Vector<JiraProject> projects, SyncWithJiraActionListenerListener syncWithJiraListener, PlannerHelper helper, MyTable... tables) {
      JPanel panel = new JPanel(new FlowLayout());
      final JComboBox jiraProjectsCombo = new JComboBox(projects);
      final JComboBox jiraProjectFixVersionCombo = new JComboBox();

      AlteringProjectListener alteringProjectListener = new AlteringProjectListener(jiraProjectFixVersionCombo);
      RefreshingFixVersionListener refreshFixVersionListener = new RefreshingFixVersionListener(jiraProjectsCombo, jiraProjectFixVersionCombo, helper);
      DownloadJirasListener downloadJirasListener = new DownloadJirasListener(jiraProjectFixVersionCombo, helper);

      downloadJirasListener.addListener(syncWithJiraListener);
      jiraProjectsCombo.addActionListener(alteringProjectListener);

      panel.add(getRadiosPanel(tables));
      panel.add(jiraProjectsCombo);
      addButton(panel, "Refresh", refreshFixVersionListener);
      panel.add(jiraProjectFixVersionCombo);
      addButton(panel, "Get Jiras", downloadJirasListener);
      return panel;
   }

   private Component getRadiosPanel(MyTable[] tables) {
      JPanel panel = new JPanel();
      panel.add(new JLabel("Tables:"));
      panel.add(getTableRadios(tables));
      return panel;
   }

   private Component getTableRadios(MyTable... tables) {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      group = new ButtonGroup();

      for (MyTable myTable : tables) {
         TableRadioButton tableRadioButton = new TableRadioButton(myTable.getTitle(), myTable);
         group.add(tableRadioButton);
         panel.add(tableRadioButton);
      }

      return panel;
   }
   
   private final Map<JiraProject, JiraVersion[]> state = new HashMap<JiraProject, JiraVersion[]>();

   private final class SyncListener implements SyncWithJiraActionListenerListener {
      private MyTable table;

      public SyncListener(MyTable table) {
         super();
         this.table = table;
      }

      public void jiraAdded(JiraIssue jiraIssue) {
         table.addJira(jiraIssue);
      }

      public void jiraSynced(JiraIssue jiraIssue, int tableRowSynced) {
         table.syncJira(jiraIssue, tableRowSynced);
      }
   }

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
      private final PlannerHelper helper;
      private Logger log = MyLogger.getLogger(RefreshingFixVersionListener.class);

      private RefreshingFixVersionListener(JComboBox jiraProjectsCombo, JComboBox jiraFixVersionCombo, PlannerHelper helper) {
         this.projectCombo = jiraProjectsCombo;
         this.fixVersionCombo = jiraFixVersionCombo;
         this.helper = helper;
      }

      public void actionPerformed(ActionEvent e) {
         log .debug("getting fixVersion : " + projectCombo.getSelectedItem());

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
                  AlertDialog.alertException(helper.getParentFrame(), e1);
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

