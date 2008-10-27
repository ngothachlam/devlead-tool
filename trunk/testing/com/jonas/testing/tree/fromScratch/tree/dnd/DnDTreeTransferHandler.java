package com.jonas.testing.tree.fromScratch.tree.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.DnDTree;
import com.jonas.testing.tree.fromScratch.tree.nodes.FixVersionNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.JiraNode;

public class DnDTreeTransferHandler extends TransferHandler {

   private Logger log = MyLogger.getLogger(DnDTreeTransferHandler.class);

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

   @Override
   final public boolean canImport(TransferSupport supp) {
      if (!supp.isDataFlavorSupported(dataFlavor)) {
         return false;
      }
      try {
         DefaultMutableTreeNode mouseOverParentNode = getParentNode(supp);
         DefaultMutableTreeNode draggedNode = getDraggedNode(supp);

         return isParentMouseOverOfTypeFixVersionAndDragedNodeIsJiraType(mouseOverParentNode, draggedNode);
      } catch (UnsupportedFlavorException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return false;
   }

   private DefaultMutableTreeNode getDraggedNode(TransferSupport supp) throws UnsupportedFlavorException, IOException {
      Transferable transferable = supp.getTransferable();
      TransferableDTO dto = (TransferableDTO) transferable.getTransferData(dataFlavor);
      DefaultMutableTreeNode newNode = dto.getNewNode();
      return newNode;
   }

   private DefaultMutableTreeNode getParentNode(TransferSupport supp) {
      JTree.DropLocation dropLocation = (JTree.DropLocation) supp.getDropLocation();
      TreePath path = dropLocation.getPath();
      DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
      return parentNode;
   }

   private boolean isParentMouseOverOfTypeFixVersionAndDragedNodeIsJiraType(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode newNode) {
      if (parentNode instanceof FixVersionNode && newNode instanceof JiraNode) {
         FixVersionNode parent = (FixVersionNode) parentNode;
         JiraNode child = (JiraNode) newNode;
         FixVersionNode childsParent = (FixVersionNode) child.getParent();
         log.debug("Parent : " + parent.getUserObject() + " Childs parent: " + childsParent.getUserObject());
         if (parent.getUserObject().equals((childsParent).getUserObject()))
            return true;
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
