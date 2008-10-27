package com.jonas.testing.tree.fromScratch.tree;

import java.awt.event.MouseEvent;
import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.tree.fromScratch.tree.dnd.DnDTreeTransferHandler;
import com.jonas.testing.tree.fromScratch.tree.model.DnDTreeModel;
import com.jonas.testing.tree.fromScratch.tree.nodes.FixVersionNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.JiraNode;
import com.jonas.testing.tree.fromScratch.tree.nodes.SprintNode;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;

public class DnDTree extends JTree {

   private Logger log = MyLogger.getLogger(DnDTree.class);

   private DnDTreeModel model;

   public DnDTree(DnDTreeModel model) {
      super(model);
      setDnD();
      ToolTipManager.sharedInstance().registerComponent(this);
   }

   public DnDTreeModel getModel() {
      return model;
   }

   @Override
   public String getToolTipText(MouseEvent event) {
      if (event == null)
         return null;
      TreePath path = getPathForLocation(event.getX(), event.getY());
      if (path != null) {
         DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) (path.getLastPathComponent());
         String str = null;
         if (nodeTemp instanceof JiraNode){
            JiraNode node = (JiraNode) nodeTemp;
            str = node.getUserObject().toString();
         }
         else if (nodeTemp instanceof SprintNode){
            SprintNode node = (SprintNode) nodeTemp;
            str = node.getUserObject().toString();
         }
         else if (nodeTemp instanceof FixVersionNode){
            FixVersionNode node = (FixVersionNode) nodeTemp;
            str = node.getUserObject().toString();
         }
         return str;
      }
      return null;
   }

   public void removeJira(String jira) {
      throw new UnsupportedOperationException("not implemented yet!");
   }

   @Override
   public void setModel(TreeModel newModel) {
      if (!(newModel instanceof DnDTreeModel))
         throw new RuntimeException("Only handles DefaultTreeModel!");
      super.setModel(newModel);
      model = (DnDTreeModel) newModel;
   }

   private void setDnD() {
      setDragEnabled(true);
      setDropMode(DropMode.INSERT);
      setTransferHandler(new DnDTreeTransferHandler(this));

      setEditable(false);
      getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
   }

   public void removeAllChildren() {
      model.removeAllChildren();
   }

   public void reload() {
      model.reload();
   }

   public void createJira(JiraDTO jira) {
      model.addJira(jira);
   }
}
