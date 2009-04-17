package com.jonas.testing.jxtreetable;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import com.jonas.testing.jxtreetable.userobject.DefaultUserObject;
import com.jonas.testing.jxtreetable.userobject.FixVersion;
import com.jonas.testing.jxtreetable.userobject.Jira;
import com.jonas.testing.jxtreetable.userobject.Sprint;

public class JiraTreeTableModel extends DefaultTreeTableModel {
   private static final Column[] columns = { Column.JIRA, Column.DESCRIPTION };
   private final BoardColumnMapping colIndexMapper;

   public JiraTreeTableModel(TreeTableNode node, BoardColumnMapping colIndexMapper) {
      super(node);
      this.colIndexMapper = colIndexMapper;
   }

   public int getColumnCount() {
      return columns.length;
   }

   /**
    * What the TableHeader displays when the Table is in a JScrollPane.
    */
   public String getColumnName(int column) {
      return columns[column].toString();
   }

   /**
    * Returns which object is displayed in this column.
    */
   public Object getValueAt(Object node, int columnIndex) {
      Object res = "";
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof DefaultUserObject) {
            DefaultUserObject defnode = (DefaultUserObject) defNode.getUserObject();
            Column column = colIndexMapper.getColumnForIndex(columnIndex);
            return defnode.getValueForColumn(column);
         }
      }
      return res;
   }

   public boolean isCellEditable(Object node, int column) {
      return true;
   }

   @Override
   public boolean isLeaf(Object node) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof Jira) {
            return true;
         } else if (defNode.getUserObject() instanceof FixVersion) {
            return false;
         } else if (defNode.getUserObject() instanceof Sprint) {
            return false;
         }
      }
      return super.isLeaf(node);
   }

   /**
    * Called when done editing a cell.
    */
   public void setValueAt(Object value, Object node, int columnIndex) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof DefaultUserObject) {
            DefaultUserObject jira = (DefaultUserObject) defNode.getUserObject();
            Column column = colIndexMapper.getColumnForIndex(columnIndex);
            jira.setValue(column, value);
         }
      }
   }
}