package com.jonas.testing.jxtreetable.dao;

import java.util.ArrayList;
import java.util.List;

public class XMLGroup {

   private List<XMLGroup> children = new ArrayList<XMLGroup>();

   private Object userObject;

   public XMLGroup() {
   }

   public XMLGroup(Object userObject) {
      this.userObject = userObject;
   }

   public XMLGroup addChild(Object child) {
      XMLGroup newChild = new XMLGroup(child);
      children.add(newChild);
      return newChild;
   }

   public List<XMLGroup> getChildren() {
      return children;
   }

   public Object getUserObject() {
      return userObject;
   }

   public void setChildren(List<XMLGroup> children) {
      this.children = children;
   }

   public void setUserObject(Object rootUserObject) {
      this.userObject = rootUserObject;
   }

}
