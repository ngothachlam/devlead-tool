package com.jonas.testing.tree.fromScratch.tree;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
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
import com.jonas.testing.tree.fromScratch.tree.renderer.DnDTreeCellRenderer;
import com.jonas.testing.tree.fromScratch.xml.JiraDTO;

public class DnDTree extends JTree {

   private Logger log = MyLogger.getLogger(DnDTree.class);

   private DnDTreeModel model;

   private DnDTreeJiraToolTipFacade tooltipFacade;

   public DnDTree(DnDTreeModel model) {
      super(model);
      setDnD();
      ToolTipManager.sharedInstance().registerComponent(this);
      setCellRenderer(new DnDTreeCellRenderer());
      tooltipFacade = new DnDTreeJiraToolTipFacade(this);
   }

   public void createJira(JiraDTO jira) {
      model.addJira(jira);
   }

   public DnDTreeModel getModel() {
      return model;
   }

   @Override
   public String getToolTipText(MouseEvent event) {
      return tooltipFacade.getToolTipText(event);
   }

   public void reload() {
      model.reload();
   }

   public void removeAllChildren() {
      model.removeAllChildren();
   }

   public void removeJira(String jira) {
      throw new UnsupportedOperationException("not implemented yet!");
   }

   private void setDnD() {
      setDragEnabled(true);
      setDropMode(DropMode.INSERT);
      setTransferHandler(new DnDTreeTransferHandler(this));

      setEditable(false);
      getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
   }

   @Override
   public void setModel(TreeModel newModel) {
      if (!(newModel instanceof DnDTreeModel))
         throw new RuntimeException("Only handles DefaultTreeModel!");
      super.setModel(newModel);
      model = (DnDTreeModel) newModel;
   }

   public List<JiraNode> getJiraNodes() {
      return model.getJiraNodes();
   }
   
   public void setDnDEnabled(boolean setDndEnabled){
      setDragEnabled(setDndEnabled);
   }

   public List<TreePath> getJiraPath(String jira) {
      List<TreePath> result = new ArrayList<TreePath>();
      List<JiraNode> nodes = getJiraNodes();
      for (JiraNode jiraNode : nodes) {
         if (jiraNode.getUserObject().equals(jira)){
            result.add(new TreePath(jiraNode.getPath()));
         }
      }
      return result;
   }
}


class DnDTreeJiraToolTipFacade {

   private final DnDTree dnDTree;

   public DnDTreeJiraToolTipFacade(DnDTree dnDTree) {
      this.dnDTree = dnDTree;
   }

   private String appendStrings(Object... strings) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < strings.length; i++) {
         sb.append(strings[i].toString());
      }
      return sb.toString();
   }

   public String getToolTipText(MouseEvent event) {
      if (event == null)
         return null;
      TreePath path = dnDTree.getPathForLocation(event.getX(), event.getY());
      if (path != null) {
         DefaultMutableTreeNode nodeTemp = (DefaultMutableTreeNode) (path.getLastPathComponent());
         String str = null;
         if (nodeTemp instanceof JiraNode) {
            JiraNode node = (JiraNode) nodeTemp;
            str = appendStrings(node.getUserObject().toString(), " ", node.getDescription(), " (Status: ", node.getStatus(), ", Resolution: ", node
                  .getResolution(), ")");
         } else if (nodeTemp instanceof SprintNode) {
            SprintNode node = (SprintNode) nodeTemp;
            str = appendStrings(node.getUserObject().toString(), " (Children: ", node.getChildCount(), ")");
         } else if (nodeTemp instanceof FixVersionNode) {
            FixVersionNode node = (FixVersionNode) nodeTemp;
            str = appendStrings(node.getUserObject().toString(), " (Children: ", node.getChildCount(), ")");
         }
         return str;
      }
      return null;

   }
}