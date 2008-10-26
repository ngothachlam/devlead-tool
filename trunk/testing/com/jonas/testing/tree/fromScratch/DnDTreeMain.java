package com.jonas.testing.tree.fromScratch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.xml.DnDTreeBuilder;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;
import com.jonas.testing.tree.fromScratch.xml.JiraParseListener;
import com.jonas.testing.tree.fromScratch.xml.JiraSaxHandler;
import com.jonas.testing.tree.fromScratch.xml.XmlParser;
import com.jonas.testing.tree.fromScratch.xml.XmlParserMock;

public class DnDTreeMain extends JFrame {

   private DnDTreeBuilder dndTreeBuilder;
   private DnDTree tree;


   private DnDTreeMain() {
      super("DnDTree");
      
      try {
         tree = new DnDTree("LLU");
         
         JiraSaxHandler saxHandler = new JiraSaxHandler();
         XmlParser parser = new XmlParserMock(saxHandler);
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

   private Component getButtonPanel() {
      JPanel panel = new JPanel();
      
      JButton refreshButton = new RefreshButton("Refresh", this, dndTreeBuilder, tree);
      panel.add(refreshButton);
      return panel;
   }

   private void makeUI() {
      setSize(300, 200);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
      getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
      setVisible(true);
   }

   
   private final class RefreshButton extends JButton implements ActionListener {
      private final Component parent;
      private final DnDTreeBuilder dndTreeBuilder;
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
         dndTreeBuilder.buildTree(tree);
         JOptionPane.showMessageDialog(parent, "Tree is ok!" + tree.getSelectionPath());
      }
   }
}

class JiraParseListenerImpl implements JiraParseListener {
   
   private Logger log = MyLogger.getLogger(JiraParseListenerImpl.class);
   
   private Map<String, DefaultMutableTreeNode> fixVersions = new HashMap<String, DefaultMutableTreeNode>();
   private Map<String, DefaultMutableTreeNode> jiras = new HashMap<String, DefaultMutableTreeNode>();
   private Map<String, DefaultMutableTreeNode> sprints = new HashMap<String, DefaultMutableTreeNode>();
   private DnDTree tree;
   
   public JiraParseListenerImpl(DnDTree tree) {
      super();
      this.tree = tree;
   }

   @Override
   public void notifyParsed(JiraDTO jira) {
      log.debug("Notify Parsed! " + jira.getKey());
      createJira(tree.getModel(), jira.getSprint(), jira.getFixVersion(), jira.getKey());
   }

   @Override
   public void notifyParsingFinished() {
      tree.getModel().reload();
   }

   @Override
   public void notifyParsingStarted() {
      int noOfRootChildren = tree.getModel().getChildCount(tree.getModel().getRoot());
      for (int child = 0; child < noOfRootChildren; child++) {
         tree.getModel().removeNodeFromParent( (MutableTreeNode) tree.getModel().getChild(tree.getModel().getRoot(), child) );
      }
      jiras.clear();
      fixVersions.clear();
      sprints.clear();
   }
   
   public DefaultMutableTreeNode createFixVersion(DefaultTreeModel model, String sprintName, String fixVersionName) {
      DefaultMutableTreeNode fixVersionNode = new DefaultMutableTreeNode(fixVersionName);
      MutableTreeNode parent = sprints.get(sprintName);
      if (parent == null) {
         parent = createSprint(model, sprintName);
      }
      model.insertNodeInto(fixVersionNode, parent, parent.getChildCount());
      fixVersions.put(fixVersionName, fixVersionNode);
      return fixVersionNode;
   }

   public DefaultMutableTreeNode createJira(DefaultTreeModel model, String sprintName, String fixVersionName, String jira) {
      DefaultMutableTreeNode jiraNode = new DefaultMutableTreeNode(jira);
      MutableTreeNode parent = fixVersions.get(fixVersionName);
      if (parent == null) {
         parent = createFixVersion(model, sprintName, fixVersionName);
      }
      if (jiras.get(jira) == null) {
         model.insertNodeInto(jiraNode, parent, parent.getChildCount());
         jiras.put(jira, jiraNode);
      } else {
         log.warn("Jira " + jira + " is not added to Model as it already exists!");
      }
      return jiraNode;
   }
   
   public DefaultMutableTreeNode createSprint(DefaultTreeModel model, String sprintName) {
      DefaultMutableTreeNode sprintNode = new DefaultMutableTreeNode(sprintName);
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
      model.insertNodeInto(sprintNode, root, root.getChildCount());
      sprints.put(sprintName, sprintNode);
      return sprintNode;
   }
}
