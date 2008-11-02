package com.jonas.testing.tree.fromScratch.tree.dnd;

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
import com.jonas.agile.devleadtool.component.tree.model.DnDTree;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.nodes.FixVersionNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.JiraNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.SprintNode;
import com.jonas.testing.tree.fromScratch.tree.xml.JiraDTO;

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

         if (transferableDTO.getNewNode() instanceof JiraNode) {
            JiraNode newNode = (JiraNode) transferableDTO.getNewNode().clone();
            if (path.getLastPathComponent() instanceof FixVersionNode) {
               FixVersionNode parentNode = (FixVersionNode) path.getLastPathComponent();
               log.debug("Parent is : " + parentNode + " and the new Element is: " + newNode + " and the childindex is : " + childIndex);
               model.insertNodeInto(newNode, parentNode, childIndex);
            } else if (path.getLastPathComponent() instanceof SprintNode) {
//               SprintNode sprintNode = (SprintNode) path.getLastPathComponent();
//               JiraDTO jiraDTO = new JiraDTO(newNode.getKey(), newNode.getId(), newNode.getDescription(), newNode.getFixVersions(), (String) sprintNode.getUserObject(), newNode.getStatus(), newNode.getResolution(), true);
//               tree.getModel().addJira(jiraDTO);
               SprintNode sprintNode = (SprintNode) path.getLastPathComponent();
               JiraDTO jiraDTO = new JiraDTO();
               jiraDTO.setKey(newNode.getKey());
               jiraDTO.setResolution(newNode.getResolution());
               jiraDTO.setSprint((String) sprintNode.getUserObject());
               jiraDTO.setStatus(newNode.getStatus());
               jiraDTO.setSummary(newNode.getDescription());
               jiraDTO.setId(newNode.getId());
               jiraDTO.setToSync(true);
               List<String> fixVersions = newNode.getFixVersions();
               for (String fixVersion : fixVersions) {
                  jiraDTO.addFixVersion(fixVersion);
               }
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

}
