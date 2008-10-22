package com.jonas.testing.tree.fromScratch;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

public class DnDTreeBuilder {

   private Map<String, DefaultMutableTreeNode> sprints = new HashMap<String, DefaultMutableTreeNode>();
   private Map<String, DefaultMutableTreeNode> fixVersions = new HashMap<String, DefaultMutableTreeNode>();
   private DefaultMutableTreeNode root;

   public JTree buildTree() {
      root = new DefaultMutableTreeNode("LLU");
      DefaultTreeModel dndTreeModel = new DefaultTreeModel(root);

      createJira(dndTreeModel, "Sprint 1", "LLU 11.1", "LLU-1");
      createJira(dndTreeModel, "Sprint 1", "LLU 12", "LLU-2");
      createJira(dndTreeModel, "Sprint 2", "LLU 12", "LLU-3");
      createJira(dndTreeModel, "Sprint 2", "LLU 11.2", "LLU-4");
      
      DnDTree dnDTree = new DnDTree(dndTreeModel);

      return dnDTree;
   }
   
   public DefaultMutableTreeNode createSprint(DefaultTreeModel model, String sprintName){
      DefaultMutableTreeNode sprintNode = new DefaultMutableTreeNode(sprintName);
      model.insertNodeInto(sprintNode, root, root.getChildCount());
      sprints.put(sprintName, sprintNode);
      return sprintNode;
   }
   
   public DefaultMutableTreeNode createFixVersion(DefaultTreeModel model, String sprintName, String fixVersionName){
      DefaultMutableTreeNode fixVersionNode = new DefaultMutableTreeNode(fixVersionName);
      MutableTreeNode parent = sprints.get(sprintName);
      if (parent == null){
         parent = createSprint(model, sprintName);
      }
      model.insertNodeInto(fixVersionNode, parent, parent.getChildCount());
      sprints.put(fixVersionName, fixVersionNode);
      return fixVersionNode;
   }
   
   public DefaultMutableTreeNode createJira(DefaultTreeModel model, String sprintName, String fixVersionName, String jira){
      DefaultMutableTreeNode jiraNode = new DefaultMutableTreeNode(jira);
      MutableTreeNode parent = fixVersions.get(fixVersionName);
      if (parent == null){
         parent = createFixVersion(model, sprintName, fixVersionName);
      }
      model.insertNodeInto(jiraNode, parent, parent.getChildCount());
      sprints.put(fixVersionName, jiraNode);
      return jiraNode;
   }
}
