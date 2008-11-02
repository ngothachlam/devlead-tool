package com.jonas.agile.devleadtool.component.tree.dnd;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TransferableDTO {

   private final DefaultTreeModel model;
   private final DefaultMutableTreeNode newNode;

   public TransferableDTO(DefaultTreeModel model, DefaultMutableTreeNode e) {
      this.model = model;
      this.newNode = e;
   }

   public DefaultTreeModel getModel() {
      return model;
   }

   public DefaultMutableTreeNode getNewNode() {
      return newNode;
   }


}
