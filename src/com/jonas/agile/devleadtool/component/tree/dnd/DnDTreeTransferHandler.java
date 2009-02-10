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
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.component.tree.SprintTree;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.nodes.FixVersionNode;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;
import com.jonas.common.logging.MyLogger;

public class DnDTreeTransferHandler extends TransferHandler {

   private DataFlavor dataFlavor;

   private Logger log = MyLogger.getLogger(DnDTreeTransferHandler.class);
   private SprintTree tree;

   public DnDTreeTransferHandler(SprintTree tree) {
      this.tree = tree;
      try {
         dataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + JiraNodesTransferableDTO.class.getName());
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
         DefaultMutableTreeNode mouseOverNode = getMouseOverNode(supp);
         JiraNode[] draggedNode = getDraggedNode(supp);

         return isParentMouseOverOfTypeFixVersionAndDragedNodeIsJiraType(mouseOverNode, draggedNode);
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
         TreePath dropPath = dropLocation.getPath();

         Transferable transferable = support.getTransferable();
         JiraNodesTransferableDTO transferableDTO = (JiraNodesTransferableDTO) transferable.getTransferData(dataFlavor);

         JiraNode[] newNodes = transferableDTO.getNewNodes();
         for (JiraNode jiraNode : newNodes) {
            if (dropPath.getLastPathComponent() instanceof SprintNode) {
               SprintNode sprintNode = (SprintNode) dropPath.getLastPathComponent();
               JiraDTO jiraDTO = new JiraDTO(jiraNode.getKey(), jiraNode.getId(), jiraNode.getSummary(), jiraNode.getFixVersions(), sprintNode.getSprintName(),
                     jiraNode.getStatus(), jiraNode.getResolution(), true, jiraNode.getOriginalEstimate());
               tree.getModel().addJira(jiraDTO);
            } else {
               return false;
            }
            TreePath newPath = dropPath.pathByAddingChild(jiraNode);
            tree.makeVisible(newPath);
            // tree.scrollRectToVisible(tree.getPathBounds(newPath));
         }
         return true;
      } catch (UnsupportedFlavorException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return false;
   }

   private JiraNode[] getDraggedNode(TransferSupport supp) throws UnsupportedFlavorException, IOException {
      Transferable transferable = supp.getTransferable();
      JiraNodesTransferableDTO dto = (JiraNodesTransferableDTO) transferable.getTransferData(dataFlavor);
      JiraNode[] newNode = dto.getNewNodes();
      return newNode;
   }

   private DefaultMutableTreeNode getMouseOverNode(TransferSupport supp) {
      JTree.DropLocation dropLocation = (JTree.DropLocation) supp.getDropLocation();
      TreePath path = dropLocation.getPath();
      DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
      return parentNode;
   }

   private boolean isParentMouseOverOfTypeFixVersionAndDragedNodeIsJiraType(DefaultMutableTreeNode theMouseOverNode, JiraNode[] draggedNodes) {
      for (JiraNode draggedNode : draggedNodes) {
         if (theMouseOverNode instanceof SprintNode) {
            if ("Closed".equals(draggedNode.getStatus())) {
               // FIXME doesn't work!
               tree.setToolTipText("Jira is Closed - cannot be moved!");
               return false;
            }
         } else {
            return false;
         }
      }
      return true;
   }

   @Override
   final protected Transferable createTransferable(JComponent c) {
      if (c instanceof SprintTree) {
         final SprintTree tree = (SprintTree) c;
         TreePath[] paths = tree.getSelectionPaths();
         for (TreePath treePath : paths) {
            JiraNode jiraNode = (JiraNode) treePath.getLastPathComponent();
            tree.addSelection(jiraNode.getKey());
         }
         tree.scrollToSelection();
         return new JiraNodeTransferable(tree, dataFlavor);
      }
      return null;
   }

   @Override
   final protected void exportDone(JComponent source, Transferable transferable, int action) {
   }

}
