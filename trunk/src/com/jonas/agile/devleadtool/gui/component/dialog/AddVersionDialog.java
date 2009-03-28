package com.jonas.agile.devleadtool.gui.component.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.TableRadioButton;
import com.jonas.agile.devleadtool.component.listener.DownloadJiraListener;
import com.jonas.agile.devleadtool.component.listener.SyncWithJiraActionListenerListener;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.common.logging.MyLogger;
import com.jonas.common.swing.MyPanel;
import com.jonas.common.swing.SwingUtil;
import com.jonas.jira.JiraIssue;
import com.jonas.jira.JiraProject;
import com.jonas.jira.JiraVersion;

public class AddVersionDialog extends JFrame {

   public AddVersionDialog(Window frame, MyTable[] tables) {
      super();
      this.setContentPane(new AddVersionPanel(this, tables));
      this.pack();

      SwingUtil.centreWindowWithinWindow(this, frame);
      setVisible(true);
   }
}


class AddVersionPanel extends MyPanel {
   private final ButtonGroup group = new ButtonGroup();
   private final JFrame frame;

   public AddVersionPanel(final JFrame frame, MyTable... tables) {
      super(new BorderLayout());
      this.frame = frame;
      Vector<JiraProject> projects = JiraProject.getProjects();
      JPanel bottomPanel = getFixversionSyncPanel(projects, tables);
      add(bottomPanel, BorderLayout.CENTER);
   }

   private JPanel getFixversionSyncPanel(Vector<JiraProject> projects, MyTable[] tables) {
      JPanel panel = new JPanel(new GridLayout(3, 1, 3, 3));
      final JComboBox jiraProjectsCombo = new JComboBox(projects);
      final JComboBox jiraProjectFixVersionCombo = new JComboBox();

      final HashMap<JiraProject, JiraVersion[]> state = new HashMap<JiraProject, JiraVersion[]>();
      RefreshVersionListener refreshFixVersionListener = new RefreshVersionListener(jiraProjectsCombo, jiraProjectFixVersionCombo, state, frame);
      DownloadJiraListener downloadJirasListener = new DownloadJiraListener(jiraProjectFixVersionCombo, frame);

      panel.add(getSubPanel("Target Table", getTableRadios(tables)));
      panel.add(getSubPanel("Source Projects", jiraProjectsCombo, getButton("Refresh", refreshFixVersionListener)));
      panel.add(getSubPanel("Source FixVersion", jiraProjectFixVersionCombo, getButton("Get Jiras", downloadJirasListener)));

      AlterProjectListener alteringProjectListener = new AlterProjectListener(jiraProjectFixVersionCombo, state);
      SyncWithJiraActionListenerListener syncWithJiraListener = new SyncListener(group);
      downloadJirasListener.addListener(syncWithJiraListener);
      jiraProjectsCombo.addActionListener(alteringProjectListener);

      return panel;
   }

   private JPanel getSubPanel(String title, Component... components) {
      JPanel panel = new JPanel(new GridLayout(components.length, 1, 5, 5));
      for (Component component : components) {
         panel.add(component);
      }
      panel.setBorder(BorderFactory.createTitledBorder(title));
      return panel;
   }

   private Component getTableRadios(MyTable[] tables) {
      JPanel panel = new JPanel(new GridLayout(tables.length, 1, 1, 1));
      for (MyTable myTable : tables) {
         TableRadioButton tableRadioButton = new TableRadioButton(myTable.getTitle(), myTable);
         group.add(tableRadioButton);
         panel.add(tableRadioButton);
      }
      return panel;
   }

   private class AlterProjectListener implements ActionListener {
      private Logger log = MyLogger.getLogger(AlterProjectListener.class);
      private final Map<JiraProject, JiraVersion[]> state;
      private final JComboBox fixVersionCombo;

      private AlterProjectListener(JComboBox jiraProjectFixVersionCombo, Map<JiraProject, JiraVersion[]> state) {
         this.fixVersionCombo = jiraProjectFixVersionCombo;
         this.state = state;
      }

      public void actionPerformed(ActionEvent e) {
         fixVersionCombo.setEditable(false);
         fixVersionCombo.removeAllItems();
         JComboBox projectsCombo = ((JComboBox) e.getSource());
         JiraVersion[] fixVersions = state.get(projectsCombo.getSelectedItem());
         log.debug("changed project to " + projectsCombo.getSelectedItem());
         if (fixVersions != null) {
            for (JiraVersion jiraVersion : fixVersions) {
               log.debug("\tRetrieving from store: " + jiraVersion);
               fixVersionCombo.addItem(jiraVersion);
            }
         }
      }
   }

   private final class RefreshVersionListener implements ActionListener {
      private final Logger log = MyLogger.getLogger(RefreshVersionListener.class);
      private final Map<JiraProject, JiraVersion[]> state;
      private JFrame parentFrame;
      private final JComboBox fixVersionCombo;
      private final JComboBox projectCombo;

      private RefreshVersionListener(JComboBox jiraProjectsCombo, JComboBox jiraFixVersionCombo, Map<JiraProject, JiraVersion[]> state,
            JFrame parentFrame) {
         this.projectCombo = jiraProjectsCombo;
         this.fixVersionCombo = jiraFixVersionCombo;
         this.state = state;
         this.parentFrame = parentFrame;
      }

      public void actionPerformed(ActionEvent e) {
         log.debug("getting fixVersion : " + projectCombo.getSelectedItem());

         fixVersionCombo.removeAllItems();
         final Object[] selectedObjects = projectCombo.getSelectedObjects();
         final ProgressDialog dialog = new ProgressDialog(parentFrame, "Refreshing Fix Versions...", "Refreshing Fix Versions...",
               selectedObjects.length, true);
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
                  AlertDialog.alertException(parentFrame, e1);
               }
               return null;
            }

            @Override
            public void done() {
               // store the fix versions
               JiraProject project = (JiraProject) projectCombo.getSelectedItem();
               state.remove(project);
               state.put(project, getFixVersionsInCombo());

               dialog.setCompleteWithDelay(300);
            }
         };
         worker.execute();
      }

      private JiraVersion[] getFixVersionsInCombo() {
         JiraVersion[] items = new JiraVersion[fixVersionCombo.getItemCount()];
         for (int i = 0; i < items.length; i++) {
            JiraVersion item = (JiraVersion) fixVersionCombo.getItemAt(i);
            log.debug("\tStoring " + item);
            items[i] = item;
         }
         return items;
      }

      private final class VersionComparator implements Comparator<JiraVersion> {
         @Override
         public int compare(JiraVersion o1, JiraVersion o2) {
            String name1 = o1.getName();
            String name2 = o2.getName();
            return name1.compareToIgnoreCase(name2);
         }
      }
   }

   private final class SyncListener implements SyncWithJiraActionListenerListener {
      private final Logger log = MyLogger.getLogger(SyncListener.class);
      private final ButtonGroup group;

      public SyncListener(ButtonGroup group) {
         super();
         this.group = group;
      }

      public void jiraAdded(JiraIssue jiraIssue) {
         MyTable table = getSelectedTable();
         log.debug("Adding jira to table " + jiraIssue.getKey());
         if (table != null)
            table.addJira(jiraIssue);
      }

      public void jiraSynced(JiraIssue jiraIssue, int tableRowSynced) {
         MyTable table = getSelectedTable();
         log.debug("Syncing jira to table " + jiraIssue.getKey());
         if (table != null)
            table.syncJira(jiraIssue, tableRowSynced);
      }

      private MyTable getSelectedTable() {
         Enumeration<AbstractButton> elements = group.getElements();
         while (elements.hasMoreElements()) {
            TableRadioButton button = (TableRadioButton) elements.nextElement();
            if (button.isSelected()) {
               return button.getTable();
            }
         }
         return null;
      }
   }
}
