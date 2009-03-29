package com.jonas.agile.devleadtool.gui.component.tree.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.FixVersionNode;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.ProjectNode;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.gui.component.tree.xml.JiraDTO;
import com.jonas.common.logging.MyLogger;

public class DnDTreeModel extends DefaultTreeModel {

   private static final String UNKNOWN_FIXVERSION = "<UnKnown FixVersion>";
   public static final String UNKNOWN_SPRINT = "<UnKnown Sprint>";

   private Logger log = MyLogger.getLogger(DnDTreeModel.class);

   // private Map<String, JiraNode> jiras = new HashMap<String, JiraNode>();
   // private Map<String, FixVersionNode> fixVersions = new HashMap<String, FixVersionNode>();
   // private Map<String, SprintNode> sprints = new HashMap<String, SprintNode>();

   public DnDTreeModel(String rootName) {
      super(new ProjectNode(rootName));
      setAsksAllowsChildren(true);
   }

   public void addJira(JiraDTO jira) {
      log.debug(jira);
      String sprint = setSprintToUnkownIfDoesntExist(jira);
      List<String> tempFixVersions = jira.getFixVersions();
      removeJira(jira.getKey());
      if (tempFixVersions.size() > 0) {
         for (String fixVersion : tempFixVersions) {
            String newFixVersion = setFixVersionToUnKnownIfDoesntExist(jira, fixVersion);
            createJira(sprint, newFixVersion, jira);
         }
      } else {
         String newFixVersion = setFixVersionToUnKnownIfDoesntExist(jira, null);
         createJira(sprint, newFixVersion, jira);
      }
   }

   FixVersionNode createFixVersion(String fixVersionName, SprintNode parent) {
      log.debug("creating fixVersion " + fixVersionName + " with parent " + parent.getSprintName());
      FixVersionNode fixVersionNode = new FixVersionNode(fixVersionName);
      insertNodeInto(fixVersionNode, parent, parent.getChildCount());
      return fixVersionNode;
   }

   void createJira(String sprintName, String fixVersionName, JiraDTO jira) {
      FixVersionNode fixVersionNode = getFixVersionNode(sprintName, fixVersionName);
      JiraNode jiraNode = fixVersionNode.getJiraNode(jira.getKey());
      if (jiraNode == null) {
         jiraNode = getJiraNode(jira, fixVersionNode);
         log.debug(jiraNode.getUserObject() + " is child? ");
         insertNodeInto(jiraNode, fixVersionNode, fixVersionNode.getChildCount());
      } else {
         TreeNode oldParent = jiraNode.getParent();
         // FIXME not working when moving one jira to anoother fixversion and then refreshing.
         if (oldParent != fixVersionNode || oldParent.getParent() != fixVersionNode.getParent()) {
            removeNodeFromParent(jiraNode);
            jiraNode = getJiraNode(jira, fixVersionNode);
            insertNodeInto(jiraNode, fixVersionNode, fixVersionNode.getChildCount());
         }
      }
   }

   public SprintNode createSprint(String sprintName) {
      log.debug("create Sprint " + sprintName);
      ProjectNode root = (ProjectNode) getRoot();
      SprintNode sprintNode = null;
      if (UNKNOWN_SPRINT.equals(sprintName))
         sprintNode = new SprintNode(sprintName, "");
      else
         sprintNode = new SprintNode(sprintName);
      insertNodeInto(sprintNode, root, root.getChildCount());
      return sprintNode;
   }

   FixVersionNode getFixVersionNode(String sprintName, String fixVersionName) {
      SprintNode parent = getSprintNode(sprintName);

      Enumeration<FixVersionNode> enu = parent.children();
      while (enu.hasMoreElements()) {
         FixVersionNode fixVersionNode = enu.nextElement();
         if (fixVersionNode.getFixVersionName().equals(fixVersionName)) {
            return fixVersionNode;
         }
      }
      return createFixVersion(fixVersionName, parent);
   }

   private JiraNode getJiraNode(JiraDTO j, FixVersionNode f) {
      return new JiraNode(j.getKey(), j.getId(), j.getSummary(), j.getResolution(), j.getStatus(), j.getSprint(), j.getFixVersions(), j
            .getSyncable(), j.getOriginalEstimate(), j.getActual(), j.getProject());
   }

   public List<DefaultMutableTreeNode> getChildNodes(DefaultMutableTreeNode root) {
      List<DefaultMutableTreeNode> jiraNodes = new ArrayList<DefaultMutableTreeNode>();
      for (Enumeration<?> e = root.preorderEnumeration(); e.hasMoreElements();) {
         DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();
         jiraNodes.add(current);
      }
      return jiraNodes;

   }

   public List<JiraNode> getJiraNodes() {
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
      List<JiraNode> jiraNodes = new ArrayList<JiraNode>();

      for (Enumeration<?> e = root.preorderEnumeration(); e.hasMoreElements();) {
         DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();
         if (current instanceof JiraNode) {
            jiraNodes.add((JiraNode) current);
         }
      }
      return jiraNodes;
   }

   public List<JiraNode> getJiraNodes(String jira) {
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
      List<JiraNode> jiraNodes = new ArrayList<JiraNode>();

      for (Enumeration<?> e = root.preorderEnumeration(); e.hasMoreElements();) {
         DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();
         if (current instanceof JiraNode && jira.equals(current.getUserObject())) {
            jiraNodes.add((JiraNode) current);
         }
      }
      return jiraNodes;
   }

   private SprintNode getSprintNode(String sprintName) {
      Enumeration<SprintNode> enu = root.children();
      while (enu.hasMoreElements()) {
         SprintNode sprintNode = enu.nextElement();
         log.debug("Existing Sprint: \"" + sprintNode.getSprintName() + "\" - new SprintName: \"" + sprintName + "\"");
         if (sprintNode.getUserObject().equals(sprintName)) {
            log.debug("Match!");
            return sprintNode;
         }
      }
      return createSprint(sprintName);
   }

   public void removeAllChildrenOf(SprintNode node) {
      int noOfRootChildren = getChildCount(node);
      for (int child = noOfRootChildren - 1; child >= 0; child--) {
         DefaultMutableTreeNode tempChild = (DefaultMutableTreeNode) getChild(node, child);
         log.debug("Removing " + tempChild);
         removeNodeFromParent(tempChild);
      }
   }

   public void removeAllChildrenOfRoot() {
      int noOfRootChildren = getChildCount(getRoot());
      for (int child = noOfRootChildren - 1; child >= 0; child--) {
         MutableTreeNode tempChild = (MutableTreeNode) getChild(getRoot(), child);
         log.debug("Removing " + tempChild);
         removeNodeFromParent(tempChild);
      }
   }

   public void removeJira(String jira) {
      List<JiraNode> jiraNodes = getJiraNodes();
      for (JiraNode jiraNode : jiraNodes) {
         if (jiraNode.getKey().equals(jira))
            removeNodeFromParent(jiraNode);
      }
   }

   private String setFixVersionToUnKnownIfDoesntExist(JiraDTO jira, String fixVersion) {
      if (fixVersion == null) {
         fixVersion = UNKNOWN_FIXVERSION;
      }
      return fixVersion;
   }

   private String setSprintToUnkownIfDoesntExist(JiraDTO jira) {
      String sprint = jira.getSprint();
      log.debug("Sprint: " + sprint);
      if (sprint == null || sprint.equals("")) {
         log.debug("Sprint set to <UnKnown>!");
         return UNKNOWN_SPRINT;
      }
      return sprint;
   }
}
