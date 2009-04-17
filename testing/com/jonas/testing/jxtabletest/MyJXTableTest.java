package com.jonas.testing.jxtabletest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import org.jdesktop.swingx.JXTable;
import com.jonas.testHelpers.TryoutTester;

public class MyJXTableTest {

   public MyJXTableTest(){
//      TreeTableNode root = new DefaultMutableTreeTableNode("Blah");
      
//      TreeTableModel treetablemodel = new DefaultTreeTableModel(root);
//      JXTreeTable treetable = new JXTreeTable(treetablemodel);
      
//      JXTable table = new JXTable(model);
//      model.loadData();
      JXTable table = new JXTable(0,2);
      
      table.setColumnControlVisible(true);
      table.setPreferredScrollableViewportSize(new Dimension(500, 70));
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      
      TryoutTester test = new TryoutTester();
      JScrollPane scrollPane = new JScrollPane(table);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(scrollPane);
      TryoutTester.showInFrame(panel);
   }
   
   public static void main(String[] args){
      MyJXTableTest test = new MyJXTableTest();
   }
   
}
