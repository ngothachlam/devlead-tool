package com.jonas.testing.tree.fromScratch.tree.model;

import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.nodes.FixVersionNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.JiraNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.ProjectNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.SprintNode;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;

public class DnDTreeModel extends DefaultTreeModel {

   private static final String UNKNOWN_FIXVERSION = "<UnKnown FixVersion>";
   private static final String UNKNOWN_SPRINT = "<UnKnown Sprint>";

   private Logger log = MyLogger.getLogger(DnDTreeModel.class);

   private Map<String, JiraNode> jiras = new HashMap<String, JiraNode>();
   private Map<String, FixVersionNode> fixVersions = new HashMap<String, FixVersionNode>();
   private Map<String, SprintNode> sprints = new HashMap<String, SprintNode>();

   public DnDTreeModel(String rootName) {
      super(new ProjectNode(rootName));
      setAsksAllowsChildren(true);
   }

   public void removeAllChildren() {
      int noOfRootChildren = getChildCount(getRoot());
      for (int child = 0; child < noOfRootChildren; child++) {
         MutableTreeNode tempChild = (MutableTreeNode) getChild(getRoot(), child);
         removeNodeFromParent(tempChild);
      }

      jiras.clear();
      fixVersions.clear();
      sprints.clear();
   }

   FixVersionNode createFixVersion(String sprintName, String fixVersionName) {
      SprintNode parent = sprints.get(sprintName);
      if (parent == null) {
         parent = createSprint(sprintName);
      }
      FixVersionNode fixVersionNode = new FixVersionNode(fixVersionName, parent);
      insertNodeInto(fixVersionNode, parent, parent.getChildCount());
      fixVersions.put(sprintName + "@#@" + fixVersionName, fixVersionNode);
      return fixVersionNode;
   }

   void createJira(String sprintName, String fixVersionName, JiraDTO jira) {
      FixVersionNode parent = fixVersions.get(sprintName + "@#@" + fixVersionName);
      if (parent == null) {
         parent = createFixVersion(sprintName, fixVersionName);
      }
      JiraNode jiraNode = jiras.get(jira.getKey());
      if (jiraNode == null) {
         jiraNode = new JiraNode(jira.getKey(), parent, jira.getResolution(), jira.getStatus(), jira.getSummary());
         insertNodeInto(jiraNode, parent, parent.getChildCount());
         jiras.put(jira.getKey(), jiraNode);
      } else {
         TreeNode parent2 = jiraNode.getParent();
         System.out.println("blah!!");
         // FIXME not working when moving one jira to anoother fixversion and then refreshing.
         if (parent2 != parent || parent2.getParent() != parent.getParent()) {
            removeNodeFromParent(jiraNode);
            insertNodeInto(jiraNode, parent, parent.getChildCount());
            // } else{
            // log.warn("Jira " + jira + " is not added to Model as it already exists in this position!");
         }
      }
   }

   SprintNode createSprint(String sprintName) {
      ProjectNode root = (ProjectNode) getRoot();
      SprintNode sprintNode = new SprintNode(sprintName);
      insertNodeInto(sprintNode, root, root.getChildCount());
      if (!sprints.containsKey(sprintName)) {
         sprints.put(sprintName, sprintNode);
      }
      return sprintNode;
   }

   public void addJira(JiraDTO jira) {
      log.debug(jira);
      String sprint = jira.getSprint();
      if (sprint == null) {
         sprint = UNKNOWN_SPRINT;
      }
      String fixVersion = jira.getFixVersion();
      if (fixVersion == null) {
         fixVersion = UNKNOWN_FIXVERSION;
      }
      createJira(sprint, fixVersion, jira);
   }

   public void removeJira(String jira) {
      DefaultMutableTreeNode node = jiras.get(jira);
      log.debug(node);
      removeNodeFromParent(node);
      jiras.remove(jira);
   }

}
