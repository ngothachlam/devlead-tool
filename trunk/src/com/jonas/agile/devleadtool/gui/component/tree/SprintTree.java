package com.jonas.agile.devleadtool.gui.component.tree;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import com.jonas.agile.devleadtool.gui.component.table.renderer.DnDTreeCellRenderer;
import com.jonas.agile.devleadtool.gui.component.tree.dnd.DnDTreeTransferHandler;
import com.jonas.agile.devleadtool.gui.component.tree.model.DnDTreeModel;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.JiraNode;
import com.jonas.agile.devleadtool.gui.component.tree.nodes.SprintNode;
import com.jonas.agile.devleadtool.gui.component.tree.xml.JiraDTO;
import com.jonas.common.logging.MyLogger;

public class SprintTree extends JTree {

   private Logger log = MyLogger.getLogger(SprintTree.class);

   private DnDTreeModel model;

   private DnDTreeJiraToolTipFacade tooltipFacade;

   public SprintTree(DnDTreeModel model) {
      super(model);
      setDnD();
      ToolTipManager.sharedInstance().registerComponent(this);
      setCellRenderer(new DnDTreeCellRenderer());
      tooltipFacade = new DnDTreeJiraToolTipFacade(this);
   }

   public void addJira(JiraDTO jira) {
      model.addJira(jira);
   }

   @Override
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
      model.removeAllChildrenOfRoot();
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

   public void removeAllChildren(SprintNode sprintNode) {
      model.removeAllChildrenOf(sprintNode);
   }
}


class DnDTreeJiraToolTipFacade {

   private final SprintTree dnDTree;

   public DnDTreeJiraToolTipFacade(SprintTree dnDTree) {
      this.dnDTree = dnDTree;
   }

   public String getToolTipText(MouseEvent event) {
      if (event == null)
         return null;
      TreePath path = dnDTree.getPathForLocation(event.getX(), event.getY());
      if (path != null) {
         Object nodeTemp = path.getLastPathComponent();
         if (nodeTemp instanceof ToolTipper) {
            ToolTipper node = (ToolTipper) nodeTemp;
            return node.getToolTipText();
         }
      }
      return null;

   }
}