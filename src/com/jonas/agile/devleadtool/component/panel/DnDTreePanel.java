package com.jonas.agile.devleadtool.component.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.agile.devleadtool.component.listener.JiraParseListenerImpl;
import com.jonas.agile.devleadtool.component.menu.MyTreePopupMenu;
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.xml.DnDTreeBuilder;
import com.jonas.agile.devleadtool.component.tree.xml.JiraSaxHandler;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParser;
import com.jonas.agile.devleadtool.component.tree.xml.XmlParserLargeMock;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.JiraProject;

public class DnDTreePanel extends JPanel {

   private Logger log = MyLogger.getLogger(DnDTreePanel.class);

   private DnDTree tree;
   private final JFrame parentFrame;

   public DnDTreePanel(DnDTree tree, DnDTreeBuilder dndTreeBuilder, JFrame parent) {
      super(new BorderLayout());
      this.tree = tree;
      this.parentFrame = parent;
      add(new JScrollPane(tree), BorderLayout.CENTER);
      add(getButtonPanel(), BorderLayout.SOUTH);
      new MyTreePopupMenu(parent, tree, dndTreeBuilder);
   }

   private Component getSouthPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(getButtonPanel(), BorderLayout.CENTER);
      panel.add(MyStatusBar.getInstance(), BorderLayout.SOUTH);
      return panel;
   }

   private Component getButtonPanel() {
      JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

      JButton uploadToJiraButton = new UploadToJiraButton("Upload to Jira", parentFrame, tree);
      JButton clearTreeButton = new ClearTreeButton("Clear Tree", this, tree);
      JButton addSprintButton = new AddSprintButton("Add Sprint", this, tree);

      panel.add(uploadToJiraButton);
      panel.add(clearTreeButton);
      panel.add(addSprintButton);
      return panel;
   }

   private final class ClearTreeButton extends JButton implements ActionListener {
      private final Component parent;
      private final DnDTree tree;

      private ClearTreeButton(String text, Component parent, DnDTree tree) {
         super(text);

         this.parent = parent;
         this.tree = tree;

         this.addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         MyStatusBar.getInstance().setMessage("Clearing tree...", true);
         tree.removeAllChildren();
      }
   }

   private final class AddSprintButton extends JButton implements ActionListener {
      private final Component parent;
      private final DnDTree tree;

      private AddSprintButton(String text, Component parent, DnDTree tree) {
         super(text);

         this.parent = parent;
         this.tree = tree;

         this.addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         Object result = JOptionPane.showInputDialog(parent, "Name the Sprint you want to add", "Adding a Sprint", JOptionPane.PLAIN_MESSAGE, null, null, null);
         if (result != null) {
            tree.getModel().createSprint((String) result);
         }
      }
   }

   private final class UploadToJiraButton extends JButton implements ActionListener {
      private final Frame parent;
      private final DnDTree tree;
      // private JiraSprintUpdater jiraSprintUpdater = new JiraSprintUpdater(ClientConstants.JIRA_URL_AOLBB);

      private UploadToJiraButton(String text, Frame parent, DnDTree tree) {
         super(text);

         this.parent = parent;
         this.tree = tree;

         this.addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         MyStatusBar.getInstance().setMessage("Uploading to Jira...", true);

         List<JiraNode> jiraNodes = tree.getJiraNodes();
         StringBuffer sb = new StringBuffer("The following Jiras had their sprints updated:");
         int noOfErrors = 0;
         try {
            List<String> jiras = new ArrayList<String>();
            MyStatusBar.getInstance().setMessage("Logging in to Jira...", false);
            for (JiraNode jiraNode : jiraNodes) {
               JiraProject project = JiraProject.getProjectByJira(jiraNode.getKey());
               try {
                  if (jiraNode.isToSync() && !jiras.contains(jiraNode.getKey())) {
                     MyStatusBar.getInstance().setMessage("Updating " + jiraNode.getKey() + "'s Sprint Details in Jira", false);
                     String sprint = jiraNode.getSprint();
                     if (DnDTreeModel.UNKNOWN_SPRINT.equals(sprint))
                        sprint = "";
                     project.getJiraClient().updateSprint(jiraNode.getKey(), sprint);
                     sb.append("\n").append(jiraNode.getKey()).append(" was moved to sprint ").append(sprint);
                     jiras.add(jiraNode.getKey());
                  }
                  jiraNode.setToSynced();
                  noOfErrors = 0;
               } catch (Throwable ex) {
                  noOfErrors++;
                  MyStatusBar.getInstance().setMessage("Failed to Upload Sprint for Jira " + jiraNode.getKey(), true);
                  AlertDialog.alertException(parent, ex);
               }
               if (noOfErrors >= 2) {
                  MyStatusBar.getInstance().setMessage("Failed to Upload more than 3 Jiras. Uploading was interrupted!", true);
                  JOptionPane.showMessageDialog(parent, "Error Happened susequent times - abandoned!", "Exception!", JOptionPane.ERROR_MESSAGE);
                  break;
               }
            }
            MyStatusBar.getInstance().setMessage("Finished uploading Jira!", true);
            tree.treeDidChange();
            JOptionPane.showMessageDialog(parent, sb.toString());
         } catch (Exception e1) {
            e1.printStackTrace();
            AlertDialog.alertException(parent, e1);
         }
      }
   }

   public void setEditable(boolean selected) {
      tree.setDnDEnabled(selected);
   }

   public DnDTree getTree() {
      return tree;
   }
}
