package com.jonas.testing.jxtreetable.dao;

import java.util.ArrayList;
import java.util.List;

public class DaoTreeNode {

   private List<DaoTreeNode> children = new ArrayList<DaoTreeNode>();

   private Object userObject;

   public DaoTreeNode() {
   }

   public DaoTreeNode(Object userObject) {
      this.userObject = userObject;
   }

   public DaoTreeNode addChild(Object child) {
      DaoTreeNode newChild = new DaoTreeNode(child);
      children.add(newChild);
      return newChild;
   }

   public List<DaoTreeNode> getChildren() {
      return children;
   }

   public Object getUserObject() {
      return userObject;
   }

   public void setChildren(List<DaoTreeNode> children) {
      this.children = children;
   }

   public void setUserObject(Object rootUserObject) {
      this.userObject = rootUserObject;
   }

}
