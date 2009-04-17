package com.jonas.testing.jxtreetable.dao;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class TreeTableDao {

   public void persist(File file, DefaultTreeTableModel treeTableModel) throws IOException {
      FileOutputStream fos = null;
      BufferedOutputStream bos = null;
      XMLEncoder xenc = null;
      try {

         fos = new FileOutputStream(file);
         bos = new BufferedOutputStream(fos);
         xenc = new XMLEncoder(bos);

         xenc.writeObject(treeTableModel.getRoot());
         xenc.writeObject(treeTableModel.getRoot().getChildAt(0));

      } finally {
         if (xenc != null) {
            xenc.close();
         }
         if (bos != null) {
            bos.close();
         }
         if (fos != null) {
            fos.close();
         }
      }
   }
}
