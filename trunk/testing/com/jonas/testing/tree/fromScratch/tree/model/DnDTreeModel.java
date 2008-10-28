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

   private static final String SEPARATOR = "@#@";
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
      String fixVersionListName = getSeparatedName(sprintName, fixVersionName);
      fixVersions.put(fixVersionListName, fixVersionNode);
      return fixVersionNode;
   }

   void createJira(String sprintName, String fixVersionName, JiraDTO jira) {
      FixVersionNode parent = getParent(sprintName, fixVersionName);
      String jiraListName = getSeparatedName(jira.getKey(), fixVersionName);
      JiraNode jiraNode = jiras.get(jiraListName);
      if (jiraNode == null) {
         jiraNode = new JiraNode(jira.getKey(), parent, jira.getResolution(), jira.getStatus(), jira.getSummary());
         insertNodeInto(jiraNode, parent, parent.getChildCount());
         jiras.put(jiraListName, jiraNode);
      } else {
         TreeNode oldParent = jiraNode.getParent();
         System.out.println("blah!!");
         // FIXME not working when moving one jira to anoother fixversion and then refreshing.
         if (oldParent != parent || oldParent.getParent() != parent.getParent()) {
            removeNodeFromParent(jiraNode);
            insertNodeInto(jiraNode, parent, parent.getChildCount());
         }
      }
   }

   private FixVersionNode getParent(String sprintName, String fixVersionName) {
      String fixVersionListName = getSeparatedName(sprintName, fixVersionName);
      FixVersionNode parent = fixVersions.get(fixVersionListName);
      if (parent == null) {
         parent = createFixVersion(sprintName, fixVersionName);
      }
      return parent;
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
      String sprint = setSprintToUnkownIfDoesntExist(jira);
      String fixVersion = setFixVersionToUnKnownIfDoesntExist(jira);
      createJira(sprint, fixVersion, jira);
   }

   private String setFixVersionToUnKnownIfDoesntExist(JiraDTO jira) {
      String fixVersion = jira.getFixVersions().get(0);
      if (fixVersion == null) {
         fixVersion = UNKNOWN_FIXVERSION;
      }
      return fixVersion;
   }

   private String setSprintToUnkownIfDoesntExist(JiraDTO jira) {
      String sprint = jira.getSprint();
      if (sprint == null) {
         sprint = UNKNOWN_SPRINT;
      }
      return sprint;
   }

   public void removeJira(String jira, String fixVersion) {
      String jiraListName = getSeparatedName(jira, fixVersion);
      DefaultMutableTreeNode node = jiras.get(jiraListName);
      log.debug(node);
      removeNodeFromParent(node);
      jiras.remove(jiraListName);
   }
   protected String getSeparatedName(String stringOne, String stringTwo){
      StringBuffer sb = new StringBuffer(stringOne);
      sb.append(SEPARATOR).append(stringTwo);
      return sb.toString();
   }
}
