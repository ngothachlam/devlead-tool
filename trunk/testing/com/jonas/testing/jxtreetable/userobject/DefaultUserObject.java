package com.jonas.testing.jxtreetable.userobject;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import com.jonas.testing.jxtreetable.column.Column;

public abstract class DefaultUserObject<T extends DefaultUserObject> implements Comparable<T>, Transferable {
   public final Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (isDataFlavorSupported(flavor)) {
         return this;
      }
      throw new UnsupportedFlavorException(flavor);
   }

   private DefaultUserObject(){
      nodeDelegate.createNode(this);
   }
   
   public DefaultUserObject(DefaultMutableTreeTableNode parent){
      this();
      nodeDelegate.setParent(parent);
   }
   
   public DefaultUserObject(DefaultParentUserObject<? extends DefaultParentUserObject> parent){
      this();
      nodeDelegate.setParent(parent);
   }
   
   public abstract String getValueForColumn(Column column);

   @Override
   public final DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { getDataFlavor() };
   }

   @Override
   public final boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(getDataFlavor());
   }

   protected abstract DataFlavor getDataFlavor();

   public abstract void setValue(Column column, Object value);

   public abstract boolean isLeaf();

   public abstract boolean isEditable(Column column);

   private NodeDelegate nodeDelegate = new NodeDelegate();

   private class NodeDelegate {
      private DefaultMutableTreeTableNode thisNode;
      private void createNode(DefaultUserObject userObject){
         thisNode = new DefaultMutableTreeTableNode(userObject);
      }
      public void setParent(DefaultMutableTreeTableNode parentNode) {
         parentNode.add(thisNode);
      }
      public void setParent(DefaultParentUserObject parent) {
         parent.getNode().add(thisNode);
      }
      public AbstractMutableTreeTableNode getNode() {
         return thisNode;
      }
      public boolean hasChildren(){
         return thisNode.getChildCount() > 0;
      }
   }
   public boolean hasChildren(){
      return nodeDelegate.hasChildren();
   }

   public AbstractMutableTreeTableNode getNode() {
      return nodeDelegate.getNode();
   }
}