package com.jonas.testing.tree.fromScratch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.agile.devleadtool.MyStatusBar;
import com.jonas.agile.devleadtool.component.dialog.AlertDialog;
import com.jonas.common.logging.MyLogger;
import com.jonas.jira.access.ClientConstants;
import com.jonas.jira.access.JiraSprintUpdater;
import com.jonas.testing.tree.fromScratch.tree.DnDTree;
import com.jonas.testing.tree.fromScratch.tree.model.DnDTreeModel;
import com.jonas.testing.tree.fromScratch.tree.nodes.JiraNode;
import com.jonas.testing.tree.fromScratch.xml.DnDTreeBuilder;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;
import com.jonas.testing.tree.fromScratch.xml.JiraParseListener;
import com.jonas.testing.tree.fromScratch.xml.JiraSaxHandler;
import com.jonas.testing.tree.fromScratch.xml.XmlParser;
import com.jonas.testing.tree.fromScratch.xml.XmlParserImpl;

public class DnDTreeMain extends JPanel {

   private Logger log = MyLogger.getLogger(DnDTreeMain.class);

   private DnDTree tree;
   private DnDTreeBuilder dndTreeBuilder;

   private final JFrame parentFrame;

   private DnDTreeMain(DnDTree tree, DnDTreeBuilder dndTreeBuilder, JFrame parent) {
      super(new BorderLayout());
      this.tree = tree;
      this.dndTreeBuilder = dndTreeBuilder;
      this.parentFrame = parent;
      add(new JScrollPane(tree), BorderLayout.CENTER);
      add(getButtonPanel(), BorderLayout.SOUTH);
   }

   public static void main(String... args) {
      try {
         JFrame frame = new JFrame();
         DnDTreeModel model = new DnDTreeModel("LLU");
         DnDTree tree = new DnDTree(model);
         JiraSaxHandler saxHandler = new JiraSaxHandler();
         saxHandler.addJiraParseListener(new JiraParseListenerImpl(tree));

         XmlParser parser = new XmlParserImpl(saxHandler);
         // XmlParser parser = new XmlParserLargeMock(saxHandler);
         // XmlParser parser = new XmlParserAtlassain(saxHandler);

         DnDTreeBuilder dndTreeBuilder = new DnDTreeBuilder(parser);

         DnDTreeMain main = new DnDTreeMain(tree, dndTreeBuilder, frame);

         frame.setSize(400, 700);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         frame.getContentPane().add(main, BorderLayout.CENTER);
         frame.getContentPane().add(MyStatusBar.getInstance(), BorderLayout.SOUTH);
         frame.setVisible(true);
      } catch (SAXException e) {
         e.printStackTrace();
      }
   }

   private Component getSouthPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(getButtonPanel(), BorderLayout.CENTER);
      panel.add(MyStatusBar.getInstance(), BorderLayout.SOUTH);
      return panel;
   }

   private Component getButtonPanel() {
      JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

      JButton refreshButton = new RefreshButton("Download from Jira", this, dndTreeBuilder, tree);
      JButton uploadToJiraButton = new UploadToJiraButton("Upload to Jira", parentFrame, tree);
      JButton clearTreeButton = new ClearTreeButton("Clear Tree", this, tree);
      JButton addSprintButton = new AddSprintButton("Add Sprint", this, tree);

      panel.add(refreshButton);
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
      private JiraSprintUpdater jiraSprintUpdater = new JiraSprintUpdater(ClientConstants.JIRA_URL_AOLBB);

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
            jiraSprintUpdater.loginToJira();
            for (JiraNode jiraNode : jiraNodes) {
               try {
                  if (jiraNode.isToSync() && !jiras.contains(jiraNode.getKey())) {
                     MyStatusBar.getInstance().setMessage("Updating " + jiraNode.getKey() + "'s Sprint Details in Jira", false);
                     jiraSprintUpdater.updateSprint(jiraNode.getKey(), jiraNode.getId(), jiraNode.getSprint());
                     sb.append("\n").append(jiraNode.getKey()).append(" was moved to sprint ").append(jiraNode.getSprint());
                     jiras.add(jiraNode.getKey());
                  }
                  jiraNode.setToSynced();
               } catch (Throwable ex) {
                  noOfErrors++;
                  MyStatusBar.getInstance().setMessage("Failed to Upload Sprint for Jira " + jiraNode.getKey(), true);
                  AlertDialog.alertException(parent, ex);
               }
               if (noOfErrors >= 3) {
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

   private final class RefreshButton extends JButton implements ActionListener {
      private final DnDTreeBuilder dndTreeBuilder;
      private final Component parent;
      private final DnDTree tree;

      private RefreshButton(String text, Component parent, DnDTreeBuilder dnDTreeBuilder, DnDTree tree) {
         super(text);

         this.parent = parent;
         this.dndTreeBuilder = dnDTreeBuilder;
         this.tree = tree;

         this.addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         log.debug("Refresh Button Pressed!");
         MyStatusBar.getInstance().setMessage("Refreshing...", false);
         dndTreeBuilder.buildTree(tree);
      }
   }
}


class JiraParseListenerImpl implements JiraParseListener {

   private Logger log = MyLogger.getLogger(JiraParseListenerImpl.class);

   private DnDTree tree;
   private int count;

   public JiraParseListenerImpl(DnDTree tree) {
      super();
      this.tree = tree;
   }

   @Override
   public void notifyParsed(final JiraDTO jira) {
      runInEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            StringBuffer sb = new StringBuffer("Building Table... (");
            sb.append(++count);
            sb.append(")");
            MyStatusBar.getInstance().setMessage(sb.toString(), false);
            tree.createJira(jira);
         }
      });
   }

   @Override
   public void notifyParsingFinished() {
      runInEventDispatchThread(new Runnable() {
         @Override
         public void run() {
            StringBuffer sb = new StringBuffer("Jiras Loaded: ");
            sb.append(++count);
            MyStatusBar.getInstance().setMessage(sb.toString(), true);
         }
      });
   }

   @Override
   public void notifyParsingStarted() {
      count = 0;
   }

   private void runInEventDispatchThread(Runnable runnableInEventDispatchThread) {
      if (SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(runnableInEventDispatchThread);
         } catch (InterruptedException e) {
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         }
      } else {
         runnableInEventDispatchThread.run();
      }
   }
}
