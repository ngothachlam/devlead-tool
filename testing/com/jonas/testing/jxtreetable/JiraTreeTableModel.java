package com.jonas.testing.jxtreetable;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.jxtreetable.userobject.DefaultUserObject;

public class JiraTreeTableModel extends DefaultTreeTableModel {
   private final ColumnMapper colIndexMapper;

   public JiraTreeTableModel(TreeTableNode node, ColumnMapper colIndexMapper) {
      super(node);
      this.colIndexMapper = colIndexMapper;
   }

   @Override
   public int getColumnCount() {
      return colIndexMapper.getColumnCount();
   }

   /**
    * What the TableHeader displays when the Table is in a JScrollPane.
    */
   @Override
   public String getColumnName(int columnIndex) {
      return colIndexMapper.getColumnForIndex(columnIndex).toString();
   }

   /**
    * Returns which object is displayed in this column.
    */
   @Override
   public Object getValueAt(Object node, int columnIndex) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof DefaultUserObject) {
            DefaultUserObject defnode = (DefaultUserObject) defNode.getUserObject();
            Column column = colIndexMapper.getColumnForIndex(columnIndex);
            return defnode.getValueForColumn(column);
         }
         MyLogger.warn(JiraTreeTableModel.class, "User object [" + defNode.getUserObject().toString() + "]is not of DefaultUserObject type");
      } else {
         MyLogger.warn(JiraTreeTableModel.class, "Node [" + node.toString() + "]is not of DefaultMutableTreeTableNode type");
      }
      return "";
   }

   @Override
   public boolean isCellEditable(Object node, int column) {
      return true;
   }

   @Override
   public boolean isLeaf(Object node) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof DefaultUserObject) {
            DefaultUserObject defnode = (DefaultUserObject) defNode.getUserObject();
            return defnode.isLeaf();
         }
         MyLogger.warn(JiraTreeTableModel.class, "User object [" + defNode.getUserObject().toString() + "]is not of DefaultUserObject type");
      } else {
         MyLogger.warn(JiraTreeTableModel.class, "Node [" + node.toString() + "]is not of DefaultMutableTreeTableNode type");
      }
      return super.isLeaf(node);
   }

   /**
    * Called when done editing a cell.
    */
   @Override
   public void setValueAt(Object value, Object node, int columnIndex) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof DefaultUserObject) {
            DefaultUserObject jira = (DefaultUserObject) defNode.getUserObject();
            Column column = colIndexMapper.getColumnForIndex(columnIndex);
            jira.setValue(column, value);
         } else {
            MyLogger.warn(JiraTreeTableModel.class, "User object [" + defNode.getUserObject().toString() + "]is not of DefaultUserObject type");
         }
      } else {
         MyLogger.warn(JiraTreeTableModel.class, "Node [" + node.toString() + "]is not of DefaultMutableTreeTableNode type");
      }
   }
}