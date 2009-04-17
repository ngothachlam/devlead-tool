package com.jonas.testing.jxtreetable.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class TreeTableDao {

   public void persist(File file, DefaultTreeTableModel treeTableModel) throws IOException {
      FileWriter fw = null;
      BufferedWriter bw = null;
      try {

         fw = new FileWriter(file);
         bw = new BufferedWriter(fw);

         TreeTableNode root = treeTableModel.getRoot();
         TreeTableNode parent = root;
         
         printRecursively(bw, parent);

      } finally {
         if (bw != null) {
            bw.flush();
            bw.close();
         }
         if (fw != null) {
            fw.close();
         }
      }
   }

   private void printRecursively(BufferedWriter bw, TreeTableNode parent) throws IOException {
      int childCount = parent.getChildCount();
      bw.write(parent.getUserObject().toString());
      bw.write("\n");
      for (int child = 0; child < childCount; child++) {
         printRecursively(bw, parent.getChildAt(child));
      }
   }

}
