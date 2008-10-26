package com.jonas.testing.tree.fromScratch;

import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class DnDTree extends JTree {

   private Logger log = MyLogger.getLogger(DnDTree.class);

   private DefaultTreeModel model;

   public DnDTree(String string) {
      super(new DefaultMutableTreeNode(string));
      setDnD();
   }

   public DefaultTreeModel getModel() {
      return model;
   }

   public void removeJira(String jira) {
      throw new UnsupportedOperationException("not implemented yet!");
   }

   @Override
   public void setModel(TreeModel newModel) {
      if (!(newModel instanceof DefaultTreeModel))
         throw new RuntimeException("Only handles DefaultTreeModel!");
      super.setModel(newModel);
      model = (DefaultTreeModel) newModel;
   }

   private void setDnD() {
      setDragEnabled(true);
      setDropMode(DropMode.INSERT);
      setTransferHandler(new DnDTreeTransferHandler(this));

      setEditable(false);
      getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
   }
}
