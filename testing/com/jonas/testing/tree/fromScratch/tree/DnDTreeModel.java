package com.jonas.testing.tree.fromScratch.tree;

import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.nodes.FixVersionNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.JiraNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.SprintNode;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;

public class DnDTreeModel extends DefaultTreeModel {

   private Logger log = MyLogger.getLogger(DnDTreeModel.class);

   private Map<String, JiraNode> jiras = new HashMap<String, JiraNode>();
   private Map<String, FixVersionNode> fixVersions = new HashMap<String, FixVersionNode>();
   private Map<String, SprintNode> sprints = new HashMap<String, SprintNode>();

   public DnDTreeModel(String rootName) {
      super(new DefaultMutableTreeNode(rootName));
      setAsksAllowsChildren(true);
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

   FixVersionNode createFixVersion(String sprintName, String fixVersionName) {
      FixVersionNode fixVersionNode = new FixVersionNode(fixVersionName);
      SprintNode parent = sprints.get(sprintName);
      if (parent == null) {
         parent = createSprint(sprintName);
      }
       insertNodeInto(fixVersionNode, parent, parent.getChildCount());
      fixVersions.put(sprintName + "@#@" + fixVersionName, fixVersionNode);
      return fixVersionNode;
   }

   void createJira(String sprintName, String fixVersionName, String jira) {
      JiraNode jiraNode = new JiraNode(jira);
      FixVersionNode parent = fixVersions.get(sprintName + "@#@" + fixVersionName);
      if (parent == null) {
         parent = createFixVersion(sprintName, fixVersionName);
      }
      if (!jiras.containsKey(jira)) {
         insertNodeInto(jiraNode, parent, parent.getChildCount());
         log.debug("Adding jira " + jira);
         jiras.put(jira, jiraNode);
      } else {
         log.warn("Jira " + jira + " is not added to Model as it already exists!");
      }
   }

   SprintNode createSprint(String sprintName) {
      SprintNode sprintNode = new SprintNode(sprintName);
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
      insertNodeInto(sprintNode, root, root.getChildCount());
      if (!sprints.containsKey(sprintName))
         sprints.put(sprintName, sprintNode);
      return sprintNode;
   }

   public void addJira(JiraDTO jira) {
      log.debug(jira);
      createJira(jira.getSprint(), jira.getFixVersion(), jira.getKey());
   }

   public void removeJira(String jira) {
      DefaultMutableTreeNode node = jiras.get(jira);
      log.debug(node);
      removeNodeFromParent(node);
      jiras.remove(jira);
   }

}
