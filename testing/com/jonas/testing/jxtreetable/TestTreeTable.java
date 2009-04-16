package com.jonas.testing.jxtreetable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
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
      DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("blah");
      DefaultMutableTreeTableNode two = new DefaultMutableTreeTableNode(new FixVersion("LLU 11"));
      DefaultMutableTreeTableNode three = new DefaultMutableTreeTableNode(new FixVersion("LLU 12"));

      DefaultMutableTreeTableNode twoOne = new DefaultMutableTreeTableNode(new Jira("llu-1"));
      DefaultMutableTreeTableNode twoTwo = new DefaultMutableTreeTableNode(new Jira("llu-2"));

      two.add(twoOne);
      two.add(twoTwo);

      root.add(two);
      root.add(three);

      DefaultTreeTableModel treeTableModel = new JiraTreeTableModel(root);
      treeTable = new JXTreeTable(treeTableModel);

      TryoutTester.showInFrame(new JScrollPane(treeTable));

   }
}


abstract class DefaultNode implements Comparable<Jira>, Transferable {
   @Override
   public final Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (isDataFlavorSupported(flavor)) {
         return this;
      }
      throw new UnsupportedFlavorException(flavor);
   }

   @Override
   public final DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { getDataFlavor() };
   }

   @Override
   public final boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(getDataFlavor());
   }

   protected abstract DataFlavor getDataFlavor();
}


class FixVersion extends DefaultNode {

   private final String name;

   FixVersion(String name) {
      this.name = name;
   }

   @Override
   public int compareTo(Jira o) {
      int res = 0;
      res = o.getKey().compareTo(getName());
      return res;
   }

   public String getName() {
      return name;
   }

   @Override
   protected DataFlavor getDataFlavor() {
      return new DataFlavor(FixVersion.class, "FixVersion");
   }
}


class Jira extends DefaultNode {

   private String description;

   private String key;

   public Jira(String key) {
      this.key = key;
   }

   public Jira(String description, String key) {
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
}


class JiraTreeTableModel extends DefaultTreeTableModel {
   private static final int DESCRIPTION = 1;
   private static final int JIRA = 0;
   private static final String[] columns = {"Jira", "Description"};
   
   public JiraTreeTableModel(TreeTableNode node) {
      super(node);
   }

   public int getColumnCount() {
      return columns.length;
   }

   /**
    * What the TableHeader displays when the Table is in a JScrollPane.
    */
   public String getColumnName(int column) {
      return columns[column];
   }

   /**
    * Returns which object is displayed in this column.
    */
   public Object getValueAt(Object node, int column) {
      Object res = "n/a";
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof Jira) {
            Jira jira = (Jira) defNode.getUserObject();
            switch (column) {
            case JIRA:
               return jira.getKey();
            case DESCRIPTION:
               return jira.getDescription();
            }
         } else if (defNode.getUserObject() instanceof FixVersion) {
            FixVersion fixVersion = (FixVersion) defNode.getUserObject();
            switch (column) {
            case JIRA:
               return fixVersion.getName();
            case DESCRIPTION:
               return "";
            }
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
         }
      }
      return super.isLeaf(node);
   }

   /**
    * Called when done editing a cell.
    */
   public void setValueAt(Object value, Object node, int column) {
      if (node instanceof DefaultMutableTreeTableNode) {
         DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode) node;
         if (defNode.getUserObject() instanceof Jira) {
            Jira jira = (Jira) defNode.getUserObject();
            switch (column) {
            case JIRA:
               jira.setKey(value.toString());
               break;
            case DESCRIPTION:
               jira.setDescription(value.toString());
               break;
            }
         }
      }
   }
}
