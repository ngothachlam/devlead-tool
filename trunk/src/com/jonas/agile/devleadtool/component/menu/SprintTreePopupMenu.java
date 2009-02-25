package com.jonas.agile.devleadtool.component.menu;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.panel.SprintInfoPanel;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.tree.SprintTree;
import com.jonas.agile.devleadtool.component.tree.nodes.FixVersionNode;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;
import com.jonas.agile.devleadtool.component.tree.xml.JiraParseListener;
import com.jonas.common.HyperLinker;
import com.jonas.common.SwingUtil;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;

public class SprintTreePopupMenu extends MyPopupMenu {
   private Frame parentFrame;
   private Logger log = MyLogger.getLogger(SprintTreePopupMenu.class);

   public SprintTreePopupMenu(final JFrame parentFrame, SprintTree tree, DnDTreeBuilder dndTreeBuilder) {
      super(tree);
      this.parentFrame = parentFrame;
      add(new JMenuItem_Browse(tree, parentFrame));
      add(new JMenuItem_Sync(tree, parentFrame, dndTreeBuilder));
      add(new Separator());
      add(new JMenuItem_SprintInfo("Sprint Info", tree));
      add(new Separator());
      add(new JMenuItem_Expand("Expand", tree));
   }

   public SprintTreePopupMenu(final JFrame parentFrame, SprintTree tree, DnDTreeBuilder dndTreeBuilder, MyTable jiraTable, MyTable boardTable) {
      this(parentFrame, tree, dndTreeBuilder);
      add(new Separator());
      add(new JMenuItem_Copy("Copy to Board Table", tree, boardTable));
      add(new JMenuItem_Copy("Copy to Jira Table", tree, jiraTable));
   }

   private class JMenuItem_Expand extends JMenuItem implements ActionListener {
      private final SprintTree tree;

      private JMenuItem_Expand(String name, SprintTree tree) {
         super(name);
         this.tree = tree;
         addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         int[] selectionPaths = tree.getSelectionRows();
         for (int i = 0; i < selectionPaths.length; i++) {
            int row = selectionPaths[i];
            tree.expandRow(row);
            int depth = tree.getPathForRow(row).getPathCount();
            log.debug("depth: " + depth + " row: " + row);
            while (++row < tree.getRowCount() && tree.getPathForRow(row).getPathCount() > depth) {
               log.debug("* depth: " + tree.getPathForRow(row).getPathCount() + " row: " + row);
               // if (tree.getPathForRow(row).getPathCount() == depth + 1) {
               if (tree.getPathForRow(row).getPathCount() == depth + 1) {
                  log.debug(" * updated");
                  tree.expandRow(row);
               }
            }
         }

      }

   }

   private class JMenuItem_SprintInfo extends JMenuItemAbstr {
      private final SprintTree tree;

      private JMenuItem_SprintInfo(String name, SprintTree tree) {
         super(name);
         this.tree = tree;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         TreePath[] paths = tree.getSelectionPaths();
         if (paths == null || paths.length == 0)
            return;
         for (TreePath treePath : paths) {
            SprintInfoPanel panel = new SprintInfoPanel();

            Object component = treePath.getLastPathComponent();
            panel.calculateInfo(component);

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setResizable(false);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.pack();
            SwingUtil.centreWindow(frame);
            frame.setVisible(true);
         }
      }
   }

   private abstract class JMenuItemAbstr extends JMenuItem implements ActionListener {
      private JMenuItemAbstr(String name) {
         super(name);
         addActionListener(this);
      }
   }

   // FIXME merge with other browse action from Table!
   private class JMenuItem_Browse extends JMenuItemAbstr {
      private final SprintTree tree;
      private Frame frame;

      public JMenuItem_Browse(SprintTree tree, Frame frame) {
         super("Open in Browser");
         this.tree = tree;
         this.frame = frame;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         int[] rows = tree.getSelectionRows();
         if (rows == null || rows.length == 0) {
            AlertDialog.alertMessage(frame, "No rows selected or table empty!");
            return;
         }

         StringBuffer sb = new StringBuffer();
         for (int j = 0; j < rows.length; j++) {
            TreePath jiraPath = tree.getPathForRow(rows[j]);
            if (jiraPath.getLastPathComponent() instanceof JiraNode) {
               JiraNode jiraNode = (JiraNode) jiraPath.getLastPathComponent();
               String jira = jiraNode.getKey();
               String jira_url = null;
               boolean error = false;
               try {
                  jira_url = PlannerHelper.getJiraUrl(jira);
               } catch (NotJiraException e1) {
                  if (sb.length() > 0) {
                     sb.append(", ");
                  }
                  sb.append(jira);
                  error = true;
               }
               if (!error) {
                  try {
                     HyperLinker.displayURL(jira_url + "/browse/" + jira);
                  } catch (URISyntaxException e1) {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
                  } catch (IOException e1) {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
                  }
               }
            }
         }
         if (sb.length() > 0) {
            sb.append(" are incorrect!");
            AlertDialog.alertMessage(frame, sb.toString());
         }

      }
   }

   private class JMenuItem_Copy extends JMenuItemAbstr {
      private final SprintTree tree;
      private MyTable table;

      public JMenuItem_Copy(String string, SprintTree tree, MyTable table) {
         super(string);
         this.tree = tree;
         this.table = table;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         TreePath[] selectedPaths = tree.getSelectionPaths();
         for (TreePath treePath : selectedPaths) {
            Object selectedTreeNode = treePath.getLastPathComponent();
            addNodeToTable(selectedTreeNode);
         }
      }

      private void addNodeToTable(Object selectedTreeNode) {
         if (selectedTreeNode instanceof JiraNode) {
            addJiraToTable(selectedTreeNode);
         } else if (selectedTreeNode instanceof SprintNode) {
            addSprintToTable(selectedTreeNode);
         } else if (selectedTreeNode instanceof FixVersionNode) {
            addFixToTable(selectedTreeNode);
         }
      }

      private void addSprintToTable(Object selectedTreeNode) {
         SprintNode sprintNode = (SprintNode) selectedTreeNode;
         for (int i = 0; i < sprintNode.getChildCount(); i++) {
            addNodeToTable(sprintNode.getChildAt(i));
         }
      }

      private void addFixToTable(Object selectedTreeNode) {
         FixVersionNode fixNode = (FixVersionNode) selectedTreeNode;
         for (int i = 0; i < fixNode.getChildCount(); i++) {
            addNodeToTable(fixNode.getChildAt(i));
         }
      }

      private void addJiraToTable(Object selectedTreeNode) {
         JiraNode jiraNode = (JiraNode) selectedTreeNode;
         table.addJira(jiraNode.getKey());
      }
   }

   private class JMenuItem_Sync extends JMenuItemAbstr {

      private final SprintTree tree;
      private Frame frame;
      private final DnDTreeBuilder dndTreeBuilder;

      public JMenuItem_Sync(SprintTree tree, Frame frame, DnDTreeBuilder dndTreeBuilder) {
         super("Dowload Jira Info");
         this.tree = tree;
         this.frame = frame;
         this.dndTreeBuilder = dndTreeBuilder;
      }

      @Override
      public void actionPerformed(ActionEvent e) {

         final SprintLogger sprintLogger = new SprintLogger();
         TreePath[] paths = tree.getSelectionPaths();
         if (paths == null || paths.length == 0) {
            AlertDialog.alertMessage(frame, "No rows selected or table empty!");
            return;
         }
         final ProgressDialog progressDialog = addDnDTreeParseListener(paths.length);

         log.debug(paths);
         if (paths.length == 1 && paths[0].getLastPathComponent().equals(tree.getModel().getRoot())) {
            log.debug("all sprints");
            final JiraProject project = JiraProject.getProjectByKey(tree.getModel().getRoot().toString());

            SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
               @Override
               protected Object doInBackground() throws Exception {
                  dndTreeBuilder.buildTree(null, project);
                  return null;
               }
            };
            worker.addPropertyChangeListener(new PropertyChangeListener() {
               @Override
               public void propertyChange(PropertyChangeEvent event) {
                  if ("state".equals(event.getPropertyName()) && SwingWorker.StateValue.DONE == event.getNewValue()) {
                     progressDialog.setCompleteWithDelay(500);
                     sprintLogger.displayAlert();
                  }
               }
            });
            worker.execute();
         } else {
            log.debug("not all sprints");
            for (int i = 0; i < paths.length; i++) {
               Object selectedElement = paths[i].getLastPathComponent();
               log.debug("Path[" + i + "]: " + paths[i] + " selectedElement: " + selectedElement);
               if (selectedElement instanceof SprintNode) {
                  final SprintNode sprintNode = (SprintNode) selectedElement;
                  final JiraProject project = JiraProject.getProjectByKey(sprintNode.getParent().toString());
                  log.debug("SprintNode: " + sprintNode);
                  tree.removeAllChildren(sprintNode);
                  sprintLogger.addSyncedSprint(sprintNode.getSprintName(), project);
               } else {
                  sprintLogger.addFailedSprint(selectedElement.toString());
               }
            }
            SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
               @Override
               protected Object doInBackground() throws Exception {
                  List<SprintToSyncDTO> sprints = sprintLogger.getSprintsToSync();
                  dndTreeBuilder.login();
                  for (SprintToSyncDTO sprint : sprints) {
                     dndTreeBuilder.buildTree(sprint.getSprint(), sprint.getProject());
                  }
                  return null;
               }
            };
            worker.addPropertyChangeListener(new PropertyChangeListener() {
               @Override
               public void propertyChange(PropertyChangeEvent event) {
                  if ("state".equals(event.getPropertyName()) && SwingWorker.StateValue.DONE == event.getNewValue()) {
                     progressDialog.setCompleteWithDelay(500);
                     sprintLogger.displayAlert();
                  }
               }
            });
            worker.execute();
         }
      }

      private ProgressDialog addDnDTreeParseListener(int progress) {
         final ProgressDialog progressDialog = new ProgressDialog(parentFrame, "Downloading Sprint Information",
               "Starting to download sprint info from Jira Server!", progress, true);
         progressDialog.setIndeterminate(false);
         this.dndTreeBuilder.addParseListener(new JiraParseListener() {

            @Override
            public void notifyParsingStarted() {
               progressDialog.increseProgress("Reading sprint data passed from Jira Server!");
            }

            @Override
            public void notifyParsed(JiraDTO jira) {
            }

            @Override
            public void notifyParsingFinished() {
            }
         });

         return progressDialog;
      }

      private class SprintLogger {
         List<SprintToSyncDTO> sprintsToSync = new ArrayList<SprintToSyncDTO>();

         public void addSyncedSprint(String sprintName, JiraProject project) {
            sprintsToSync.add(new SprintToSyncDTO(sprintName, project));
         }

         public List<SprintToSyncDTO> getSprintsToSync() {
            return sprintsToSync;
         }

         public void displayAlert() {
         }

         public void addFailedSprint(String string) {
         }

      }

      private class SprintToSyncDTO {

         private final String sprint;
         private final JiraProject project;

         public SprintToSyncDTO(String sprint, JiraProject project) {
            this.sprint = sprint;
            this.project = project;
         }

         public JiraProject getProject() {
            return project;
         }

         public String getSprint() {
            return sprint;
         }

      }

      private void downloadAllSprints() {
         JiraProject project = JiraProject.getProjectByKey(tree.getModel().getRoot().toString());
         dndTreeBuilder.buildTree(null, project);
      }
   }
}
