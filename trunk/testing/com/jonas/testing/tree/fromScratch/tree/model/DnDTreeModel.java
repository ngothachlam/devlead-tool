package com.jonas.testing.tree.fromScratch.tree.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
      for (int child = noOfRootChildren - 1; child >= 0; child--) {
         MutableTreeNode tempChild = (MutableTreeNode) getChild(getRoot(), child);
         log.debug("Removing " + tempChild);
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
         jiraNode = new JiraNode(jira.getKey(), jira.getId(), jira.getSummary(), parent, jira.getResolution(), jira.getStatus(), jira.getSprint(),
               jira.getFixVersions(), jira.getSyncable());
         insertNodeInto(jiraNode, parent, parent.getChildCount());
         jiras.put(jiraListName, jiraNode);
      } else {
         TreeNode oldParent = jiraNode.getParent();
         System.out.println("blah!!");
         // FIXME not working when moving one jira to anoother fixversion and then refreshing.
         if (oldParent != parent || oldParent.getParent() != parent.getParent()) {
            removeNodeFromParent(jiraNode);
            jiras.remove(jiraListName);

            jiraListName = getSeparatedName(jira.getKey(), fixVersionName);
            jiraNode = new JiraNode(jira.getKey(), jira.getId(), jira.getSummary(), parent, jira.getResolution(), jira.getStatus(), jira.getSprint(),
                  jira.getFixVersions(), jira.getSyncable());
            insertNodeInto(jiraNode, parent, parent.getChildCount());
            jiras.put(jiraListName, jiraNode);
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

   public SprintNode createSprint(String sprintName) {
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
      List<String> tempFixVersions = jira.getFixVersions();
      for (String string : tempFixVersions) {
         String fixVersion = setFixVersionToUnKnownIfDoesntExist(jira, string);
         createJira(sprint, fixVersion, jira);
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

   public List<JiraNode> getJiraNodes(String jira) {
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
      List<JiraNode> jiraNodes = new ArrayList<JiraNode>();

      for (Enumeration<?> e = root.preorderEnumeration(); e.hasMoreElements();) {
         DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();
         System.out.println("current: " + current.getUserObject());
         if (current instanceof JiraNode && jira.equals(current.getUserObject())) {
            jiraNodes.add((JiraNode) current);
         }
      }
      return jiraNodes;
   }

   public List<JiraNode> getJiraNodes() {
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
      List<JiraNode> jiraNodes = new ArrayList<JiraNode>();

      for (Enumeration<?> e = root.preorderEnumeration(); e.hasMoreElements();) {
         DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();
         System.out.println("current: " + current.getUserObject());
         if (current instanceof JiraNode) {
            jiraNodes.add((JiraNode) current);
         }
      }
      return jiraNodes;
   }

   protected String getSeparatedName(String stringOne, String stringTwo) {
      StringBuffer sb = new StringBuffer(stringOne);
      sb.append(SEPARATOR).append(stringTwo);
      return sb.toString();
   }
}
