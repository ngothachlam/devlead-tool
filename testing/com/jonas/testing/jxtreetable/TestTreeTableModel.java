package com.jonas.testing.jxtreetable;

import java.io.Serializable;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import com.jonas.common.logging.MyLogger;
import com.jonas.testing.jxtreetable.column.Column;
import com.jonas.testing.jxtreetable.column.ColumnMapper;
import com.jonas.testing.jxtreetable.userobject.DefaultUserObject;

public class TestTreeTableModel extends DefaultTreeTableModel implements Serializable{
   private final ColumnMapper colIndexMapper;
   private final static Logger log = MyLogger.getLogger(TestTreeTableModel.class);

   public TestTreeTableModel(TreeTableNode root, ColumnMapper colIndexMapper) {
      super(root);
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
      return colIndexMapper.getColumn(columnIndex).toString();
   }

   /**
    * Returns which object is displayed in this column.
    */
   @Override
   public Object getValueAt(Object node, int columnIndex) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         Object userObject = defNode.getUserObject();
         if (userObject instanceof DefaultUserObject) {
            DefaultUserObject<?> defnode = (DefaultUserObject<?>) userObject;
            Column column = colIndexMapper.getColumn(columnIndex);
            return defnode.getValueForColumn(column);
         }
         if (!"root".equalsIgnoreCase(userObject.toString()))
            log.warn("User object [" + userObject.toString() + "]is not of DefaultUserObject type");
      } else {
         log.warn("Node [" + node.toString() + "] is not of DefaultMutableTreeTableNode type");
      }
      return "";
   }

   @Override
   public boolean isCellEditable(Object node, int columnIndex) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         Object userObject = defNode.getUserObject();
         if (userObject instanceof DefaultUserObject) {
            DefaultUserObject<?> defnode = (DefaultUserObject<?>) userObject;
            Column column = colIndexMapper.getColumn(columnIndex);
            return defnode.isEditable(column);
         }
         if (!"root".equalsIgnoreCase(userObject.toString()))
            log.warn("User object [" + userObject.toString() + "] is not of DefaultUserObject type");
      } else {
         log.warn("Node [" + node.toString() + "] is not of DefaultMutableTreeTableNode type");
      }
      return super.isCellEditable(node, columnIndex);
   }

   @Override
   public boolean isLeaf(Object node) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         Object userObject = defNode.getUserObject();
         if (userObject instanceof DefaultUserObject) {
            DefaultUserObject<?> defnode = (DefaultUserObject<?>) userObject;
            return defnode.isLeaf();
         }
         if (!"root".equalsIgnoreCase(userObject.toString()))
            log.warn("User object [" + userObject.toString() + "] is not of DefaultUserObject type");
      } else {
         log.warn("Node [" + node.toString() + "] is not of DefaultMutableTreeTableNode type");
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
         Object userObject = defNode.getUserObject();
         if (userObject instanceof DefaultUserObject) {
            DefaultUserObject<?> defnode = (DefaultUserObject<?>) userObject;
            Column column = colIndexMapper.getColumn(columnIndex);
            defnode.setValue(column, value);
         } else {
            if (!"root".equalsIgnoreCase(userObject.toString()))
               log.warn("User object [" + userObject.toString() + "]is not of DefaultUserObject type");
         }
      } else {
         log.warn("Node [" + node.toString() + "] is not of DefaultMutableTreeTableNode type");
      }
   }
   
   private interface NodeCommand<T>{
      public T getReturnValue();
   }
   
}