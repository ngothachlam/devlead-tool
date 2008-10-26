package com.jonas.testing.tree.fromScratch.tree;

import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;

public class DnDTreeModel extends DefaultTreeModel {

   private Logger log = MyLogger.getLogger(DnDTreeModel.class);

   private Map<String, DefaultMutableTreeNode> fixVersions = new HashMap<String, DefaultMutableTreeNode>();
   private Map<String, DefaultMutableTreeNode> jiras = new HashMap<String, DefaultMutableTreeNode>();
   private Map<String, DefaultMutableTreeNode> sprints = new HashMap<String, DefaultMutableTreeNode>();

   public DnDTreeModel(String rootName) {
      super(new DefaultMutableTreeNode(rootName));
   }

   public void removeAllChildren() {
      int noOfRootChildren = getChildCount(root);
      for (int child = 0; child < noOfRootChildren; child++) {
         MutableTreeNode tempChild = (MutableTreeNode) getChild(root, child);
         removeNodeFromParent(tempChild);
      }

      jiras.clear();
      fixVersions.clear();
      sprints.clear();
   }

   DefaultMutableTreeNode createFixVersion(String sprintName, String fixVersionName) {
      DefaultMutableTreeNode fixVersionNode = new DefaultMutableTreeNode(fixVersionName);
      MutableTreeNode parent = sprints.get(sprintName);
      if (parent == null) {
         parent = createSprint(sprintName);
      }
      insertNodeInto(fixVersionNode, parent, parent.getChildCount());
      fixVersions.put(fixVersionName, fixVersionNode);
      return fixVersionNode;
   }

   DefaultMutableTreeNode createJira(String sprintName, String fixVersionName, String jira) {
      DefaultMutableTreeNode jiraNode = new DefaultMutableTreeNode(jira);
      MutableTreeNode parent = fixVersions.get(fixVersionName);
      if (parent == null) {
         parent = createFixVersion(sprintName, fixVersionName);
      }
      if (jiras.get(jira) == null) {
         insertNodeInto(jiraNode, parent, parent.getChildCount());
         jiras.put(jira, jiraNode);
      } else {
         log.warn("Jira " + jira + " is not added to Model as it already exists!");
      }
      return jiraNode;
   }

   DefaultMutableTreeNode createSprint(String sprintName) {
      DefaultMutableTreeNode sprintNode = new DefaultMutableTreeNode(sprintName);
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
      insertNodeInto(sprintNode, root, root.getChildCount());
      sprints.put(sprintName, sprintNode);
      return sprintNode;
   }

   public void createJira(JiraDTO jira) {
      createJira(jira.getSprint(), jira.getFixVersion(), jira.getKey());
   }

}
