package com.jonas.agile.devleadtool.component.tree.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.tree.DnDTree;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.nodes.FixVersionNode;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;
import com.jonas.common.logging.MyLogger;

public class DnDTreeTransferHandler extends TransferHandler {

   private DataFlavor dataFlavor;

   private Logger log = MyLogger.getLogger(DnDTreeTransferHandler.class);
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

   @Override
   final public int getSourceActions(JComponent c) {
      return MOVE;
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
         DefaultTreeModel model = ((DnDTree) support.getComponent()).getModel();
         int childIndex = dropLocation.getChildIndex();
         if (childIndex == -1) {
            childIndex = model.getChildCount(path.getLastPathComponent());
         }

         if (transferableDTO.getNewNode() instanceof JiraNode) {
            JiraNode newNode = (JiraNode) transferableDTO.getNewNode().clone();
            if (path.getLastPathComponent() instanceof FixVersionNode) {
               FixVersionNode parentNode = (FixVersionNode) path.getLastPathComponent();
               log.debug("Parent is : " + parentNode + " and the new Element is: " + newNode + " and the childindex is : " + childIndex);
               model.insertNodeInto(newNode, parentNode, childIndex);
            } else if (path.getLastPathComponent() instanceof SprintNode) {
               SprintNode sprintNode = (SprintNode) path.getLastPathComponent();
               JiraDTO jiraDTO = new JiraDTO(newNode.getKey(), newNode.getId(), newNode.getDescription(), newNode.getFixVersions(), (String) sprintNode
                     .getUserObject(), newNode.getStatus(), newNode.getResolution(), true);
               tree.getModel().addJira(jiraDTO);

            }
            TreePath newPath = path.pathByAddingChild(newNode);
            tree.makeVisible(newPath);
            tree.scrollRectToVisible(tree.getPathBounds(newPath));
            return true;
         }
         return false;
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

   private boolean isParentMouseOverOfTypeFixVersionAndDragedNodeIsJiraType(DefaultMutableTreeNode mouseOverParentNode, DefaultMutableTreeNode newNode) {
      if (mouseOverParentNode instanceof FixVersionNode && newNode instanceof JiraNode) {
         FixVersionNode parent = (FixVersionNode) mouseOverParentNode;
         JiraNode child = (JiraNode) newNode;
         FixVersionNode childsParent = (FixVersionNode) child.getParent();
         // log.debug("Parent : " + parent.getUserObject() + " Childs parent: " + childsParent.getUserObject());
         if (parent.equals(childsParent))
            return true;
      } else if (mouseOverParentNode instanceof SprintNode && newNode instanceof JiraNode) {
         JiraNode child = (JiraNode) newNode;
         if ("Closed".equals(child.getStatus()))
            return false;
         SprintNode parent = (SprintNode) mouseOverParentNode;
         SprintNode childsSprint = (SprintNode) child.getParent().getParent();
         if (!parent.equals(childsSprint))
            return true;
      }
      return false;
   }

   @Override
   final protected Transferable createTransferable(JComponent c) {
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
               Object transferDto = transferable.getTransferData(dataFlavor);
               if (transferDto instanceof TransferableDTO) {
                  TransferableDTO dto = (TransferableDTO) transferDto;
                  //USABILITY fix so that 1: when selecting a node in tree that has several fixversions - select all of them. 
                  //USABILITY fix so that 2: when dragging several nodes in the tree and dropping them - created (target) and deleted(source) correctly 
                  DnDTreeModel treeModel = (DnDTreeModel) tree.getModel();
                  List<JiraNode> jiraNodes = treeModel.getJiraNodes((String) dto.getNewNode().getUserObject());
                  for (JiraNode jiraNode : jiraNodes) {
                     treeModel.removeNodeFromParent(jiraNode);
                  }
               } else {
                  log.warn("The transfer object is not an instance of TransferableDTO but the source is JTree. Don't know what to do on exportDone!");
               }
            }
         }
      } catch (UnsupportedFlavorException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}
