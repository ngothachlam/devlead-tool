package com.jonas.agile.devleadtool.component.tree.dnd;

import javax.swing.tree.DefaultMutableTreeNode;

public class TransferableDTO {

   private final DefaultMutableTreeNode newNode;

   public TransferableDTO(DefaultMutableTreeNode e) {
      this.newNode = e;
   }

   public DefaultMutableTreeNode getNewNode() {
      return newNode;
   }


}
