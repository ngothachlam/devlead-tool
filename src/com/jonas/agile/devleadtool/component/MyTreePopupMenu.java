package com.jonas.agile.devleadtool.component;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.NotJiraException;
import com.jonas.agile.devleadtool.PlannerHelper;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.listener.OpenJirasListener;
import com.jonas.agile.devleadtool.component.table.Column;
import com.jonas.agile.devleadtool.component.table.MyTable;
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.common.HyperLinker;
import com.jonas.common.logging.MyLogger;

public class MyTreePopupMenu extends MyPopupMenu {
   private Frame parentFrame;
   private Logger log = MyLogger.getLogger(MyTreePopupMenu.class);
   private final DnDTree source;
   private DnDTreeBuilder dndTreeBuilder;

   public MyTreePopupMenu(Frame parentFrame, DnDTree source, DnDTreeBuilder dndTreeBuilder, PlannerHelper helper) {
      super(source);
      this.parentFrame = parentFrame;
      this.source = source;
      this.dndTreeBuilder = dndTreeBuilder;
      add(new JMenuItem_Sync());
      add(new JMenuItem_Browse(source));
   }

   private abstract class JMenuItemAbstr extends JMenuItem implements ActionListener {
      private JMenuItemAbstr(String name) {
         super(name);
         addActionListener(this);
      }
   }

   private class JMenuItem_Browse extends JMenuItemAbstr {
      private final DnDTree tree;

      public JMenuItem_Browse(DnDTree tree) {
         super("Browse Selected");
         this.tree = tree;
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         int[] rows = table.getSelectedRows();
         if (rows.length == 0)
            AlertDialog.alertMessage(helper.getParentFrame(), "No rows selected!");
         StringBuffer sb = new StringBuffer();
         for (int j = 0; j < rows.length; j++) {
            String jira = (String) table.getValueAt(Column.Jira, rows[j]);
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
         if (sb.length() > 0) {
            sb.append(" are incorrect!");
            AlertDialog.alertMessage(helper.getParentFrame(), sb.toString());
         }
         
      }
   }
   
   private class JMenuItem_Sync extends JMenuItemAbstr {

      public JMenuItem_Sync() {
         super("Sync Selected");
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         TreePath[] paths = source.getSelectionPaths();
         List<String> syncedSprints = new ArrayList<String>();
         List<String> failedSprints = new ArrayList<String>();
         for (int i = 0; i < paths.length; i++) {
            Object selectedElement = paths[i].getLastPathComponent();
            log.debug("Path[" + i + "]: " + paths[i]);
            if (selectedElement instanceof SprintNode) {
               SprintNode sprintNode = (SprintNode) selectedElement;
               syncedSprints.add(sprintNode.getSprintName());
               log.debug("SprintNode: " + sprintNode);
               sprintNode.removeAllChildren();
               dndTreeBuilder.buildTree(source, sprintNode.getSprintName());
            } else {
               failedSprints.add(selectedElement.toString());
            }
         }
         StringBuffer sb = trawlAndDisplayMessage(syncedSprints, "The following sprints were synced: ");
         sb = appendToSprint(failedSprints, sb, "\nThe following were seleced but are NOT sprints: ");
         if (sb != null)
            AlertDialog.alertMessage(parentFrame, sb.toString());
      }

      private StringBuffer appendToSprint(List<String> failedSprints, StringBuffer sb, String string) {
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
