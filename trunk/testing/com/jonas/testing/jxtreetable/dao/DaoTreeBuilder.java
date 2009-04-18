package com.jonas.testing.jxtreetable.dao;

import java.util.List;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class DaoTreeBuilder {

   public DaoTreeNode buildDaoTree(DefaultTreeTableModel treeTableModel) {
      TreeTableNode parent = treeTableModel.getRoot();
      DaoTreeNode tree = new DaoTreeNode(parent.getUserObject());
      
      buildTreeRecursively(treeTableModel, parent, tree);
      
      return tree;
   }

   private void buildTreeRecursively(DefaultTreeTableModel treeTableModel, TreeTableNode parent, DaoTreeNode tree) {
      int childCount = treeTableModel.getChildCount(parent);
      for (int childIndex = 0; childIndex < childCount; childIndex++) {
         TreeTableNode child = (TreeTableNode) treeTableModel.getChild(parent, childIndex);
         DaoTreeNode childTree = tree.addChild(child.getUserObject());
         if(child.getChildCount()>0){
            buildTreeRecursively(treeTableModel, child, childTree);
         }
      }
   }

   public DefaultMutableTreeTableNode readDaoTreeAndGetRoot(DaoTreeNode tree) {
      DefaultMutableTreeTableNode parent = readTreeRecursively(tree);
      return parent;
   }

   private DefaultMutableTreeTableNode readTreeRecursively(DaoTreeNode tree) {
      DefaultMutableTreeTableNode parent = new DefaultMutableTreeTableNode(tree.getUserObject());
      List<DaoTreeNode> children = tree.getChildren();
      for (DaoTreeNode child : children) {
         parent.add(readTreeRecursively(child));
      }
      return parent;
   }

}
