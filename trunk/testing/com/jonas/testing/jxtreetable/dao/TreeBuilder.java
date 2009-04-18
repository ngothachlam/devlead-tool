package com.jonas.testing.jxtreetable.dao;

import java.util.List;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class TreeBuilder {

   public XMLGroup buildTree(DefaultTreeTableModel treeTableModel) {
      TreeTableNode parent = treeTableModel.getRoot();
      XMLGroup tree = new XMLGroup(parent.getUserObject());
      
      buildTreeRecursively(treeTableModel, parent, tree);
      
      return tree;
   }

   private void buildTreeRecursively(DefaultTreeTableModel treeTableModel, TreeTableNode parent, XMLGroup tree) {
      int childCount = treeTableModel.getChildCount(parent);
      for (int childIndex = 0; childIndex < childCount; childIndex++) {
         TreeTableNode child = (TreeTableNode) treeTableModel.getChild(parent, childIndex);
         XMLGroup childTree = tree.addChild(child.getUserObject());
         if(child.getChildCount()>0){
            buildTreeRecursively(treeTableModel, child, childTree);
         }
      }
   }

   public DefaultMutableTreeTableNode readTree(XMLGroup tree) {
      DefaultMutableTreeTableNode parent = readTreeRecursively(tree);
      return parent;
   }

   private DefaultMutableTreeTableNode readTreeRecursively(XMLGroup tree) {
      DefaultMutableTreeTableNode parent = new DefaultMutableTreeTableNode(tree.getUserObject());
      List<XMLGroup> children = tree.getChildren();
      for (XMLGroup child : children) {
         parent.add(readTreeRecursively(child));
      }
      return parent;
   }

}
