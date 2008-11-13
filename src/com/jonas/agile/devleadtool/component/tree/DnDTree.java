package com.jonas.agile.devleadtool.component.tree;

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
import com.jonas.agile.devleadtool.component.table.renderer.DnDTreeCellRenderer;
import com.jonas.agile.devleadtool.component.tree.dnd.DnDTreeTransferHandler;
import com.jonas.agile.devleadtool.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.component.tree.nodes.FixVersionNode;
import com.jonas.agile.devleadtool.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.component.tree.nodes.Status;
import com.jonas.agile.devleadtool.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.component.tree.xml.JiraDTO;
import com.jonas.common.logging.MyLogger;

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

   public void setDnDEnabled(boolean setDndEnabled) {
      setDragEnabled(setDndEnabled);
   }

   public List<TreePath> getJiraPaths(String jira) {
      List<TreePath> result = new ArrayList<TreePath>();
      List<JiraNode> nodes = getJiraNodes();
      for (JiraNode jiraNode : nodes) {
         if (jiraNode.getUserObject().equals(jira)) {
            result.add(new TreePath(jiraNode.getPath()));
         }
      }
      return result;
   }

   public void scrollToSelection() {
      // USABILITY - add an inidcator that there is a selected row (both in tree and table) outside of the scrollpane
      TreeSelectionModel tsm = getSelectionModel();
      TreePath minPath = getPathForRow(tsm.getMinSelectionRow());
      TreePath maxPath = getPathForRow(tsm.getMaxSelectionRow());
      if (minPath != null)
         scrollPathToVisible(minPath);
      if (maxPath != null)
         scrollPathToVisible(maxPath);
   }

   public void addSelection(String jira) {
      List<TreePath> jiraPathList = getJiraPaths(jira);
      TreePath[] jiraTreePath = jiraPathList.toArray(new TreePath[jiraPathList.size()]);
      addSelectionPaths(jiraTreePath);
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
            JiraNode jiraNode = (JiraNode) nodeTemp;
            str = appendStrings(jiraNode.getUserObject().toString(), " ", jiraNode.getDescription(), " (Status: ", jiraNode.getStatus(), ", Resolution: ",
                  jiraNode.getResolution(), ")");
         } else if (nodeTemp instanceof SprintNode) {
            SprintNode node = (SprintNode) nodeTemp;
            Status lowestResolution = node.getLowestStatus();
            str = appendStrings(node.getUserObject().toString(), " (Status: ", lowestResolution, ", Children: ", node.getChildCount(), ")");
            
         } else if (nodeTemp instanceof FixVersionNode) {
            FixVersionNode node = (FixVersionNode) nodeTemp;
            str = appendStrings(node.getUserObject().toString(), " (Children: ", node.getChildCount(), ")");
         }
         return str;
      }
      return null;

   }
}