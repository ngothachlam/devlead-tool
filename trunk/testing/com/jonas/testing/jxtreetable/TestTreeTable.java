package com.jonas.testing.jxtreetable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import com.jonas.testHelpers.TryoutTester;

/**
 * Test class of the basics of JXTreeTable with the DefaultTreeTableModel and DefaultMutableTreeNodes. Everything in one class to get a simple
 * runnable example. see https://swingx.dev.java.net/
 * 
 * @author jorgen.rapp
 * 
 */
public class TestTreeTable {
   private JXTreeTable treeTable;

   public TestTreeTable() {
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
         e.printStackTrace();
      }
      TestTreeTable test = new TestTreeTable();
      test.create();
   }

   /**
    * creates the demo on the event dispatch thread.
    * 
    */
   public void create() {
      DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("root");
      DefaultMutableTreeTableNode sprint = new DefaultMutableTreeTableNode(new Sprint("12-1"));
      DefaultMutableTreeTableNode fixVersionOne = new DefaultMutableTreeTableNode(new FixVersion("LLU 11"));
      DefaultMutableTreeTableNode fixVersionTwo = new DefaultMutableTreeTableNode(new FixVersion("LLU 12"));
      DefaultMutableTreeTableNode jiraOne = new DefaultMutableTreeTableNode(new Jira("llu-1", "Description for llu-1"));
      DefaultMutableTreeTableNode jiraTwo = new DefaultMutableTreeTableNode(new Jira("llu-2", "Description for llu-2"));

      addAsChildren(root, sprint);
      addAsChildren(sprint, fixVersionOne, fixVersionTwo);
      addAsChildren(fixVersionOne, jiraOne, jiraTwo);

      DefaultTreeTableModel treeTableModel = new JiraTreeTableModel(root, BoardTreeTableColumns.boardColumnMapping);
      treeTable = new JXTreeTable(treeTableModel);
      treeTable.setFillsViewportHeight(true);
      treeTable.setColumnControlVisible(true);

      TryoutTester.showInFrame(new JScrollPane(treeTable));
   }

   private void addAsChildren(DefaultMutableTreeTableNode parent, DefaultMutableTreeTableNode... children) {
      for (DefaultMutableTreeTableNode child : children) {
         parent.add(child);
      }
   }
}


abstract class DefaultParentUserObject extends DefaultUserObject {
   public final String getName() {
      return name;
   }

   public DefaultParentUserObject(String name) {
      super();
      this.name = name;
   }

   public final void setName(String name) {
      this.name = name;
   }

   private String name;

   @Override
   public final String getValueForColumn(Column column) {
      switch (column) {
      case JIRA:
         return name;
      }
      return "";
   }
}


abstract class DefaultUserObject implements Comparable<Jira>, Transferable {
   @Override
   public final Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (isDataFlavorSupported(flavor)) {
         return this;
      }
      throw new UnsupportedFlavorException(flavor);
   }

   public abstract String getValueForColumn(Column column);

   @Override
   public final DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { getDataFlavor() };
   }

   @Override
   public final boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(getDataFlavor());
   }

   protected abstract DataFlavor getDataFlavor();

   public abstract void setValue(Column column, Object value);
}


class Sprint extends DefaultParentUserObject {

   Sprint(String name) {
      super(name);
   }

   @Override
   public int compareTo(Jira o) {
      int res = 0;
      res = o.getKey().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(Sprint.class, "Sprint");
   }

   @Override
   public void setValue(Column column, Object value) {
   }

}


class FixVersion extends DefaultParentUserObject {

   FixVersion(String name) {
      super(name);
   }

   @Override
   public int compareTo(Jira o) {
      int res = 0;
      res = o.getKey().compareTo(getName());
      return res;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(FixVersion.class, "FixVersion");
   }

   @Override
   public void setValue(Column column, Object value) {
   }
}


class Jira extends DefaultUserObject {

   private String description;
   private String key;
   private String fixVersion;
   private String sprint;

   public Jira(String key) {
      this.key = key;
   }

   public Jira(String key, String description) {
      super();
      this.description = description;
      this.key = key;
   }

   @Override
   public int compareTo(Jira o) {
      int res = 0;
      res = o.getKey().compareTo(getKey());
      if (0 == res) {
         res = o.getDescription().compareTo(getDescription());
      }
      return res;
   }

   public String getDescription() {
      return description;
   }

   public String getKey() {
      return key;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setKey(String key) {
      this.key = key;
   }

   protected DataFlavor getDataFlavor() {
      return new DataFlavor(Jira.class, "Jira");
   }

   public String getFixVersion() {
      return fixVersion;
   }

   public String getSprint() {
      return sprint;
   }

   @Override
   public String getValueForColumn(Column column) {
      switch (column) {
      case JIRA:
         return key;
      case DESCRIPTION:
         return description;
      }
      return "";
   }

   @Override
   public void setValue(Column column, Object value) {
      switch (column) {
      case JIRA:
         setKey(value.toString());
         break;
      case DESCRIPTION:
         setDescription(value.toString());
         break;
      }

   }
}


interface TreeTableColumns{
   public Column getColumnForIndex(int columnIndex);
   public int getColumnCount();
}

class BoardTreeTableColumns implements TreeTableColumns {
   private final Map<Integer, Column> indexMap = new HashMap<Integer, Column>();

   private static int columnIndex = 0;
   public static final BoardTreeTableColumns boardColumnMapping = new BoardTreeTableColumns();
   
   private BoardTreeTableColumns() {
      add(Column.JIRA);
      add(Column.DESCRIPTION);
   }

   private void add(Column column){
      indexMap.put(columnIndex++, column);
   }
   
   @Override
   public Column getColumnForIndex(int columnIndex) {
      return indexMap.get(columnIndex);
   }

   @Override
   public int getColumnCount() {
      return indexMap.size();
   }
   
}


enum Column {
   JIRA("Jira"), DESCRIPTION("Description");

   private final String name;
   
   @Override
   public String toString() {
      return name;
   }

   Column(String name) {
      this.name = name;
   }

}


class JiraTreeTableModel extends DefaultTreeTableModel {
   private final TreeTableColumns columMapper;

   public JiraTreeTableModel(TreeTableNode node, TreeTableColumns columnMapper) {
      super(node);
      this.columMapper = columnMapper;
   }

   public int getColumnCount() {
      return columMapper.getColumnCount();
   }

   /**
    * What the TableHeader displays when the Table is in a JScrollPane.
    */
   public String getColumnName(int columnIndex) {
      return columMapper.getColumnForIndex(columnIndex).toString();
   }

   /**
    * Returns which object is displayed in this column.
    */
   public Object getValueAt(Object node, int columnIndex) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof DefaultUserObject) {
            DefaultUserObject defnode = (DefaultUserObject) defNode.getUserObject();
            Column column = columMapper.getColumnForIndex(columnIndex);
            return defnode.getValueForColumn(column);
         }
      }
      return "";
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
            Column column = columMapper.getColumnForIndex(columnIndex);
            jira.setValue(column, value);
         }
      }
   }
}
