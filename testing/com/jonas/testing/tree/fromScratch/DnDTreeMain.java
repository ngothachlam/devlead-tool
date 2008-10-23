package com.jonas.testing.tree.fromScratch;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class DnDTreeMain extends JFrame {

   private DnDTreeMain(){
      super("Rearrangeable Tree");
      setSize(300, 200);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      DnDTreeBuilder dndTreeBuilder = new DnDTreeBuilder();
      JTree tree = dndTreeBuilder.buildTree();
      
      getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
      setVisible(true);
   }
   
   public static void main(String... args){
      DnDTreeMain main = new DnDTreeMain();
   }
}
