package com.jonas.testing.tree.fromScratch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.jonas.jira.access.JiraHttpClient;
import com.jonas.jira.access.JiraSoapClient;
import com.jonas.testing.tree.fromScratch.tree.DnDTree;
import com.jonas.testing.tree.fromScratch.tree.model.DnDTreeModel;
import com.jonas.testing.tree.fromScratch.tree.nodes.JiraNode;
import com.jonas.testing.tree.fromScratch.xml.DnDTreeBuilder;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;
import com.jonas.testing.tree.fromScratch.xml.JiraParseListener;
import com.jonas.testing.tree.fromScratch.xml.JiraSaxHandler;
import com.jonas.testing.tree.fromScratch.xml.XmlParser;
import com.jonas.testing.tree.fromScratch.xml.XmlParserAtlassain;

public class DnDTreeMain extends JFrame {

   private Logger log = MyLogger.getLogger(DnDTreeMain.class);

   private DnDTree tree;
   private DnDTreeBuilder dndTreeBuilder;

   private DnDTreeMain() {
      super("DnDTree");

      try {
         DnDTreeModel model = new DnDTreeModel("LLU");
         tree = new DnDTree(model);

         JiraSaxHandler saxHandler = new JiraSaxHandler();
         // XmlParser parser = new XmlParserImpl(saxHandler);
         // XmlParser parser = new XmlParserLargeMock(saxHandler);
         XmlParser parser = new XmlParserAtlassain(saxHandler);
         dndTreeBuilder = new DnDTreeBuilder(parser);

         saxHandler.addJiraParseListener(new JiraParseListenerImpl(tree));

         makeUI();

      } catch (SAXException e) {
         e.printStackTrace();
      }
   }

   public static void main(String... args) {
      DnDTreeMain main = new DnDTreeMain();
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
      JButton uploadToJiraButton = new UploadToJiraButton("Upload to Jira", this, tree);
      JButton clearTreeButton = new ClearTreeButton("Clear Tree", this, tree);
      JButton addSprintButton = new AddSprintButton("Add Sprint", this, tree);

      panel.add(refreshButton);
      panel.add(uploadToJiraButton);
      panel.add(clearTreeButton);
      panel.add(addSprintButton);
      return panel;
   }

   private void makeUI() {
      setSize(400, 700);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
      getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);
      setVisible(true);
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
         tree.getModel().createSprint("Test");
      }
   }

   private final class UploadToJiraButton extends JButton implements ActionListener {
      private final Frame parent;
      private final DnDTree tree;

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
         JiraSoapClient soapClient = new JiraSoapClient(ClientConstants.AOLBB_WS);
         try {
            List<String> jiras = new ArrayList<String>();
            for (JiraNode jiraNode : jiraNodes) {
               try {
                  if (jiraNode.isToSync() && !jiras.contains(jiraNode.getKey())) {
                     soapClient.updateSprint(jiraNode.getKey(), jiraNode.getSprint());
                     sb.append("\n").append(jiraNode.getKey()).append(" was moved to sprint ").append(jiraNode.getSprint());
                     jiras.add(jiraNode.getKey());
                  }
                  jiraNode.setToSynced();
               } catch (Throwable ex) {
                  noOfErrors++;
                  AlertDialog.alertException(parent, ex);
               }
               if (noOfErrors >= 3) {
                  JOptionPane.showMessageDialog(parent, "Error Happened susequent times - abandoned!", "Exception!", JOptionPane.ERROR_MESSAGE);
                  break;
               }
            }
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
