package com.jonas.agile.devleadtool.component.tree.dnd;

import java.util.ArrayList;
import java.util.List;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;

public class JiraNodesTransferableDTO {

   private final List<JiraNode> nodes = new ArrayList<JiraNode>();

   public JiraNodesTransferableDTO(JiraNode... nodes) {
      for (JiraNode node : nodes) {
         this.nodes.add(node);
      }
   }

   public JiraNode[] getNewNodes() {
      return nodes.toArray(new JiraNode[nodes.size()]);
   }

   public void addNode(JiraNode node){
      nodes.add(node);
   }
}
