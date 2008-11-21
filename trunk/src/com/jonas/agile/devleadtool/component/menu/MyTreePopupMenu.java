package com.jonas.agile.devleadtool.component.menu;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.dialog.ProgressDialog;
import com.jonas.agile.devleadtool.component.listener.SprintParseListener;
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;
import com.jonas.agile.devleadtool.component.tree.xml.JiraParseListener;
import com.jonas.common.HyperLinker;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;

public class MyTreePopupMenu extends MyPopupMenu {
   private Frame parentFrame;
   private Logger log = MyLogger.getLogger(MyTreePopupMenu.class);
   private DnDTreeBuilder dndTreeBuilder;

   public MyTreePopupMenu(final JFrame parentFrame, DnDTree tree, DnDTreeBuilder dndTreeBuilder) {
      super(tree);
      this.parentFrame = parentFrame;
      this.dndTreeBuilder = dndTreeBuilder;
      add(new JMenuItem_Browse(tree, parentFrame));
      add(new JMenuItem_Sync(tree, parentFrame));
      
      dndTreeBuilder.addParseListener(new SprintParseListener(){
         ProgressDialog progressDialog;
         @Override
         public void loggingInToJiraServer() {
            progressDialog = new ProgressDialog(parentFrame, "Downloading Sprint Information", "Starting to download sprint info from Jira Server!", 0);
            progressDialog.setIndeterminate(false);
         }
         @Override
         public void accessingDataOnJiraServer() {
            progressDialog.setNote("Accessing data on Jira Server!");
         }
         @Override
         public void notifyParsingStarted() {
            progressDialog.setNote("Reading data passed from Jira Server!");
         }
         @Override
         public void notifyParsed(JiraDTO jira) {
            progressDialog.setNote("Read in data from " + jira.getKey());
         }
         @Override
         public void notifyParsingFinished() {
            progressDialog.setNote("Finished accessing the Jira Server!");
            progressDialog.setCompleteWithDelay(500); 
         }
      });
   }

   private abstract class JMenuItemAbstr extends JMenuItem implements ActionListener {
      private JMenuItemAbstr(String name) {
         super(name);
         addActionListener(this);
      }
   }

   // FIXME merge with other browse action from Table!
   private class JMenuItem_Browse extends JMenuItemAbstr {
      private final DnDTree tree;
      private Frame frame;

      public JMenuItem_Browse(DnDTree tree, Frame frame) {
         super("Open in Browser");
         this.tree = tree;
         this.frame = frame;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         int[] rows = tree.getSelectionRows();
         if (rows == null || rows.length == 0){
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

   private class JMenuItem_Sync extends JMenuItemAbstr {

      private final DnDTree tree;
      private Frame frame;

      public JMenuItem_Sync(DnDTree tree, Frame frame) {
         super("Dowload Jira Info");
         this.tree = tree;
         this.frame = frame;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         StringBuffer sb = null;
         TreePath[] paths = tree.getSelectionPaths();
         if (paths == null|| paths.length == 0) {
            AlertDialog.alertMessage(frame, "No rows selected or table empty!");
            return;
         }
         List<String> syncedSprints = new ArrayList<String>();
         List<String> failedSprints = new ArrayList<String>();
         log.debug(paths);
         if (paths.length == 1 && paths[0].getLastPathComponent().equals(tree.getModel().getRoot())) {
            log.debug("all sprints");
            downloadAllSprints();
         } else {
            log.debug("not all sprints");
            for (int i = 0; i < paths.length; i++) {
               Object selectedElement = paths[i].getLastPathComponent();
               log.debug("Path[" + i + "]: " + paths[i] + " selectedElement: " + selectedElement);
               if (selectedElement instanceof SprintNode) {
                  SprintNode sprintNode = (SprintNode) selectedElement;
                  syncedSprints.add(sprintNode.getSprintName());
                  log.debug("SprintNode: " + sprintNode);
                  tree.removeAll(sprintNode);
                  JiraProject project = JiraProject.getProjectByKey(sprintNode.getParent().toString());
                  dndTreeBuilder.buildTree(tree, sprintNode.getSprintName(), project);
               } else {
                  failedSprints.add(selectedElement.toString());
               }
            }
            sb = trawlAndDisplayMessage(syncedSprints, "The following sprints were synced:\n");
            sb = appendToSprint(sb, failedSprints, "\nThe following were seleced but are NOT sprints:\n");
         }
         if (sb != null) {
            AlertDialog.alertMessage(parentFrame, sb.toString());
         }
      }

      private void downloadAllSprints() {
         JiraProject project = JiraProject.getProjectByKey(tree.getModel().getRoot().toString());
         dndTreeBuilder.buildTree(tree, null, project);
      }

      private StringBuffer appendToSprint(StringBuffer sb, List<String> failedSprints, String string) {
         if (sb == null)
            sb = trawlAndDisplayMessage(failedSprints, string);
         else
            sb.append(trawlAndDisplayMessage(failedSprints, string));
         return sb;
      }

      private StringBuffer trawlAndDisplayMessage(List<String> syncedSprints, String str) {
         if (syncedSprints.size() > 0) {
            StringBuffer sb = new StringBuffer(str);
            for (String string : syncedSprints) {
               sb.append(string).append("\n");
            }
            return sb;
         }
         return null;
      }
   }
}
