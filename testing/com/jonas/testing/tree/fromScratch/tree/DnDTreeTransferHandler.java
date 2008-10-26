package com.jonas.testing.tree.fromScratch.tree;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.JTree.DropLocation;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;

public class DnDTreeTransferHandler extends TransferHandler {

   private Logger log = MyLogger.getLogger(DnDTreeTransferHandler.class);
   private Toolkit toolkit = Toolkit.getDefaultToolkit();

   private DataFlavor dataFlavor;
   private DnDTree tree;

   public DnDTreeTransferHandler(DnDTree tree) {
      this.tree = tree;
      try {
         dataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + TransferableDTO.class.getName());
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
   }

   // EXPORT:

   @Override
   final protected Transferable createTransferable(JComponent c) {
      log.debug("createTransferable: JComponent:" + c);

      if (c instanceof JTree) {
         final JTree tree = (JTree) c;
         TreePath path = tree.getSelectionPath();
         if ((path == null) || (path.getPathCount() <= 1)) {
            return null;
         }
         return new DnDTransferable(tree, dataFlavor);
      }

      return null;
   }

   @Override
   final protected void exportDone(JComponent source, Transferable transferable, int action) {
      log.debug("exportDone: Source:" + source + " Data: " + transferable + " Action: " + action);
      try {
         if (action == MOVE) {
            if (source instanceof JTree) {
               final JTree tree = (JTree) source;
               TransferableDTO dto;
               dto = (TransferableDTO) transferable.getTransferData(dataFlavor);
               DefaultTreeModel treeModel = dto.getModel();
               treeModel.removeNodeFromParent(dto.getNewNode());
            }
         }
      } catch (UnsupportedFlavorException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   final public int getSourceActions(JComponent c) {
      log.debug("getSourceActions: JComponent:" + c);
      return MOVE;
   }

   // IMPORT:

   @Override
   final public boolean canImport(TransferSupport supp) {
      if (!supp.isDataFlavorSupported(dataFlavor)) {
         return false;
      }
      try {
         JTree.DropLocation dropLocation = (JTree.DropLocation) supp.getDropLocation();
         TreePath path = dropLocation.getPath();
         DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) path.getLastPathComponent();
         Transferable transferable = supp.getTransferable();
         TransferableDTO dto = (TransferableDTO) transferable.getTransferData(dataFlavor);
         dto.getNewNode().getLevel();
         //FIXME I believe the next step is to make a fix version always a node (not leaf) even when they have no children
         log.debug("CanImport: " + lastPathComponent + " leafCount : " + lastPathComponent.getLeafCount() +" parent Depth: " + lastPathComponent.getLevel() + " nodeDepth : " + dto.getNewNode().getLevel());
         if (lastPathComponent.getLevel() + 1 == dto.getNewNode().getLevel()) {
            return true;
         }
      } catch (UnsupportedFlavorException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return false;
   }

   @Override
   public boolean importData(TransferHandler.TransferSupport support) {
      try {
         if (!canImport(support)) {
            return false;
         }

         JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();

         TreePath path = dropLocation.getPath();

         Transferable transferable = support.getTransferable();
         TransferableDTO transferableDTO;
         transferableDTO = (TransferableDTO) transferable.getTransferData(dataFlavor);
         DefaultTreeModel model = transferableDTO.getModel();
         int childIndex = dropLocation.getChildIndex();
         if (childIndex == -1) {
            childIndex = model.getChildCount(path.getLastPathComponent());
         }

         DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) transferableDTO.getNewNode().clone();
         DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
         log.debug("Parent is : " + parentNode + " and the new Element is: " + newNode + " and the childindex is : " + childIndex);
         model.insertNodeInto(newNode, parentNode, childIndex);

         TreePath newPath = path.pathByAddingChild(newNode);
         tree.makeVisible(newPath);
         tree.scrollRectToVisible(tree.getPathBounds(newPath));

         return true;
      } catch (UnsupportedFlavorException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return false;
   }

}
